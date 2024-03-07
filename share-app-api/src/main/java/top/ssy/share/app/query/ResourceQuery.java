package top.ssy.share.app.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.ssy.share.app.common.model.Query;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description :
 * @create : 2024-03-05 13:14
 **/

@Data
@Schema(name = "ResourceQuery", description = "资源查询")
public class ResourceQuery extends Query {

    @Schema(description = "关键词")
    private String keyword;
    @Schema(description = "网盘类型")
    private Integer diskType;
    @Schema(description = "资源类型")
    private Integer resType;
    @Schema(description = "标签")
    private Integer tagId;
}
