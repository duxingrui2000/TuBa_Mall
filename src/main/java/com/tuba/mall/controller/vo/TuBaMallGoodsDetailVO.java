package com.tuba.mall.controller.vo;
/**
 * @Author dxr
 * @Description 对商品信息做个抽象封装，方便前端调用对象
 * @Date 19:55 6.21
 * @Param
 * @return
 **/
public class TuBaMallGoodsDetailVO {
    private Long goodsId;
    
    private String goodsName;
    
    private String goodsIntro;
    
    private String goodsCoverImg;
    
    private String[] goodsCarouselList;
    
    private Integer sellingPrice;
    
    private Integer originalPrice;
    
    private String goodsDetailContent;
    
    public Long getGoodsId() {
        return goodsId;
    }
    
    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
    
    public String getGoodsName() {
        return goodsName;
    }
    
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
    
    public String getGoodsIntro() {
        return goodsIntro;
    }
    
    public void setGoodsIntro(String goodsIntro) {
        this.goodsIntro = goodsIntro;
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
    
    public Integer getOriginalPrice() {
        return originalPrice;
    }
    
    public void setOriginalPrice(Integer originalPrice) {
        this.originalPrice = originalPrice;
    }
    
    public String getGoodsDetailContent() {
        return goodsDetailContent;
    }
    
    public void setGoodsDetailContent(String goodsDetailContent) {
        this.goodsDetailContent = goodsDetailContent;
    }
    
    public String[] getGoodsCarouselList() {
        return goodsCarouselList;
    }
    
    public void setGoodsCarouselList(String[] goodsCarouselList) {
        this.goodsCarouselList = goodsCarouselList;
    }
}
