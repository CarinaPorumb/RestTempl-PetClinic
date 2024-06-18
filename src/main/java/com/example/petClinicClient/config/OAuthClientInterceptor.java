package com.example.petClinicClient.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
public class OAuthClientInterceptor implements ClientHttpRequestInterceptor {

    private final OAuth2AuthorizedClientManager manager;

    public OAuthClientInterceptor(OAuth2AuthorizedClientManager manager) {
        this.manager = manager;
    }

    @Override
    public @NonNull ClientHttpResponse intercept(@NonNull HttpRequest request, @NonNull byte[] body, @NonNull ClientHttpRequestExecution execution) throws IOException {
        String registrationID = "springauth";

        Authentication principal = new UsernamePasswordAuthenticationToken("client", "secret", Collections.singletonList(new SimpleGrantedAuthority("ROLE_CLIENT")));

        OAuth2AuthorizeRequest oAuth2AuthorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId(registrationID)
                .principal(principal)
                .build();

        OAuth2AuthorizedClient client = manager.authorize(oAuth2AuthorizeRequest);
        if (client == null) {
            log.error("Failed to obtain OAuth2 authorized client");
            throw new IllegalStateException("Missing credentials");
        }

        String token = client.getAccessToken().getTokenValue();
        log.debug("Authorization token: {}", token);


        request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        log.debug("Authorization header added for request to {}", request.getURI());
        return execution.execute(request, body);
    }

}