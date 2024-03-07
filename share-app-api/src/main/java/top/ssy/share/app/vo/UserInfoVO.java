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

    @Schema(name = "主键")
    private Integer pkId;
    @Schema(name = "手机号")
    private String phone;
    @Schema(name = "微信openId")
    private String wxOpenId;
    @Schema(name = "头像")
    private String avatar;
    @Schema(name = "昵称")
    private String nickname;
    @Schema(name = "性别")
    private Integer gender;
    @Schema(name = "积分")
    private Integer bonus;
    @Schema(name = "生日")
    private String birthday;
    @Schema(name = "描述")
    private String remark;
    @Schema(name = "今日是否签到")
    private Boolean isDailyCheck;
}
