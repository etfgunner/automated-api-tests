package com.api.tests;

import com.api.tests.base.BaseTest;
import com.api.tests.client.ApiClient;
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

    @Test
    @Story("Get Book by ID")
    @Description("Verify that GET /api/v1/Books/{id} returns details of a specific book")
    public void testGetBookById() {
        logTestInfo("testGetBookById", "Retrieve a specific book by ID");

        int bookId = 1;
        Book book = ApiClient.get(ApiEndpoint.BOOKS_BY_ID, bookId, 200, Book.class);
        
        assertNotNull(book);
        assertEquals(book.getId(), bookId);
        assertNotNull(book.getTitle());
        assertNotNull(book.getDescription());
    }

    @Test
    @Story("Get Book by ID")
    @Description("Verify that GET /api/v1/Books/{id} returns 404 for non-existent book")
    public void testGetNonExistentBook() {
        logTestInfo("testGetNonExistentBook", "Verify 404 for non-existent book");

        int nonExistentId = 999999;
        ApiClient.get(ApiEndpoint.BOOKS_BY_ID, nonExistentId, 404);
    }

    @Test
    @Story("Create Book")
    @Description("Verify that POST /api/v1/Books creates a new book successfully")
    public void testCreateBook() {
        logTestInfo("testCreateBook", "Create a new book via POST request");

        Book newBook = Book.builder()
                .id(0)
                .title(TestDataGenerator.generateRandomBookTitle())
                .description("Test book description")
                .pageCount(200)
                .excerpt("Test excerpt")
                .publishDate("2024-01-01T00:00:00Z")
                .build();

        Book createdBook = ApiClient.post(ApiEndpoint.BOOKS, newBook, 200, Book.class);
        assertNotNull(createdBook);
        assertEquals(createdBook.getTitle(), newBook.getTitle());
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

    @Test
    @Story("Create Book")
    @Description("Verify that POST /api/v1/Books returns 400 for invalid book data")
    public void testCreateBookWithInvalidData() {
        logTestInfo("testCreateBookWithInvalidData", "Test POST with invalid data");

        String invalidBookJson = "{}";
        ApiClient.post(ApiEndpoint.BOOKS, invalidBookJson, 400);
    }

    @Test
    @Story("Delete Book")
    @Description("Verify that DELETE /api/v1/Books/{id} returns 404 for non-existent book")
    public void testDeleteNonExistentBook() {
        logTestInfo("testDeleteNonExistentBook", "Verify DELETE returns 404 for non-existent book");

        int nonExistentId = 999999;
        ApiClient.delete(ApiEndpoint.BOOKS_BY_ID, nonExistentId, 404);
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

    @Test
    @Story("Update Book")
    @Description("Verify that PUT /api/v1/Books/{id} returns 400 for invalid data")
    public void testUpdateBookWithInvalidData() {
        logTestInfo("testUpdateBookWithInvalidData", "Test PUT with invalid data");

        int bookId = 1;
        
        Book invalidBook = Book.builder()
                .id(bookId)
                .title("")
                .description("Valid description")
                .pageCount(-10)
                .excerpt("Valid excerpt")
                .publishDate("invalid-date-format")
                .build();

        ApiClient.put(ApiEndpoint.BOOKS_BY_ID, bookId, invalidBook, 400);
    }
}