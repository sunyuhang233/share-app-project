package top.ssy.share.app.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.ssy.share.app.entity.Notice;
import top.ssy.share.app.vo.NoticeVO;

/**
 * @author Flobby
 */

@Mapper
public interface NoticeConvert {
    NoticeConvert INSTANCE = Mappers.getMapper(NoticeConvert.class);

    NoticeVO convert(Notice notice);
}