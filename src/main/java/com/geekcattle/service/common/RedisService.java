/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.geekcattle.service.common;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * author geekcattle
 * date 2017/3/22 0022 下午 15:38
 */

public interface  RedisService {

    public Jedis getResource();

    public void returnResource(Jedis jedis);

    public void set(String key, String value);

    public String get(String key);

    public void setMap(String key, Map value, int seconds);

    public void setMapField(String key, String field, String value);

    public Map getMap(String key);

    public void remove(String key);

}
