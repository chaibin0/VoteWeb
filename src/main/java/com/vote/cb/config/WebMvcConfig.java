package com.vote.cb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/").setViewName("main/index");
    registry.addViewController("/user/signup").setViewName("user/signup");
    registry.addViewController("/user/login").setViewName("user/login");
  }
}
