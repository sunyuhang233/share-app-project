package top.ssy.share.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.ssy.share.app.common.result.PageResult;
import top.ssy.share.app.common.result.Result;
import top.ssy.share.app.query.NoticeQuery;
import top.ssy.share.app.service.NoticeService;
import top.ssy.share.app.vo.NoticeVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 公告接口
 * @create : 2024-03-01 14:33
 **/


@RestController
@AllArgsConstructor
@Tag(name = "公告接口", description = "公告接口")
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping("/index")
    @Operation(summary = "首页置顶公告")
    public Result<List<NoticeVO>> index() {
        return Result.ok(noticeService.indexPageNotice());
    }

    @GetMapping("swiper")
    @Operation(summary = "首页轮播公告")
    public Result<List<NoticeVO>> swiper() {
        return Result.ok(noticeService.swiperNotice());
    }

    @PostMapping("/page")
    @Operation(summary = "分页")
    public Result<PageResult<NoticeVO>> page(@RequestBody @Valid NoticeQuery query) {
        return Result.ok(noticeService.getNoticeList(query));
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "公告详情")
    public Result<NoticeVO> detail(@PathVariable Integer id) {
        return Result.ok(noticeService.detail(id));
    }
}
