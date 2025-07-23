package com.api.tests.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiEndpoint {
    
    // Books endpoints
    BOOKS("/Books"),
    BOOKS_BY_ID("/Books/{id}"),
    
    // Authors endpoints  
    AUTHORS("/Authors"),
    AUTHORS_BY_ID("/Authors/{id}");

    private final String path;

    /**
     * Get the endpoint path with ID parameter replaced
     * @param id the ID to replace {id} placeholder with
     * @return the path with ID substituted
     */
    public String withId(Object id) {
        return path.replace("{id}", String.valueOf(id));
    }
}