package top.ssy.share.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 修改dto
 * @create : 2024-03-04 14:25
 **/

@Data
@Schema(name = "用户修改dto")
public class UserEditDTO {
    @Schema(name = "主键")
    private Integer pkId;
    @Schema(name = "头像")
    private String avatar;
    @Schema(name = "昵称")
    private String nickname;
    @Schema(name = "性别")
    private Integer gender;
    @Schema(name = "生日")
    private String birthday;
    @Schema(name = "描述")
    private String remark;

}
