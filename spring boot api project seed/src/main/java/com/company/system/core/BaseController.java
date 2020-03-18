package com.company.system.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Lipanye_Arthur@163.com
 * @Date 2020/03/17 17:45
 * @Decription 公共Controller，里面包含生成的Token 方法
 */
public class BaseController {
    @Autowired
    private RedisCompent redisCompent;

    @PostMapping("/getToken")
    @ResponseBody
    public String getToken(){
        String token = redisCompent.createToken();
        Map<String,String> map = new HashMap<>();
        map.put("token",token);
        return map.toString();
    }

}
