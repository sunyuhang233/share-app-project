package top.ssy.share.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.ssy.share.app.entity.Notice;
import top.ssy.share.app.query.NoticeQuery;
import top.ssy.share.app.vo.NoticeVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : mapper
 * @create : 2024-03-01 13:06
 **/

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {

    List<NoticeVO> indexPageNotice();

    List<NoticeVO> getNoticePage(Page<NoticeVO> page, @Param("query") NoticeQuery query);

    NoticeVO getNoticeDetail(Integer id);

    List<NoticeVO> swiperNotice();
}
