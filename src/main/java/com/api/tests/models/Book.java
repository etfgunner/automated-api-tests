package com.api.tests.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private int id;
    private String title;
    private String description;
    
    @JsonProperty("pageCount")
    private int pageCount;
    
    private String excerpt;
    
    @JsonProperty("publishDate")
    private String publishDate;
}