package com.tuba.mall.dao;

import com.tuba.mall.entity.Carousel;
import com.tuba.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//轮播图数据访问对象（根据id删除，增，查，更新，查所有list数据，批量删除轮播图以及index首页
// 根据轮播图数量搜索出轮播图数据供调用）
public interface CarouselMapper {
    int deleteByPrimaryKey(Integer carouselId);
    
    int insert(Carousel record);
    
    int insertSelective(Carousel record);
    
    Carousel selectByPrimaryKey(Integer carouselId);
    
    int updateByPrimaryKeySelective(Carousel record);
    
    int updateByPrimaryKey(Carousel record);
    
    List<Carousel> findCarouselList(PageQueryUtil pageUtil);
    
    int getTotalCarousels(PageQueryUtil pageUtil);
    
    int deleteBatch(Integer[] ids);
    
    List<Carousel> findCarouselsByNum(@Param("number") int number);
}
