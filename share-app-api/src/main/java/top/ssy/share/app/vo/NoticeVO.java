package top.ssy.share.app.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description :
 * @create : 2024-03-05 11:12
 **/

@Data
@Schema(name = "NoticeVO", description = "公告返回vo")
public class NoticeVO {
    @Schema(name = "主键")
    private Integer pkId;
    @Schema(name = "标题")
    private String title;
    @Schema(name = "封面")
    private String cover;
    @Schema(name = "内容")
    private String content;
    @Schema(name = "管理员id")
    private Integer adminId;
    @Schema(name = "管理员名称")
    private String adminName;
    @Schema(name = "是否置顶")
    private Integer isTop;
    @Schema(name = "createTime", description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
