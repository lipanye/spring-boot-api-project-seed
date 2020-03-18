package com.company.system.core;


import com.alibaba.fastjson.JSON;
import com.company.system.annotation.TokenAnnotation;
import com.company.system.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Author Lipanye_Arthur@163.com
 * @Date 2020/03/17 16:58
 * @Decription Token拦截器
 */
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisCompent redisCompent;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler)
            throws Exception {
        if(!(handler instanceof  HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        TokenAnnotation tokenAnnotation = method.getAnnotation(TokenAnnotation.class);
        if(tokenAnnotation!= null ){
            try {
                return redisCompent.checkToken(httpServletRequest);
            }catch (Exception e){
                log.warn("签名认证失败，请求接口: {}，请求IP：{}，请求参数：{}",
                        httpServletRequest.getRequestURI(),getIpAddress(httpServletRequest), JSON.toJSONString(httpServletRequest.getParameterMap()));
                throw new ServiceException(e.getMessage(),e);
            }
        }
        return true;
    }

    private Object getIpAddress(HttpServletRequest request) {
        String ip =request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)){
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if(ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)){
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if(ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        //如果是多级代理，那么取第一个ip为客户端ip
        if(ip != null && ip.indexOf(",") != -1){
            ip = ip.substring(0,ip.indexOf(",")).trim();
        }
        return ip;
    }


    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {

    }
}
