package com.vote.cb.exception;

import java.time.LocalDateTime;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class RestControllerErrorAdvice {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> badRequest(MethodArgumentNotValidException e) {

    log.info(e.getMessage(), e);

    BindingResult bindingResult = e.getBindingResult();
    if (bindingResult != null) {

      return ResponseEntity.badRequest()
          .body(bindingResult.getFieldError().getDefaultMessage());
    }

    return ResponseEntity.badRequest()
        .body("invalid Request");
  }

  @ExceptionHandler(UnAuthorizedException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> unAuthroized(UnAuthorizedException e) {

    log.info(e.getMessage(), e);

    return ResponseEntity.badRequest()
        .body(new ExceptionDetails(LocalDateTime.now(), "401", "bad request", e.getMessage()));
  }

  @ExceptionHandler(ApplyNotFoundException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> notFoundApply(ApplyNotFoundException e) {

    log.info(e.getMessage(), e);

    return ResponseEntity.badRequest()
        .body(new ExceptionDetails(LocalDateTime.now(), "400", "bad request", e.getMessage()));
  }

  @ExceptionHandler(MemberNotFoundException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> notFoundMember(MemberNotFoundException e) {

    log.info(e.getMessage(), e);

    return ResponseEntity.badRequest()
        .body(new ExceptionDetails(LocalDateTime.now(), "400", "bad request", e.getMessage()));
  }

  @ExceptionHandler(CandidateNotFoundException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> notFoundCandidate(CandidateNotFoundException e) {

    log.info(e.getMessage(), e);

    return ResponseEntity.badRequest()
        .body(new ExceptionDetails(LocalDateTime.now(), "400", "bad request", e.getMessage()));
  }

  @ExceptionHandler(VoterNotFoundException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> notFoundVoter(VoterNotFoundException e) {

    return ResponseEntity.badRequest()
        .body(new ExceptionDetails(LocalDateTime.now(), "400", "bad request", e.getMessage()));
  }


  @ExceptionHandler(VoteInfoNotFoundException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> notFoundVoteInfo(VoteInfoNotFoundException e) {

    log.info(e.getMessage(), e);

    return ResponseEntity.badRequest()
        .body(new ExceptionDetails(LocalDateTime.now(), "400", "bad request", e.getMessage()));
  }



  @ExceptionHandler(AlreadyRegiststeredException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> alreadyRegister(AlreadyRegiststeredException e) {

    log.info(e.getMessage(), e);

    return ResponseEntity.badRequest()
        .body(new ExceptionDetails(LocalDateTime.now(), "400", "bad request", e.getMessage()));
  }


}
