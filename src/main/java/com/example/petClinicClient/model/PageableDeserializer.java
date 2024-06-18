package com.example.petClinicClient.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;

public class PageableDeserializer extends JsonDeserializer<Pageable> {

    @Override
    public Pageable deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        int page = (node.has("page") ? node.get("page").asInt() : (node.has("number") ? node.get("number").asInt() : 0));
        int size = (node.has("size") ? node.get("size").asInt() : (node.has("pageSize") ? node.get("pageSize").asInt() : 10));
        JsonNode sortNode = node.get("sort");
        Sort sort = Sort.unsorted();

        if (sortNode != null && sortNode.isArray()) {
            for (JsonNode sortItem : sortNode) {
                String property = sortItem.get("property").asText();
                String direction = sortItem.get("direction").asText();
                sort = sort.and(Sort.by(Sort.Direction.fromString(direction), property));
            }
        }
        return PageRequest.of(page, size, sort);
    }

}