package com.tuba.mall.util;

import java.io.Serializable;

/**
 * @Author dxr
 * @Description 响应结果集封装，主要的应用场景是Api请求，
 * 现在主流的Api请求方式是http-json，而比较主流的接口交互方式是Restful,
 * 在这种交互模式下，我们对接口响应做统一封装，
 * 可以使得代码更加规范，接口响应数据的解析更为简洁。
 * @Date 15:23 6.23
 **/
public class Result<T> implements Serializable {
    private int resultCode;
    private String message;
    private T data;
    
    public Result(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }
    
    public Result() {
    }
    
    public int getResultCode() {
        return resultCode;
    }
    
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    @Override
    public String toString() {
        return "Result{" +
                "resultCode=" + resultCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
