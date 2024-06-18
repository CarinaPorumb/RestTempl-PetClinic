package com.example.petClinicClient.client;

import com.example.petClinicClient.exception.CustomServiceException;
import com.example.petClinicClient.model.PetDTO;
import com.example.petClinicClient.model.PetDTOPageImpl;
import com.example.petClinicClient.model.PetType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PetClientImpl implements PetClient {

    private final RestTemplate restTemplate;

    @Value("${rest.template.rootUrl}")
    private String rootUrl;

    public static final String PET_PATH = "/pet";
    public static final String PET_PATH_ID = PET_PATH + "/{id}";

    public PetClientImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public Page<PetDTO> listPets() {
        return this.listPets(null, null, null, null, null, null);
    }

    @Override
    public Page<PetDTO> listPets(String name, PetType petType, Integer age, Double weight, Integer pageNumber, Integer pageSize) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(rootUrl + PET_PATH)
                .queryParamIfPresent("name", Optional.ofNullable(name))
                .queryParamIfPresent("petType", Optional.ofNullable(petType))
                .queryParamIfPresent("age", Optional.ofNullable(age))
                .queryParamIfPresent("weight", Optional.ofNullable(weight))
                .queryParamIfPresent("pageNumber", Optional.ofNullable(pageNumber))
                .queryParamIfPresent("pageSize", Optional.ofNullable(pageSize));

        try {
            ResponseEntity<PetDTOPageImpl> response = restTemplate.getForEntity(uriComponentsBuilder.toUriString(), PetDTOPageImpl.class);
            return response.getBody();
        } catch (RestClientException e) {
            log.error("Error retrieving list of pets", e);
            throw new CustomServiceException("Failed to list pets", e);
        }
    }

    @Override
    public PetDTO getById(UUID id) {
        try {
            return restTemplate.getForObject(rootUrl + PET_PATH_ID, PetDTO.class, id);
        } catch (RestClientException e) {
            log.error("Error retrieving pet by id: {}", id, e);
            throw new CustomServiceException("Failed to retrieve pet", e);
        }
    }

    @Override
    public PetDTO saveNewPet(PetDTO petDTO) {
        try {
            URI uri = restTemplate.postForLocation(rootUrl + PET_PATH, petDTO);
            log.info("Post location URI: {}", uri);

            if (uri == null) {
                log.error("Failed to get URI when saving new pet");
                throw new CustomServiceException("Failed to save new pet due to null URI");
            }

            // Ensure the returned URI is absolute
            URI absoluteUri;
            if (uri.isAbsolute()) {
                absoluteUri = uri;
            } else {
                absoluteUri = UriComponentsBuilder.fromHttpUrl(rootUrl).path(uri.getPath()).build().toUri();
            }
            log.info("Absolute URI: {}", absoluteUri);

            return restTemplate.getForObject(absoluteUri, PetDTO.class);

        } catch (RestClientException e) {
            log.error("Error saving new pet", e);
            throw new CustomServiceException("Failed to save new pet", e);
        }
    }


    @Override
    public PetDTO updatePet(PetDTO petDTO) {
        try {
            restTemplate.put(rootUrl + PET_PATH_ID, petDTO, petDTO.getId());
            return getById(petDTO.getId());
        } catch (RestClientException e) {
            log.error("Error updating pet", e);
            throw new CustomServiceException("Failed to update pet", e);
        }
    }

    @Override
    public void deleteById(UUID id) {
        try {
            restTemplate.delete(rootUrl + PET_PATH_ID, id);
        } catch (RestClientException e) {
            log.error("Error deleting pet with ID: {}", id, e);
            throw new CustomServiceException("Failed to delete pet", e);
        }
    }

}