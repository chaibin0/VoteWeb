package com.vote.cb.exception;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class ExceptionDetails {

  LocalDateTime timestamp;

  String status;

  String error;

  String message;

}
