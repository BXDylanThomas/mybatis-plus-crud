package com.abit.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@PropertySource("classpath:config/redis.properties")
public class RedisConfig {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private Integer port;

    @Value("${redis.password}")
    private String password;

    @Value("${redis.timeout}")
    private Integer timeout;

    @Value("${redis.maxIdle}")
    private Integer maxIdle;

    @Value("${redis.maxTotal}")
    private Integer maxTotal;

    @Value("${redis.maxWaitMillis}")
    private Integer maxWaitMillis;

    @Value("${redis.minEvictableIdleTimeMillis}")
    private Integer minEvictableIdleTimeMillis;

    @Value("${redis.numTestsPerEvictionRun}")
    private Integer numTestsPerEvictionRun;

    @Value("${redis.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;

    @Value("${redis.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${redis.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.redis.cluster.nodes}")
    private String clusterNodes;

    @Value("${spring.redis.cluster.max-redirects}")
    private Integer mmaxRedirectsac;

    @Autowired
    @Qualifier(value = "redisConnectionFactory")
    private JedisConnectionFactory redisConnectionFactory;

    @Autowired
    @Qualifier(value = "tranRedisConnectionFactory")
    private TransJedisConnectionFactory tranRedisConnectionFactory;

    //----------------------------Common Config Begin---------------------------------------
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲数
        jedisPoolConfig.setMaxIdle(maxIdle);
        // 连接池的最大数据库连接数
        jedisPoolConfig.setMaxTotal(maxTotal);
        // 最大建立连接等待时间
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        // 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        jedisPoolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        jedisPoolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        // 是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        // 在空闲时检查有效性, 默认false
        jedisPoolConfig.setTestWhileIdle(testWhileIdle);
        return jedisPoolConfig;
    }

    //----------------------------Common Config End---------------------------------------

    @Bean(name = "redisConnectionFactory")
    public JedisConnectionFactory redisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
        initFactory(jedisPoolConfig, jedisConnectionFactory);
        return jedisConnectionFactory;
    }

    @Bean(name = "tranRedisConnectionFactory")
    public TransJedisConnectionFactory tranRedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        TransJedisConnectionFactory transJedisConnectionFactory = new TransJedisConnectionFactory(jedisPoolConfig);
        initFactory(jedisPoolConfig, transJedisConnectionFactory);
        return transJedisConnectionFactory;
    }

    private void initFactory(JedisPoolConfig jedisPoolConfig, JedisConnectionFactory jedisConnectionFactory) {
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        jedisConnectionFactory.setHostName(host);
        jedisConnectionFactory.setPort(port);
        jedisConnectionFactory.setPassword(password);
        jedisConnectionFactory.setTimeout(timeout);
    }



    /**
     * 实例化 RedisTemplate - 没事务 - Jackson序列化方式, 用于存储大多数的表对象和分页缓存
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        initDomainRedisTemplate(redisTemplate, redisConnectionFactory, new GenericJackson2JsonRedisSerializer(), false);
        return redisTemplate;
    }

    /**
     * 实例化 RedisTemplate - 有事务 - Jackson序列化方式, 用于存储大多数的表对象和分页缓存
     */
    @Bean(name = "transRedisTemplate")
    public RedisTemplate<String, Object> transRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        initDomainRedisTemplate(redisTemplate, tranRedisConnectionFactory, new GenericJackson2JsonRedisSerializer(), true);
        return redisTemplate;
    }

    /**
     * 实例化 RedisTemplate - 没事务 - Protostuff序列化方式, 用于存储大多数的表对象和分页缓存
     */
    @Bean(name = "redisTemplate4Protostuff")
    public RedisTemplate<String, Object> redisTemplate4Protostuff() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        initDomainRedisTemplate(redisTemplate, redisConnectionFactory, new ByteRedisSerializer(), false);
        return redisTemplate;
    }

    /**
     * 实例化 RedisTemplate - 有事务 - Protostuff序列化方式, 用于存储大多数的表对象和分页缓存
     */
    @Bean(name = "transRedisTemplate4Protostuff")
    public RedisTemplate<String, Object> transRedisTemplate4Protostuff() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        initDomainRedisTemplate(redisTemplate, tranRedisConnectionFactory, new ByteRedisSerializer(), true);
        return redisTemplate;
    }

    /**
     * 设置数据存入 redis 的序列化方式,并设置事务
     */
    private void initDomainRedisTemplate(RedisTemplate<String, Object> redisTemplate, RedisConnectionFactory factory, RedisSerializer valueSerializer, boolean isUseTran) {
        //如果不配置Serializer，那么存储的时候缺省使用String，如果用User类型存储，那么会提示错误User can't cast to String！
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);

        redisTemplate.setEnableTransactionSupport(isUseTran);
        redisTemplate.setConnectionFactory(factory);
    }
}