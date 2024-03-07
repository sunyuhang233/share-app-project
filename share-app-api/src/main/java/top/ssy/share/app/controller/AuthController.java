package top.ssy.share.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.ssy.share.app.common.result.Result;
import top.ssy.share.app.dto.WxLoginDTO;
import top.ssy.share.app.service.AuthService;
import top.ssy.share.app.vo.UserLoginVO;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 认证接口
 * @create : 2024-03-04 13:47
 **/

@RestController
@RequestMapping("/auth")
@Tag(name = "认证接口")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "登录")
    public Result<UserLoginVO> login(@RequestParam("phone") String phone, @RequestParam("code") String code) {
        return Result.ok(authService.login(phone, code));
    }

    @PostMapping("/logout")
    @Operation(summary = "登出")
    public Result<String> logout(@RequestHeader("Authorization") String accessToken) {
        authService.logout(accessToken);
        return Result.ok();
    }

    @PostMapping("/exchangePhone")
    @Operation(summary = "换绑手机号")
    public Result<String> exchangePhone(@RequestParam("phone") String phone,
                                        @RequestParam("code") String code,
                                        @RequestHeader("Authorization") String accessToken) {
        authService.exchangePhone(phone, code, accessToken);
        return Result.ok();
    }

    @PostMapping("weChatLogin")
    @Operation(summary = "微信登录")
    public Result<UserLoginVO> weChatLogin(@RequestBody WxLoginDTO dto) {
        return Result.ok(authService.weChatLogin(dto));
    }
}
