package com.api.tests;

import com.api.tests.base.BaseTest;
import com.api.tests.client.ApiClient;
import com.api.tests.enums.ApiEndpoint;
import com.api.tests.models.Author;
import com.api.tests.utils.TestDataGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Feature("Authors API - FakeRestAPI")
public class AuthorsApiTest extends BaseTest {

    @Test
    @Story("Get All Authors")
    @Description("Verify that GET /api/v1/Authors returns a list of all authors")
    public void testGetAllAuthors() {
        logTestInfo("testGetAllAuthors", "Retrieve all authors from the API");

        Author[] authors = ApiClient.get(ApiEndpoint.AUTHORS, 200, Author[].class);
        
        assertNotNull(authors);
        assertTrue(authors.length > 0);
        
        // Validate first author structure
        Author firstAuthor = authors[0];
        assertTrue(firstAuthor.getId() > 0);
        assertTrue(firstAuthor.getIdBook() > 0);
        assertNotNull(firstAuthor.getFirstName());
        assertNotNull(firstAuthor.getLastName());
    }

    @Test
    @Story("Get Author by ID")
    @Description("Verify that GET /api/v1/Authors/{id} returns details of a specific author")
    public void testGetAuthorById() {
        logTestInfo("testGetAuthorById", "Retrieve a specific author by ID");

        int authorId = 1;
        Author author = ApiClient.get(ApiEndpoint.AUTHORS_BY_ID, authorId, 200, Author.class);
        
        assertNotNull(author);
        assertEquals(author.getId(), authorId);
        assertNotNull(author.getFirstName());
        assertNotNull(author.getLastName());
        assertTrue(author.getIdBook() > 0);
    }

    @Test
    @Story("Get Author by ID")
    @Description("Verify that GET /api/v1/Authors/{id} returns 404 for non-existent author")
    public void testGetNonExistentAuthor() {
        logTestInfo("testGetNonExistentAuthor", "Verify 404 for non-existent author");

        int nonExistentId = 999999;
        ApiClient.get(ApiEndpoint.AUTHORS_BY_ID, nonExistentId, 404);
    }

    @Test
    @Story("Create Author")
    @Description("Verify that POST /api/v1/Authors creates a new author successfully")
    public void testCreateAuthor() {
        logTestInfo("testCreateAuthor", "Create a new author via POST request");

        String[] nameParts = TestDataGenerator.generateRandomName().split(" ");
        Author newAuthor = Author.builder()
                .id(1)
                .idBook(1)
                .firstName(nameParts[0])
                .lastName(nameParts.length > 1 ? nameParts[1] : "TestLastName")
                .build();

        Author createdAuthor = ApiClient.post(ApiEndpoint.AUTHORS, newAuthor, 200, Author.class);
        assertNotNull(createdAuthor);
        assertTrue(createdAuthor.getId() > 0);
        assertEquals(createdAuthor.getFirstName(), newAuthor.getFirstName());
        assertEquals(createdAuthor.getLastName(), newAuthor.getLastName());
    }

    @Test
    @Story("Update Author")
    @Description("Verify that PUT /api/v1/Authors/{id} updates an existing author")
    public void testUpdateAuthor() {
        logTestInfo("testUpdateAuthor", "Update an existing author via PUT request");

        int authorId = 1;
        
        Author updatedAuthor = Author.builder()
                .id(authorId)
                .idBook(1)
                .firstName("Updated_John")
                .lastName("Updated_Doe")
                .build();

        Author responseAuthor = ApiClient.put(ApiEndpoint.AUTHORS_BY_ID, authorId, updatedAuthor, 200, Author.class);
        assertNotNull(responseAuthor);
        assertEquals(responseAuthor.getId(), authorId);
        assertEquals(responseAuthor.getFirstName(), updatedAuthor.getFirstName());
        assertEquals(responseAuthor.getLastName(), updatedAuthor.getLastName());
    }

    @Test
    @Story("Delete Author")
    @Description("Verify that DELETE /api/v1/Authors/{id} deletes an author successfully")
    public void testDeleteAuthor() {
        logTestInfo("testDeleteAuthor", "Delete an author via DELETE request");

        // Create an author first
        String[] nameParts = TestDataGenerator.generateRandomName().split(" ");
        Author authorToDelete = Author.builder()
                .id(0)
                .idBook(1)
                .firstName(nameParts[0])
                .lastName(nameParts.length > 1 ? nameParts[1] : "TestLastName")
                .build();

        Author createdAuthor = ApiClient.post(ApiEndpoint.AUTHORS, authorToDelete, 200, Author.class);
        int authorId = createdAuthor.getId();

        // Delete the author
        ApiClient.delete(ApiEndpoint.AUTHORS_BY_ID, authorId, 200);
    }

