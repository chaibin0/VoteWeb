package com.vote.cb.exception;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;


@AllArgsConstructor
@Getter
public class ExceptionDetails {

  LocalDateTime timestamp;

  int status;

  String error;

  String message;

}
