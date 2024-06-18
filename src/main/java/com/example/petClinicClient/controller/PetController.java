package com.example.petClinicClient.controller;

import com.example.petClinicClient.client.PetClient;
import com.example.petClinicClient.model.PetDTO;
import com.example.petClinicClient.model.PetType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/pet")
@RequiredArgsConstructor
public class PetController {

    private final PetClient petClient;

    @GetMapping
    public Page<PetDTO> listPets(@RequestParam(required = false) String name,
                                 @RequestParam(required = false) PetType petType,
                                 @RequestParam(required = false) Integer age,
                                 @RequestParam(required = false) Double weight,
                                 @RequestParam(required = false) Integer pageNumber,
                                 @RequestParam(required = false) Integer pageSize) {
        return petClient.listPets(name, petType, age, weight, pageNumber, pageSize);
    }

    @GetMapping("/{id}")
    public PetDTO getPet(@PathVariable UUID id) {
        return petClient.getById(id);
    }

    @PostMapping
    public PetDTO createPet(@RequestBody PetDTO petDTO) {
        return petClient.saveNewPet(petDTO);
    }

    @PutMapping("/{id}")
    public PetDTO updatePet(@PathVariable UUID id, @RequestBody PetDTO petDTO) {
        petDTO.setId(id);
        return petClient.updatePet(petDTO);
    }

    @DeleteMapping("/{id}")
    public void deletePet(@PathVariable UUID id) {
        petClient.deleteById(id);
    }

}