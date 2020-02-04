package com.vote.cb.voter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vote.cb.apply.controller.VoterApiController;
import com.vote.cb.apply.controller.VoterController;
import com.vote.cb.apply.controller.dto.ApplyRequestDto;
import com.vote.cb.apply.controller.dto.VoterDto;
import com.vote.cb.apply.domain.Apply;
import com.vote.cb.apply.domain.Voter;
import com.vote.cb.apply.domain.enums.VoterStatusType;
import com.vote.cb.apply.service.ApplyService;
import com.vote.cb.apply.service.VoterService;
import com.vote.cb.interceptor.BlackUserApiInterceptor;
import com.vote.cb.interceptor.BlackUserInterceptor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(controllers = {VoterApiController.class, VoterController.class})
class VoterWebMvcTest {

  Apply apply;

  @Autowired
  MockMvc mvc;

  @Autowired
  ObjectMapper mapper;

  @MockBean
  VoterService voterService;

  @MockBean
  ApplyService applyService;

  @MockBean
  BlackUserApiInterceptor blackUserApiInterceptor;

  @MockBean
  BlackUserInterceptor blackUserInterceptor;

  @BeforeEach
  void setUp() {

    ApplyRequestDto applyDto = ApplyRequestDto
        .builder().build();
  }

  @DisplayName("유권자 리스트 API 테스트")
  @WithMockUser(username = "test", password = "test", authorities = {"USER"})
  @Test
  void getVotertest() throws Exception {

    Apply apply = Apply.builder().build();

    Voter voter1 = Voter.builder()
        .name("일번")
        .phone("01000000001")
        .createdAt(LocalDateTime.now())
        .createdBy("TEST_SERVER")
        .ssn("일번")
        .status(VoterStatusType.UNVOTED)
        .apply(apply)
        .build();

    Voter voter2 = Voter.builder()
        .name("이번")
        .phone("01000000002")
        .createdAt(LocalDateTime.now())
        .createdBy("TEST_SERVER")
        .ssn("이번")
        .status(VoterStatusType.UNVOTED)
        .apply(apply)
        .build();

    Long applyId = 1L;
    Page<Voter> page = new PageImpl<Voter>(new ArrayList<>(Arrays.asList(voter1, voter2)));

    given(voterService.getVoterListByApply(Mockito.any(Pageable.class), Mockito.any(User.class),
        Mockito.eq(applyId)))
            .willReturn(page);

    given(blackUserApiInterceptor.preHandle(any(), any(), any())).willReturn(true);

    mvc.perform(get("/api/v1/apply/1/voter"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].name").value("일번"))
        .andExpect(jsonPath("$.content[1].name").value("이번"))
        .andDo(print());

  }


  @DisplayName("유권자 등록 API 테스트")
  @WithMockUser(username = "test", password = "test", authorities = {"USER"})
  @Test
  void registerVotertest() throws Exception {

    VoterDto dto = VoterDto.builder()
        .voterName("등록")
        .voterPhone("01000000000")
        .build();
    Long applyId = 1L;

    given(voterService.registerVoter(Mockito.any(User.class), Mockito.any(VoterDto.class),
        Mockito.eq(applyId)))
            .willReturn(ResponseEntity.created(null).build());
    given(blackUserApiInterceptor.preHandle(any(), any(), any())).willReturn(true);

    mvc.perform(
        post("/api/v1/apply/1/voter").contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(dto)))
        .andExpect(status().isCreated())
        .andDo(print());
  }

  @DisplayName("유권자 수정 API 테스트")
  @WithMockUser(username = "test", password = "test", authorities = {"USER"})
  @Test
  void modifyVotertest() throws Exception {

    VoterDto dto = VoterDto.builder()
        .voterId(1L)
        .voterName("등록")
        .voterPhone("01000000000")
        .build();
    Long applyId = 1L;

    given(voterService.modifyVoter(Mockito.any(User.class), Mockito.any(VoterDto.class),
        Mockito.eq(applyId)))
            .willReturn(ResponseEntity.ok().build());

    given(blackUserApiInterceptor.preHandle(any(), any(), any())).willReturn(true);

    mvc.perform(
        post("/api/v1/apply/1/voter").contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(dto)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @DisplayName("유권자 삭제 API 테스트")
  @WithMockUser(username = "test", password = "test", authorities = {"USER"})
  @Test
  void delteVotertest() throws Exception {

    Long voterId = 1L;
    Long applyId = 1L;

    given(
        voterService.removeVoter(Mockito.any(User.class), Mockito.eq(applyId), Mockito.eq(voterId)))
            .willReturn(ResponseEntity.ok().build());
    given(blackUserApiInterceptor.preHandle(any(), any(), any())).willReturn(true);

    mvc.perform(delete("/api/v1/apply/1/voter/1"))
        .andExpect(status().isOk())
        .andDo(print());
  }
}
