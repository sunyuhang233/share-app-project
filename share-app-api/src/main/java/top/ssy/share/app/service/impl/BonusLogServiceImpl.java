package top.ssy.share.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ssy.share.app.common.cache.TokenStoreCache;
import top.ssy.share.app.common.exception.ErrorCode;
import top.ssy.share.app.common.exception.ServerException;
import top.ssy.share.app.common.model.Query;
import top.ssy.share.app.common.result.PageResult;
import top.ssy.share.app.convert.BonusLogConvert;
import top.ssy.share.app.entity.BonusLog;
import top.ssy.share.app.entity.User;
import top.ssy.share.app.enums.BonusActionEnum;
import top.ssy.share.app.mapper.BonusLogMapper;
import top.ssy.share.app.mapper.UserMapper;
import top.ssy.share.app.service.BonusLogService;
import top.ssy.share.app.vo.BonusLogVO;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 积分
 * @create : 2024-03-04 17:16
 **/

@Slf4j
@Service
@AllArgsConstructor
public class BonusLogServiceImpl extends ServiceImpl<BonusLogMapper, BonusLog> implements BonusLogService {
    private final TokenStoreCache tokenStoreCache;
    private final UserMapper userMapper;

    @Override
    public PageResult<BonusLogVO> page(Query query, String accessToken) {
        Integer userId = tokenStoreCache.getUser(accessToken).getPkId();
        Page<BonusLog> bonusLogPage = baseMapper.selectPage(new Page<>(query.getPage(), query.getLimit()), new LambdaQueryWrapper<BonusLog>().eq(BonusLog::getUserId, userId));
        List<BonusLogVO> voList = BonusLogConvert.INSTANCE.convert(bonusLogPage.getRecords());
        return new PageResult<>(voList, bonusLogPage.getTotal());
    }

    @Override
    public void addBonusLog(Integer userId, BonusActionEnum contentEnum, Integer bonus) {
        addBonusLog(userId, contentEnum.getDesc(), bonus);
    }

    @Override
    public void addBonusLog(Integer userId, BonusActionEnum contentEnum) {
        addBonusLog(userId, contentEnum.getDesc(), contentEnum.getBonus());
    }

    public void addBonusLog(Integer userId, String content, Integer bonus) {
        BonusLog bonusLog = new BonusLog();
        bonusLog.setUserId(userId);
        bonusLog.setContent(content);
        bonusLog.setBonus(bonus);
        save(bonusLog);
        User user = userMapper.selectById(userId);
        // 添加积分
        user.setBonus(user.getBonus() + bonus);
        userMapper.updateById(user);
    }

    @Override
    public void dailyCheck(String accessToken) {
        Integer userId = tokenStoreCache.getUser(accessToken).getPkId();
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new ServerException(ErrorCode.USER_NOT_EXIST);
        }
        // 检查今日是否签到
        boolean todayCheck = isTodayCheck(userId);
        if (todayCheck) {
            throw new ServerException(ErrorCode.ALREADY_HAS_CHECK);
        }
        // 签到成功，插入数据
        addBonusLog(user.getPkId(), BonusActionEnum.DAILY_SIGN);
    }

    @Override
    public boolean isTodayCheck(Integer userId) {
        LambdaQueryWrapper<BonusLog> wrapper = new LambdaQueryWrapper<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        // 构造开始时间
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date start = calendar.getTime();
        // 构造结束时间
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        Date end = calendar.getTime();
        wrapper.eq(BonusLog::getUserId, userId)
                .eq(BonusLog::getContent, BonusActionEnum.DAILY_SIGN.getDesc())
                .between(BonusLog::getCreateTime, start, end);
        // 查询今日是否签到
        BonusLog bonusLog = baseMapper.selectOne(wrapper);
        return bonusLog != null;
    }
}
