package com.vote.cb.config;

import com.vote.cb.interceptor.BlackUserApiInterceptor;
import com.vote.cb.interceptor.BlackUserInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  
  
  public void addViewControllers(ViewControllerRegistry registry) {

    registry.addViewController("/").setViewName("/main/index");
    registry.addViewController("/user/signup").setViewName("/user/signup");
    registry.addViewController("/user/login").setViewName("/user/login");
    registry.addViewController("/black").setViewName("/error/block");
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {

    registry.addInterceptor(blackUserApiInterceptor()).addPathPatterns("/api/v1/vote/making/**",
        "/api/v1/apply/**");

    registry.addInterceptor(blackUserInterceptor()).addPathPatterns("/vote/making/**",
        "/apply/**");

  }

  @Bean
  public BlackUserApiInterceptor blackUserApiInterceptor() {

    return new BlackUserApiInterceptor();
  }

  @Bean
  public BlackUserInterceptor blackUserInterceptor() {

    return new BlackUserInterceptor();
  }


}
