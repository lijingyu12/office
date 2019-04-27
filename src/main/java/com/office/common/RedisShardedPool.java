package com.office.common;

import com.office.util.PropertiesUtil;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

public class RedisShardedPool {
    private static ShardedJedisPool pool;//sharded jedis连接池
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20"));//控制最大连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","10")); //连接池中最大的空闲的实例的个数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","2")); //连接池中最小的空闲状态jedis实例的个数
    private static Boolean testOnBorrow = Boolean.valueOf(PropertiesUtil.getProperty("redis.test.borrow","true"));//在borrow一个jedis实例的时候，是否要进行验证操作，如果复制为true，则得到的jedis实例肯定是可用的
    private static Boolean testOnReturn = Boolean.valueOf(PropertiesUtil.getProperty("redis.test.return","false"));//在还的时候是否需要验证，如果赋值为true，则放回jedispool的jedis实例肯定是可用的

    private static String redis1Ip = PropertiesUtil.getProperty("redis1.ip");
    private static Integer redis1Port = Integer.valueOf(PropertiesUtil.getProperty("redis1.port"));
   /* private static String redis2Ip = PropertiesUtil.getProperty("redis2.ip");
    private static Integer redis2Port = Integer.valueOf(PropertiesUtil.getProperty("redis2.port"));*/

    private static void initPool(){
        //System.out.println(PropertiesUtil.getProperty("redis1.ip"));
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        config.setBlockWhenExhausted(true);//连接耗尽的时候是否阻塞，false会抛出异常,true阻塞直到超时,默认为true

        JedisShardInfo info1 = new JedisShardInfo(redis1Ip,redis1Port,1000*2);
    /*    JedisShardInfo info2 = new JedisShardInfo(redis2Ip,redis2Port,1000*2);*/
        List<JedisShardInfo> jedisShardInfoList = new ArrayList<JedisShardInfo>(2);
        jedisShardInfoList.add(info1);
       /* jedisShardInfoList.add(info2);*/
        //MURMUR-HASH分片策略
        pool = new ShardedJedisPool(config,jedisShardInfoList,Hashing.MURMUR_HASH,Sharded.DEFAULT_KEY_TAG_PATTERN);





    }
    static {
        initPool();
    }

    public static ShardedJedis getJedis(){
        return pool.getResource();
    }
    public static void returnResource(ShardedJedis jedis){
        pool.returnResource(jedis);

    }
    public static void returnBrokenResource(ShardedJedis jedis){
        pool.returnBrokenResource(jedis);
    }

    public static void main(String[] args) {
        ShardedJedis jedis = pool.getResource();
        for(int i = 0;i<10;i++){
            jedis.set("key"+i,"value"+i);
        }

        returnResource(jedis);

        System.out.println("program is end");
    }
}
