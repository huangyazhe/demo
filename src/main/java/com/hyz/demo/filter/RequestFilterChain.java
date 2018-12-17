package com.hyz.demo.filter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: huangyazhe
 * Date: 2018/12/16
 * Time: 15:59
 */
public class RequestFilterChain implements FilterChain {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException {
        servletRequest.getParameter("body");
        servletResponse.setContentType("body");
    }
}
