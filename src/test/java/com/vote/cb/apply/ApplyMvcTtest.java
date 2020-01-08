package com.vote.cb.apply;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vote.cb.apply.controller.ApplyApiController;
import com.vote.cb.apply.controller.dto.ApplyRequestDto;
import com.vote.cb.apply.service.ApplyService;

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

@WebMvcTest(controllers = ApplyApiController.class)
class ApplyMvcTtest {

  @Autowired
  MockMvc mvc;

  @MockBean
  ApplyService applyService;

  @MockBean
  User user;

  @DisplayName("이용신청서 등록 Controller Test")
  @Test
  @WithMockUser(username = "test00", password = "test00", roles = {"USER"})
  void registerApplyForm() throws Exception {

    given(applyService.registerApply(Mockito.any(User.class), Mockito.any(ApplyRequestDto.class)))
        .willReturn(ResponseEntity.created(null).build());

    mvc.perform(post("/api/v1/apply")
        .contentType(MediaType.APPLICATION_JSON)
        .content(
            "{\r\n"
                + "  \"name\":\"임채빈\",\r\n"
                + "  \"email\":\"abc@naver.com\",\r\n"
                + "  \"phone\":\"01000000000\",\r\n"
                + "  \"voteTitle\":\"테스트\",\r\n"
                + "  \"expectedCount\":\"10\",\r\n"
                + "  \"startVote\":\"2020-01-03T01:01:00\",\r\n"
                + "  \"endVote\":\"2020-01-06T02:02:00\"\r\n"
                + "}"))
        .andExpect(status().isCreated())
        .andDo(print());
  }


  @DisplayName("신청서 삭제 Controller Test")
  @WithMockUser(username = "test00", password = "test00", roles = {"USER"})
  @Test
  public void deleteTest() throws Exception {

    given(applyService.removeApply(Mockito.any(User.class), Mockito.anyLong()))
        .willReturn(ResponseEntity.accepted().build());
    mvc.perform(delete("/api/v1/apply/1"))
        .andExpect(status().isAccepted())
        .andDo(print());
  }

  @DisplayName("신청서 수정 Controller Test")
  @WithMockUser(username = "test00", password = "test00", roles = {"USER"})
  @Test
  public void updateTest() throws Exception {

    given(applyService.modifyApply(Mockito.any(User.class), Mockito.any(ApplyRequestDto.class)))
        .willReturn(ResponseEntity.ok().build());

    mvc.perform(put("/api/v1/apply")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\r\n"
            + "  \"id\": 65,\r\n"
            + "  \"name\":\"임채빈\",\r\n"
            + "  \"email\":\"abc@naver.com\",\r\n"
            + "  \"phone\":\"01000000000\",\r\n"
            + "  \"voteTitle\":\"2번째 테스트\",\r\n"
            + "  \"expectedCount\":\"10\",\r\n"
            + "  \"startVote\":\"2020-01-03T01:01:00\",\r\n"
            + "  \"endVote\":\"2020-01-06T02:02:00\"\r\n"
            + "}"))
        .andDo(print())
        .andExpect(status().isOk());
  }
}
