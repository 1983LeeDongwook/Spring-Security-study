package net.skhu.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by ds on 2018-03-26.
 */

/**
 * Reids의 설정 파일
 */

@Configuration
@EnableRedisRepositories
@EnableRedisHttpSession
public class RedisConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    /**
     * Jedis는 자바에서 Redis을 사용하기 위한 클라이언트
     * @return redis 설정 객체
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        return jedisPoolConfig;
    }

    /**
     * application.properties 파일의 redis 설정을 토대로 redis커넥션 팩토리 객체 생성
     * @param jedisPoolConfig
     * @return
     */
    @Bean
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
        jedisConnectionFactory.setHostName(redisHost);
        jedisConnectionFactory.setPort(redisPort);
        jedisConnectionFactory.setPassword(redisPassword);
        jedisConnectionFactory.setUsePool(true);
        return jedisConnectionFactory;
    }

    /**
     * serializer 의 각종 설정
     * tomcat context 로 설정한 쿠키 기능들도 여기서 설정
     * @return
     */
    @Bean
    public CookieSerializer cookieSerializer()
    {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        return serializer;
    }
}
