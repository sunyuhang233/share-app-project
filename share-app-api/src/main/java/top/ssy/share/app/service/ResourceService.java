package top.ssy.share.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.ssy.share.app.common.result.PageResult;
import top.ssy.share.app.dto.ResourcePublishDTO;
import top.ssy.share.app.entity.Resource;
import top.ssy.share.app.query.ResourceQuery;
import top.ssy.share.app.query.UserActionResourceQuery;
import top.ssy.share.app.vo.ResourceDetailVO;
import top.ssy.share.app.vo.ResourceListItemVO;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 资源服务
 * @create : 2024-03-05 12:40
 **/

public interface ResourceService extends IService<Resource> {

    void publish(ResourcePublishDTO dto, String accessToken);

    PageResult<ResourceListItemVO> getResourcePage(ResourceQuery query);

    ResourceDetailVO getResourceDetail(Integer resourceId, String accessToken);

    PageResult<ResourceListItemVO> getUserActionResourcePage(UserActionResourceQuery query, String accessToken);
}
