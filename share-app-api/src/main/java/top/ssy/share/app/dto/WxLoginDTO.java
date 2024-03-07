package top.ssy.share.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 微信登录dto
 * @create : 2024-03-04 16:23
 **/

@Data
@Schema(name = "微信登录")
public class WxLoginDTO {

    private String code;
    private String encryptedData;
    private String iv;

}
