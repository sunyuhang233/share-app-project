package top.ssy.share.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.ssy.share.app.common.result.Result;
import top.ssy.share.app.service.CategoryService;
import top.ssy.share.app.vo.CategoryVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 分类接口
 * @create : 2024-03-05 12:24
 **/

@RestController
@AllArgsConstructor
@Tag(name = "分类接口")
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/list")
    @Operation(summary = "获取分类列表")
    public Result<List<CategoryVO>> queryByType() {
        return Result.ok(categoryService.getCategoryList());
    }
}
