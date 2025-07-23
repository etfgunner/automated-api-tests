# Books API Test Automation

This is a simplified test automation framework for testing REST APIs, specifically the FakeRestAPI Books service. The framework has been streamlined for reliability and ease of use with minimal configuration.

## What's in the toolbox?

These technologies are chosen because they work well together and make the framework both powerful and easy to maintain:

- **Java 11** - Solid, reliable language with great tooling
- **TestNG** - My preferred testing framework - more flexible than JUnit for complex scenarios  
- **RestAssured** - Makes API testing feel natural and readable
- **Maven** - Handles all the dependency management headaches
- **Allure** - Beautiful test reports that actually help debug issues
- **Lombok** - Cuts down on boilerplate code significantly
- **Logback** - Configurable logging that doesn't get in your way
- **DataFaker** - Generates realistic test data on the fly

## How it's organized

Project structure simplified for clarity and maintainability:

```
books-api-tests/
├── src/main/java/com/api/tests/
│   ├── client/
│   │   └── ApiClient.java                  # Simplified HTTP client for all API calls
│   ├── config/
│   │   └── ConfigManager.java              # Configuration management
│   ├── enums/
│   │   └── ApiEndpoint.java                # API endpoint definitions
│   ├── models/
│   │   ├── Book.java                       # Book data model
│   │   └── Author.java                     # Author data model  
│   ├── utils/
│   │   └── TestDataGenerator.java          # Random test data generation
│   └── base/
│       └── BaseTest.java                   # Common test setup and utilities
├── src/test/java/com/api/tests/
│   ├── BooksApiTest.java                   # Essential book API tests (11 tests)
│   └── AuthorsApiTest.java                 # Essential author API tests (13 tests)
├── src/test/resources/
│   ├── config.properties                   # Main configuration file
│   ├── testng.xml                          # TestNG test suite setup (simplified)
│   └── testdata/                           # Minimal JSON test data files
│       ├── validBooks.json                 # 2 book records
│       ├── invalidBooks.json               # 1 validation test case
│       ├── validAuthors.json               # 2 author records
│       ├── invalidAuthors.json             # 1 validation test case
│       ├── testIds.json                    # Minimal ID test data
│       ├── bulkTestData.json               # 1 bulk test record
│       └── validationData.json             # Minimal validation data
├── pom.xml                                 # Maven dependencies and plugins
└── azure-pipelines.yml                    # CI/CD pipeline configuration
```

## Getting started

### What you'll need

- Java 11+ (I tested with 11, but newer versions should work fine)
- Maven 3.6+ 
- An IDE that speaks Java (I recommend IntelliJ, but Eclipse works too)

### Setting it up

Getting this running locally is pretty straightforward:

```bash
# Clone the project
git clone <your-repository-url>
cd books-api-tests

# Install all dependencies
mvn clean install
```

That's it! Maven will download everything you need.

### Running the tests

Once everything's set up, you have several ways to run the tests:

```bash
# Run everything (default - uses testng.xml)
mvn clean test

# Run just the book tests
mvn clean test -Dtest=BooksApiTest

# Run just the author tests  
mvn clean test -Dtest=AuthorsApiTest

# Run with extra logging to debug issues
mvn clean test -Dlogging.enabled=true
```

### TestNG Configuration

The `testng.xml` file is configured for simplicity and reliability:

- **Single test suite** - Runs both Books and Authors tests sequentially
- **No parallel execution** - Prevents concurrency issues
- **Minimal configuration** - Essential tests only

```bash
# Run specific test classes
mvn clean test -Dtest=BooksApiTest
mvn clean test -Dtest=AuthorsApiTest
```

### Seeing the results

The framework generates beautiful Allure reports that make it easy to see what passed, failed, and why. Here's how to generate and view them:

#### Option 1: Generate and serve automatically (recommended)
```bash
# Run tests first
mvn clean test

# Generate report and open it in your browser automatically
mvn allure:serve
```

