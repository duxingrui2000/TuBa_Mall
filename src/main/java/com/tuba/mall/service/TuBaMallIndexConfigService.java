package com.tuba.mall.service;


import com.tuba.mall.controller.vo.TuBaMallIndexConfigGoodsVO;
import com.tuba.mall.entity.IndexConfig;
import com.tuba.mall.util.PageQueryUtil;
import com.tuba.mall.util.PageResult;

import java.util.List;

public interface TuBaMallIndexConfigService {
    /**
     * 后台分页
     */
    PageResult getConfigsPage(PageQueryUtil pageUtil);

    String saveIndexConfig(IndexConfig indexConfig);

    String updateIndexConfig(IndexConfig indexConfig);

    IndexConfig getIndexConfigById(Long id);

    /**
     * 返回固定数量的首页配置商品对象(首页调用)
     */
    List<TuBaMallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number);

    Boolean deleteBatch(Long[] ids);
}
