package com.hyz.demo.filter;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: huangyazhe
 * Date: 2018/12/16
 * Time: 15:20
 */

@Component
@ServletComponentScan
@WebFilter(urlPatterns = "/",filterName = "loginFilter")
public class Mfiter implements Filter {
    private String encoding="UTF-8";
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("1");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("request 1 body: "+servletRequest.getParameter("body"));
        //1 解决中文乱码问题：1)只能解决POST乱码2)响应乱码
        servletRequest.setCharacterEncoding(encoding);
        servletResponse.setContentType("text/html;charset="+encoding);

        //2 创建自定义的Request对象 ：解决get乱码
        MyRequest myRequest = new MyRequest((HttpServletRequest) servletRequest);

        System.out.println("request 2 body: "+servletRequest.getParameter("body"));
        //3 放行
        filterChain.doFilter(myRequest, servletResponse);

    }

    @Override
    public void destroy() {
        System.out.println("3");
    }
}
