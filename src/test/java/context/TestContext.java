package context;


import io.restassured.response.Response;
import lombok.Getter;
import lombok.Setter;
import models.Book;

@Getter
@Setter
public class TestContext {

    private Response response;
    private Book book;
    private int randomBookId;
}
