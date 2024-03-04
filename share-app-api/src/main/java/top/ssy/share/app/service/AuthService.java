package top.ssy.share.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.ssy.share.app.entity.User;

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
}
