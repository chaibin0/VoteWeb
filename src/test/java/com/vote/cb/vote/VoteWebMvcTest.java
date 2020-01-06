package com.vote.cb.vote;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.vote.cb.vote.controller.VoteApiController;
import com.vote.cb.vote.controller.VoteMakingApiController;
import com.vote.cb.vote.controller.dto.VoteInfoDto;
import com.vote.cb.vote.controller.dto.VoteResponseDto;
import com.vote.cb.vote.domain.VoteInfomation;
import com.vote.cb.vote.service.VoteService;

/**
 * VoteWebMvcTest
 */
@WebMvcTest(controllers = VoteMakingApiController.class)
public class VoteWebMvcTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  VoteService voteService;

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
        .content("{\r\n" +
            "    \"applyId\" : 35,\r\n" +
            "    \"voteInfoTitle\" : \"이것은 투표 제목\",\r\n" +
            "    \"voteInfoCount\" : 10,\r\n" +
            "    \"voteInfoDesc\" : \"이것은 투표 설명\",\r\n" +
            "    \"vote\" : [\r\n" +
            "        {\r\n" +
            "            \"voteSeqNum\" : 1,\r\n" +
            "            \"voteSelNum\" : 1,\r\n" +
            "            \"voteName\" : \"이것은 1번째 투표 제목이여\",\r\n" +
            "            \"voteElecNum\" : 1,\r\n" +
            "            \"candidate\" :[\r\n" +
            "                {\r\n" +
            "                    \"candidateSeqNo\" : 1,\r\n" +
            "                    \"candidateName\" : \"후보자 이름이여1\",\r\n" +
            "                    \"candidateDesc\" : \"추가 설명할거있으면 여기서 말혀1\"\r\n" +
            "                },\r\n" +
            "                {\r\n" +
            "                    \"candidateSeqNo\" : 2,\r\n" +
            "                    \"candidateName\" : \"후보자 이름이여\",\r\n" +
            "                    \"candidateDesc\" : \"추가 설명할거있으면 여기서 말혀2\"\r\n" +
            "                }\r\n" +
            "            ]\r\n" +
            "        }\r\n" +
            "    ]\r\n" +
            "}\r\n" +
            "\r\n"))
        .andExpect(status().isCreated())
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
}
