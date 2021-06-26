package com.tuba.mall.controller.mall;

import com.tuba.mall.common.Constants;
import com.tuba.mall.common.IndexConfigTypeEnum;
import com.tuba.mall.controller.vo.TuBaMallIndexCarouseIVO;
import com.tuba.mall.controller.vo.TuBaMallIndexCategoryVO;
import com.tuba.mall.controller.vo.TuBaMallIndexConfigGoodsVO;
import com.tuba.mall.service.TuBaMallCarouselService;
import com.tuba.mall.service.TuBaMallCategoryService;
import com.tuba.mall.service.TuBaMallIndexConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Resource
    private TuBaMallCarouselService tuBaMallCarouselService;
    
    @Resource
    private TuBaMallIndexConfigService tuBaMallIndexConfigService;
    
    @Resource
    private TuBaMallCategoryService tuBaMallCategoryService;
    
    @GetMapping({"/index", "/", "/index.html"})
    public String indexPage(HttpServletRequest request) {
        List<TuBaMallIndexCategoryVO> categories = tuBaMallCategoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
            return "error/error_5xx";
        }
        List<TuBaMallIndexCarouseIVO> carousels = tuBaMallCarouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<TuBaMallIndexConfigGoodsVO> hotGoodses = tuBaMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        List<TuBaMallIndexConfigGoodsVO> newGoodses = tuBaMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        List<TuBaMallIndexConfigGoodsVO> recommendGoodses = tuBaMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.INDEX_GOODS_RECOMMOND_NUMBER);
        request.setAttribute("categories", categories);//分类数据
        request.setAttribute("carousels", carousels);//轮播图
        request.setAttribute("hotGoodses", hotGoodses);//热销商品
        request.setAttribute("newGoodses", newGoodses);//新品
        request.setAttribute("recommendGoodses", recommendGoodses);//推荐商品
        return "mall/index";
    }
}
