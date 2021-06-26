package com.tuba.mall.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author dxr
 * @Description 自定义异常控制器，是最为灵活的控制错误页面的方式
 * 新建一个错误处理 Controller 来覆盖默认的处理方式，
 * ErrorController 是 Spring Boot 提供的一种错误页面配置方案，
 * 使用 ErrorController 这种方式是比较简单的一种方案，
 * 在 controller/common 包中新增 ErrorPageController.java 并实现 ErrorController 类
 * @Date 14:58 6.23
 **/
public class ErrorPageController implements ErrorController {
    
    private static ErrorPageController errorPageController;
    
    private final static String ERROR_PATH = "/error";
    
    @Autowired
    private ErrorAttributes errorAttributes;
    
    public ErrorPageController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }
    
    public ErrorPageController() {
        if (errorPageController == null) {
            errorPageController = new ErrorPageController(errorAttributes);
        }
    }
    
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
    
    @RequestMapping(value = ERROR_PATH, produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request) {
        HttpStatus status = getStatus(request);
        if (HttpStatus.BAD_REQUEST == status) {
            return new ModelAndView("error/error_400");
        } else if (HttpStatus.NOT_FOUND == status) {
            return new ModelAndView("error/error_404");
        } else {
            return new ModelAndView("error/error_5xx");
        }
    }
    
    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode != null) {
            return HttpStatus.valueOf(statusCode);
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
