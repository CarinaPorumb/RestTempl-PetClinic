//package com.example.petClinicClient.client;
//
//import com.example.petClinicClient.model.PetDTO;
//import com.example.petClinicClient.model.PetType;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.web.client.HttpClientErrorException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class PetClientImplTest {
//
//    @Autowired
//    PetClientImpl petClient;
//
//    @BeforeEach
//    void setUp() {
//    }
//
//    @Test
//    void listPets() {
//        petClient.listPets(null, null, null, null, null, null);
//    }
//
//    @Test
//    void testListPets() {
//        petClient.listPets("Nyx", null, null, null, null, null);
//    }
//
//    @Test
//    void getById() {
//        Page<PetDTO> petDTO = petClient.listPets();
//        PetDTO pet = petDTO.getContent().get(0);
//        PetDTO petById = petClient.getById(pet.getId());
//
//        assertNotNull(petById);
//    }
//
//    @Test
//    void saveNewPet() {
//        PetDTO pet = PetDTO.builder()
//                .name("Zigzag")
//                .petType(PetType.FISH)
//                .age(2)
//                .weight(0.15)
//                .build();
//
//        PetDTO savedPet = petClient.saveNewPet(pet);
//        assertNotNull(savedPet);
//    }
//
//    @Test
//    void updatePet() {
//        PetDTO testPet = PetDTO.builder()
//                .name("Zigzag")
//                .petType(PetType.FISH)
//                .age(2)
//                .weight(0.15)
//                .build();
//
//        PetDTO petDTO = petClient.saveNewPet(testPet);
//
//        final String newName = "Test Name";
//        petDTO.setName(newName);
//
//        PetDTO updatedPet = petClient.updatePet(petDTO);
//        assertEquals(newName, updatedPet.getName());
//    }
//
//
//    @Test
//    void deleteById() {
//        PetDTO testPet = PetDTO.builder()
//                .name("Zigzag")
//                .petType(PetType.FISH)
//                .age(2)
//                .weight(0.15)
//                .build();
//
//        PetDTO petDTO = petClient.saveNewPet(testPet);
//        petClient.deleteById(petDTO.getId());
//
//        assertThrows(HttpClientErrorException.class, () -> {  //to avoid 404 NotFound
//            petClient.getById(petDTO.getId());
//        });
//    }
//}