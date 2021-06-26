package com.tuba.mall.dao;

import com.tuba.mall.entity.TuBaMallOrder;
import com.tuba.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author dxr
 * @Description 订单
 * @Date 20:28
 **/
public interface TuBaMallOrderMapper {
    int deleteByPrimaryKey(Long orderId);
    
    int insert(TuBaMallOrder record);
    
    int insertSelective(TuBaMallOrder record);
    
    TuBaMallOrder selectByPrimaryKey(Long orderId);
    
    TuBaMallOrder selectByOrderNo(String orderNo);
    
    int updateByPrimaryKeySelective(TuBaMallOrder record);
    
    int updateByPrimaryKey(TuBaMallOrder record);
    
    List<TuBaMallOrder> findTuBaMallOrderList(PageQueryUtil pageUtil);
    
    int getTotalTuBaMallOrders(PageQueryUtil pageUtil);
    
    List<TuBaMallOrder> selectByPrimaryKeys(@Param("orderIds") List<Long> orderIds);
    
    //出库完成，order_status=3
    int checkOut(@Param("orderIds") List<Long> orderIds);
    
    //
    int closeOrder(@Param("orderIds") List<Long> orderIds, @Param("orderStatus") int orderStatus);
    
    //配货完成，order_status=2
    int checkDone(@Param("orderIds") List<Long> asList);
}
