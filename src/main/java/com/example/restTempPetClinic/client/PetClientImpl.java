package com.example.restTempPetClinic.client;

import com.example.restTempPetClinic.model.PetDTO;
import com.example.restTempPetClinic.model.PetDTOPageImpl;
import com.example.restTempPetClinic.model.PetType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PetClientImpl implements PetClient {

    private final RestTemplateBuilder restTemplateBuilder;

    public static final String PET_PATH = "/api/v1/pet";
    public static final String PET_PATH_ID = PET_PATH + "/{id}";

    @Override
    public Page<PetDTO> listPets() {
        return this.listPets(null, null, null, null, null, null);
    }

    @Override
    public Page<PetDTO> listPets(String name, PetType petType, Integer age, Double weight, Integer pageNumber, Integer pageSize) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(PET_PATH);

        if (name != null) {
            uriComponentsBuilder.queryParam("name", name);
        }
        if (petType != null) {
            uriComponentsBuilder.queryParam("petType", petType);
        }
        if (age != null) {
            uriComponentsBuilder.queryParam("age", age);
        }
        if (weight != null) {
            uriComponentsBuilder.queryParam("weight", weight);
        }
        if (pageNumber != null) {
            uriComponentsBuilder.queryParam("pageNumber", pageNumber);
        }
        if (pageSize != null) {
            uriComponentsBuilder.queryParam("pageNumber", pageSize);
        }

        ResponseEntity<PetDTOPageImpl> response = restTemplate.getForEntity(uriComponentsBuilder.toUriString(), PetDTOPageImpl.class);


        return response.getBody();
    }

    @Override
    public PetDTO getById(UUID id) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        return restTemplate.getForObject(PET_PATH_ID, PetDTO.class, id);
    }

    @Override
    public PetDTO saveNewPet(PetDTO petDTO) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        URI uri = restTemplate.postForLocation(PET_PATH, petDTO);
        assert uri != null;
        return restTemplate.getForObject(uri.getPath(), PetDTO.class);
    }

    @Override
    public PetDTO updatePet(PetDTO petDTO) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.put(PET_PATH_ID, petDTO, petDTO.getId());
        return getById(petDTO.getId());
    }

    @Override
    public void deleteById(UUID id) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        restTemplate.delete(PET_PATH_ID, id);
    }

}
