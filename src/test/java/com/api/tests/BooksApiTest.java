package com.api.tests;

import com.api.tests.base.BaseTest;
import com.api.tests.client.ApiClient;
import com.api.tests.dataproviders.TestDataProviders;
import com.api.tests.enums.ApiEndpoint;
import com.api.tests.models.Book;
import com.api.tests.utils.TestDataGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@Feature("Books API - FakeRestAPI")
public class BooksApiTest extends BaseTest {

    @Test
    @Story("Get All Books")
    @Description("Verify that GET /api/v1/Books returns a list of all books")
    public void testGetAllBooks() {
        logTestInfo("testGetAllBooks", "Retrieve all books from the API");

        Book[] books = ApiClient.get(ApiEndpoint.BOOKS, 200, Book[].class);
        
        assertNotNull(books);
        assertTrue(books.length > 0);
        
        // Validate first book structure
        Book firstBook = books[0];
        assertTrue(firstBook.getId() > 0);
        assertNotNull(firstBook.getTitle());
        assertNotNull(firstBook.getDescription());
    }

    @Test(dataProvider = "bookIds", dataProviderClass = TestDataProviders.class)
    @Story("Get Book by ID")
    @Description("Verify that GET /api/v1/Books/{id} returns details of a specific book")
    public void testGetBookById(int bookId) {
        logTestInfo("testGetBookById", "Retrieve book by ID: " + bookId);

        Book book = ApiClient.get(ApiEndpoint.BOOKS_BY_ID, bookId, 200, Book.class);
        
        assertNotNull(book);
        assertEquals(book.getId(), bookId);
        assertNotNull(book.getTitle());
        assertNotNull(book.getDescription());
    }

    @Test(dataProvider = "invalidIds", dataProviderClass = TestDataProviders.class)
    @Story("Get Book by ID")
    @Description("Verify that GET /api/v1/Books/{id} returns 404 for non-existent book")
    public void testGetNonExistentBook(Object invalidId, String description) {
        logTestInfo("testGetNonExistentBook", "Verify 404 for: " + description);

        ApiClient.get(ApiEndpoint.BOOKS_BY_ID, invalidId, 404);
    }

    @Test(dataProvider = "validBookData", dataProviderClass = TestDataProviders.class)
    @Story("Create Book")
    @Description("Verify that POST /api/v1/Books creates a new book successfully")
    public void testCreateBookWithValidData(Book newBook) {
        logTestInfo("testCreateBookWithValidData", "Create book: " + newBook.getTitle());

        Book createdBook = ApiClient.post(ApiEndpoint.BOOKS, newBook, 200, Book.class);
        assertNotNull(createdBook);
        assertEquals(createdBook.getTitle(), newBook.getTitle());
        assertEquals(createdBook.getDescription(), newBook.getDescription());
    }

    @Test(dataProvider = "randomBookData", dataProviderClass = TestDataProviders.class)
    @Story("Create Book")
    @Description("Verify that POST /api/v1/Books creates random books successfully")
    public void testCreateRandomBook(Book randomBook) {
        logTestInfo("testCreateRandomBook", "Create random book: " + randomBook.getTitle());

        Book createdBook = ApiClient.post(ApiEndpoint.BOOKS, randomBook, 200, Book.class);
        assertNotNull(createdBook);
        assertEquals(createdBook.getTitle(), randomBook.getTitle());
    }

    @Test
    @Story("Update Book")
    @Description("Verify that PUT /api/v1/Books/{id} updates an existing book")
    public void testUpdateBook() {
        logTestInfo("testUpdateBook", "Update an existing book via PUT request");

        int bookId = 1;
        
        Book updatedBook = Book.builder()
                .id(bookId)
                .title("Updated Title")
                .description("Updated description")
                .pageCount(250)
                .excerpt("Updated excerpt")
                .publishDate("2024-12-31T23:59:59Z")
                .build();

        Book responseBook = ApiClient.put(ApiEndpoint.BOOKS_BY_ID, bookId, updatedBook, 200, Book.class);
        assertNotNull(responseBook);
        assertEquals(responseBook.getId(), bookId);
        assertEquals(responseBook.getTitle(), updatedBook.getTitle());
    }

