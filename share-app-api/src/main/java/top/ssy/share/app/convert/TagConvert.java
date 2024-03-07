package top.ssy.share.app.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.ssy.share.app.entity.Tag;
import top.ssy.share.app.vo.TagVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description :
 * @create : 2024-03-01 16:02
 **/

@Mapper
public interface TagConvert {

    TagConvert INSTANCE = Mappers.getMapper(TagConvert.class);

    TagVO convert(Tag tag);

    List<TagVO> convert(List<Tag> tags);
}
