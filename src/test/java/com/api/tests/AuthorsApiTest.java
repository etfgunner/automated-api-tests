package com.api.tests;

import com.api.tests.base.BaseTest;
import com.api.tests.client.ApiClient;
import com.api.tests.dataproviders.TestDataProviders;
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

    @Test(dataProvider = "authorIds", dataProviderClass = TestDataProviders.class)
    @Story("Get Author by ID")
    @Description("Verify that GET /api/v1/Authors/{id} returns details of a specific author")
    public void testGetAuthorById(int authorId) {
        logTestInfo("testGetAuthorById", "Retrieve author by ID: " + authorId);

        Author author = ApiClient.get(ApiEndpoint.AUTHORS_BY_ID, authorId, 200, Author.class);
        
        assertNotNull(author);
        assertEquals(author.getId(), authorId);
        assertNotNull(author.getFirstName());
        assertNotNull(author.getLastName());
        assertTrue(author.getIdBook() > 0);
    }

    @Test(dataProvider = "invalidIds", dataProviderClass = TestDataProviders.class)
    @Story("Get Author by ID")
    @Description("Verify that GET /api/v1/Authors/{id} returns 404 for non-existent author")
    public void testGetNonExistentAuthor(Object invalidId, String description) {
        logTestInfo("testGetNonExistentAuthor", "Verify 404 for: " + description);

        ApiClient.get(ApiEndpoint.AUTHORS_BY_ID, invalidId, 404);
    }

    @Test(dataProvider = "validAuthorData", dataProviderClass = TestDataProviders.class)
    @Story("Create Author")
    @Description("Verify that POST /api/v1/Authors creates a new author successfully")
    public void testCreateAuthorWithValidData(Author newAuthor) {
        logTestInfo("testCreateAuthorWithValidData", 
            "Create author: " + newAuthor.getFirstName() + " " + newAuthor.getLastName());

        Author createdAuthor = ApiClient.post(ApiEndpoint.AUTHORS, newAuthor, 200, Author.class);
        assertNotNull(createdAuthor);
        assertEquals(createdAuthor.getFirstName(), newAuthor.getFirstName());
        assertEquals(createdAuthor.getLastName(), newAuthor.getLastName());
        assertEquals(createdAuthor.getIdBook(), newAuthor.getIdBook());
    }

    @Test(dataProvider = "randomAuthorData", dataProviderClass = TestDataProviders.class)
    @Story("Create Author")
    @Description("Verify that POST /api/v1/Authors creates random authors successfully")
    public void testCreateRandomAuthor(Author randomAuthor) {
        logTestInfo("testCreateRandomAuthor", 
            "Create random author: " + randomAuthor.getFirstName() + " " + randomAuthor.getLastName());

        Author createdAuthor = ApiClient.post(ApiEndpoint.AUTHORS, randomAuthor, 200, Author.class);
        assertNotNull(createdAuthor);
        assertEquals(createdAuthor.getFirstName(), randomAuthor.getFirstName());
        assertEquals(createdAuthor.getLastName(), randomAuthor.getLastName());
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

    @Test(dataProvider = "invalidAuthorData", dataProviderClass = TestDataProviders.class)
    @Story("Create Author")
    @Description("Verify that POST /api/v1/Authors returns 400 for invalid author data")
    public void testCreateAuthorWithInvalidData(Author invalidAuthor, String expectedError) {
        logTestInfo("testCreateAuthorWithInvalidData", "Test invalid data: " + expectedError);

        ApiClient.post(ApiEndpoint.AUTHORS, invalidAuthor, 400);
    }

    @Test(dataProvider = "invalidIds", dataProviderClass = TestDataProviders.class)
    @Story("Delete Author")
    @Description("Verify that DELETE /api/v1/Authors/{id} returns 404 for non-existent author")
    public void testDeleteNonExistentAuthor(Object invalidId, String description) {
        logTestInfo("testDeleteNonExistentAuthor", "Verify DELETE 404 for: " + description);

        ApiClient.delete(ApiEndpoint.AUTHORS_BY_ID, invalidId, 404);
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

    @Test(dataProvider = "bulkTestData", dataProviderClass = TestDataProviders.class)
    @Story("Bulk Operations")
    @Description("Verify bulk operations with author and book data")
    public void testBulkAuthorOperations(int bookId, String bookTitle, String authorFirstName, String authorLastName) {
        logTestInfo("testBulkAuthorOperations", 
            String.format("Testing bulk author data: %s %s for Book ID %d (%s)", 
                authorFirstName, authorLastName, bookId, bookTitle));

        // Create an author with the bulk test data
        Author bulkAuthor = Author.builder()
                .id(0)
                .idBook(bookId)
                .firstName(authorFirstName)
                .lastName(authorLastName)
                .build();

        Author createdAuthor = ApiClient.post(ApiEndpoint.AUTHORS, bulkAuthor, 200, Author.class);
        assertNotNull(createdAuthor);
        assertEquals(createdAuthor.getFirstName(), authorFirstName);
        assertEquals(createdAuthor.getLastName(), authorLastName);
        assertEquals(createdAuthor.getIdBook(), bookId);
    }

    @Test
    @Story("Author-Book Relationship")
    @Description("Verify authors are properly associated with valid book IDs")
    public void testAuthorBookIdValidation() {
        logTestInfo("testAuthorBookIdValidation", "Validate author-book ID relationships");

        Author[] authors = ApiClient.get(ApiEndpoint.AUTHORS, 200, Author[].class);
        assertNotNull(authors);
        assertTrue(authors.length > 0);
        
        // Verify all authors have valid book IDs
        for (Author author : authors) {
            assertTrue(author.getIdBook() > 0, 
                String.format("Author %s %s should have a valid book ID", 
                    author.getFirstName(), author.getLastName()));
        }
    }
}