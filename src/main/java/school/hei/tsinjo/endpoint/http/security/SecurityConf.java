package school.hei.tsinjo.endpoint.http.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConf {

  private static final Logger log = LoggerFactory.getLogger(SecurityConf.class);

  private final String casdoorClientId;
  private final String casdoorLogoutUrl;
  private final String tsinjoLogoutUrl;
  private final ClientRegistrationRepository clientRegistrationRepository;

  public SecurityConf(
      @Value("${spring.security.oauth2.client.registration.casdoor.clientid}")
          String casdoorClientId,
      @Value("${casdoor.logout.url}") String casdoorLogoutUrl,
      @Value("${tsinjo.logout.url}") String tsinjoLogoutUrl,
      ClientRegistrationRepository clientRegistrationRepository) {
    this.casdoorClientId = casdoorClientId;
    this.casdoorLogoutUrl = casdoorLogoutUrl;
    this.tsinjoLogoutUrl = tsinjoLogoutUrl;
    this.clientRegistrationRepository = clientRegistrationRepository;
  }

  @Bean
  public GeneratedStateAuthorizationRequestRepository generatedStateRepository() {
    return new GeneratedStateAuthorizationRequestRepository();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(Customizer.withDefaults())
        .authorizeHttpRequests(
            authz -> authz.requestMatchers("/").permitAll().anyRequest().authenticated())
        .oauth2Login(
            oauth2 ->
                oauth2
                    .loginPage("/oauth2/authorization/casdoor")
                    .authorizationEndpoint(
                        auth ->
                            auth.authorizationRequestResolver(
                                new DynamicStateAuthorizationRequestResolver(
                                    clientRegistrationRepository,
                                    "/oauth2/authorization",
                                    generatedStateRepository())))
                    .successHandler(
                        (request, response, authentication) -> {
                          log.info("✅ OAuth2 login SUCCESS");
                          log.info("User: {}", authentication.getName());
                          log.info("Authorities: {}", authentication.getAuthorities());
                          response.sendRedirect("/history");
                        })
                    .failureHandler(
                        (request, response, exception) -> {
                          log.error("❌ OAuth2 login FAILURE", exception);
                          log.error("Message: {}", exception.getMessage());
                          new SimpleUrlAuthenticationFailureHandler("/oauth2/authorization/casdoor")
                              .onAuthenticationFailure(request, response, exception);
                          log.info("🔄 Forced redirect to /oauth2/authorization/casdoor executed");
                        }))
        .logout(
            logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                    .logoutSuccessHandler(
                        (request, response, authentication) -> {
                          if (authentication != null) {
                            var principal = (DefaultOidcUser) authentication.getPrincipal();
                            String accessToken = principal.getIdToken().getTokenValue();
                            response.sendRedirect(
                                casdoorLogoutUrl
                                    + "?id_token_hint="
                                    + accessToken
                                    + "&post_logout_redirect_uri="
                                    + tsinjoLogoutUrl);
                          } else {
                            response.sendRedirect(tsinjoLogoutUrl);
                          }
                        }));
    return http.build();
  }
}
