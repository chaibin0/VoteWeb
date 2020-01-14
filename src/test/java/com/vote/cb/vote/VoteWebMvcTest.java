package com.vote.cb.vote;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vote.cb.vote.controller.VoteApiController;
import com.vote.cb.vote.controller.VoteMakingApiController;
import com.vote.cb.vote.controller.dto.CandidateDto;
import com.vote.cb.vote.controller.dto.VoteDto;
import com.vote.cb.vote.controller.dto.VoteInfoDto;
import com.vote.cb.vote.controller.dto.VoteResponseDto;
import com.vote.cb.vote.controller.dto.VoteSignDto;
import com.vote.cb.vote.controller.dto.VotingDto;
import com.vote.cb.vote.domain.Candidate;
import com.vote.cb.vote.domain.Vote;
import com.vote.cb.vote.domain.VoteInfomation;
import com.vote.cb.vote.service.VoteService;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.UnexpectedTypeException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;


@WebMvcTest(controllers = {VoteMakingApiController.class, VoteApiController.class})
public class VoteWebMvcTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  VoteService voteService;

  ObjectMapper objectMapper = new ObjectMapper();

  @MockBean
  User user;

  /// api/v1/vote/making
  @DisplayName("투표 등록 Controller 테스트")
  @WithMockUser(username = "test00", password = "test00", roles = {"USER"})
  @Test
  public void insertTest() throws Exception {

    VoteInfomation voteInfomation = new VoteInfomation();
    given(voteService.saveVoteInfo(Mockito.any(User.class), Mockito.any(VoteInfoDto.class)))
        .willReturn(ResponseEntity.created(null).body(voteInfomation));

    mvc.perform(post("/api/v1/vote/making")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\r\n"
            + "    \"applyId\" : 35,\r\n"
            + "    \"voteInfoTitle\" : \"투표 제목\",\r\n"
            + "    \"voteInfoCount\" : 10,\r\n"
            + "    \"voteInfoDesc\" : \"투표 설명\",\r\n"
            + "    \"vote\" : [\r\n"
            + "        {\r\n"
            + "            \"voteSeqNum\" : 1,\r\n"
            + "            \"voteSelNum\" : 1,\r\n"
            + "            \"voteName\" : \"1번째 투표 제목이여\",\r\n"
            + "            \"voteElecNum\" : 1,\r\n"
            + "            \"candidate\" :[\r\n"
            + "                {\r\n"
            + "                    \"candidateSeqNo\" : 1,\r\n"
            + "                    \"candidateName\" : \"후보자1\",\r\n"
            + "                    \"candidateDesc\" : \"추가 설명1\"\r\n"
            + "                },\r\n"
            + "                {\r\n"
            + "                    \"candidateSeqNo\" : 2,\r\n"
            + "                    \"candidateName\" : \"후보자2\",\r\n"
            + "                    \"candidateDesc\" : \"추가 설명2\"\r\n"
            + "                }\r\n"
            + "            ]\r\n"
            + "        }\r\n"
            + "    ]\r\n"
            + "}\r\n"
            + "\r\n"))
        .andExpect(status().isCreated())
        .andDo(print());
  }

  /// api/v1/vote/making
  @DisplayName("투표 등록 Controller 후보자가 한명인 테스트")
  @WithMockUser(username = "test00", password = "test00", roles = {"USER"})
  @Test
  public void insertTestOnlyOneCandidate() throws Exception {

    given(voteService.saveVoteInfo(Mockito.any(User.class), Mockito.any(VoteInfoDto.class)))
        .willThrow(MethodArgumentNotValidException.class);

    mvc.perform(post("/api/v1/vote/making")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\r\n"
            + "    \"applyId\" : 35,\r\n"
            + "    \"voteInfoTitle\" : \"투표 제목\",\r\n"
            + "    \"voteInfoCount\" : 10,\r\n"
            + "    \"voteInfoDesc\" : \"투표 설명\",\r\n"
            + "    \"vote\" : [\r\n"
            + "        {\r\n"
            + "            \"voteSeqNum\" : 1,\r\n"
            + "            \"voteSelNum\" : 1,\r\n"
            + "            \"voteName\" : \"1번째 투표 제목이여\",\r\n"
            + "            \"voteElecNum\" : 1,\r\n"
            + "            \"candidate\" :[\r\n"
            + "                {\r\n"
            + "                    \"candidateSeqNo\" : 1,\r\n"
            + "                    \"candidateName\" : \"후보자1\",\r\n"
            + "                    \"candidateDesc\" : \"추가 설명1\"\r\n"
            + "                }"
            + "            ]\r\n"
            + "        }\r\n"
            + "    ]\r\n"
            + "}\r\n"
            + "\r\n"))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @DisplayName("투표 조회 테스트")
  @WithMockUser(username = "test00", password = "test00", roles = {"USER"})
  @Test
  public void readTest() throws Exception {

    VoteResponseDto responseDto = VoteResponseDto.builder()
        .applyId(1L)
        .id(1L)
        .name("투표명")
        .description("수정")
        .count(10)
        .current(10)
        .voteList(null)
        .build();
    given(voteService.getVoteListByApplyId(Mockito.any(User.class), Mockito.anyLong()))
        .willReturn(responseDto);

    mvc.perform(get("/api/v1/vote/making/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.applyId").value(1L))
        .andExpect(jsonPath("$.name").value("투표명"))
        .andExpect(jsonPath("$.description").value("수정"))
        .andExpect(jsonPath("$.count").value(10))
        .andExpect(jsonPath("$.current").value(10))
        .andDo(print());
  }

  @DisplayName("투표 인증 테스트")
  @Test
  public void authVoterTest() throws Exception {


    VoteSignDto dto = VoteSignDto.builder()
        .name("일번")
        .phone("01000000000")
        .uid("abc")
        .build();

    ObjectMapper mapper = new ObjectMapper();
    MockHttpSession session = new MockHttpSession();

    given(voteService.authVoterInfo(Mockito.any(VoteSignDto.class), Mockito.any(HttpSession.class)))
        .willReturn(ResponseEntity.ok().build());

    mvc.perform(post("/api/v1/vote/sign").session(session)
        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dto)))
        .andDo(print())
        .andExpect(status().isOk());

  }


  @DisplayName("투표 테스트")
  @Test
  public void voteTest() throws Exception {


    List<VotingDto> votingList = new ArrayList<>();
    votingList.add(VotingDto
        .builder()
        .voteInfoId(1L)
        .voteId(1L)
        .candidateSequenceNumber(1L)
        .candidateId(1L)
        .build());

    String name = "일번";
    String phone = "01000000000";

    MockHttpSession session = new MockHttpSession();
    session.setAttribute("name", name);
    session.setAttribute("phone", phone);
    session.setAttribute("uid", "abc");

    given(voteService.vote(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(),
        Mockito.anyList()))
            .willReturn(ResponseEntity.created(URI.create("/vote/success")).build());

    mvc.perform(post("/api/v1/vote/voting")
        .session(session)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(votingList)))
        .andDo(print())
        .andExpect(status().isCreated());
  }


  @DisplayName("투표조회 테스트 개설자 용")
  @WithMockUser(username = "test00", password = "test00", roles = {"USER"})
  @Test
  public void getVotesTest() throws Exception {

    Long voteNum = 128L;
    Long applyId = 1L;

    Candidate candidate1 = Candidate.builder().id(1L).name("첫번째").build();
    Candidate candidate2 = Candidate.builder().id(2L).name("두번째").build();

    List<Candidate> candidateList = new ArrayList<>(Arrays.asList(candidate1, candidate2));

    Vote vote1 = Vote.builder()
        .name("첫번째 투표")
        .id(1L)
        .candidateList(candidateList)
        .build();


    List<Vote> list = new ArrayList<>(Arrays.asList(vote1));

    VoteResponseDto responseDto = VoteResponseDto.builder()
        .applyId(voteNum)
        .id(applyId)
        .name("투표명")
        .description("수정")
        .count(10)
        .current(10)
        .voteList(list)
        .build();



    given(voteService.getVoteListByApplyId(Mockito.any(User.class), Mockito.eq(128L)))
        .willReturn(responseDto);

    mvc.perform(get("/api/v1/vote/making/128"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("투표명"))
        .andExpect(jsonPath("$.voteList[0].name").value("첫번째 투표"))
        .andExpect(jsonPath("$.voteList[0].candidateList[0].name").value("첫번째"))
        .andExpect(jsonPath("$.voteList[0].candidateList[1].name").value("두번째"));

  }


  @DisplayName("투표수정 테스트")
  @WithMockUser(username = "test00", password = "test00", roles = {"USER"})
  @Test
  public void modifyVotesTest() throws Exception {

    CandidateDto candidateDto1 = CandidateDto.builder()
        .candidateSeqNo(1)
        .candidateName("후보자1")
        .build();

    CandidateDto candidateDto2 = CandidateDto.builder()
        .candidateSeqNo(2)
        .candidateName("후보자2")
        .build();

    VoteDto voteDto = VoteDto.builder()
        .voteElecNum(1)
        .voteName("수정된투표이름")
        .voteSelNum(1)
        .voteSeqNum(1)
        .candidate(Arrays.asList(candidateDto1, candidateDto2))
        .build();

    VoteInfoDto dto = VoteInfoDto.builder()
        .applyId(1L)
        .voteInfoTitle("수정된 투표")
        .voteInfoDesc("설명")
        .voteDto(Arrays.asList(voteDto))
        .build();

    given(voteService.modifyVoteInfo(Mockito.any(User.class), Mockito.any(VoteInfoDto.class)))
        .willReturn(ResponseEntity.accepted().build());

    mvc.perform(put("/api/v1/vote/making").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)))
        .andDo(print())
        .andExpect(status().isAccepted());
  }

}
