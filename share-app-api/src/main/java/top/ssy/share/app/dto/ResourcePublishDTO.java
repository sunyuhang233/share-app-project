package top.ssy.share.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description :
 * @create : 2024-03-05 12:42
 **/

@Data
@Schema(description = "资源发布dto")
public class ResourcePublishDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4343086529032567678L;
    @Schema(description = "标题")
    private String title;
    @Schema(description = "网盘类型")
    private Integer diskType;
    @Schema(description = "资源类型")
    private List<Integer> resType;
    @Schema(description = "标签")
    private List<Integer> tags;
    @Schema(description = "下载链接")
    private String downloadUrl;
    @Schema(description = "详情")
    private String detail;
    @Schema(description = "价格")
    private Integer price;
    @Schema(description = "是否置顶")
    private Integer isTop;

}
