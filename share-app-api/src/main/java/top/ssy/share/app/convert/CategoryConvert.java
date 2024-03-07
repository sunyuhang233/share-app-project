package top.ssy.share.app.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import top.ssy.share.app.entity.Category;
import top.ssy.share.app.vo.CategoryVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : share-app-api
 * @description : 分类
 * @create : 2024-03-01 14:53
 **/

@Mapper
public interface CategoryConvert {
    CategoryConvert INSTANCE = Mappers.getMapper(CategoryConvert.class);

    List<CategoryVO> convert(List<Category> list);
}
