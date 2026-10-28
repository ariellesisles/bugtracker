package com.project.bugtracker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class OAuth2LoginSecurityConfig {

@Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(authorizeRequests ->
            authorizeRequests.anyRequest().authenticated())
            .oauth2Login(Customizer.withDefaults())
            .logout(logout -> logout.logoutSuccessHandler(oidcLogoutSuccessHandler()));

//            authorizeRequests
//                    .requestMatchers("/bugtracker/ui").authenticated()
//                    .requestMatchers("/bugtracker/ui/admin/**").hasAuthority("SCOPE_bugtracker.admin")
//                    .requestMatchers("/bugtracker/**").hasAllAuthorities("SCOPE_bugtracker.admin",
//                            "SCOPE_bugtracker.user")
//                    .anyRequest().authenticated())
//
//            .oauth2Login(oauth2Login ->
//                    oauth2Login.authorizationEndpoint(
//                            cfg ->
//                                    cfg.authorizationRequestResolver(pkceResolver(clientRegistrationRepository))))
//            .logout(logout-> logout.logoutSuccessHandler(oidcLogoutSuccessHandler()));

return http.build();
}

    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedLogoutSuccessHandler(this.clientRegistrationRepository);

        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/bugtracker/ui");
        return oidcLogoutSuccessHandler;
    }

    // Helper method to resolve authorization requests with PKCE enabled
    private OAuth2AuthorizationRequestResolver pkceResolver(ClientRegistrationRepository repo) {
        DefaultOAuth2AuthorizationRequestResolver resolver =
                new DefaultOAuth2AuthorizationRequestResolver(repo, "/oauth2/authorization");

        // Automatically adds PKCE (Proof Key for Code Exchange) support
        resolver.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers.withPkce());

        return resolver;
    }
}

