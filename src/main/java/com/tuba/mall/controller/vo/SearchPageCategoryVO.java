package com.tuba.mall.controller.vo;

import com.tuba.mall.entity.GoodsCategory;

import java.io.Serializable;
import java.util.List;

/**
 * @Author dxr
 * @Description 搜索页面分类数据VO
 * @Date 20:58 6.22
 **/
public class SearchPageCategoryVO implements Serializable {
    private String firstLevelCategoryName;
    
    private String secondLevelCategoryName;
    
    private String currentCategoryName;
    
    private List<GoodsCategory> secondLevelCategoryList;
    
    private List<GoodsCategory> thirdLevelCategoryList;
    
    public String getFirstLevelCategoryName() {
        return firstLevelCategoryName;
    }
    
    public void setFirstLevelCategoryName(String firstLevelCategoryName) {
        this.firstLevelCategoryName = firstLevelCategoryName;
    }
    
    public String getSecondLevelCategoryName() {
        return secondLevelCategoryName;
    }
    
    public void setSecondLevelCategoryName(String secondLevelCategoryName) {
        this.secondLevelCategoryName = secondLevelCategoryName;
    }
    
    public String getCurrentCategoryName() {
        return currentCategoryName;
    }
    
    public void setCurrentCategoryName(String currentCategoryName) {
        this.currentCategoryName = currentCategoryName;
    }
    
    public List<GoodsCategory> getSecondLevelCategoryList() {
        return secondLevelCategoryList;
    }
    
    public void setSecondLevelCategoryList(List<GoodsCategory> secondLevelCategoryList) {
        this.secondLevelCategoryList = secondLevelCategoryList;
    }
    
    public List<GoodsCategory> getThirdLevelCategoryList() {
        return thirdLevelCategoryList;
    }
    
    public void setThirdLevelCategoryList(List<GoodsCategory> thirdLevelCategoryList) {
        this.thirdLevelCategoryList = thirdLevelCategoryList;
    }
}
