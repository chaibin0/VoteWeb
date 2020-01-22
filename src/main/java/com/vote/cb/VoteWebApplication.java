package com.vote.cb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class VoteWebApplication {

  public static void main(String[] args) {

    SpringApplication.run(VoteWebApplication.class, args);
  }
}
