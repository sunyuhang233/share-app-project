package top.ssy.share.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.ssy.share.app.entity.Tag;
import top.ssy.share.app.vo.TagVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 标签服务
 * @create : 2024-03-05 11:42
 **/

public interface TagService extends IService<Tag> {

    List<TagVO> getTagList();

    List<String> queryTagNamesByIds(List<Integer> ids);
}
