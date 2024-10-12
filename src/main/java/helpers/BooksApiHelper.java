package helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import models.Book;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BooksApiHelper {

    private static final Logger log = LogManager.getLogger(BooksApiHelper.class);

    private final PropertiesLoader configLoader;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String baseUrl;

    public BooksApiHelper() {
        configLoader = new PropertiesLoader();
        baseUrl = configLoader.getBaseUrl();
    }

    private RequestSpecification authenticatedRequest() {
        String username = configLoader.getUsername();
        String password = configLoader.getPassword();
        return RestAssured.given()
                .auth().basic(username, password)
                .contentType("application/json");
    }

    public List<Book> getAllBooks() {
        log.info("Retrieving all books");
        return getAllBooksResponse().jsonPath().getList("$", Book.class);
    }

    public Response getAllBooksResponse() {
        log.info("Executing GET request for all books: {}",  baseUrl);
        return authenticatedRequest().get(baseUrl);
    }

    public Response getBookById(int bookId) {
        log.info("Executing GET request for book with ID: {}", baseUrl + bookId);
        return authenticatedRequest().get(baseUrl + bookId);
    }

    public Response createBook(Book newBook) {
        log.info("Creating a new book: {}", newBook.getName());
        return authenticatedRequest()
                .body(newBook)
                .post(baseUrl);
    }

    public Response updateBookById(int bookId, Book updatedBook) {
        log.info("Updating book with ID: {} with new data", bookId);
        return authenticatedRequest()
                .body(updatedBook)
                .put(baseUrl + bookId);
    }

    public Response deleteBook(int bookId) {
        log.info("Deleting book with ID: {}", bookId);
        return authenticatedRequest().delete(baseUrl + bookId);
    }

    public Book readBookFromJsonFile(String fileName) throws IOException {
        log.info("Reading book data from JSON file: {}", fileName);
        return objectMapper.readValue(new File("src/main/resources/testdata/" + fileName), Book.class);
    }

    public boolean validateJsonSchema(JsonNode responseJson, JsonNode schemaJson) throws ProcessingException {
        log.info("Validating response against the schema");
        JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        JsonSchema jsonSchema = factory.getJsonSchema(schemaJson);
        return jsonSchema.validate(responseJson).isSuccess();
    }
}
