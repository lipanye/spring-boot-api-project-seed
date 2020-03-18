package com.company.system.core;

import com.company.system.exception.ServiceException;
import com.company.system.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

/**
 * @Author Lipanye_Arthur@163.com
 * @Date 2020/03/17 17:05
 * @Decription
 */
@Component
public class RedisCompent {
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 生成token，并存入redis
     * @return
     */
    public String createToken(){
        StringBuilder token = new StringBuilder();
        token.append("token:").append(new Random().nextInt(9999999-1111111+1)+1111111);
        redisUtil.setEx(token.toString(),token.toString(),10000L);
        return token.toString();
    }

    /**
     * 验证token：从Header中获取token，然后去redis中进行验证，如果存在则删除，保证token只能用一次
     * @param request 从header中获取token
     * @return
     */
    public boolean checkToken(HttpServletRequest request){
        String tokenName = request.getHeader("Token");
        if(StringUtils.isEmpty(tokenName)){
            throw new ServiceException("Token不能为空", new RuntimeException());
        }
        if(!redisUtil.exists(tokenName)){
            throw new ServiceException("Token不存在或已失效", new RuntimeException());
        }
        boolean isRemove = redisUtil.remove(tokenName);
        if(!isRemove){
            throw new ServiceException("删除Token失败", new RuntimeException());
        }
        return true;
    }



}
