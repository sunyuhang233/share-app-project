package top.ssy.share.app.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(name = "图片url上传地址")
@AllArgsConstructor
public class FileUrlVO {

    @Schema(name = "file_url")
    private String fileUrl;
}