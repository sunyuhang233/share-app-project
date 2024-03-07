package top.ssy.share.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.ssy.share.app.common.model.Query;
import top.ssy.share.app.common.result.PageResult;
import top.ssy.share.app.entity.BonusLog;
import top.ssy.share.app.enums.BonusActionEnum;
import top.ssy.share.app.vo.BonusLogVO;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 积分日志service
 * @create : 2024-03-04 17:13
 **/


public interface BonusLogService extends IService<BonusLog> {

    PageResult<BonusLogVO> page(Query query, String accessToken);

    void addBonusLog(Integer userId, BonusActionEnum contentEnum, Integer bonus);

    void addBonusLog(Integer userId, BonusActionEnum contentEnum);

    void dailyCheck(String accessToken);

    /**
     * 今日是否签到
     *
     * @param userId 用户 ID
     * @return boolean
     */
    boolean isTodayCheck(Integer userId);
}
