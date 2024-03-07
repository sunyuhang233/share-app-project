package top.ssy.share.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ssy.share.app.common.cache.TokenStoreCache;
import top.ssy.share.app.common.exception.ErrorCode;
import top.ssy.share.app.common.exception.ServerException;
import top.ssy.share.app.convert.UserConvert;
import top.ssy.share.app.dto.UserEditDTO;
import top.ssy.share.app.entity.User;
import top.ssy.share.app.mapper.UserMapper;
import top.ssy.share.app.service.BonusLogService;
import top.ssy.share.app.service.UserService;
import top.ssy.share.app.vo.UserInfoVO;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 用户登录
 * @create : 2024-03-04 14:11
 **/

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final TokenStoreCache tokenStoreCache;
    private final BonusLogService bonusLogService;

    @Override
    public UserInfoVO userInfo(String accessToken) {
        Integer userId = tokenStoreCache.getUser(accessToken).getPkId();
        // 查询数据库
        User user = baseMapper.selectById(userId);
        if (user == null) {
            log.error("用户不存在, userId: {}", userId);
            throw new ServerException(ErrorCode.USER_NOT_EXIST);
        }
        UserInfoVO userInfoVO = UserConvert.INSTANCE.convert(user);
        userInfoVO.setIsDailyCheck(bonusLogService.isTodayCheck(userId));
        return userInfoVO;
    }

    @Override
    public UserInfoVO updateInfo(UserEditDTO userEditDTO, String accessToken) {
        Integer userId = tokenStoreCache.getUser(accessToken).getPkId();
        userEditDTO.setPkId(userId);
        User user = UserConvert.INSTANCE.convert(userEditDTO);
        if (user.getPkId() == null) {
            throw new ServerException(ErrorCode.PARAMS_ERROR);
        }
        try {
            if (baseMapper.updateById(user) < 1) {
                throw new ServerException("修改失败");
            }
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
        return userInfo(accessToken);
    }




}
