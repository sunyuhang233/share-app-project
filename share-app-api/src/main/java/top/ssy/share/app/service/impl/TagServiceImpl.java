package top.ssy.share.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import top.ssy.share.app.convert.TagConvert;
import top.ssy.share.app.entity.Tag;
import top.ssy.share.app.mapper.TagMapper;
import top.ssy.share.app.service.TagService;
import top.ssy.share.app.vo.TagVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description :
 * @create : 2024-03-05 11:43
 **/

@Service
@AllArgsConstructor
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public List<TagVO> getTagList() {
        return TagConvert.INSTANCE.convert(list());
    }

    @Override
    public List<String> queryTagNamesByIds(List<Integer> ids) {
        return baseMapper.selectBatchIds(ids)
                .stream()
                .map(Tag::getTitle)
                .toList();
    }

}
