package school.hei.tsinjo.conf;

import org.springframework.test.context.DynamicPropertyRegistry;

public class EnvConf {
  void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("env", () -> "test");
    registry.add(
        "spring.datasource.url", () -> "jdbc:h2:mem:testdb;CASE_INSENSITIVE_IDENTIFIERS=TRUE");
    registry.add("spring.datasource.driverClassName", () -> "org.h2.Driver");
    registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.H2Dialect");

    registry.add("spring.security.oauth2.client.provider.cognito.authorization-uri", () -> "dummy");
    registry.add("spring.security.oauth2.client.provider.cognito.token-uri", () -> "dummy");
    registry.add("spring.security.oauth2.client.registration.cognito.provider", () -> "cognito");
    registry.add("spring.security.oauth2.client.registration.cognito.client-id", () -> "dummy");
    registry.add(
        "spring.security.oauth2.client.registration.cognito.redirect-uri",
        () -> "{baseUrl}/login/oauth2/code/cognito");
    registry.add(
        "spring.security.oauth2.client.registration.cognito.authorization-grant-type",
        () -> "authorization_code");
    registry.add("cognito.logout.url", () -> "dummy");
    registry.add("tsinjo.logout.url", () -> "dummy");

    registry.add(
        "vola.api.url",
        () -> "https://ypmoi24xu4kt5ts77p7te5uhme0uyxnv.lambda-url.eu-west-3.on.aws");
    registry.add("vola.api.key", () -> "ec48ed08-3a83-4b95-9f7e-ce5c07f541eb");
  }
}
