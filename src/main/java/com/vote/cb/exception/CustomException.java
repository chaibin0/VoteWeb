package com.vote.cb.exception;


public class CustomException extends RuntimeException {

  private static final long serialVersionUID = 1804454393231287519L;

  public static final CustomException APPLY_NOT_FOUND = new ApplyNotFoundException();

  public static final CustomException VOTE_NOT_FOUND = new VoteNotFoundException();

  public static final CustomException VOTER_NOT_FOUND = new VoterNotFoundException();

  public static final CustomException VOTEINFO_NOT_FOUND = new VoteInfoNotFoundException();

  public static final CustomException CANDIDATE_NOT_FOUND = new CandidateNotFoundException();

  public static final CustomException MEMBER_NOT_FOUND = new MemberNotFoundException();

  public static final CustomException UNAUTHORIZED = new UnAuthorizedException();

  public static final CustomException ALREADY_REGISTERED = new AlreadyRegiststeredException();
}
