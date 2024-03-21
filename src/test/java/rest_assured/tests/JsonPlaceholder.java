package rest_assured.tests;

import rest_assured.helpers.ReqresHelper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import restobjects.Address;
import restobjects.User;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class JsonPlaceholder {

    @BeforeEach
    public void setup() {

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://jsonplaceholder.typicode.com")
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        RestAssured.responseSpecification = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    @Test
    public void usageOfPojoInPost() {
        Address userAddress = new Address(
                "Krakow",
                "30-333",
                "Czerwone Maki",
                23,
                "33-122-431");
        User user = new User(
                ReqresHelper.faker.name().firstName(),
                ReqresHelper.faker.name().lastName(),
                new Random().nextInt(60) + 1,
                ReqresHelper.faker.job().field(),
                userAddress);


        var pojo = given().contentType(ContentType.JSON).body(user).when().post("/users").then().statusCode(201).log().all();
        pojo.body("address.city", equalTo("Krakow"))
                .body("id", is(11));

        System.out.println("++++++++++++++++       Created user is not visible after POST       +++++++++++++++++++");
        given().when().get("/users").then().log().body();
    }
}
