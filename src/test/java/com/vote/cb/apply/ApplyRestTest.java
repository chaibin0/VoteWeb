package com.vote.cb.apply;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;

import com.vote.cb.apply.service.ApplyServiceImpl;


@RestClientTest(ApplyServiceImpl.class)
class ApplyRestTest {

  @Autowired
  private ApplyServiceImpl applyService;

  @Autowired
  private MockRestServiceServer server;

  @BeforeEach
  void setUp() throws Exception {}

  void RESTAPItest() {

    String applicationJSon = "{"
        + "\"userName\" : \"�씠由�\","
        + "\"email\": \"junit@test.com\","
        + "\"phone\":\"010-0000-0000\","
        + "\"voteTitle\" : \"�씠寃껋� �젣紐⑹씠�뿬\","
        + "\"expectedCount\" : 10,"
        + "\"startVote\" : \"2019-11-14T04:03\","
        + "\"endVote\" : \"2019-11-14T04:03\""
        + "}";

    server.expect(requestTo("http://localhost:8080/api/v1/apply"))
        .andExpect(method(HttpMethod.POST))
        .andExpect(content().json(applicationJSon));
  }


}
