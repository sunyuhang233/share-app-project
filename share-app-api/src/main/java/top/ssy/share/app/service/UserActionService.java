package top.ssy.share.app.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.ssy.share.app.entity.UserAction;
import top.ssy.share.app.enums.UserActionEnum;
import top.ssy.share.app.vo.UserActionListInfo;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 用户行为
 * @create : 2024-03-05 12:57
 **/


public interface UserActionService extends IService<UserAction> {

    void insertUserAction(Integer userId, Integer resourceId, UserActionEnum userActionEnum);

    UserActionListInfo selectResourceListByUserIdAndType(Integer userId, UserActionEnum userActionEnum, Page<UserAction> page);

    Integer selectCountByResourceIdAndType(Integer resourceId, UserActionEnum userActionEnum);

    Boolean resourceIsAction(Integer userId, Integer resourceId, UserActionEnum userActionEnum);

    void collectResource(Integer resourceId, String accessToken);
    void likeResource(Integer resourceId, String accessToken);
    void exchangeResource(Integer resourceId, String accessToken);
}
