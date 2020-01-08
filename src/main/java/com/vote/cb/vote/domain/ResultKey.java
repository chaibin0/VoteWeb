package com.vote.cb.vote.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ResultKey implements java.io.Serializable {

  private static final long serialVersionUID = -1963497032876101764L;

  long voterId;

  long candidateId;
}
