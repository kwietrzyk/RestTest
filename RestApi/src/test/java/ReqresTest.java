import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ReqresTest {

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test
    public void getAllResources() {
        Response resp = RestAssured.get("/users");
        resp.then().log().all().body("data", hasSize(6));

        RequestSpecification reqSpec = given().queryParam("page", 2);
        resp = reqSpec.get("/users");
        resp.then().log().all();
    }

    @Test
    public void getSingleData() {
        Response resp = RestAssured.get("/users/2");
        resp.then().log().all().body("data.last_name", equalTo("Weaver"));

        RequestSpecification reqSpec = given().queryParams("page", 2, "id", 11);
        resp = reqSpec.get("/users");
        resp.then().log().ifStatusCodeIsEqualTo(200);

        RestAssured.reset();     // Do not put it here if setup is done only beforeAll
    }

    @Test
    public void usageOfPojoInPost() {
        Address userAddress = new Address("Krakow", "30-333", "Czerwone Maki", 23, "33-122-431");
        User user = new User("Katarzyna", "Bambaryla", 31, "programmer", userAddress);

        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
        var pojo = given().contentType(ContentType.JSON).body(user).when().post("/users").then().statusCode(201).log().all();
        pojo.body("id", is(11));

        System.out.println("++++++++++++++++       Stworzony user nie zapisuje sie na stale tylko na czas operacji POST       +++++++++++++++++++");
        given().when().get("/users").then().log().body();
    }
}
