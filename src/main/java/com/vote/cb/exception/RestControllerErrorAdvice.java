package com.vote.cb.exception;

import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestControllerErrorAdvice {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> badRequest(MethodArgumentNotValidException e) {

    log.info(e.getMessage());

    BindingResult bindingResult = e.getBindingResult();
    if (bindingResult != null) {

      return ResponseEntity.badRequest()
          .body(new ExceptionDetails(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
              HttpStatus.BAD_REQUEST.getReasonPhrase(),
              bindingResult.getFieldError().getDefaultMessage()));
    }

    return ResponseEntity.badRequest()
        .body(new ExceptionDetails(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            "invalid"));
  }

  @ExceptionHandler(UnAuthorizedException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> unAuthroized(UnAuthorizedException e) {

    log.info(e.getMessage());

    return ResponseEntity.badRequest()
        .body(new ExceptionDetails(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage()));
  }

  @ExceptionHandler(ApplyNotFoundException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> notFoundApply(ApplyNotFoundException e) {

    log.info(e.getMessage());

    return ResponseEntity.badRequest()
        .body(new ExceptionDetails(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage()));
  }

  @ExceptionHandler(MemberNotFoundException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> notFoundMember(MemberNotFoundException e) {

    log.info(e.getMessage());

    return ResponseEntity.badRequest()
        .body(new ExceptionDetails(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage()));
  }

  @ExceptionHandler(CandidateNotFoundException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> notFoundCandidate(CandidateNotFoundException e) {

    log.info(e.getMessage());

    return ResponseEntity.badRequest()
        .body(new ExceptionDetails(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage()));
  }

  @ExceptionHandler(VoterNotFoundException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> notFoundVoter(VoterNotFoundException e) {

    log.info(e.getMessage());

    return ResponseEntity.badRequest()
        .body(new ExceptionDetails(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage()));
  }


  @ExceptionHandler(VoteInfoNotFoundException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> notFoundVoteInfo(VoteInfoNotFoundException e) {

    log.info(e.getMessage());

    return ResponseEntity.badRequest()
        .body(new ExceptionDetails(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage()));
  }



  @ExceptionHandler(AlreadyRegiststeredException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> alreadyRegister(AlreadyRegiststeredException e) {

    log.info(e.getMessage(), e);

    return ResponseEntity.badRequest()
        .body(new ExceptionDetails(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(), e.getMessage()));
  }


}
