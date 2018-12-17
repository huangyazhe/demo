package com.hyz.demo.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.UnsupportedEncodingException;

/**
 * Created with IntelliJ IDEA.
 * User: huangyazhe
 * Date: 2018/12/16
 * Time: 15:40
 */
public class MyRequest extends HttpServletRequestWrapper {

    public MyRequest(HttpServletRequest request) {
        super(request);
       /* request.setAttribute("body","888");

        System.out.println(getAttribute("body"));*/
    }

    @Override
    public String getParameter(String name) {
        /*String value = super.getParameter(name);
        if (super.getMethod().equalsIgnoreCase("GET")&&value!=null) {
            try {
                value = new String(value.getBytes("ISO-8859-1"), "utf-8");
                value = "9999";

                //request.setAttribute("body","888");


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }*/

        String value = super.getParameter(name);
        if(value.equals("123")){
            value = "000";
        }

        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (super.getMethod().equalsIgnoreCase("GET")&&values!=null) {
            try {
                int i=0;
                for (String value : values) {
                    values[i++] = new String(value.getBytes("ISO-8859-1"), "utf-8");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return values;
    }

}
