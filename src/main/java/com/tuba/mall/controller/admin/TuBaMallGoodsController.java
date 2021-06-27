package com.tuba.mall.controller.admin;

import com.tuba.mall.common.Constants;
import com.tuba.mall.common.ServiceResultEnum;
import com.tuba.mall.common.TuBaMallCategoryLevelEnum;
import com.tuba.mall.common.TuBaMallException;
import com.tuba.mall.entity.GoodsCategory;
import com.tuba.mall.entity.TuBaMallGoods;
import com.tuba.mall.service.TuBaMallCategoryService;
import com.tuba.mall.service.TuBaMallGoodsService;
import com.tuba.mall.util.PageQueryUtil;
import com.tuba.mall.util.PageResult;
import com.tuba.mall.util.Result;
import com.tuba.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class TuBaMallGoodsController {
    @Resource
    private TuBaMallGoodsService tuBaMallGoodsService;
    @Resource
    private TuBaMallCategoryService tuBaMallCategoryService;
    
    @GetMapping("/goods")
    public String goodsPage(HttpServletRequest request) {
        request.setAttribute("path", "tuba_mall_goods");
        return "admin/tuba_mall_goods";
    }
    
    @GetMapping(value = "/goods/searchId")
    public String  goodsSearch(@RequestParam("goodsId") String goodsId, @RequestParam Map<String, Object> params, HttpSession session){
        Long id = Long.parseLong(goodsId);
        return "redirect:/admin/goods/edit/"+id;
    }
    
    @GetMapping("/goods/search")
    public String goodsSearch(HttpServletRequest request){
        request.setAttribute("path", "tuba_mall_goodsSearch");
        return "admin/tuba_mall_goodsSearch";
    }
    
    @GetMapping("/goods/edit")
    public String edit(HttpServletRequest request) {
        request.setAttribute("path", "edit");
        //查询所有的一级分类
        List<GoodsCategory> firstLevelCategories = tuBaMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), TuBaMallCategoryLevelEnum.LEVEL_ONE.getLevel());
        if (!CollectionUtils.isEmpty(firstLevelCategories)) {
            //查询一级分类列表中第一个实体的所有二级分类
            List<GoodsCategory> secondLevelCategories = tuBaMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), TuBaMallCategoryLevelEnum.LEVEL_TWO.getLevel());
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                //查询二级分类列表中第一个实体的所有三级分类
                List<GoodsCategory> thirdLevelCategories = tuBaMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), TuBaMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                request.setAttribute("firstLevelCategories", firstLevelCategories);
                request.setAttribute("secondLevelCategories", secondLevelCategories);
                request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                request.setAttribute("path", "goods-edit");
                return "admin/tuba_mall_goods_edit";
            }
        }
        return "error/error_5xx";
    }
    
    @GetMapping("/goods/edit/{goodsId}")
    public String edit(HttpServletRequest request, @PathVariable("goodsId") Long goodsId) {
        request.setAttribute("path", "edit");
        TuBaMallGoods tuBaMallGoods = tuBaMallGoodsService.getTuBaMallGoodsById(goodsId);
        if (tuBaMallGoods == null) {
            return "error/error_400";
        }
        if (tuBaMallGoods.getGoodsCategoryId() > 0) {
            if (tuBaMallGoods.getGoodsCategoryId() != null || tuBaMallGoods.getGoodsCategoryId() > 0) {
                //有分类字段则查询相关分类数据返回给前端以供分类的三级联动显示
                GoodsCategory currentGoodsCategory = tuBaMallCategoryService.getGoodsCategoryById(tuBaMallGoods.getGoodsCategoryId());
                //商品表中存储的分类id字段为三级分类的id，不为三级分类则是错误数据
                if (currentGoodsCategory != null && currentGoodsCategory.getCategoryLevel() == TuBaMallCategoryLevelEnum.LEVEL_THREE.getLevel()) {
                    //查询所有的一级分类
                    List<GoodsCategory> firstLevelCategories = tuBaMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), TuBaMallCategoryLevelEnum.LEVEL_ONE.getLevel());
                    //根据parentId查询当前parentId下所有的三级分类
                    List<GoodsCategory> thirdLevelCategories = tuBaMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(currentGoodsCategory.getParentId()), TuBaMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                    //查询当前三级分类的父级二级分类
                    GoodsCategory secondCategory = tuBaMallCategoryService.getGoodsCategoryById(currentGoodsCategory.getParentId());
                    if (secondCategory != null) {
                        //根据parentId查询当前parentId下所有的二级分类
                        List<GoodsCategory> secondLevelCategories = tuBaMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondCategory.getParentId()), TuBaMallCategoryLevelEnum.LEVEL_TWO.getLevel());
                        //查询当前二级分类的父级一级分类
                        GoodsCategory firestCategory = tuBaMallCategoryService.getGoodsCategoryById(secondCategory.getParentId());
                        if (firestCategory != null) {
                            //所有分类数据都得到之后放到request对象中供前端读取
                            request.setAttribute("firstLevelCategories", firstLevelCategories);
                            request.setAttribute("secondLevelCategories", secondLevelCategories);
                            request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                            request.setAttribute("firstLevelCategoryId", firestCategory.getCategoryId());
                            request.setAttribute("secondLevelCategoryId", secondCategory.getCategoryId());
                            request.setAttribute("thirdLevelCategoryId", currentGoodsCategory.getCategoryId());
                        }
                    }
                }
            }
        }
        if (tuBaMallGoods.getGoodsCategoryId() == 0) {
            //查询所有的一级分类
            List<GoodsCategory> firstLevelCategories = tuBaMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), TuBaMallCategoryLevelEnum.LEVEL_ONE.getLevel());
            if (!CollectionUtils.isEmpty(firstLevelCategories)) {
                //查询一级分类列表中第一个实体的所有二级分类
                List<GoodsCategory> secondLevelCategories = tuBaMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), TuBaMallCategoryLevelEnum.LEVEL_TWO.getLevel());
                if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                    //查询二级分类列表中第一个实体的所有三级分类
                    List<GoodsCategory> thirdLevelCategories = tuBaMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), TuBaMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                    request.setAttribute("firstLevelCategories", firstLevelCategories);
                    request.setAttribute("secondLevelCategories", secondLevelCategories);
                    request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                }
            }
        }
        request.setAttribute("goods", tuBaMallGoods);
        request.setAttribute("path", "goods-edit");
        return "admin/tuba_mall_goods_edit";
    }
    
    /**
     * 列表
     */
    @RequestMapping(value = "/goods/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(tuBaMallGoodsService.getTuBaMallGoodsPage(pageUtil));
    }
    
    /**
     * 添加
     */
    @RequestMapping(value = "/goods/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody TuBaMallGoods tuBaMallGoods) {
        if (StringUtils.isEmpty(tuBaMallGoods.getGoodsName())
                || StringUtils.isEmpty(tuBaMallGoods.getGoodsIntro())
                || StringUtils.isEmpty(tuBaMallGoods.getTag())
                || Objects.isNull(tuBaMallGoods.getOriginalPrice())
                || Objects.isNull(tuBaMallGoods.getGoodsCategoryId())
                || Objects.isNull(tuBaMallGoods.getSellingPrice())
                || Objects.isNull(tuBaMallGoods.getStockNum())
                || Objects.isNull(tuBaMallGoods.getGoodsSellStatus())
                || StringUtils.isEmpty(tuBaMallGoods.getGoodsCoverImg())
                || StringUtils.isEmpty(tuBaMallGoods.getGoodsDetailContent())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = tuBaMallGoodsService.saveTuBaMallGoods(tuBaMallGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }
    
    
    /**
     * 修改
     */
    @RequestMapping(value = "/goods/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody TuBaMallGoods tuBaMallGoods) {
        if (Objects.isNull(tuBaMallGoods.getGoodsId())
                || StringUtils.isEmpty(tuBaMallGoods.getGoodsName())
                || StringUtils.isEmpty(tuBaMallGoods.getGoodsIntro())
                || StringUtils.isEmpty(tuBaMallGoods.getTag())
                || Objects.isNull(tuBaMallGoods.getOriginalPrice())
                || Objects.isNull(tuBaMallGoods.getSellingPrice())
                || Objects.isNull(tuBaMallGoods.getGoodsCategoryId())
                || Objects.isNull(tuBaMallGoods.getStockNum())
                || Objects.isNull(tuBaMallGoods.getGoodsSellStatus())
                || StringUtils.isEmpty(tuBaMallGoods.getGoodsCoverImg())
                || StringUtils.isEmpty(tuBaMallGoods.getGoodsDetailContent())) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        String result = tuBaMallGoodsService.updateTuBaMallGoods(tuBaMallGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }
    
    /**
     * 详情
     */
    @GetMapping("/goods/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Long id) {
        TuBaMallGoods goods = tuBaMallGoodsService.getTuBaMallGoodsById(id);
        if (goods == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(goods);
    }
    
    /**
     * 批量修改销售状态
     */
    @RequestMapping(value = "/goods/status/{sellStatus}", method = RequestMethod.PUT)
    @ResponseBody
    public Result delete(@RequestBody Long[] ids, @PathVariable("sellStatus") int sellStatus) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (sellStatus != Constants.SELL_STATUS_UP && sellStatus != Constants.SELL_STATUS_DOWN) {
            return ResultGenerator.genFailResult("状态异常！");
        }
        if (tuBaMallGoodsService.batchUpdateSellStatus(ids, sellStatus)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("修改失败");
        }
    }
}
