package com.company.system.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    private Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    @Autowired
    private RedisTemplate redisTemplate;

    public int delNX(String key,String value){
        try{
            StringBuilder sbScript = new StringBuilder();
            sbScript.append("if redis.call('get','").append(key).append("')").append("=='").append(value).append("'").
                    append(" then ").
                    append(" return redis.call('del','").append(key).append("')").
                    append(" else ").
                    append(" return 0").
                    append(" end");

            //return Integer.valueOf(jedis.eval(sbScript.toString()).toString());
        }catch (Exception e){
        }
        return 0;
    }

    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    public boolean set (final String key,Object value){
        boolean result = false;
        try {
            ValueOperations<Serializable,Object> operations = redisTemplate.opsForValue();
            operations.set(key,value);
            result = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存设置时效时间
     * @param key
     * @param value
     * @param expireTime
     * @return
     */
    public boolean setEx(final String key,Object value,Long expireTime){
        boolean result = false;
        try{
            ValueOperations<Serializable,Object> operations = redisTemplate.opsForValue();
            operations.set(key,value);
            redisTemplate.expire(key,expireTime, TimeUnit.SECONDS);
            result=true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断缓存中是否有对应的value
     * @param key
     * @return
     */
    public boolean exists(final String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     * @param key
     * @return
     */
    public Object get(final String key){
        Object result = null;
        ValueOperations<Serializable,Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * 删除对应的value
     * @param key
     * @return
     */
    public boolean remove(final String key){
        if(exists(key)){
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }



}
