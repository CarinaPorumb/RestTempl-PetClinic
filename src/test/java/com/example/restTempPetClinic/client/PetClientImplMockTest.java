package com.example.restTempPetClinic.client;

import com.example.restTempPetClinic.config.OAuthClientInterceptor;
import com.example.restTempPetClinic.config.RestTemplateBuilderConfig;
import com.example.restTempPetClinic.model.PetDTO;
import com.example.restTempPetClinic.model.PetDTOPageImpl;
import com.example.restTempPetClinic.model.PetType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

import static com.example.restTempPetClinic.client.PetClientImpl.PET_PATH;
import static com.example.restTempPetClinic.client.PetClientImpl.PET_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RestClientTest
@Import(RestTemplateBuilderConfig.class)
class PetClientImplMockTest {

    public static final String URL = "http://localhost:8080";
    public static final String BEARER_TEST = "Bearer test";

    PetClient petClient;

    MockRestServiceServer server;

    @Autowired
    RestTemplateBuilder restTemplateBuilderConfigured;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ClientRegistrationRepository clientRegistrationRepository;

    @Mock
    RestTemplateBuilder mockRestTemplateBuilder = new RestTemplateBuilder(new MockServerRestTemplateCustomizer());

    PetDTO petDTO;

    String dtoJson;

    @MockBean
    OAuth2AuthorizedClientManager manager;

    @TestConfiguration
    public static class TestConfig {

        @Bean
        ClientRegistrationRepository clientRegistrationRepository() {
            return new InMemoryClientRegistrationRepository(ClientRegistration
                    .withRegistrationId("springauth")
                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                    .clientId("test")
                    .tokenUri("test")
                    .build());
        }

        @Bean
        OAuth2AuthorizedClientService auth2AuthorizedClientService(ClientRegistrationRepository clientRegistrationRepository) {
            return new InMemoryOAuth2AuthorizedClientService(clientRegistrationRepository);
        }

        @Bean
        OAuthClientInterceptor oAuthClientInterceptor(OAuth2AuthorizedClientManager manager, ClientRegistrationRepository clientRegistrationRepository) {
            return new OAuthClientInterceptor(manager, clientRegistrationRepository);
        }
    }

    @BeforeEach
    void setUp() throws JsonProcessingException {
        ClientRegistration clientRegistration = clientRegistrationRepository
                .findByRegistrationId("springauth");

        OAuth2AccessToken token = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                "test", Instant.MIN, Instant.MAX);

        when(manager.authorize(any())).thenReturn(new OAuth2AuthorizedClient(clientRegistration,
                "test", token));

        RestTemplate restTemplate = restTemplateBuilderConfigured.build();
        server = MockRestServiceServer.bindTo(restTemplate).build();

        when(mockRestTemplateBuilder.build()).thenReturn(restTemplate);

        petClient = new PetClientImpl(mockRestTemplateBuilder);
        petDTO = getPetDTO();
        dtoJson = objectMapper.writeValueAsString(petDTO);
    }


    @Test
    void listPetsWithQueryParam() throws JsonProcessingException {
        String response = objectMapper.writeValueAsString(getPage());

        URI uri = UriComponentsBuilder.fromHttpUrl(URL + PET_PATH)
                .queryParam("name", "Rocco")
                .build().toUri();

        server.expect(method(HttpMethod.GET))
                .andExpect(requestTo(uri))
                .andExpect(header("Authorization", BEARER_TEST))
                .andExpect(queryParam("name", "Rocco"))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        Page<PetDTO> responsePage = petClient.listPets("Rocco", null, null, null, null, null);

        assertThat(responsePage.getContent().size()).isEqualTo(1);
        System.out.println(response);
    }

    @Test
    void testListPets() throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(getPage());

        server.expect(method(HttpMethod.GET))
                .andExpect(requestTo(URL + PET_PATH))
                .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));

        Page<PetDTO> dtos = petClient.listPets();
        assertThat(dtos.getContent().size()).isGreaterThan(0);
    }

    @Test
    void getById() {
        mockGetOperation();
        PetDTO responsePet = petClient.getById(petDTO.getId());
        assertThat(responsePet.getId()).isEqualTo(petDTO.getId());
    }

    @Test
    void saveNewPet() {
        URI uri = UriComponentsBuilder.fromPath(PET_PATH_ID).build(petDTO.getId());

        server.expect(method(HttpMethod.POST))
                .andExpect(requestTo(URL + PET_PATH))
                .andExpect(header("Authorization", BEARER_TEST))
                .andRespond(withAccepted().location(uri));

        mockGetOperation();

        PetDTO responsePet = petClient.saveNewPet(petDTO);
        assertThat(responsePet.getId()).isEqualTo(petDTO.getId());
    }

    @Test
    void updatePet() {
        server.expect(method(HttpMethod.PUT))
                .andExpect(requestToUriTemplate(URL + PET_PATH_ID, petDTO.getId()))
                .andExpect(header("Authorization", BEARER_TEST))
                .andRespond(withNoContent());

        mockGetOperation();

        PetDTO responsePet = petClient.updatePet(petDTO);
        assertThat(responsePet.getId()).isEqualTo(petDTO.getId());
    }

    @Test
    void deleteById() {
        server.expect(method(HttpMethod.DELETE))
                .andExpect(requestToUriTemplate(URL + PET_PATH_ID, petDTO.getId()))
                .andExpect(header("Authorization", BEARER_TEST))
                .andRespond(withNoContent());
        petClient.deleteById(petDTO.getId());

        server.verify();
    }

    @Test
    void testDeleteNotFound() {
        server.expect(method(HttpMethod.DELETE))
                .andExpect(requestToUriTemplate(URL + PET_PATH_ID, petDTO.getId()))
                .andExpect(header("Authorization", BEARER_TEST))
                .andRespond(withResourceNotFound());

        assertThrows(HttpClientErrorException.class, () -> {
            petClient.deleteById(petDTO.getId());
        });
        server.verify();
    }

    private void mockGetOperation() {
        server.expect(method(HttpMethod.GET))
                .andExpect(requestToUriTemplate(URL + PET_PATH_ID, petDTO.getId()))
                .andExpect(header("Authorization", BEARER_TEST))
                .andRespond(withSuccess(dtoJson, MediaType.APPLICATION_JSON));
    }

    PetDTO getPetDTO() {
        return PetDTO.builder()
                .id(UUID.randomUUID())
                .name("Test Pet")
                .petType(PetType.BIRD)
                .age(10)
                .weight(0.3)
                .build();
    }

    PetDTOPageImpl getPage() {
        return new PetDTOPageImpl(Arrays.asList(getPetDTO()), 1, 25, 1);
    }

}