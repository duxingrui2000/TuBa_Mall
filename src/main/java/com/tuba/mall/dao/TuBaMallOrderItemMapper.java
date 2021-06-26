package com.tuba.mall.dao;

import com.tuba.mall.entity.TuBaMallOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author dxr
 * @Description 订单项
 * @Date 20:24
 **/
public interface TuBaMallOrderItemMapper {
    int deleteByPrimaryKey(Long orderItemId);
    
    int insert(TuBaMallOrderItem record);
    
    int insertSelective(TuBaMallOrderItem record);
    
    TuBaMallOrderItem selectByPrimaryKey(Long orderItemId);
    
    /**
     * 根据订单id获取订单项列表
     */
    List<TuBaMallOrderItem> selectByOrderId(Long orderId);
    
    /**
     * 根据订单ids获取订单项列表
     */
    List<TuBaMallOrderItem> selectByOrderIds(@Param("orderIds") List<Long> orderIds);
    
    /**
     * 批量insert订单项数据
     */
    int insertBatch(@Param("orderItems") List<TuBaMallOrderItem> orderItems);
    
    int updateByPrimaryKeySelective(TuBaMallOrderItem record);
    
    int updateByPrimaryKey(TuBaMallOrderItem record);
}
