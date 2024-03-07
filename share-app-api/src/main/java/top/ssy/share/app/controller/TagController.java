package top.ssy.share.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ssy.share.app.common.result.Result;
import top.ssy.share.app.service.TagService;
import top.ssy.share.app.vo.TagVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 标签接口
 * @create : 2024-03-05 12:24
 **/

@RestController
@AllArgsConstructor
@Tag(name = "标签接口")
@RequestMapping("/tag")
public class TagController {
    private final TagService tagService;

    @GetMapping("/list")
    @Operation(summary = "获取标签列表")
    public Result<List<TagVO>> queryByType() {
        return Result.ok(tagService.getTagList());
    }
}
