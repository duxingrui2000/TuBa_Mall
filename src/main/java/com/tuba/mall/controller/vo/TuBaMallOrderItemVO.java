package com.tuba.mall.controller.vo;

import java.io.Serializable;
/**
 * @Author dxr
 * @Description 封装了商品的具体信息，
 *  供OrderDetailVO 和 OrderItemVO使用
 * @Date 17:11 6.22
 **/
public class TuBaMallOrderItemVO implements Serializable {
    
    private Long goodsId;
    
    private Integer goodsCount;
    
    private String goodsName;
    
    private String goodsCoverImg;
    
    private Integer sellingPrice;
    
    public Long getGoodsId() {
        return goodsId;
    }
    
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
    
    public Integer getGoodsCount() {
        return goodsCount;
    }
    
    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }
    
    public String getGoodsName() {
        return goodsName;
    }
    
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
    
    public String getGoodsCoverImg() {
        return goodsCoverImg;
    }
    
    public void setGoodsCoverImg(String goodsCoverImg) {
        this.goodsCoverImg = goodsCoverImg;
    }
    
    public Integer getSellingPrice() {
        return sellingPrice;
    }
    
    public void setSellingPrice(Integer sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}
