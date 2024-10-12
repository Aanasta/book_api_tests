package stepDefinitions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import context.TestContext;
import helpers.BooksApiHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import models.Book;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.io.File;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class BooksStepDef {

    private static final Logger log = LogManager.getLogger(BooksStepDef.class);

    private final TestContext testContext;
    private final BooksApiHelper booksApiHelper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Given("I execute GET request to get all books")
    public void iExecuteGetRequestToGetAllBooks() {
        testContext.setResponse(booksApiHelper.getAllBooksResponse());;
    }

    @Given("I execute GET request to get the first book from the books response")
    public void iExecuteGetRequestToGetTheFirstBookFromBooksResponse() {
        int bookId = booksApiHelper.getAllBooks().get(0).getId();
        log.info("Executing GET request to retrieve book with ID: {}", bookId);
        testContext.setResponse(booksApiHelper.getBookById(bookId));
    }

    @SneakyThrows
    @Given("I execute POST request to create a new book from the file {string}")
    public void iExecutePOSTRequestToCreateANewBookFromAFile(String file) {
        Book newBook = booksApiHelper.readBookFromJsonFile(file);
        testContext.setResponse(booksApiHelper.createBook(newBook));
        testContext.setBook(newBook);
    }

    @SneakyThrows
    @And("I execute PUT request to update the book by ID with data from the file {string}")
    public void iExecutePUTRequestToUpdateTheBookByIDWithDataFromTheFile(String file) {
        int bookId = testContext.getRandomBookId();
        log.info("Executing PUT request to update book with ID: {}", bookId);
        Book updatedBook = booksApiHelper.readBookFromJsonFile(file);
        testContext.setResponse(booksApiHelper.updateBookById(bookId, updatedBook));
        testContext.setBook(updatedBook);
    }

    @And("I execute DELETE request to remove the book by ID")
    public void iExecuteDELETERequestToRemoveTheBookByID() {
        int bookId = testContext.getRandomBookId();
        log.info("Executing DELETE request to delete book with ID: {}", bookId);
        testContext.setResponse(booksApiHelper.deleteBook(bookId));
    }

    @And("I execute GET request to get the book by ID")
    public void iExecuteGETRequestToGetTheBookByID() {
        testContext.setResponse(booksApiHelper.getBookById(testContext.getRandomBookId()));
    }

    @And("I remember the ID of a random book")
    public void iRememberTheIDOfARandomBook() {
        List<Book> books = booksApiHelper.getAllBooks();
        int bookId = books.get(new Random().nextInt(books.size())).getId();
        log.info("Remembering ID for the random book: {}", bookId);
        testContext.setRandomBookId(bookId);
    }

    @SneakyThrows
    @And("I assert the book details match the file {string}")
    public void iAssertTheBookDetailsMatchTheFile(String file) {
        Book actualBook = testContext.getResponse().as(Book.class);
        Book expectedBook = booksApiHelper.readBookFromJsonFile(file);
        String differences = expectedBook.compare(actualBook);
        Assert.assertTrue(differences.isEmpty(), "Actual book details are not as expected: " + differences);
    }

    @Then("I assert the response code is {int}")
    public void iAssertTheResponseCodeIs(int expectedCode) {
        Assert.assertEquals(testContext.getResponse().getStatusCode(), expectedCode, "Status Code is not as expected");
    }

    @And("I assert the response contains a list of books")
    public void iAssertTheResponseContainsAListOfBooks() {
        Assert.assertFalse(booksApiHelper.getAllBooks().isEmpty(), "The list of books is empty");
    }

    @And("I assert the books count is more than {int}")
    public void iAssertTheBooksCountIsMoreThan(int minCount) {
        Assert.assertTrue(booksApiHelper.getAllBooks().size() > minCount, "The number of books is not greater than " + minCount);
    }

    @SneakyThrows
    @And("I assert the response matches {string} schema")
    public void iAssertTheResponseMatchesSchema(String schema) {
        JsonNode responseJson = objectMapper.readTree(testContext.getResponse().getBody().asString());
        JsonNode schemaJson = objectMapper.readTree(new File("src/test/resources/schemas/" + schema));
        Assert.assertTrue(booksApiHelper.validateJsonSchema(responseJson, schemaJson),
                "Response does not match the expected schema");
    }
}
