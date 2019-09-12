package com.tensquare.manageer.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@Component
public class ManagerFilter extends ZuulFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("经过了后台过滤器");
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        if(request.getMethod().equals("OPTIONS")){
            return  null;
        }
        if(request.getRequestURI().indexOf("login")>0){
            return  null;
        }

        String header = request.getHeader("Authorization");
        if(header!=null && !"".equals(header)){
            if(header.startsWith("Bearer ")){
                String token = header.substring(7);
                try {
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles = (String) claims.get("roles");
                    if(roles.equals("admin")){
                     currentContext.addZuulRequestHeader("Authorization",header);
                     return  null;
                    }

                }catch (Exception e){
                    currentContext.setSendZuulResponse(false);//终止运行
                }
            }

        }
        currentContext.setSendZuulResponse(false);//终止运行
        currentContext.setResponseStatusCode(403);
        currentContext.setResponseBody("权限不足");
        currentContext.getResponse().setContentType("text/html;charset=utf-8");
        return null;
    }
}
