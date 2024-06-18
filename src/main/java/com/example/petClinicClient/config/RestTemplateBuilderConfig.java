package com.example.petClinicClient.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RestTemplateBuilderConfig {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Value("${rest.template.rootUrl}")
    String rootUrl;

    @Bean
    @Primary
    public OAuth2AuthorizedClientManager auth2AuthorizedClientManager() {

        log.info("Configuring OAuth2AuthorizedClientManager.");

        var authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        var authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository,
                authorizedClientService);

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return authorizedClientManager;
    }

    @Bean
    public RestTemplateBuilder restTemplateBuilder(OAuthClientInterceptor interceptor) {
        if (rootUrl == null) {
            log.error("Root URL must not be null");
            throw new IllegalArgumentException("Root URL must not be null");
        }

        log.info("Configuring RestTemplateBuilder with root URL: {}", rootUrl);
        return new RestTemplateBuilder()
                .additionalInterceptors(interceptor)
                .uriTemplateHandler(new DefaultUriBuilderFactory(rootUrl));
    }

}