package com.vote.cb.apply;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

import com.vote.cb.apply.service.ApplyServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;


@RestClientTest(ApplyServiceImpl.class)
class ApplyRestTest {

  @Autowired
  private MockRestServiceServer server;

  @BeforeEach
  void setUp() throws Exception {}


  void registerApplicationTestByRestServer() {

    String applicationJSon = "{"
        + "\"userName\" : \"이름\","
        + "\"email\": \"junit@test.com\","
        + "\"phone\":\"01000000000\","
        + "\"voteTitle\" : \"이것은 제목이여\","
        + "\"expectedCount\" : 10,"
        + "\"startVote\" : \"2019-11-14T04:03\","
        + "\"endVote\" : \"2019-11-14T04:03\""
        + "}";

    server.expect(requestTo("http://localhost:8080/api/v1/apply"))
        .andExpect(method(HttpMethod.POST))
        .andExpect(content().json(applicationJSon));
  }


}
