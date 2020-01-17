package com.vote.cb.admin;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vote.cb.admin.controller.AdminApiController;
import com.vote.cb.admin.service.AdminService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AdminApiController.class)
class AdminMvcTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  AdminService adminService;

  @DisplayName("신청서 거부 Controller Test")
  @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
  @Test
  public void rejectTest() throws Exception {

    given(adminService.rejectApply(Mockito.anyLong()))
        .willReturn(ResponseEntity.accepted().build());

    mvc.perform(post("/api/v1/admin/apply/reject")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"id\" : 2}"))
        .andExpect(status().isAccepted())
        .andDo(print());
  }

  @DisplayName("신청서 승인 Controller Test")
  @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
  @Test
  public void approvalTest() throws Exception {

    given(adminService.approvalApply(Mockito.eq(2L)))
        .willReturn(ResponseEntity.accepted().build());
    mvc.perform(post("/api/v1/admin/apply/approval")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"id\" : 2}"))
        .andExpect(status().isAccepted())
        .andDo(print());
  }

  @DisplayName("유저 삭제 관리자용 Controller Test")
  @WithMockUser(username = "admin", password = "admin", roles = {"ADMIN"})
  @Test
  public void removeUser() throws Exception {

    String id = "test";
    doReturn(ResponseEntity.accepted().build()).when(adminService).removeUser(Mockito.eq(id));

    mvc.perform(delete("/api/v1/admin/user/remove")
        .param("id", "test"))
        .andExpect(status().isAccepted())
        .andDo(print());
  }


}
