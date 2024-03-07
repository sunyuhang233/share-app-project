package top.ssy.share.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.ssy.share.app.dto.UserEditDTO;
import top.ssy.share.app.entity.User;
import top.ssy.share.app.vo.UserInfoVO;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 用户服务
 * @create : 2024-03-04 11:54
 **/

public interface UserService extends IService<User> {

    /**
     * 用户信息
     *
     * @param accessToken 访问令牌
     * @return {@link UserInfoVO}
     */
    UserInfoVO userInfo(String accessToken);

    /**
     * 更新信息
     *
     * @param userEditDTO 用户编辑 DTO
     * @param accessToken 访问令牌
     * @return UserInfoVO
     */
    UserInfoVO updateInfo(UserEditDTO userEditDTO, String accessToken);
}
