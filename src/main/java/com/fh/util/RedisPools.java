package com.fh.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPools {
    //redis连接池
    private  static JedisPool jedispool;

    private RedisPools(){
    }
    //静态代码快
    static {
        JedisPoolConfig poolConfig=new JedisPoolConfig();
        //最大连接数
        poolConfig.setMaxTotal(200);
        //最大空闲时间
        poolConfig.setMaxIdle(20);
        //最小空闲时间
        poolConfig.setMinIdle(5);
        jedispool=new JedisPool("192.168.184.135");
    };
    public static Jedis getredis(){
        Jedis resource = jedispool.getResource();
        return resource;
    }
    //关闭连接
    public static void  returnjedis(Jedis jedis){
        if(jedis!=null){
             jedis.close();
        }
    }


}
