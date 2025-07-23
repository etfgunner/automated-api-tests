package com.api.tests.dataproviders;

import com.api.tests.models.Author;
import com.api.tests.models.Book;
import com.api.tests.utils.TestDataGenerator;
import com.api.tests.utils.JsonDataReader;
import org.testng.annotations.DataProvider;

import java.util.List;

public class TestDataProviders {

    @DataProvider(name = "validBookData")
    public static Object[][] getValidBookData() {
        List<Book> books = JsonDataReader.readValidBooks();
        Object[][] data = new Object[books.size()][];
        
        for (int i = 0; i < books.size(); i++) {
            data[i] = new Object[]{books.get(i)};
        }
        
        return data;
    }

    @DataProvider(name = "invalidBookData")
    public static Object[][] getInvalidBookData() {
        List<JsonDataReader.InvalidTestData<Book>> invalidBooks = JsonDataReader.readInvalidBooks();
        Object[][] data = new Object[invalidBooks.size()][];
        
        for (int i = 0; i < invalidBooks.size(); i++) {
            JsonDataReader.InvalidTestData<Book> invalidData = invalidBooks.get(i);
            data[i] = new Object[]{invalidData.getTestData(), invalidData.getExpectedError()};
        }
        
        return data;
    }

    @DataProvider(name = "validAuthorData")
    public static Object[][] getValidAuthorData() {
        List<Author> authors = JsonDataReader.readValidAuthors();
        Object[][] data = new Object[authors.size()][];
        
        for (int i = 0; i < authors.size(); i++) {
            data[i] = new Object[]{authors.get(i)};
        }
        
        return data;
    }

    @DataProvider(name = "invalidAuthorData")
    public static Object[][] getInvalidAuthorData() {
        List<JsonDataReader.InvalidTestData<Author>> invalidAuthors = JsonDataReader.readInvalidAuthors();
        Object[][] data = new Object[invalidAuthors.size()][];
        
        for (int i = 0; i < invalidAuthors.size(); i++) {
            JsonDataReader.InvalidTestData<Author> invalidData = invalidAuthors.get(i);
            data[i] = new Object[]{invalidData.getTestData(), invalidData.getExpectedError()};
        }
        
        return data;
    }

    @DataProvider(name = "bookSearchData")
    public static Object[][] getBookSearchData() {
        JsonDataReader.ValidationData validationData = JsonDataReader.readValidationData();
        List<JsonDataReader.ValidationData.SearchTerm> searchTerms = validationData.getBookSearchTerms();
        Object[][] data = new Object[searchTerms.size()][];
        
        for (int i = 0; i < searchTerms.size(); i++) {
            JsonDataReader.ValidationData.SearchTerm searchTerm = searchTerms.get(i);
            data[i] = new Object[]{searchTerm.getSearchTerm(), searchTerm.getDescription()};
        }
        
        return data;
    }

    @DataProvider(name = "bookIds")
    public static Object[][] getBookIds() {
        JsonDataReader.TestIds testIds = JsonDataReader.readTestIds();
        List<Integer> bookIds = testIds.getValidBookIds();
        Object[][] data = new Object[bookIds.size()][];
        
        for (int i = 0; i < bookIds.size(); i++) {
            data[i] = new Object[]{bookIds.get(i)};
        }
        
        return data;
    }

    @DataProvider(name = "authorIds")
    public static Object[][] getAuthorIds() {
        JsonDataReader.TestIds testIds = JsonDataReader.readTestIds();
        List<Integer> authorIds = testIds.getValidAuthorIds();
        Object[][] data = new Object[authorIds.size()][];
        
        for (int i = 0; i < authorIds.size(); i++) {
            data[i] = new Object[]{authorIds.get(i)};
        }
        
        return data;
    }

    @DataProvider(name = "invalidIds")
    public static Object[][] getInvalidIds() {
        JsonDataReader.TestIds testIds = JsonDataReader.readTestIds();
        List<JsonDataReader.TestIds.InvalidId> invalidIds = testIds.getInvalidIds();
        Object[][] data = new Object[invalidIds.size()][];
        
        for (int i = 0; i < invalidIds.size(); i++) {
            JsonDataReader.TestIds.InvalidId invalidId = invalidIds.get(i);
            data[i] = new Object[]{invalidId.getId(), invalidId.getDescription()};
        }
        
        return data;
    }

    @DataProvider(name = "randomBookData")
    public static Object[][] getRandomBookData() {
        Object[][] data = new Object[3][];
        
        for (int i = 0; i < 3; i++) {
            data[i] = new Object[] {
                Book.builder()
                    .id(0)
                    .title(TestDataGenerator.generateRandomBookTitle())
                    .description("Auto-generated book description for testing purposes")
                    .pageCount(TestDataGenerator.generateRandomYear() % 500 + 100)
                    .excerpt("This is an auto-generated excerpt for testing")
                    .publishDate("2024-01-01T00:00:00.000Z")
                    .build()
            };
        }
        
        return data;
    }

    @DataProvider(name = "randomAuthorData")
    public static Object[][] getRandomAuthorData() {
        Object[][] data = new Object[3][];
        
        for (int i = 0; i < 3; i++) {
            String fullName = TestDataGenerator.generateRandomName();
            String[] nameParts = fullName.split(" ");
            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : "TestLastName";
            
            data[i] = new Object[] {
                Author.builder()
                    .id(0)
                    .idBook(i + 1) // Different book IDs
                    .firstName(firstName)
                    .lastName(lastName)
                    .build()
            };
        }
        
        return data;
    }

    @DataProvider(name = "bulkTestData")
    public static Object[][] getBulkTestData() {
        List<JsonDataReader.BulkTestData> bulkData = JsonDataReader.readBulkTestData();
        Object[][] data = new Object[bulkData.size()][];
        
        for (int i = 0; i < bulkData.size(); i++) {
            JsonDataReader.BulkTestData bulk = bulkData.get(i);
            data[i] = new Object[]{
                bulk.getBookId(), 
                bulk.getBookTitle(), 
                bulk.getAuthorFirstName(), 
                bulk.getAuthorLastName()
            };
        }
        
        return data;
    }

    @DataProvider(name = "pageCountValidation")
    public static Object[][] getPageCountValidationData() {
        JsonDataReader.ValidationData validationData = JsonDataReader.readValidationData();
        List<JsonDataReader.ValidationData.PageCountValidation> pageCountValidations = 
            validationData.getPageCountValidation();
        Object[][] data = new Object[pageCountValidations.size()][];
        
        for (int i = 0; i < pageCountValidations.size(); i++) {
            JsonDataReader.ValidationData.PageCountValidation validation = pageCountValidations.get(i);
            data[i] = new Object[]{
                validation.getPageCount(), 
                validation.isShouldBeValid(), 
                validation.getDescription()
            };
        }
        
        return data;
    }
}