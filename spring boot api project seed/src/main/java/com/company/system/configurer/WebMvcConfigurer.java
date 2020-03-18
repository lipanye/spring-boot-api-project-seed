package com.company.system.configurer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.company.system.core.Result;
import com.company.system.core.ResultCode;
import com.company.system.core.TokenInterceptor;
import com.company.system.exception.ServiceException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author Lipanye_Arthur@163.com
 * @Date 2020/3/10 0010 14:40
 * @Decription Spring MVC 配置
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {
    protected final Logger logger = LoggerFactory.getLogger(WebMvcConfigurer.class);
    @Autowired
    private TokenInterceptor tokenInterceptor;

    /**
     * 当前激活的配置
     */
    @Value("${spring.profiles.active}")
    private String env;
    /**
     * 定义加密的密钥，没写配置文件的原因在于，防止有人恶意更改
     */
    private final String secret="Potato";

    /**
     * 使用阿里的FastJson 作为Json MessageConverter，这样就不会覆盖掉Spring boot的一些默认配置
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        /**
         * SerializerFeature.WriteMapNullValue -->保留空的字段
         * SerializerFeature.WriteNullStringAsEmpty --> String null ==>> ""
         * SerializerFeature.WriteNullNumberAsZero -->Number null ==>> 0
         */
        config.setSerializerFeatures(
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero);
        converter.setFastJsonConfig(config);
        converter.setDefaultCharset(Charset.forName("UTF-8"));
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
        converters.add(converter);
    }

    /**
     * 统一 异常处理
     * @param exceptionResolvers
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new HandlerExceptionResolver() {
            @Override
            public ModelAndView resolveException(HttpServletRequest httpServletRequest,
                                                 HttpServletResponse httpServletResponse, Object handler, Exception e) {
                Result result = new Result();
                //业务失败异常记录
                if(e instanceof ServiceException){
                    result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
                    logger.info(e.getMessage());
                } else if(e instanceof NoHandlerFoundException){
                    result.setCode(ResultCode.NOT_FOUND).setMessage("接口["+httpServletRequest.getRequestURI()+"] 不存在");
                } else if(e instanceof ServletException){
                    result.setCode(ResultCode.FAIL).setMessage(e.getMessage());
                }else {
                    result.setCode(ResultCode.INTERNAL_SERVER_ERROR)
                            .setMessage("接口["+httpServletRequest.getRequestURI()+"] 内部错误，请联系管理员");
                    String message;
                    if(handler instanceof HandlerMethod){
                        HandlerMethod method = (HandlerMethod)handler;
                        message =String.format("接口[%s]出现异常，方法:%s,%s,异常摘要：%s",
                                httpServletRequest.getRequestURL(),
                                method.getBean().getClass().getName(),
                                method.getMethod().getName(),
                                e.getMessage());
                    }else{
                        message = e.getMessage();
                    }
                    logger.error(message,e);
                }
                responseResult(httpServletResponse,result);
                return new ModelAndView();
            }
        });
    }

    /**
     * 解决跨域问题
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //registry.addMapping("/**");
    }

    /**
     * 响应报文
     * @param httpServletResponse
     * @param result
     */
    private void responseResult(HttpServletResponse httpServletResponse, Result result) {
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setHeader("Content-type","application/json;charset=UTF-8");
        httpServletResponse.setStatus(200);
        try {
            httpServletResponse.getWriter().write(JSON.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //通过接口注解认证拦截器
        registry.addInterceptor(tokenInterceptor);

        //开发环境忽略校验
        //if(!"dev".equals(env)){
            //简单接口签名认证拦截器，实际项目中可以使用Json Web Token或其他更好的方式替代。
            /*registry.addInterceptor(new HandlerInterceptorAdapter() {
                @Override
                public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                    //验证签名
                    boolean pass =validateSign(request);
                    if(pass){
                        return true;
                    }else{
                        logger.warn("签名认证失败，请求接口: {}，请求IP：{}，请求参数：{}",
                                request.getRequestURI(),getIpAddress(request),JSON.toJSONString(request.getParameterMap()));
                        Result result = new Result();
                        result.setCode(ResultCode.UNAUTHORIZED).setMessage("签名认证失败");
                        responseResult(response,result);
                        return false;
                    }
                }
            });*/
            //registry.addInterceptor(tokenInterceptor);
        //}
    }

    /*
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
    }*/

    /**
     * 一个简单签名认证，规则：
     *1、将请求参数按照ASCII码排序
     * 2、拼接为a=value&b=value..这样的字符串（不包含sign）
     * 3\混合密钥(secret)j进行md5获得签名，与请求的签名进行比较
     * @param request
     * @return
     */
    /*private boolean validateSign(HttpServletRequest request) {
        //获取请求的签名：例如sign=19e907700db7ad91318424a97c54ed57
        String requestSign = request.getParameter("sign");
        if(StringUtils.isEmpty(requestSign)){
            return false;
        }
        //将请求过来的参数转成List，删除sign参数，排序，Collections.sort(keys);执行排序动作
        List<String> keys = new ArrayList<>(request.getParameterMap().keySet());
        keys.remove("sign");
        Collections.sort(keys);

        StringBuilder sb = new StringBuilder();
        for(String key : keys){
            //拼接字符串
            sb.append(key).append("=").append(request.getParameter(key)).append("&");
        }
        String linkString = sb.toString();
        //去掉最后一个‘&’
        linkString = StringUtils.substring(linkString,0,linkString.length()-1);
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>linkString："+linkString);
        String sign = DigestUtils.md5Hex(linkString+secret);
        return StringUtils.equals(sign,requestSign);
    }*/


}
