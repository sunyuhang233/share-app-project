package top.ssy.share.app.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import top.ssy.share.app.common.cache.RedisCache;
import top.ssy.share.app.common.cache.RedisKeys;
import top.ssy.share.app.common.cache.TokenStoreCache;
import top.ssy.share.app.common.exception.ErrorCode;
import top.ssy.share.app.common.exception.ServerException;
import top.ssy.share.app.dto.WxLoginDTO;
import top.ssy.share.app.entity.User;
import top.ssy.share.app.enums.AccountStatusEnum;
import top.ssy.share.app.mapper.UserMapper;
import top.ssy.share.app.service.AuthService;
import top.ssy.share.app.utils.AESUtil;
import top.ssy.share.app.utils.CommonUtils;
import top.ssy.share.app.utils.TokenUtils;
import top.ssy.share.app.vo.UserLoginVO;

import static top.ssy.share.app.common.constant.Constant.*;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 实现类
 * @create : 2024-03-04 11:56
 **/

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl extends ServiceImpl<UserMapper, User> implements AuthService {
    private final RedisCache redisCache;
    private final TokenStoreCache tokenStoreCache;

    @Override
    public boolean checkUserEnabled(Integer userId) {
        User user = baseMapper.selectById(userId);
        if (ObjectUtils.isEmpty(user)) {
            return false;
        }
        return user.getEnabled().equals(AccountStatusEnum.ENABLED.getValue());
    }

    @Override
    @Options(useGeneratedKeys = true, keyProperty = "pk_id")
    @Transactional(rollbackFor = Exception.class)
    public UserLoginVO login(String phone, String code) {
        String smsCacheKey = RedisKeys.getSmsKey(phone);
        String redisCode = (String) redisCache.get(smsCacheKey);
        if (ObjectUtils.isEmpty(redisCode) || !redisCode.equals(code)) {
            throw new ServerException(ErrorCode.SMS_CODE_ERROR);
        }
        redisCache.delete(smsCacheKey);
        User user = baseMapper.getByPhone(phone);
        if (ObjectUtils.isEmpty(user)) {
            throw new ServerException("账户不存在，请先注册");
        }
        // 用户被禁用
        if (!user.getEnabled().equals(AccountStatusEnum.ENABLED.getValue())) {
            throw new ServerException(ErrorCode.ACCOUNT_DISABLED);
        }
        String accessToken = TokenUtils.generator();
        String accessTokenKey = RedisKeys.getAccessTokenKey(accessToken);
        redisCache.set(accessTokenKey, user.getPkId(), CommonUtils.createRandomExpireTime());
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setPkId(user.getPkId());
        userLoginVO.setPhone(user.getPhone());
        userLoginVO.setWxOpenId(user.getWxOpenId());
        userLoginVO.setAccessToken(accessToken);
        tokenStoreCache.saveUser(accessToken, userLoginVO);
        return userLoginVO;
    }

    @Override
    public void logout(String accessToken) {
        String accessTokenKey = RedisKeys.getAccessTokenKey(accessToken);
        redisCache.delete(accessTokenKey);
        tokenStoreCache.deleteUser(accessToken);
    }

    @Override
    public void exchangePhone(String phone, String code, String accessToken) {
        if (!CommonUtils.checkPhone(phone)) {
            throw new ServerException(ErrorCode.PARAMS_ERROR);
        }
        String redisCode = (String) redisCache.get(RedisKeys.getSmsKey(phone));
        if (ObjectUtils.isEmpty(redisCode) || !redisCode.equals(code)) {
            throw new ServerException(ErrorCode.SMS_CODE_ERROR);
        }
        redisCache.delete(RedisKeys.getSmsKey(phone));

        User userByPhone = baseMapper.getByPhone(phone);
        UserLoginVO userLogin = tokenStoreCache.getUser(accessToken);
        if (ObjectUtils.isNotEmpty(userByPhone)) {
            if (!userLogin.getPkId().equals(userByPhone.getPkId())) {
                throw new ServerException(ErrorCode.PHONE_IS_EXIST);
            }
            if (userLogin.getPhone().equals(phone)) {
                throw new ServerException(ErrorCode.THE_SAME_PHONE);
            }
        }
        User user = baseMapper.selectById(userLogin.getPkId());
        user.setPhone(phone);
        if (baseMapper.updateById(user) < 1) {
            throw new ServerException(ErrorCode.OPERATION_FAIL);
        }
    }

    @Override
    @Options(useGeneratedKeys = true, keyProperty = "pk_id")
    public UserLoginVO weChatLogin(WxLoginDTO loginDTO) {
        /**
         * https://developers.weixin.qq.com/doc/oplatform/Mobile_App/WeChat_Login/Development_Guide.html#%E5%87%86%E5%A4%87%E5%B7%A5%E4%BD%9C
         */
        String url = "https://api.weixin.qq.com/sns/jscode2session?" +
                "appid=" + APP_ID +
                "&secret=" + APP_SECRET +
                "&js_code=" + loginDTO.getCode() +
                "&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        String jsonData = restTemplate.getForObject(url, String.class);
        if (StringUtils.contains(jsonData, WX_ERR_CODE)) {
            // 出错了
            throw new ServerException("openId获取失败," + jsonData);
        }
        // 解析返回数据
        JSONObject jsonObject = JSON.parseObject(jsonData);
        log.info("wxData: {}", jsonData);
        String openid = jsonObject.getString(WX_OPENID);
        String sessionKey = jsonObject.getString(WX_SESSION_KEY);
        // 对用户加密数据解密
        String jsonUserData = AESUtil.decrypt(loginDTO.getEncryptedData(), sessionKey, loginDTO.getIv());
        log.info("wxUserInfo: {}", jsonUserData);

        JSONObject wxUserData = JSON.parseObject(jsonUserData);

        User user = baseMapper.getByWxOpenId(openid);
        if (ObjectUtils.isEmpty(user)) {
            log.info("用户不存在，创建用户, openId: {}", openid);
            user = new User();
            user.setWxOpenId(openid);
            user.setNickname(wxUserData.getString("nickName"));
            user.setAvatar(wxUserData.getString("avatarUrl"));
            user.setGender(wxUserData.getInteger("gender"));
            user.setEnabled(AccountStatusEnum.ENABLED.getValue());
            user.setBonus(0);
            user.setRemark("这个人很懒，什么都没有写");
            baseMapper.insert(user);
        }
        // 用户被禁用
        if (!user.getEnabled().equals(AccountStatusEnum.ENABLED.getValue())) {
            throw new ServerException(ErrorCode.ACCOUNT_DISABLED);
        }
        String accessToken = TokenUtils.generator();
        String accessTokenKey = RedisKeys.getAccessTokenKey(accessToken);
        redisCache.set(accessTokenKey, user.getPkId(), CommonUtils.createRandomExpireTime());
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setPkId(user.getPkId());
        if (StringUtils.isNotBlank(user.getPhone())) {
            userLoginVO.setPhone(user.getPhone());
        }
        userLoginVO.setWxOpenId(user.getWxOpenId());
        userLoginVO.setAccessToken(accessToken);
        tokenStoreCache.saveUser(accessToken, userLoginVO);
        return userLoginVO;
    }
}
