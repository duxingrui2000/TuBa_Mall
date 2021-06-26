package com.tuba.mall.controller.common;

import com.tuba.mall.common.Constants;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author dxr
 * @Description 作为login页面中请求验证码图片流交给th:src显示
 * @Date 14:24 6.23
 **/
@Controller
public class CommonController {
    
    @GetMapping("/common/kaptcha")
    public void defaultKaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);//设置过期的时间期限
        response.setContentType("image/png");
    
        // 三个参数分别为宽、高、位数
        SpecCaptcha captcha = new SpecCaptcha(150, 40, 4);
    
        // 设置类型 数字和字母混合
        captcha.setCharType(Captcha.TYPE_DEFAULT);
    
        //设置字体
        captcha.setCharType(Captcha.FONT_9);
    
        // 验证码存入session
        request.getSession().setAttribute("verifyCode", captcha.text().toLowerCase());
    
        // 输出图片流
        captcha.out(response.getOutputStream());
        
    }
    
    @GetMapping("/common/mall/kaptcha")
    public void mallKaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png");
        
        // 三个参数分别为宽、高、位数
        SpecCaptcha captcha = new SpecCaptcha(110, 40, 4);
        
        // 设置类型 数字和字母混合
        captcha.setCharType(Captcha.TYPE_DEFAULT);
        
        //设置字体
        captcha.setCharType(Captcha.FONT_9);
        
        // 验证码存入session
        request.getSession().setAttribute(Constants.MALL_VERIFY_CODE_KEY, captcha.text().toLowerCase());
        
        // 输出图片流
        captcha.out(response.getOutputStream());
    }
    
}
