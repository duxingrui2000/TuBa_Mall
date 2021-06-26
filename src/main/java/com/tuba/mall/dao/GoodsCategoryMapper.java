package com.tuba.mall.dao;

import com.tuba.mall.entity.GoodsCategory;
import com.tuba.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author dxr
 * @Description 商品分类
 * @Date 15:52 6.24
 **/
public interface GoodsCategoryMapper {
    int deleteByPrimaryKey(Long categoryId);
    
    int insert(GoodsCategory record);
    
    int insertSelective(GoodsCategory record);
    
    GoodsCategory selectByPrimaryKey(Long categoryId);
    
    GoodsCategory selectByLevelAndName(@Param("categoryLevel") Byte categoryLevel, @Param("categoryName") String categoryName);
    
    int updateByPrimaryKeySelective(GoodsCategory record);
    
    int updateByPrimaryKey(GoodsCategory record);
    
    //也是分页的
    List<GoodsCategory> findGoodsCategoryList(PageQueryUtil pageUtil);
    
    int getTotalGoodsCategories(PageQueryUtil pageUtil);
    
    int deleteBatch(Integer[] ids);
    
    //number为0代表查询所有
    List<GoodsCategory> selectByLevelAndParentIdsAndNumber(@Param("parentIds") List<Long> parentIds, @Param("categoryLevel") int categoryLevel, @Param("number") int number);
}
