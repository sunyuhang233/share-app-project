package top.ssy.share.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.ssy.share.app.entity.Category;
import top.ssy.share.app.vo.CategoryVO;

import java.util.List;

/**
 * @author : Flobby
 * @program : share-admin-api
 * @description :
 * @create : 2024-03-01 14:55
 **/

public interface CategoryService extends IService<Category> {

    List<CategoryVO> getCategoryList();

    List<String> queryCategoryNameList(List<Integer> pkIdList);
}
