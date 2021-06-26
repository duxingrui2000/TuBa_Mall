package com.tuba.mall.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author dxr
 * @Description 分页管理，注意start根据当前页码page与每页条数limit计算得来
 * @Date 11:15 6.24
 **/
public class PageQueryUtil extends LinkedHashMap<String, Object> {
    
    //当前页码
    private int page;
    //每页的条数
    private int limit;
    
    public PageQueryUtil(Map<String, Object> params) {
        this.putAll(params);
        
        //分页参数
        this.page = Integer.parseInt(params.get("page").toString());
        this.limit = Integer.parseInt(params.get("limit").toString());
        this.put("start", (page - 1) * limit);
        this.put("page", page);
        this.put("limit", limit);
    }
    
    public int getPage() {
        return page;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
    public int getLimit() {
        return limit;
    }
    
    public void setLimit(int limit) {
        this.limit = limit;
    }
    
    @Override
    public String toString() {
        return "PageUtil{" +
                "page=" + page +
                ", limit=" + limit +
                '}';
    }
}
