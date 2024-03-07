package top.ssy.share.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 用户登录vo
 * @create : 2024-03-04 13:35
 **/

@Data
@Schema(name = "用户登录vo")
public class UserLoginVO implements Serializable {


    @Serial
    private static final long serialVersionUID = 8212240698099812005L;

    @Schema(name = "用户ID")
    private Integer pkId;

    @Schema(name = "手机号")
    private String phone;

    @Schema(name = "微信OpenId")
    private String wxOpenId;

    @Schema(name = "令牌")
    private String accessToken;
}
