package com.tuba.mall.service;

import com.tuba.mall.entity.TuBaMallGoods;
import com.tuba.mall.util.PageQueryUtil;
import com.tuba.mall.util.PageResult;

import java.util.List;

public interface TuBaMallGoodsService {
    /**
     * 后台分页
     */
    PageResult getTuBaMallGoodsPage(PageQueryUtil pageUtil);

    /**
     * 添加商品
     */
    String saveTuBaMallGoods(TuBaMallGoods goods);

    /**
     * 批量新增商品数据
     */
    void batchSaveTuBaMallGoods(List<TuBaMallGoods> tuBaMallGoodsList);

    /**
     * 修改商品信息
     */
    String updateTuBaMallGoods(TuBaMallGoods goods);

    /**
     * 获取商品详情
     */
    TuBaMallGoods getTuBaMallGoodsById(Long id);

    /**
     * 批量修改销售状态(上架下架)
     */
    Boolean batchUpdateSellStatus(Long[] ids, int sellStatus);

    /**
     * 商品搜索
     */
    PageResult searchTuBaMallGoods(PageQueryUtil pageUtil);
}
