package com.tuba.mall.controller.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @Author dxr
 * @Description 对二级分类目录做个封装
 * @Date 20:18 6.21
 **/
public class SecondLevelCategoryVO implements Serializable {
    private Long categoryId;
    
    private Long parentId;//该二级目录所属的一级目录的id
    
    private Byte categoryLevel;
    
    private String categoryName;
    
    private List<ThirdLevelCategoryVO> thirdLevelCategoryVOS;//该二级目录下属的三级目录
    
    public Long getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    
    public Long getParentId() {
        return parentId;
    }
    
    public void setParentId(Long parentId) {
        this.parentId = parentId;
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
    
    public List<ThirdLevelCategoryVO> getThirdLevelCategoryVOS() {
        return thirdLevelCategoryVOS;
    }
    
    public void setThirdLevelCategoryVOS(List<ThirdLevelCategoryVO> thirdLevelCategoryVOS) {
        this.thirdLevelCategoryVOS = thirdLevelCategoryVOS;
    }
}
