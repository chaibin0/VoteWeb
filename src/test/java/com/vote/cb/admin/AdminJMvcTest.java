package com.vote.cb.admin;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import com.vote.cb.admin.controller.AdminApiController;
import com.vote.cb.admin.service.AdminService;

@WebMvcTest(controllers = AdminApiController.class)
class AdminJMvcTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  AdminService adminService;

  @DisplayName("신청서 거부 Controller Test")
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
  @Test
  public void approvalTest() throws Exception {

    given(adminService.approvalApply(Mockito.anyLong()))
        .willReturn(ResponseEntity.accepted().build());
    mvc.perform(post("/api/v1/admin/apply/approval")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"id\" : 2}"))
        .andExpect(status().isAccepted())
        .andDo(print());
  }

}
