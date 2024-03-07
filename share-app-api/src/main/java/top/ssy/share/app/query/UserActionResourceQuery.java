package top.ssy.share.app.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.ssy.share.app.common.model.Query;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 用户行为资源查询
 * @create : 2024-03-05 14:14
 **/

@Data
@Schema(name = "UserActionResourceQuery", description = "用户行为资源查询")
public class UserActionResourceQuery  extends Query {

    /**
     * @see top.ssy.share.app.enums.UserActionEnum
     */
    @Schema(description = "用户行为")
    private Integer type;
}
