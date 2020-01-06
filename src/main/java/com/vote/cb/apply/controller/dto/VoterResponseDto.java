package com.vote.cb.apply.controller.dto;

import java.util.List;
import com.vote.cb.apply.domain.Voter;

public class VoterResponseDto {

  boolean hasVoted;

  List<Voter> voterList;
}
