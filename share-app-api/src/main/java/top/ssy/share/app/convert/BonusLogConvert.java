package top.ssy.share.app.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.ssy.share.app.vo.BonusLogVO;
import top.ssy.share.app.entity.BonusLog;

import java.util.List;

/**
 * @author : Flobby
 * @program : share-admin-api
 * @description : 积分转换
 * @create : 2024-03-02 14:04
 **/

@Mapper
public interface BonusLogConvert {
    BonusLogConvert INSTANCE = Mappers.getMapper(BonusLogConvert.class);

    List<BonusLogVO> convert(List<BonusLog> list);
}
