package com.tuba.mall.service;

import com.tuba.mall.controller.vo.TuBaMallIndexCarouseIVO;
import com.tuba.mall.entity.Carousel;
import com.tuba.mall.util.PageQueryUtil;
import com.tuba.mall.util.PageResult;

import java.util.List;

/**
 * @Author dxr
 * @Description 轮播图
 * @Date 11:13 6.24
 **/
public interface TuBaMallCarouselService {

    /**
     * @Author dxr
     * @Description 后台管理员调用去新增或修改轮播图信息
     **/
    PageResult getCarouselPage(PageQueryUtil pageUtil);
    
    String saveCarousel(Carousel carousel);
    
    String updateCarousel(Carousel carousel);
    
    Carousel getCarouselById(Integer id);
    
    //根据选择的id后代批量删除
    Boolean deleteBatch(Integer[] ids);
    
    /**
     * 返回固定数量的轮播图对象(首页调用)
     */
    List<TuBaMallIndexCarouseIVO> getCarouselsForIndex(int number);
}
