package com.tuba.mall.controller.vo;

import java.util.Date;
import java.util.List;

/**
 * @Author dxr
 * @Description 用户的订单详情页面，封装重要数据，详细
 * 数据库为了节省空间使用tinyint（一个字节所以大小-128-127足够用了）
 * 这里要多出几个对应string字段存相应status代表的含义
 * @Date 20:30 6.21
 **/
public class TuBaMallOrderDetailVO {
    private String orderNo;
    
    private Integer totalPrice;
    
    private Byte payStatus;
    
    private String payStatusString;
    
    private Byte payType;
    
    private String payTypeString;
    
    private Date payTime;
    
    private Byte orderStatus;
    
    private String orderStatusString;
    
    private String userAddress;
    
    private Date createTime;
    
//    private Date updateTime;
    
    private List<TuBaMallOrderItemVO> tuBaMallOrderItemVOS;
    
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
    
    public Byte getPayStatus() {
        return payStatus;
    }
    
    public void setPayStatus(Byte payStatus) {
        this.payStatus = payStatus;
    }
    
    public String getPayStatusString() {
        return payStatusString;
    }
    
    public void setPayStatusString(String payStatusString) {
        this.payStatusString = payStatusString;
    }
    
    public Byte getPayType() {
        return payType;
    }
    
    public void setPayType(Byte payType) {
        this.payType = payType;
    }
    
    public String getPayTypeString() {
        return payTypeString;
    }
    
    public void setPayTypeString(String payTypeString) {
        this.payTypeString = payTypeString;
    }
    
    public Date getPayTime() {
        return payTime;
    }
    
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
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
    
//    public Date getUpdateTime() {
//        return updateTime;
//    }
//
//    public void setUpdateTime(Date updateTime) {
//        this.updateTime = updateTime;
//    }
    
    public List<TuBaMallOrderItemVO> getTuBaMallOrderItemVOS() {
        return tuBaMallOrderItemVOS;
    }
    
    public void setTuBaMallOrderItemVOS(List<TuBaMallOrderItemVO> tuBaMallOrderItemVOS) {
        this.tuBaMallOrderItemVOS = tuBaMallOrderItemVOS;
    }
}
