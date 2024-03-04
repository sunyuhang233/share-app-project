package top.ssy.share.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import top.ssy.share.app.entity.User;
import top.ssy.share.app.enums.AccountStatusEnum;
import top.ssy.share.app.mapper.UserMapper;
import top.ssy.share.app.service.AuthService;

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

    @Override
    public boolean checkUserEnabled(Integer userId) {
        User user = baseMapper.selectById(userId);
        if (ObjectUtils.isEmpty(user)) {
            return false;
        }
        return user.getEnabled().equals(AccountStatusEnum.ENABLED.getValue());
    }
}
