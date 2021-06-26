package com.tuba.mall.controller.vo;

import java.util.List;

/**
 * @Author dxr
 * @Description 对图吧商城的一级标题做个封装
 * @Date 20:17 6.21
 **/
public class TuBaMallIndexCategoryVO {
    private Long categoryId;
    
    private Byte categoryLevel;
    
    private String categoryName;
    
    private List<SecondLevelCategoryVO> secondLevelCategoryVOS;
    
    public Long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    public Byte getCategoryLevel() {
        return categoryLevel;
    }
    
    public void setCategoryLevel(Byte categoryLevel) {
        this.categoryLevel = categoryLevel;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public List<SecondLevelCategoryVO> getSecondLevelCategoryVOS() {
        return secondLevelCategoryVOS;
    }
    
    public void setSecondLevelCategoryVOS(List<SecondLevelCategoryVO> secondLevelCategoryVOS) {
        this.secondLevelCategoryVOS = secondLevelCategoryVOS;
    }
}
