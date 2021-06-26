package com.tuba.mall.controller.common;

import com.tuba.mall.common.TuBaMallException;
import com.tuba.mall.util.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author dxr
 * @Description 自定义一个全局异常处理类
 * 全局异常处理的主要应用场景也是Api请求，
 * 例如：当我们在service中处理业务流程时，需要向接口请求返回一些异常信息，
 * 但是局限于service方法的返回值无法接收此类信息，
 * 此时使用全局异常就是最好的解决方案。我们可以定义一个RuntimeException类型的业务异常，
 * 由于此类异常不需要做异常处理，所以对我们原有的代码没有任何的侵入，
 * 只需在需要返回异常信息的位置，手动抛出一个业务异常，
 * 这个业务异常携带着我们事先定义好的响应码对象，
 * 最终由全局异常处理器进行捕捉，通过响应结果集工具类返回给接口请求。
 * @Date 15:17 6.23
 **/
/*@ControllerAdvice是一个特殊的@Component，用于标识一个类，这个类中被以下三种注解标识的方法：
@ExceptionHandler，@InitBinder，@ModelAttribute，将作用于所有的@Controller类的接口上。
*/
@RestControllerAdvice
public class TuBaMallExceptionHandler {
    /*当一个Controller中有方法加了@ExceptionHandler之后，
    这个Controller其他方法中没有捕获的异常就会以参数的形式传入加了@ExceptionHandler注解的那个方法中。
    */
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e, HttpServletRequest req) {
        Result result = new Result();
        result.setResultCode(500);
        //区分是否为自定义异常
        if (e instanceof TuBaMallException) {
            result.setMessage(e.getMessage());
        } else {
            e.printStackTrace();
            result.setMessage("未知异常");
        }
        //检查请求是否为ajax, 如果是 ajax 请求则返回 Result json串, 如果不是 ajax 请求则返回 error 视图
        String contentTypeHeader = req.getHeader("Content-Type");
        String acceptHeader = req.getHeader("Accept");
        String xRequestedWith = req.getHeader("X-Requested-With");
        if ((contentTypeHeader != null && contentTypeHeader.contains("application/json"))
                || (acceptHeader != null && acceptHeader.contains("application/json"))
                || "XMLHttpRequest".equalsIgnoreCase(xRequestedWith)) {
            return result;
        } else {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("message", e.getMessage());
            modelAndView.addObject("url", req.getRequestURL());
            modelAndView.addObject("stackTrace", e.getStackTrace());
            modelAndView.addObject("author", "四川大学11小组");
            modelAndView.addObject("ltd", "图吧商城");
            modelAndView.setViewName("error/error");
            return modelAndView;
        }
    }
}
