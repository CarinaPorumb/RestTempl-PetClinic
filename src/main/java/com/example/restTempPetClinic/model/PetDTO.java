package com.example.restTempPetClinic.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class PetDTO {

    private UUID id;
    private String name;
    private PetType petType;
    private Integer age;
    private Double weight;

}