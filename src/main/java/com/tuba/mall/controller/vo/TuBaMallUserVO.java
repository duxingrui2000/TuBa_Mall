package com.tuba.mall.controller.vo;

import java.io.Serializable;
/**
 * @Author dxr
 * @Description 对商城user实体表做个抽象简化
 * @Date 20:50 6.22
 **/
public class TuBaMallUserVO implements Serializable {
    
    private Long userId;
    
    private String nickName;
    
    private String loginName;
    
    private String introduceSign;
    
    private String address;
    
    private int shopCartItemCount;//这个字段是需要的，我要在拦截器随时更新购物车的商品数量
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getNickName() {
        return nickName;
    }
    
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    
    public String getLoginName() {
        return loginName;
    }
    
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    
    public String getIntroduceSign() {
        return introduceSign;
    }
    
    public void setIntroduceSign(String introduceSign) {
        this.introduceSign = introduceSign;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public int getShopCartItemCount() {
        return shopCartItemCount;
    }
    
    public void setShopCartItemCount(int shopCartItemCount) {
        this.shopCartItemCount = shopCartItemCount;
    }
}
