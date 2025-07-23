package com.api.tests.utils;

import com.api.tests.models.Author;
import com.api.tests.models.Book;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JsonDataReader {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String TEST_DATA_PATH = "/testdata/";

    public static List<Book> readValidBooks() {
        try {
            InputStream inputStream = JsonDataReader.class.getResourceAsStream(TEST_DATA_PATH + "validBooks.json");
            if (inputStream == null) {
                log.error("Could not find validBooks.json file");
                return new ArrayList<>();
            }
            
            return objectMapper.readValue(inputStream, new TypeReference<List<Book>>() {});
        } catch (IOException e) {
            log.error("Error reading valid books data", e);
            return new ArrayList<>();
        }
    }

    public static List<InvalidTestData<Book>> readInvalidBooks() {
        try {
            InputStream inputStream = JsonDataReader.class.getResourceAsStream(TEST_DATA_PATH + "invalidBooks.json");
            if (inputStream == null) {
                log.error("Could not find invalidBooks.json file");
                return new ArrayList<>();
            }
            
            JsonNode rootNode = objectMapper.readTree(inputStream);
            List<InvalidTestData<Book>> invalidData = new ArrayList<>();
            
            for (JsonNode node : rootNode) {
                Book book = objectMapper.treeToValue(node.get("testData"), Book.class);
                String expectedError = node.get("expectedError").asText();
                invalidData.add(new InvalidTestData<>(book, expectedError));
            }
            
            return invalidData;
        } catch (IOException e) {
            log.error("Error reading invalid books data", e);
            return new ArrayList<>();
        }
    }

    public static List<Author> readValidAuthors() {
        try {
            InputStream inputStream = JsonDataReader.class.getResourceAsStream(TEST_DATA_PATH + "validAuthors.json");
            if (inputStream == null) {
                log.error("Could not find validAuthors.json file");
                return new ArrayList<>();
            }
            
            return objectMapper.readValue(inputStream, new TypeReference<List<Author>>() {});
        } catch (IOException e) {
            log.error("Error reading valid authors data", e);
            return new ArrayList<>();
        }
    }

    public static List<InvalidTestData<Author>> readInvalidAuthors() {
        try {
            InputStream inputStream = JsonDataReader.class.getResourceAsStream(TEST_DATA_PATH + "invalidAuthors.json");
            if (inputStream == null) {
                log.error("Could not find invalidAuthors.json file");
                return new ArrayList<>();
            }
            
            JsonNode rootNode = objectMapper.readTree(inputStream);
            List<InvalidTestData<Author>> invalidData = new ArrayList<>();
            
            for (JsonNode node : rootNode) {
                Author author = objectMapper.treeToValue(node.get("testData"), Author.class);
                String expectedError = node.get("expectedError").asText();
                invalidData.add(new InvalidTestData<>(author, expectedError));
            }
            
            return invalidData;
        } catch (IOException e) {
            log.error("Error reading invalid authors data", e);
            return new ArrayList<>();
        }
    }

    public static TestIds readTestIds() {
        try {
            InputStream inputStream = JsonDataReader.class.getResourceAsStream(TEST_DATA_PATH + "testIds.json");
            if (inputStream == null) {
                log.error("Could not find testIds.json file");
                return new TestIds();
            }
            
            return objectMapper.readValue(inputStream, TestIds.class);
        } catch (IOException e) {
            log.error("Error reading test IDs data", e);
            return new TestIds();
        }
    }

    public static List<BulkTestData> readBulkTestData() {
        try {
            InputStream inputStream = JsonDataReader.class.getResourceAsStream(TEST_DATA_PATH + "bulkTestData.json");
            if (inputStream == null) {
                log.error("Could not find bulkTestData.json file");
                return new ArrayList<>();
            }
            
            return objectMapper.readValue(inputStream, new TypeReference<List<BulkTestData>>() {});
        } catch (IOException e) {
            log.error("Error reading bulk test data", e);
            return new ArrayList<>();
        }
    }

    public static ValidationData readValidationData() {
        try {
            InputStream inputStream = JsonDataReader.class.getResourceAsStream(TEST_DATA_PATH + "validationData.json");
            if (inputStream == null) {
                log.error("Could not find validationData.json file");
                return new ValidationData();
            }
            
            return objectMapper.readValue(inputStream, ValidationData.class);
        } catch (IOException e) {
            log.error("Error reading validation data", e);
            return new ValidationData();
        }
    }

    // Helper classes for JSON data structure
    public static class InvalidTestData<T> {
        private final T testData;
        private final String expectedError;

        public InvalidTestData(T testData, String expectedError) {
            this.testData = testData;
            this.expectedError = expectedError;
        }

        public T getTestData() {
            return testData;
        }

        public String getExpectedError() {
            return expectedError;
        }
    }

    public static class TestIds {
        private List<Integer> validBookIds = new ArrayList<>();
        private List<Integer> validAuthorIds = new ArrayList<>();
        private List<InvalidId> invalidIds = new ArrayList<>();

        // Getters and setters
        public List<Integer> getValidBookIds() { return validBookIds; }
        public void setValidBookIds(List<Integer> validBookIds) { this.validBookIds = validBookIds; }

        public List<Integer> getValidAuthorIds() { return validAuthorIds; }
        public void setValidAuthorIds(List<Integer> validAuthorIds) { this.validAuthorIds = validAuthorIds; }

        public List<InvalidId> getInvalidIds() { return invalidIds; }
        public void setInvalidIds(List<InvalidId> invalidIds) { this.invalidIds = invalidIds; }

        public static class InvalidId {
            private int id;
            private String description;

            // Getters and setters
            public int getId() { return id; }
            public void setId(int id) { this.id = id; }

            public String getDescription() { return description; }
            public void setDescription(String description) { this.description = description; }
        }
    }

    public static class BulkTestData {
        private int bookId;
        private String bookTitle;
        private String authorFirstName;
        private String authorLastName;

        // Getters and setters
        public int getBookId() { return bookId; }
        public void setBookId(int bookId) { this.bookId = bookId; }

        public String getBookTitle() { return bookTitle; }
        public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

        public String getAuthorFirstName() { return authorFirstName; }
        public void setAuthorFirstName(String authorFirstName) { this.authorFirstName = authorFirstName; }

        public String getAuthorLastName() { return authorLastName; }
        public void setAuthorLastName(String authorLastName) { this.authorLastName = authorLastName; }
    }

    public static class ValidationData {
        private List<PageCountValidation> pageCountValidation = new ArrayList<>();
        private List<SearchTerm> bookSearchTerms = new ArrayList<>();

        // Getters and setters
        public List<PageCountValidation> getPageCountValidation() { return pageCountValidation; }
        public void setPageCountValidation(List<PageCountValidation> pageCountValidation) { 
            this.pageCountValidation = pageCountValidation; 
        }

        public List<SearchTerm> getBookSearchTerms() { return bookSearchTerms; }
        public void setBookSearchTerms(List<SearchTerm> bookSearchTerms) { 
            this.bookSearchTerms = bookSearchTerms; 
        }

        public static class PageCountValidation {
            private int pageCount;
            private boolean shouldBeValid;
            private String description;

            // Getters and setters
            public int getPageCount() { return pageCount; }
            public void setPageCount(int pageCount) { this.pageCount = pageCount; }

            public boolean isShouldBeValid() { return shouldBeValid; }
            public void setShouldBeValid(boolean shouldBeValid) { this.shouldBeValid = shouldBeValid; }

            public String getDescription() { return description; }
            public void setDescription(String description) { this.description = description; }
        }

        public static class SearchTerm {
            private String searchTerm;
            private String description;

            // Getters and setters
            public String getSearchTerm() { return searchTerm; }
            public void setSearchTerm(String searchTerm) { this.searchTerm = searchTerm; }

            public String getDescription() { return description; }
            public void setDescription(String description) { this.description = description; }
        }
    }
}