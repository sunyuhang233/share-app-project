package top.ssy.share.app.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 资源详细信息
 * @create : 2024-03-05 13:35
 **/

@Data
@Schema(name = "ResourceDetailVO", description = "资源详细信息")
public class ResourceDetailVO {
    @Schema(description = "主键")
    private Integer pkId;
    @Schema(description = "标题")
    private String title;
    @Schema(description = "作者")
    private String author;
    @Schema(description = "作者头像")
    private String authorAvatar;
    @Schema(description = "网盘类型")
    private String diskType;
    @Schema(description = "资源类型")
    private List<String> resType;
    @Schema(description = "标签")
    private List<String> tags;
    @Schema(description = "价格")
    private Integer price;
    @Schema(name = "downloadUrl", description = "下载路径")
    private String downloadUrl;
    @Schema(name = "detail", description = "详情")
    private String detail;
    @Schema(name = "remark", description = "审核描述")
    private String remark;
    @Schema(description = "点赞数")
    private Integer likeNum;
    @Schema(description = "下载数")
    private Integer downloadNum;
    @Schema(description = "收藏数")
    private Integer collectNum;
    @Schema(description = "是否点赞")
    private Boolean isLike;
    @Schema(description = "是否下载")
    private Boolean isDownload;
    @Schema(description = "是否收藏")
    private Boolean isCollect;
    @Schema(description = "审核状态")
    private Integer status;
    @Schema(description = "是否置顶")
    private Integer isTop;
    @Schema(description = "发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
