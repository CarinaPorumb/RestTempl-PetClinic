package com.example.restTempPetClinic.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true, value = "pageable")
public class PetDTOPageImpl<PetDTO> extends PageImpl<com.example.restTempPetClinic.model.PetDTO> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PetDTOPageImpl(@JsonProperty("content") List<com.example.restTempPetClinic.model.PetDTO> content,
                          @JsonProperty("number") int page,
                          @JsonProperty("size") int size,
                          @JsonProperty("totalElements") long total) {
        super(content, PageRequest.of(page, size), total);
    }

    public PetDTOPageImpl(List<com.example.restTempPetClinic.model.PetDTO> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PetDTOPageImpl(List<com.example.restTempPetClinic.model.PetDTO> content) {
        super(content);
    }
}