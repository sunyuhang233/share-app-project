package top.ssy.share.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.ssy.share.app.convert.CategoryConvert;
import top.ssy.share.app.entity.Category;
import top.ssy.share.app.mapper.CategoryMapper;
import top.ssy.share.app.service.CategoryService;
import top.ssy.share.app.vo.CategoryVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description :
 * @create : 2024-03-05 11:49
 **/

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<CategoryVO> getCategoryList() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        return CategoryConvert.INSTANCE.convert(list(wrapper));
    }

    @Override
    public List<String> queryCategoryNameList(List<Integer> pkIdList) {
        return baseMapper.selectBatchIds(pkIdList)
                .stream()
                .map(Category::getTitle)
                .toList();
    }
}
