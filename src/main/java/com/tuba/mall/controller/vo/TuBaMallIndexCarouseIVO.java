package com.tuba.mall.controller.vo;

import java.io.Serializable;
/**
 * @Author dxr
 * @Description 对图吧商城首页信息的（轮播图）做个封装
 **/
public class TuBaMallIndexCarouseIVO implements Serializable {
    private String carouselUrl;//该轮播图的相对路径地址
    private String redirectUrl;//点击可以跳转的路径，默认为空
    
    public String getCarouselUrl() {
        return carouselUrl;
    }
    
    public void setCarouselUrl(String carouselUrl) {
        this.carouselUrl = carouselUrl;
    }
    
    public String getRedirectUrl() {
        return redirectUrl;
    }
    
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
