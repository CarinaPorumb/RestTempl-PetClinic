package com.example.petClinicClient.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.Serial;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PetDTOPageImpl extends PageImpl<PetDTO> {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PetDTOPageImpl(@JsonProperty("content") List<PetDTO> content,
                          @JsonProperty("number") int page,
                          @JsonProperty("size") int size,
                          @JsonProperty("totalElements") long total,
                          @JsonProperty("pageable")
                          @JsonDeserialize(using = PageableDeserializer.class)
                          Pageable pageable) {
        super(content, pageable, total);
    }

}