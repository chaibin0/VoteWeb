package com.vote.cb.user;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vote.cb.user.controller.MemberApiController;
import com.vote.cb.user.controller.MemberController;
import com.vote.cb.user.controller.dto.CheckUserIdResponseDto;
import com.vote.cb.user.controller.dto.SignUpDto;
import com.vote.cb.user.service.MemberService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {MemberController.class, MemberApiController.class})
public class MemberControllerTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  MemberService memberSerivce;

  @SpyBean(name = "passwordEncoder")
  PasswordEncoder passwordEncoder;

  ObjectMapper mapper = new ObjectMapper();

  @DisplayName("아이디 유효성검사 API테스트(다른 아이디)")
  @Test
  public void checkDifferentIdTest() throws Exception {

    String checkId = "testId";
    CheckUserIdResponseDto dto = new CheckUserIdResponseDto(true);
    given(memberSerivce.checkUserId(Mockito.eq(checkId))).willReturn(dto);

    mvc.perform(get("/api/v1/user/check-id")
        .param("userId", checkId))
        .andExpect(jsonPath("$.checkUserId").value(true))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @DisplayName("아이디 유효성검사 API테스트(같은 아이디)")
  @Test
  public void checkSameIdTest() throws Exception {

    String checkId = "testId";
    CheckUserIdResponseDto dto = new CheckUserIdResponseDto(false);
    given(memberSerivce.checkUserId(checkId)).willReturn(dto);

    mvc.perform(get("/api/v1/user/check-id")
        .param("userId", checkId))
        .andExpect(jsonPath("$.checkUserId").value(false))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @DisplayName("회원가입 API 테스트")
  @Test
  public void signUpTest() throws Exception {

    SignUpDto dto = SignUpDto.builder()
        .id("testId")
        .email("email")
        .name("name")
        .password("password")
        .phone("01000004444")
        .build();

    doReturn(ResponseEntity.created(null)
        .body(dto.toMember(passwordEncoder)))
            .when(memberSerivce)
            .signUpUser(Mockito.any(dto.getClass()));

    mvc.perform(post("/api/v1/user/signup")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(dto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.userId").value(dto.getId()))
        .andExpect(jsonPath("$.email").value(dto.getEmail()))
        .andExpect(jsonPath("$.name").value(dto.getName()))
        .andExpect(jsonPath("$.phone").value(dto.getPhone()))
        .andExpect(jsonPath("$.status").value("NORMAL"))
        .andDo(print());

  }
}
