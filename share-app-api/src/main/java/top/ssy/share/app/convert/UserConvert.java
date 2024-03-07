package top.ssy.share.app.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.ssy.share.app.dto.UserEditDTO;
import top.ssy.share.app.entity.User;
import top.ssy.share.app.vo.UserInfoVO;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 用户转换类
 * @create : 2024-03-04 13:41
 **/

@Mapper
public interface UserConvert {

    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    UserInfoVO convert(User user);

    User convert(UserEditDTO dto);
}
