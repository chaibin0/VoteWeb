package com.vote.cb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VoteWebApplication {

  public static void main(String[] args) {

    SpringApplication.run(VoteWebApplication.class, args);
  }


  // JDK 9이상만 가능
  // @Bean
  // public ConfigurableServletWebServerFactory tomcatCustomizer() {
  //
  // TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
  // factory.addConnectorCustomizers(connector -> connector.addUpgradeProtocol(new
  // Http2Protocol()));
  // return factory;
  // }

  // 다중 커넥터 (8080포트도 실행할수 있게함 , security문제생김)
  // @Bean
  // public ServletWebServerFactory serveltContainer() {
  //
  // TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
  // tomcat.addAdditionalTomcatConnectors(createStandardConnector());
  // return tomcat;
  // }
  //
  // private Connector createStandardConnector() {
  //
  // Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
  // connector.setPort(8080);
  // return connector;
  // }


}
