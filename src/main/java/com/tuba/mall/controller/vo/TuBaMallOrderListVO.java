package com.tuba.mall.controller.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author dxr
 * @Description 订单列表页面，封装了不同订单所有的商品信息
 * （依靠List<TuBaMallOrderItemVO> tuBaMallOrderItemVOS),
 * 点进具体订单详情具体信息靠TuBaMallOrderDetailVO
 * @Date 17:07 6.22
 **/
public class TuBaMallOrderListVO implements Serializable {
    
    private Long orderId;
    
    private String orderNo;
    
    private Integer totalPrice;
    
    private Byte payType;
    
    private Byte orderStatus;
    
    private String orderStatusString;
    
    private String userAddress;
    
    private Date createTime;
    
    private List<TuBaMallOrderItemVO> tuBaMallOrderItemVOS;
    
    public Long getOrderId() {
        return orderId;
    }
    
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    
    public String getOrderNo() {
        return orderNo;
    }
    
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    
    public Integer getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public Byte getPayType() {
        return payType;
    }
    
    public void setPayType(Byte payType) {
        this.payType = payType;
    }
    
    public Byte getOrderStatus() {
        return orderStatus;
    }
    
    public void setOrderStatus(Byte orderStatus) {
        this.orderStatus = orderStatus;
    }
    
    public String getOrderStatusString() {
        return orderStatusString;
    }
    
    public void setOrderStatusString(String orderStatusString) {
        this.orderStatusString = orderStatusString;
    }
    
    public String getUserAddress() {
        return userAddress;
    }
    
    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }
    
    public Date getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public List<TuBaMallOrderItemVO> getTuBaMallOrderItemVOS() {
        return tuBaMallOrderItemVOS;
    }
    
    public void setTuBaMallOrderItemVOS(List<TuBaMallOrderItemVO> tuBaMallOrderItemVOS) {
        this.tuBaMallOrderItemVOS = tuBaMallOrderItemVOS;
    }
}
