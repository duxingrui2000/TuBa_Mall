package com.tuba.mall.dao;

import com.tuba.mall.entity.TuBaMallShoppingCartItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author dxr
 * @Description 购物车
 * @Date 20:34
 **/
public interface TuBaMallShoppingCartItemMapper {
    int deleteByPrimaryKey(Long cartItemId);
    
    int insert(TuBaMallShoppingCartItem record);
    
    int insertSelective(TuBaMallShoppingCartItem record);
    
    TuBaMallShoppingCartItem selectByPrimaryKey(Long cartItemId);
    //一个用户id和一个商品id可以确定当前用户的购物车中是否已经有了该商品，有了就不要重复添加而是改变count
    TuBaMallShoppingCartItem selectByUserIdAndGoodsId(@Param("TuBaMallUserId") Long TuBaMallUserId, @Param("goodsId") Long goodsId);
    //一个用户当前购物车可能有多个商品项
    List<TuBaMallShoppingCartItem> selectByUserId(@Param("TuBaMallUserId") Long TuBaMallUserId, @Param("number") int number);
    
    int selectCountByUserId(Long TuBaMallUserId);
    
    int updateByPrimaryKeySelective(TuBaMallShoppingCartItem record);
    
    int updateByPrimaryKey(TuBaMallShoppingCartItem record);
    
    int deleteBatch(List<Long> ids);
}
