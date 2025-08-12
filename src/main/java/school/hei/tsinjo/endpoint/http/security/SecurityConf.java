package school.hei.tsinjo.endpoint.http.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConf {

  private final String cognitoClientId;
  private final String cognitoLogoutUrl;
  private final String tsinjoLogoutUrl;

  public SecurityConf(
      @Value("${spring.security.oauth2.client.registration.cognito.clientid}")
          String cognitoClientId,
      @Value("${cognito.logout.url}") String cognitoLogoutUrl,
      @Value("${tsinjo.logout.url}") String tsinjoLogoutUrl) {
    this.cognitoClientId = cognitoClientId;
    this.cognitoLogoutUrl = cognitoLogoutUrl;
    this.tsinjoLogoutUrl = tsinjoLogoutUrl;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(Customizer.withDefaults())
        .authorizeHttpRequests(
            authz -> authz.requestMatchers("/").permitAll().anyRequest().authenticated())
        .oauth2Login(
            oauth2 ->
                oauth2
                    .successHandler(
                        (request, response, authentication) -> response.sendRedirect("/history"))
                    .failureHandler(
                        // On success redirection from Cognito hits amazonaws.com URL instead of
                        // custom domain URL
                        // so it is incorrectly interpreted as authorization_request_not_found.
                        // Redo the call and it will be Ok.
                        new SimpleUrlAuthenticationFailureHandler("/oauth2/authorization/cognito")))
        .logout(
            logout ->
                logout.logoutSuccessHandler(
                    (request, response, authentication) ->
                        response.sendRedirect(
                            cognitoLogoutUrl
                                + "?client_id="
                                + cognitoClientId
                                + "&logout_uri="
                                + tsinjoLogoutUrl)));
    return http.build();
  }
}
