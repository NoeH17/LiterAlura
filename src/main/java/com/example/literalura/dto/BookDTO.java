package com.example.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookDTO(

        Long id,
        String title,
        List<AuthorDTO> authors,

        @JsonAlias("languages")
        List<String> languages

) {}