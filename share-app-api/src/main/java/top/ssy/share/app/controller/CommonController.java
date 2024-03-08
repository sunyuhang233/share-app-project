package top.ssy.share.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.ssy.share.app.common.result.Result;
import top.ssy.share.app.service.CommonService;
import top.ssy.share.app.vo.FileUrlVO;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : common
 * @create : 2024-03-04 12:29
 **/

@Tag(name = "基础服务")
@RestController
@RequestMapping("/common")
@AllArgsConstructor
public class CommonController {
    private final CommonService commonService;

    @Operation(summary = "发送短信")
    @PostMapping("/sendSms")
    public Result<Object> sendSms(@RequestParam("phone") String phone) {
        commonService.sendSms(phone);
        return Result.ok();
    }

    @PostMapping(value = "/upload/img")
    @Operation(summary = "图片上传")
    public Result<FileUrlVO> upload(@RequestBody MultipartFile file) {
        return Result.ok(commonService.upload(file));
    }
}
