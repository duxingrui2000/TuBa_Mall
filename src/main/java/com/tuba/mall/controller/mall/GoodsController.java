package com.tuba.mall.controller.mall;

import com.tuba.mall.common.Constants;
import com.tuba.mall.common.ServiceResultEnum;
import com.tuba.mall.common.TuBaMallException;
import com.tuba.mall.controller.vo.SearchPageCategoryVO;
import com.tuba.mall.controller.vo.TuBaMallGoodsDetailVO;
import com.tuba.mall.entity.TuBaMallGoods;
import com.tuba.mall.service.TuBaMallCategoryService;
import com.tuba.mall.service.TuBaMallGoodsService;
import com.tuba.mall.util.BeanUtil;
import com.tuba.mall.util.PageQueryUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class GoodsController {
    
    @Resource
    private TuBaMallGoodsService tuBaMallGoodsService;
    @Resource
    private TuBaMallCategoryService tuBaMallCategoryService;
    
    @GetMapping({"/search", "/search.html"})
    public String searchPage(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        if (StringUtils.isEmpty(params.get("page"))) {
            params.put("page", 1);
        }
        params.put("limit", Constants.GOODS_SEARCH_PAGE_LIMIT);
        //封装分类数据
        if (params.containsKey("goodsCategoryId") && !StringUtils.isEmpty(params.get("goodsCategoryId") + "")) {
            Long categoryId = Long.valueOf(params.get("goodsCategoryId") + "");
            SearchPageCategoryVO searchPageCategoryVO = tuBaMallCategoryService.getCategoriesForSearch(categoryId);
            if (searchPageCategoryVO != null) {
                request.setAttribute("goodsCategoryId", categoryId);
                request.setAttribute("searchPageCategoryVO", searchPageCategoryVO);
            }
        }
        //封装参数供前端回显
        if (params.containsKey("orderBy") && !StringUtils.isEmpty(params.get("orderBy") + "")) {
            request.setAttribute("orderBy", params.get("orderBy") + "");
        }
        String keyword = "";
        //对keyword做过滤 去掉空格
        if (params.containsKey("keyword") && !StringUtils.isEmpty((params.get("keyword") + "").trim())) {
            keyword = params.get("keyword") + "";
        }
        request.setAttribute("keyword", keyword);
        params.put("keyword", keyword);
        //搜索上架状态下的商品
        params.put("goodsSellStatus", Constants.SELL_STATUS_UP);
        //封装商品数据
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        request.setAttribute("pageResult", tuBaMallGoodsService.searchTuBaMallGoods(pageUtil));
        return "mall/search";
    }
    
    @GetMapping("/goods/detail/{goodsId}")
    public String detailPage(@PathVariable("goodsId") Long goodsId, HttpServletRequest request) {
        if (goodsId < 1) {
            return "error/error_5xx";
        }
        TuBaMallGoods goods = tuBaMallGoodsService.getTuBaMallGoodsById(goodsId);
        if (goods == null) {
            TuBaMallException.fail(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
        }
        if (Constants.SELL_STATUS_UP != goods.getGoodsSellStatus()) {
            TuBaMallException.fail(ServiceResultEnum.GOODS_PUT_DOWN.getResult());
        }
        TuBaMallGoodsDetailVO goodsDetailVO = new TuBaMallGoodsDetailVO();
        BeanUtil.copyProperties(goods, goodsDetailVO);
        goodsDetailVO.setGoodsCarouselList(goods.getGoodsCarousel().split(","));
        request.setAttribute("goodsDetail", goodsDetailVO);
        return "mall/detail";
    }
}
