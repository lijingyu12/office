package com.office.service;

import com.office.common.ServerResponse;
import com.office.pojo.Category;

import java.util.List;

public interface ICategoryService {
    ServerResponse addCatogry(String categoryName,Integer parentId);
    ServerResponse updateCategoryName(Integer categoryId,String categoryName);
    ServerResponse<List<Category>> getChildrenParellelCategory(Integer categoryId);
    ServerResponse<List<Integer>> selectCatogryAndChildrenById(Integer categoryId);

}
