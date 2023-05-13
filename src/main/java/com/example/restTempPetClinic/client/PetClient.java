package com.example.restTempPetClinic.client;

import com.example.restTempPetClinic.model.PetDTO;
import com.example.restTempPetClinic.model.PetType;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface PetClient {

    Page<PetDTO> listPets();

    Page<PetDTO> listPets(String name, PetType petType, Integer age, Double weight, Integer pageNumber, Integer pageSize);

    PetDTO getById(UUID id);

    PetDTO saveNewPet(PetDTO petDTO);

    PetDTO updatePet(PetDTO petDTO);

    void deleteById(UUID id);

}