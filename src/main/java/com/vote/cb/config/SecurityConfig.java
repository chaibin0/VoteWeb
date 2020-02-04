package com.vote.cb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userDetailService;

  @Autowired
  public SecurityConfig(@Lazy UserDetailsService userDetailService) {

    this.userDetailService = userDetailService;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/").permitAll()
        .antMatchers("/vote/**").permitAll()
        .antMatchers("/user/login").permitAll()
        .antMatchers("/mypage/**").hasRole("USER")
        .antMatchers("/admin/**").hasRole("ADMIN")
        .antMatchers("/api/v1/admin/**").hasRole("ADMIN")
        .antMatchers("/apply/**").access("hasRole('ADMIN') or hasRole('USER')")
        .and()
        .formLogin()
        .loginPage("/user/login") 
        .and()
        .logout()
        .logoutUrl("/user/logout")
        .logoutSuccessUrl("/")
        .and()
        .httpBasic();
  }

  @Override
  public void configure(WebSecurity web) throws Exception {

    web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
  }

  @Bean
  public PasswordEncoder passwordEncoder() {

    return new BCryptPasswordEncoder();
  }
}
