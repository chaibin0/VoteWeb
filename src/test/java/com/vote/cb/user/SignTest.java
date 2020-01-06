package com.vote.cb.user;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import com.vote.cb.user.service.MemberService;
import com.vote.cb.user.service.MemberServiceImpl;

@RestClientTest(MemberServiceImpl.class)
class SignTest {

  @Autowired
  MockRestServiceServer server;

  @Autowired
  MemberService userService;

  @BeforeEach
  void setUp() throws Exception {}

  @Test
  void test() {

    String data = "{" + "\"userName\" : \"임채빈\"," + "\"phone\" : \"01000000000\"" + "}";
    server.expect(requestTo("/api/v1/validation")).andExpect(method(HttpMethod.POST))
        .andExpect(content().json(data));

  }

}
