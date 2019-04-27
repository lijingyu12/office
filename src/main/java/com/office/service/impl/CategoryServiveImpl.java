package com.office.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.office.common.ServerResponse;
import com.office.dao.CategoryMapper;
import com.office.pojo.Category;
import com.office.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@Slf4j
@Service("iCatogryService")
public class CategoryServiveImpl implements ICategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCatogry(String categoryName, Integer parentId) {
        if(StringUtils.isBlank(categoryName) || parentId==null){
            return ServerResponse.createByErrorMessage("品类添加错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);
        int resultCount = categoryMapper.insert(category);
        if(resultCount>0){
            return ServerResponse.createBySuccess("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    @Override
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        if(categoryId==null||StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("更新品类错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount>0){
            return ServerResponse.createBySuccess("更新品类名字成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParellelCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(CollectionUtils.isEmpty(categoryList)){  //同时判断是否为空集合或者null集合
            log.info("未找到当前分类的子分类");

        }
        return ServerResponse.createBySuccess(categoryList);
    }

    @Override
    public ServerResponse<List<Integer>> selectCatogryAndChildrenById(Integer categoryId){
        Set<Category> categorySet = Sets.newHashSet();
        findchildCategory(categorySet,categoryId);
        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId != null){
            for(Category categoryItem :categorySet){
                categoryIdList.add(categoryItem.getId());
            }
        }
        return  ServerResponse.createBySuccess(categoryIdList);

    }

    //递归查找子分类
    private Set<Category> findchildCategory(Set<Category> categorySet,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category!=null){
            categorySet.add(category);
        }
        //查找子节点，递归算法退出条件
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId); //mybatis对于集合的返回 如果没有查到不会返回null
        for(Category categoryItem :categoryList){
            findchildCategory(categorySet,categoryItem.getId());
        }
        return categorySet;

    }
}
