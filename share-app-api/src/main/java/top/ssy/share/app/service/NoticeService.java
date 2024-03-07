package top.ssy.share.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.ssy.share.app.common.result.PageResult;
import top.ssy.share.app.entity.Notice;
import top.ssy.share.app.query.NoticeQuery;
import top.ssy.share.app.vo.NoticeVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 公告通知
 * @create : 2024-03-05 11:10
 **/


public interface NoticeService extends IService<Notice> {

    List<NoticeVO> indexPageNotice();

    PageResult<NoticeVO> getNoticeList(NoticeQuery query);

    NoticeVO detail(Integer id);

    List<NoticeVO> swiperNotice();
}
