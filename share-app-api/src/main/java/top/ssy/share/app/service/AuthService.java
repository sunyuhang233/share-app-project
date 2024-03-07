package top.ssy.share.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.ssy.share.app.dto.WxLoginDTO;
import top.ssy.share.app.entity.User;
import top.ssy.share.app.vo.UserLoginVO;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 账户认证服务
 * @create : 2024-03-04 11:55
 **/


public interface AuthService extends IService<User> {

    /**
     * 检查用户是否启用
     *
     * @param userId 用户 ID
     * @return boolean
     */
    boolean checkUserEnabled(Integer userId);

    /**
     * 登录
     *
     * @param phone 电话
     * @param code  验证码
     * @return {@link UserLoginVO}
     */
    UserLoginVO login(String phone, String code);

    /**
     * 注销
     *
     * @param accessToken 访问令牌
     */
    void logout(String accessToken);

    /**
     * 交换电话
     *
     * @param phone       电话
     * @param code        法典
     * @param accessToken 访问令牌
     */
    void exchangePhone(String phone, String code, String accessToken);

    /**
     * 微信登录
     *
     * @param loginDTO DTO
     * @return {@link UserLoginVO}
     */
    UserLoginVO weChatLogin(WxLoginDTO loginDTO);
}
