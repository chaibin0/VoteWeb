package com.vote.cb.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(annotations = RestController.class)
public class RestControllerErrorAdvice {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> badRequest(MethodArgumentNotValidException e) {

    BindingResult bindingResult = e.getBindingResult();
    final List<FieldError> errors = bindingResult.getFieldErrors();

    return ResponseEntity.badRequest()
        .body(errors);
  }

  @ExceptionHandler(UnAuthorizedException.class)
  @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
  public ResponseEntity<?> unAuthroized(UnAuthorizedException e) {

    return ResponseEntity.badRequest()
        .body(new ExceptionDetails(LocalDateTime.now(), "401", "UNAUTHORIZED", e.getMessage()));
  }

  @ExceptionHandler(ApplyNotFoundException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> notFoundApply(ApplyNotFoundException e) {

    return ResponseEntity.badRequest()
        .body(new ExceptionDetails(LocalDateTime.now(), "400", "bad request", e.getMessage()));
  }

  @ExceptionHandler(MemberNotFoundException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> notFoundMember(MemberNotFoundException e) {

    return ResponseEntity.badRequest()
        .body(new ExceptionDetails(LocalDateTime.now(), "400", "bad request", e.getMessage()));
  }

  @ExceptionHandler(CandidateNotFoundException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<?> notFoundCandidate(CandidateNotFoundException e) {

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

    return ResponseEntity.badRequest()
        .body(new ExceptionDetails(LocalDateTime.now(), "400", "bad request", e.getMessage()));
  }

}
