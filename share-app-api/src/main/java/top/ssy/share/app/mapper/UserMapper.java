package top.ssy.share.app.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.ssy.share.app.entity.User;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : mapper
 * @create : 2024-03-01 13:06
 **/

@Mapper
public interface UserMapper extends BaseMapper<User> {

    default User getByPhone(String phone){
        return this.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
    }

}