    @Test
    @Story("Create Author")
    @Description("Verify that POST /api/v1/Authors returns 400 for invalid author data")
    public void testCreateAuthorWithInvalidData() {
        logTestInfo("testCreateAuthorWithInvalidData", "Test POST with invalid data");

        String invalidAuthorJson = "{}";
        ApiClient.post(ApiEndpoint.AUTHORS, invalidAuthorJson, 400);
    }

    @Test
    @Story("Delete Author")
    @Description("Verify that DELETE /api/v1/Authors/{id} returns 404 for non-existent author")
    public void testDeleteNonExistentAuthor() {
        logTestInfo("testDeleteNonExistentAuthor", "Verify DELETE returns 404 for non-existent author");

        int nonExistentId = 999999;
        ApiClient.delete(ApiEndpoint.AUTHORS_BY_ID, nonExistentId, 404);
    }

    @Test
    @Story("Create Author")
    @Description("Verify that POST /api/v1/Authors validates required fields")
    public void testCreateAuthorWithMissingFields() {
        logTestInfo("testCreateAuthorWithMissingFields", "Test POST with missing required fields");

        Author invalidAuthor = Author.builder()
                .id(0)
                .idBook(1)
                .firstName("")
                .lastName("")
                .build();

        ApiClient.post(ApiEndpoint.AUTHORS, invalidAuthor, 400);
    }

    @Test
    @Story("Update Author")
    @Description("Verify that PUT /api/v1/Authors/{id} returns 400 for invalid data")
    public void testUpdateAuthorWithInvalidData() {
        logTestInfo("testUpdateAuthorWithInvalidData", "Test PUT with invalid data");

        int authorId = 1;
        
        Author invalidAuthor = Author.builder()
                .id(authorId)
                .idBook(-1)
                .firstName("")
                .lastName("")
                .build();

        ApiClient.put(ApiEndpoint.AUTHORS_BY_ID, authorId, invalidAuthor, 400);
    }

    @Test
    @Story("Data Validation")
    @Description("Verify that authors have proper data types and formats")
    public void testAuthorDataValidation() {
        logTestInfo("testAuthorDataValidation", "Validate author data types and formats");

        Author[] authors = ApiClient.get(ApiEndpoint.AUTHORS, 200, Author[].class);
        assertNotNull(authors);
        assertTrue(authors.length > 0);
        
        for (Author author : authors) {
            assertTrue(author.getId() > 0, "Author ID should be positive");
            assertTrue(author.getIdBook() > 0, "Book ID should be positive");
            assertNotNull(author.getFirstName(), "Author first name should not be null");
            assertNotNull(author.getLastName(), "Author last name should not be null");
        }
    }

    @Test
    @Story("Business Logic")
    @Description("Verify that authors can be associated with different books")
    public void testAuthorBookAssociation() {
        logTestInfo("testAuthorBookAssociation", "Test author-book association");

        String[] nameParts1 = TestDataGenerator.generateRandomName().split(" ");
        Author author1 = Author.builder()
                .id(0)
                .idBook(1)
                .firstName(nameParts1[0])
                .lastName(nameParts1.length > 1 ? nameParts1[1] : "LastName1")
                .build();

        String[] nameParts2 = TestDataGenerator.generateRandomName().split(" ");
        Author author2 = Author.builder()
                .id(0)
                .idBook(2)
                .firstName(nameParts2[0])
                .lastName(nameParts2.length > 1 ? nameParts2[1] : "LastName2")
                .build();

        Author createdAuthor1 = ApiClient.post(ApiEndpoint.AUTHORS, author1, 200, Author.class);
        Author createdAuthor2 = ApiClient.post(ApiEndpoint.AUTHORS, author2, 200, Author.class);

        assertNotNull(createdAuthor1);
        assertNotNull(createdAuthor2);
        assertNotEquals(createdAuthor1.getIdBook(), createdAuthor2.getIdBook());
        assertEquals(createdAuthor1.getIdBook(), 1);
        assertEquals(createdAuthor2.getIdBook(), 2);
    }
}