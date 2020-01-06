package com.vote.cb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ControllerErrorAdvice {

  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public ModelAndView nullex(Exception e) {

    ModelAndView model = new ModelAndView();
    model.setViewName("error/error");
    return model;
  }

  @ExceptionHandler(ApplyNotFoundException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ModelAndView notFoundApply(ApplyNotFoundException e) {

    ModelAndView model = new ModelAndView();
    model.setViewName("error/error");
    return model;
  }
}
