package top.ssy.share.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description :
 * @create : 2024-03-01 15:58
 **/

@Data
@Schema(name = "标签返回vo")
public class TagVO {
    @Schema(name = "pkId", description = "主键")
    private Integer pkId;
    @Schema(name = "title", description = "标题")
    private String title;
    @Schema(name = "isHot", description = "是否热门")
    private Integer isHot;
}
