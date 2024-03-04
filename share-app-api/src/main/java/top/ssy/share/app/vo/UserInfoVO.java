package top.ssy.share.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 用户信息
 * @create : 2024-03-04 10:43
 **/

@Data
@Schema(name = "用户信息")
public class UserInfoVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -45095106764580159L;

    private Integer pkId;
}
