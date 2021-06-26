package com.tuba.mall.controller.vo;

import java.io.Serializable;

/**
 * @Author dxr
 * @Description 三级分类目录
 * @Date 20:21 6.21
 **/
public class ThirdLevelCategoryVO implements Serializable {
    private Long categoryId;
    
    private Byte categoryLevel;
    
    private String categoryName;
    
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
}
