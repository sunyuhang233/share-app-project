package top.ssy.share.app.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 用户行为返回vo
 * @create : 2024-03-05 14:26
 **/

@Data
@AllArgsConstructor
public class UserActionListInfo {
    private long total;
    private List<Integer> resourceIdList;
}
