package com.lachesis.mnis.core;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.Jedis;

public class RedisTest extends SpringTest{

	private Jedis jedis;
	
	@Autowired
	private RedisTemplate redisTemplate;

	@Before
	public void beforeClass() {
		jedis = new Jedis("10.2.10.77", 6379);// 连接redis
		jedis.auth("redis102220");// 验证密码
	}
	
    @Test  
    public void testSet() {  
        jedis.set("key", "value");  
    }  
      
    @Test  
    public void testGet() {  
        System.out.println(jedis.get("key"));  
    }  
      
    @Test  
    public void testRenameKey() {  
        jedis.rename("key", "key1");  
    }  
      
  
    @Test  
    public void testDel() {  
        jedis.del("key1");  
    }  

    @Test  
    public void testKeys() {  
        System.out.println(jedis.keys("*"));  
    }  
    
    @Test
    public void testRedis(){
    	redisTemplate.getConnectionFactory().getConnection();
    }
}
