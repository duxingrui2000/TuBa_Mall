package com.tuba.mall.service;

import com.tuba.mall.controller.vo.TuBaMallOrderDetailVO;
import com.tuba.mall.controller.vo.TuBaMallOrderItemVO;
import com.tuba.mall.controller.vo.TuBaMallShoppingCartItemVO;
import com.tuba.mall.controller.vo.TuBaMallUserVO;
import com.tuba.mall.entity.TuBaMallOrder;
import com.tuba.mall.util.PageQueryUtil;
import com.tuba.mall.util.PageResult;

import java.util.List;

public interface TuBaMallOrderService {
    /**
     * 后台分页
     */
    PageResult getTuBaMallOrdersPage(PageQueryUtil pageUtil);

    /**
     * 订单信息修改
     */
    String updateOrderInfo(TuBaMallOrder tuBaMallOrder);

    /**
     * 配货
     */
    String checkDone(Long[] ids);

    /**
     * 出库
     */
    String checkOut(Long[] ids);

    /**
     * 关闭订单
     */
    String closeOrder(Long[] ids);

    /**
     * 保存订单
     */
    String saveOrder(TuBaMallUserVO user, List<TuBaMallShoppingCartItemVO> myShoppingCartItems);

    /**
     * 获取订单详情
     */
    TuBaMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

    /**
     * 获取订单详情
     */
    TuBaMallOrder getTuBaMallOrderByOrderNo(String orderNo);

    /**
     * 我的订单列表
     */
    PageResult getMyOrders(PageQueryUtil pageUtil);

    /**
     * 手动取消订单
     */
    String cancelOrder(String orderNo, Long userId);

    /**
     * 确认收货
     */
    String finishOrder(String orderNo, Long userId);

    String paySuccess(String orderNo, int payType);

    List<TuBaMallOrderItemVO> getOrderItems(Long id);
}
