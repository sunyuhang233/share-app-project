package top.ssy.share.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ssy.share.app.common.cache.TokenStoreCache;
import top.ssy.share.app.common.exception.ServerException;
import top.ssy.share.app.entity.Resource;
import top.ssy.share.app.entity.User;
import top.ssy.share.app.entity.UserAction;
import top.ssy.share.app.enums.BonusActionEnum;
import top.ssy.share.app.enums.UserActionEnum;
import top.ssy.share.app.mapper.ResourceMapper;
import top.ssy.share.app.mapper.UserActionMapper;
import top.ssy.share.app.mapper.UserMapper;
import top.ssy.share.app.service.BonusLogService;
import top.ssy.share.app.service.UserActionService;
import top.ssy.share.app.vo.UserActionListInfo;

import java.util.stream.Collectors;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description :
 * @create : 2024-03-05 12:58
 **/

@Slf4j
@Service
@AllArgsConstructor
public class UserActionServiceImpl extends ServiceImpl<UserActionMapper, UserAction> implements UserActionService {
    private final TokenStoreCache tokenStoreCache;
    private final ResourceMapper resourceMapper;
    private final BonusLogService bonusLogService;
    private final UserMapper userMapper;


    @Override
    public void insertUserAction(Integer userId, Integer resourceId, UserActionEnum userActionEnum) {
        UserAction userAction = new UserAction();
        userAction.setUserId(userId);
        userAction.setResourceId(resourceId);
        userAction.setType(userActionEnum.getCode());
        save(userAction);
    }

    @Override
    public UserActionListInfo selectResourceListByUserIdAndType(Integer userId, UserActionEnum userActionEnum, Page<UserAction> page) {
        LambdaQueryWrapper<UserAction> queryWrapper = new LambdaQueryWrapper<>();
        Page<UserAction> pageResult = page(page, queryWrapper.eq(UserAction::getUserId, userId).eq(UserAction::getType, userActionEnum.getCode()));
        return new UserActionListInfo(pageResult.getTotal(),
                pageResult.getRecords()
                        .stream()
                        .map(UserAction::getResourceId)
                        .collect(Collectors.toList()));
    }

    @Override
    public Integer selectCountByResourceIdAndType(Integer resourceId, UserActionEnum userActionEnum) {
        LambdaQueryWrapper<UserAction> queryWrapper = new LambdaQueryWrapper<>();
        return Math.toIntExact(count(queryWrapper.eq(UserAction::getResourceId, resourceId).eq(UserAction::getType, userActionEnum.getCode())));
    }

    @Override
    public Boolean resourceIsAction(Integer userId, Integer resourceId, UserActionEnum userActionEnum) {
        LambdaQueryWrapper<UserAction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAction::getResourceId, resourceId)
                .eq(UserAction::getUserId, userId)
                .eq(UserAction::getType, userActionEnum.getCode());
        return baseMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public void collectResource(Integer resourceId, String accessToken) {
        actionResource(tokenStoreCache.getUser(accessToken).getPkId(), resourceId, UserActionEnum.COLLECT);
    }

    @Override
    public void likeResource(Integer resourceId, String accessToken) {
        actionResource(tokenStoreCache.getUser(accessToken).getPkId(), resourceId, UserActionEnum.LIKE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void exchangeResource(Integer resourceId, String accessToken) {
        Resource resource = resourceMapper.selectById(resourceId);
        Integer price = resource.getPrice();
        Integer userId = tokenStoreCache.getUser(accessToken).getPkId();
        User user = userMapper.selectById(userId);
        if (user.getBonus() < price) {
            throw new ServerException("积分不足");
        }
        actionResource(userId, resourceId, UserActionEnum.EXCHANGE);
        bonusLogService.addBonusLog(userId, BonusActionEnum.RESOURCE_EXCHANGE, -price);
        bonusLogService.addBonusLog(resource.getAuthor(), BonusActionEnum.RESOURCE_BE_EXCHANGED);
    }

    public void actionResource(Integer userId, Integer resourceId, UserActionEnum userActionEnum) {
        LambdaQueryWrapper<UserAction> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAction::getResourceId, resourceId)
                .eq(UserAction::getUserId, userId)
                .eq(UserAction::getType, userActionEnum.getCode());
        if (baseMapper.selectCount(queryWrapper) > 0) {
            if (userActionEnum.equals(UserActionEnum.EXCHANGE)) {
                throw new ServerException("已兑换过该资源");
            }
            baseMapper.delete(queryWrapper);
        } else {
            insertUserAction(userId, resourceId, userActionEnum);
        }
    }

}
