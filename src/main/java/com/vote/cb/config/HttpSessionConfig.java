package com.vote.cb.config;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import redis.embedded.RedisServer;

@Configuration
@EnableRedisHttpSession
public class HttpSessionConfig {
  
  @Value("${spring.redis.port}")
  private int redisPort;

  private RedisServer redisServer;
  
  @PostConstruct
  public void redisServer() throws IOException {

    redisServer = new RedisServer(redisPort);
    redisServer.start();
  }

  @PreDestroy
  public void stopRedis() {

    if (redisServer != null) {
      redisServer.stop();
    }
  }

  @Bean
  public LettuceConnectionFactory connectionFactory() {

    return new LettuceConnectionFactory();
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate() {

    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(connectionFactory());
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    return redisTemplate;
  }
}
