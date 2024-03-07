package top.ssy.share.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.ssy.share.app.common.model.Query;
import top.ssy.share.app.common.result.PageResult;
import top.ssy.share.app.common.result.Result;
import top.ssy.share.app.dto.UserEditDTO;
import top.ssy.share.app.query.UserActionResourceQuery;
import top.ssy.share.app.service.BonusLogService;
import top.ssy.share.app.service.ResourceService;
import top.ssy.share.app.service.UserActionService;
import top.ssy.share.app.service.UserService;
import top.ssy.share.app.vo.BonusLogVO;
import top.ssy.share.app.vo.ResourceListItemVO;
import top.ssy.share.app.vo.UserInfoVO;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 用户接口
 * @create : 2024-03-04 14:10
 **/

@Slf4j
@RestController
@RequestMapping("/user")
@AllArgsConstructor
@Tag(name = "用户接口")
public class UserController {
    private final UserService userService;
    private final BonusLogService bonusLogService;
    private final ResourceService resourceService;
    private final UserActionService userActionService;

    @GetMapping("info")
    @Operation(summary = "查询用户信息")
    public Result<UserInfoVO> userInfo(@RequestHeader("Authorization") String token) {
        return Result.ok(userService.userInfo(token));
    }

    @PostMapping("update")
    @Operation(summary = "修改用户信息")
   public Result<UserInfoVO> update(@RequestBody UserEditDTO userEditDTO, @RequestHeader("Authorization") String token){
        return Result.ok(userService.updateInfo(userEditDTO, token));
    }

    @PostMapping("bonus/page")
    @Operation(summary = "积分明细")
    public Result<PageResult<BonusLogVO>> bonusPage(@RequestBody @Valid Query query, @RequestHeader("Authorization") String accessToken) {
        return Result.ok(bonusLogService.page(query, accessToken));
    }

    @PostMapping("dailyCheck")
    @Operation(summary = "每日签到")
    public Result<Object> dailyCheck(@RequestHeader("Authorization") String accessToken) {
        bonusLogService.dailyCheck(accessToken);
        return Result.ok();
    }

    @PostMapping("resource")
    @Operation(summary = "资源行为列表")
    public Result<PageResult<ResourceListItemVO>> resourcePage(@RequestBody UserActionResourceQuery query, @RequestHeader("Authorization") String accessToken) {
        return Result.ok(resourceService.getUserActionResourcePage(query, accessToken));
    }

    @PostMapping("resource/collect")
    @Operation(summary = "收藏资源")
    public Result<Object> collectResource(@RequestParam Integer resourceId, @RequestHeader("Authorization") String accessToken) {
        userActionService.collectResource(resourceId, accessToken);
        return Result.ok();
    }

    @PostMapping("resource/like")
    @Operation(summary = "点赞资源")
    public Result<Object> likeResource(@RequestParam Integer resourceId, @RequestHeader("Authorization") String accessToken) {
        userActionService.likeResource(resourceId, accessToken);
        return Result.ok();
    }

    @PostMapping("resource/exchange")
    @Operation(summary = "兑换下载资源")
    public Result<Object> exchangeResource(@RequestParam Integer resourceId, @RequestHeader("Authorization") String accessToken) {
        userActionService.exchangeResource(resourceId, accessToken);
        return Result.ok();
    }

}