This is the easiest option - it generates the report and starts a local web server, then opens your default browser to view it. The report stays available until you stop the Maven process (Ctrl+C).

#### Option 2: Generate static report files
```bash
# Run tests first  
mvn clean test

# Generate the report files
mvn allure:report
```

This creates static HTML files in `target/site/allure-maven-plugin/`. To view the report:

#### What you'll see in the reports

The Allure reports are much more detailed than standard test output. They include:

- **Dashboard overview** - pass/fail rates, test duration, trends
- **Test suites breakdown** - organized by feature and story
- **Individual test details** - with request/response data, timing, and any attachments
- **Timeline view** - shows which tests ran when (useful for parallel execution)
- **Categories** - groups failures by type (product defects, test defects, etc.)
- **Environment info** - what configuration was used for the test run

If a test fails, you can click into it and see exactly what request was sent, what response came back, and where things went wrong. Much better for debugging than console logs!

## Configuration

I kept the configuration simple - just one properties file to worry about. You can override any setting via system properties if needed.

The main settings you might want to tweak:

| Setting | What it does | Default value |
|---------|--------------|---------------|
| `base.url` | Which API endpoint to test | `https://fakerestapi.azurewebsites.net/api/v1` |
| `request.timeout` | How long to wait for responses | `30000` (30 seconds) |
| `logging.enabled` | Whether to log request/response details | `true` |
| `api.key` | API key if authentication is needed | (none) |

You can override these when running tests:
```bash
mvn test -Dbase.url=https://my-custom-api.com -Drequest.timeout=10000
```

## What gets tested

Essential API coverage:

### Books API Coverage (11 Tests)
- ✅ **GET /api/v1/Books** - Get all books with response validation
- ✅ **GET /api/v1/Books/{id}** - Get specific book details  
- ✅ **GET /api/v1/Books/{id}** - 404 for non-existent books
- ✅ **POST /api/v1/Books** - Create new books
- ✅ **PUT /api/v1/Books/{id}** - Update existing books
- ✅ **DELETE /api/v1/Books/{id}** - Delete books
- ✅ **POST /api/v1/Books** - Invalid data validation (400)
- ✅ **DELETE /api/v1/Books/{id}** - 404 for non-existent books
- ✅ **Data validation** - Field validation and data types
- ✅ **PUT /api/v1/Books/{id}** - Invalid data validation (400)

### Authors API Coverage (13 Tests)
- ✅ **GET /api/v1/Authors** - Get all authors
- ✅ **GET /api/v1/Authors/{id}** - Get specific author details
- ✅ **GET /api/v1/Authors/{id}** - 404 for non-existent authors
- ✅ **POST /api/v1/Authors** - Create new authors
- ✅ **PUT /api/v1/Authors/{id}** - Update author information
- ✅ **DELETE /api/v1/Authors/{id}** - Delete authors
- ✅ **POST /api/v1/Authors** - Invalid data validation (400)
- ✅ **DELETE /api/v1/Authors/{id}** - 404 for non-existent authors
- ✅ **POST /api/v1/Authors** - Missing fields validation (400)
- ✅ **PUT /api/v1/Authors/{id}** - Invalid data validation (400)
- ✅ **Data validation** - Author field validation
- ✅ **Business logic** - Author-book associations

## Simplified Test Data Approach

Test data is stored in minimal JSON files to support data-driven testing:

### Current Test Data Structure

All JSON files contain minimal datasets to keep total test executions under 50:

**Test data files**:
- `validBooks.json` - 2 books for happy path testing
- `invalidBooks.json` - 1 invalid scenario for validation testing  
- `validAuthors.json` - 2 authors for creation tests
- `invalidAuthors.json` - 1 invalid scenario for error handling
- `testIds.json` - Minimal ID sets for boundary testing
- `bulkTestData.json` - 1 record for bulk operation testing
- `validationData.json` - Essential validation test cases

## Examples of the JSON approach

