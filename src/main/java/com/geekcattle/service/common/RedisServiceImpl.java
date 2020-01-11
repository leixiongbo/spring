/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.geekcattle.service.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * author geekcattle
 * date 2017/3/22 0022 下午 16:16
 */
@Service
public class RedisServiceImpl implements RedisService {

    private static Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Autowired
    private JedisPool jedisPool;

    @Override
    public Jedis getResource() {
        return jedisPool.getResource();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void returnResource(Jedis jedis) {
        if(jedis != null){
            jedisPool.returnResourceObject(jedis);
        }
    }

    @Override
    public void set(String key, String value) {
        Jedis jedis=null;
        try{
            jedis = getResource();
            jedis.set(key, value);
            logger.info("Redis set success - " + key + ", value:" + value);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Redis set error: "+ e.getMessage() +" - " + key + ", value:" + value);
        }finally{
            returnResource(jedis);
        }
    }

    @Override
    public String get(String key) {
        String result = null;
        Jedis jedis=null;
        try{
            jedis = getResource();
            result = jedis.get(key);
            logger.info("Redis get success - " + key + ", value:" + result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Redis set error: "+ e.getMessage() +" - " + key + ", value:" + result);
        }finally{
            returnResource(jedis);
        }
        return result;
    }

    @Override
    public void setMap(String key, Map value, int seconds) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.hmset(key, value);//向redis中存入键
            jedis.expire(key, seconds);
            logger.info("Redis set success - " + key + ", value:" + value);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Redis set error: " + e.getMessage() + " - " + key + ", value:" + value);
        } finally {
            returnResource(jedis);
        }
    }

    @Override
    public void setMapField(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.hset(key, field, value);//向redis中存入键
            logger.info("Redis set success - " + key + ", value:" + value);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Redis set error: " + e.getMessage() + " - " + key + ", value:" + value);
        } finally {
            returnResource(jedis);
        }
    }



    @Override
    public Map getMap(String key) {
        Map result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.hgetAll(key);
            logger.info("Redis get success - " + key + ", value:" + result);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Redis set error: " + e.getMessage() + " - " + key + ", value:" + result);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    @Override
    public void remove(String key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.del(key);
            logger.info("Redis remove success - " + key);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Redis remove error: " + e.getMessage() + " - " + key);
        } finally {
            returnResource(jedis);
        }
    }

}