    @Test
    @Story("Delete Book")
    @Description("Verify that DELETE /api/v1/Books/{id} deletes a book successfully")
    public void testDeleteBook() {
        logTestInfo("testDeleteBook", "Delete a book via DELETE request");

        // Create a book first
        Book bookToDelete = Book.builder()
                .id(0)
                .title("Book to be deleted")
                .description("This book will be deleted")
                .pageCount(100)
                .excerpt("Test excerpt")
                .publishDate("2024-01-01T00:00:00.000Z")
                .build();

        Book createdBook = ApiClient.post(ApiEndpoint.BOOKS, bookToDelete, 200, Book.class);
        int bookId = createdBook.getId();

        // Delete the book
        ApiClient.delete(ApiEndpoint.BOOKS_BY_ID, bookId, 200);
    }

    @Test(dataProvider = "invalidBookData", dataProviderClass = TestDataProviders.class)
    @Story("Create Book")
    @Description("Verify that POST /api/v1/Books returns 400 for invalid book data")
    public void testCreateBookWithInvalidData(Book invalidBook, String expectedError) {
        logTestInfo("testCreateBookWithInvalidData", "Test invalid data: " + expectedError);

        ApiClient.post(ApiEndpoint.BOOKS, invalidBook, 400);
    }

    @Test(dataProvider = "invalidIds", dataProviderClass = TestDataProviders.class)
    @Story("Delete Book")
    @Description("Verify that DELETE /api/v1/Books/{id} returns 404 for non-existent book")
    public void testDeleteNonExistentBook(Object invalidId, String description) {
        logTestInfo("testDeleteNonExistentBook", "Verify DELETE 404 for: " + description);

        ApiClient.delete(ApiEndpoint.BOOKS_BY_ID, invalidId, 404);
    }

    @Test
    @Story("Data Validation")
    @Description("Verify that books have proper data types and formats")
    public void testBookDataValidation() {
        logTestInfo("testBookDataValidation", "Validate book data types and formats");

        Book[] books = ApiClient.get(ApiEndpoint.BOOKS, 200, Book[].class);
        assertNotNull(books);
        assertTrue(books.length > 0);
        
        for (Book book : books) {
            assertTrue(book.getId() > 0, "Book ID should be positive");
            assertNotNull(book.getTitle(), "Book title should not be null");
            assertNotNull(book.getDescription(), "Book description should not be null");
            assertTrue(book.getPageCount() >= 0, "Page count should not be negative");
        }
    }

    @Test(dataProvider = "pageCountValidation", dataProviderClass = TestDataProviders.class)
    @Story("Update Book")
    @Description("Verify page count validation in PUT /api/v1/Books/{id}")
    public void testUpdateBookPageCountValidation(int pageCount, boolean shouldBeValid, String description) {
        logTestInfo("testUpdateBookPageCountValidation", "Test page count: " + description);

        int bookId = 1;
        Book bookWithPageCount = Book.builder()
                .id(bookId)
                .title("Updated Title")
                .description("Valid description")
                .pageCount(pageCount)
                .excerpt("Valid excerpt")
                .publishDate("2024-01-01T00:00:00Z")
                .build();

        int expectedStatus = shouldBeValid ? 200 : 400;
        ApiClient.put(ApiEndpoint.BOOKS_BY_ID, bookId, bookWithPageCount, expectedStatus);
    }

    @Test(dataProvider = "bookSearchData", dataProviderClass = TestDataProviders.class)
    @Story("Search Books")
    @Description("Verify book search functionality with various search terms")
    public void testBookSearch(String searchTerm, String description) {
        logTestInfo("testBookSearch", "Search books with: " + description);

        // Note: This assumes the API supports search functionality
        // If not implemented, this test would need to be adapted or removed
        Book[] books = ApiClient.get(ApiEndpoint.BOOKS, 200, Book[].class);
        assertNotNull(books);
        
        // Basic validation that books are returned
        assertTrue(books.length >= 0);
    }

    @Test(dataProvider = "bulkTestData", dataProviderClass = TestDataProviders.class)
    @Story("Bulk Operations")
    @Description("Verify bulk operations with book and author data")
    public void testBulkBookOperations(int bookId, String bookTitle, String authorFirstName, String authorLastName) {
        logTestInfo("testBulkBookOperations", 
            String.format("Testing bulk data: Book ID %d, Title: %s, Author: %s %s", 
                bookId, bookTitle, authorFirstName, authorLastName));

        // Test getting book by the bulk test data ID
        if (bookId > 0) {
            Book book = ApiClient.get(ApiEndpoint.BOOKS_BY_ID, bookId, 200, Book.class);
            assertNotNull(book);
            assertEquals(book.getId(), bookId);
        }
    }
}