Let me show you how this works in practice. Here's a simple test that creates books using data from a JSON file:

```java
@Test(dataProvider = "validBookData", dataProviderClass = TestDataProviders.class)
public void testCreateBookWithDataProvider(Book bookData) {
    Response response = requestSpec
        .body(bookData)
        .when()
        .post("/Books")
        .then()
        .statusCode(200)
        .extract().response();
    
    Book createdBook = response.as(Book.class);
    assertEquals(createdBook.getTitle(), bookData.getTitle());
}
```

This single test method will run once for each book defined in `validBooks.json`. Want to add more test cases? Just add more books to the JSON file.

### The test data files

I've organized the test data into several JSON files:

- **`validBooks.json`** - Good book data for happy path testing
- **`invalidBooks.json`** - Broken data to test error handling  
- **`validAuthors.json`** - Author data for creation tests
- **`invalidAuthors.json`** - Bad author data for validation testing

Each invalid data file includes both the test data and a description of what should happen (like "Empty title should be rejected").

Here's what the invalid book data looks like:

```json
[
  {
    "testData": {
      "id": 0,
      "title": "",
      "description": "Valid description", 
      "pageCount": 100,
      "excerpt": "Valid excerpt",
      "publishDate": "2024-01-01T00:00:00Z"
    },
    "expectedError": "Empty title should be rejected"
  },
  {
    "testData": {
      "id": 0,
      "title": "Valid Title",
      "description": "Valid description",
      "pageCount": -10,
      "excerpt": "Valid excerpt", 
      "publishDate": "2024-01-01T00:00:00Z"
    },
    "expectedError": "Negative page count should be rejected"
  }
]
```

The test method automatically reads this structure and knows to expect a 400 status code for each scenario.

## Using the Simplified ApiClient

The `ApiClient` provides a clean, simple interface for all HTTP operations without complex threading or filters.

### Basic Usage Examples

```java
// GET all books with expected status code
Book[] books = ApiClient.get(ApiEndpoint.BOOKS, 200, Book[].class);

// GET book by ID with validation
Book book = ApiClient.get(ApiEndpoint.BOOKS_BY_ID, 1, 200, Book.class);

// POST create new book
Book newBook = Book.builder().title("New Book").build();
Book created = ApiClient.post(ApiEndpoint.BOOKS, newBook, 200, Book.class);

// PUT update book
Book updated = ApiClient.put(ApiEndpoint.BOOKS_BY_ID, 1, updatedBook, 200, Book.class);

// DELETE book (status code validation)
ApiClient.delete(ApiEndpoint.BOOKS_BY_ID, bookId, 200);
```

### Error Handling

```java
// Expect 404 for non-existent resources
ApiClient.get(ApiEndpoint.BOOKS_BY_ID, 999999, 404);

// Expect 400 for invalid data
ApiClient.post(ApiEndpoint.BOOKS, invalidBook, 400);
```

### Available Endpoints

The `ApiEndpoint` enum provides type-safe endpoint definitions:
- `BOOKS` - `/Books`
- `BOOKS_BY_ID` - `/Books/{id}`  
- `AUTHORS` - `/Authors`
- `AUTHORS_BY_ID` - `/Authors/{id}`

The `BaseTest` class handles all the setup (request specs, logging, Allure configuration), so your test classes stay focused on the actual testing.

## CI/CD Pipeline

There's an Azure DevOps pipeline configuration included (`azure-pipelines.yml`) that runs the tests and publishes the Allure reports as build artifacts. Just import it into your Azure DevOps project and it should work out of the box.

## License

MIT License - feel free to use this however you want.

## Note

Several tests are failing since the API validations weren't properly implemented on the fake API used for this demo. The goal of automation tests is not to have all passing tests, but to find issues in system. Screenshot of failed tests (from allure) is attached in /screenshots folder. Total tests 22, 6 of them are failing, the reason is missing validations (page count negative, empty title etc).