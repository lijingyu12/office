package com.office.controller.backend;

import com.office.common.ServerResponse;
import com.office.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/category")
public class CategoryManageController {
    @Autowired
    ICategoryService iCategoryService;

    @RequestMapping("add")
    @ResponseBody
    public ServerResponse addCategory(String categoryName, Integer parentId){
        return  iCategoryService.addCatogry(categoryName,parentId);
    }

    @RequestMapping("update")
    @ResponseBody
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName){
        return iCategoryService.updateCategoryName(categoryId,categoryName);
    }

    @RequestMapping("get_category")
    @ResponseBody
    public ServerResponse getChildrenParallelCatogory( @RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        return iCategoryService.getChildrenParellelCategory(categoryId);
    }

    /**
     * 获取所有子分类
     * @param categoryId
     * @return
     */
    @RequestMapping("get_deep_Category")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCatagory(@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        return iCategoryService.selectCatogryAndChildrenById(categoryId);
    }

}
