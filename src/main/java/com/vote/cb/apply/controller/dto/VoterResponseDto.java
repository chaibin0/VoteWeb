package com.vote.cb.apply.controller.dto;

import com.vote.cb.apply.domain.Voter;
import java.util.List;

public class VoterResponseDto {

  boolean hasVoted;

  List<Voter> voterList;
}
