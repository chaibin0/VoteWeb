package com.vote.cb.vote.service;

import com.vote.cb.vote.controller.dto.VoteInfoDto;
import com.vote.cb.vote.controller.dto.VoteResponseDto;
import com.vote.cb.vote.controller.dto.VoteSignDto;
import com.vote.cb.vote.controller.dto.VotingDto;
import com.vote.cb.vote.domain.VoteInfomation;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;


public interface VoteService {

  VoteInfomation getVoteList(String name, String phone, String uid) throws Exception;

  ResponseEntity<VoteInfomation> saveVoteInfo(User user, VoteInfoDto dto) throws Exception;

  ResponseEntity<?> authVoterInfo(String uid, VoteSignDto dto, HttpSession session);

  boolean invalidUid(String uid);

  ResponseEntity<?> vote(String name, String phone, String uid, List<VotingDto> dto);

  VoteResponseDto getVoteListByApplyId(User user, Long applyId);

  ResponseEntity<?> modifyVoteInfo(User user, @Valid VoteInfoDto dto) throws Exception;

  boolean checkVote(String name, String phone, String uid);

  boolean isApproval(Long applyId);

}
