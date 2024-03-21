package rest_assured.tests;

import rest_assured.helpers.ReqresHelper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.containsString;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AutomationExerciseTest{

    /* Exrecises from: https://automationexercise.com/api_list
    Unfortunately, many of proposed scenarios are deprecated, return unexpected responses
     */
    private static RequestSpecification reqSpec;
    private static ResponseSpecification respSpec;

    enum Endpoint {
        PRODUCTS_LIST ("/productsList"),
        BRANDS_LIST ("/brandsList"),
        SEARCH_PRODUCT ("/searchProduct"),
        VERIFY_LOGIN ("/verifyLogin"),
        CREATE_ACCOUNT ("/createAccount"),
        DELETE_ACCOUNT ("/deleteAccount"),
        UPDATE_ACCOUNT ("/updateAccount"),
        GET_USER_DETAIL_BY_EMAIL ("/getUserDetailByEmail");

        private final String value;

        Endpoint(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @BeforeAll
    public static void setup() {

        RestAssured.baseURI = "https://automationexercise.com/api";
        RestAssured.registerParser("text/html", Parser.JSON);
        reqSpec = new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
        respSpec = new ResponseSpecBuilder()
                .log(LogDetail.BODY)
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("TC1: Get All Products List")
    public void testGetAllProductsListShouldSuccess() {

        Response response = given(reqSpec).when().get(Endpoint.PRODUCTS_LIST.value);
        response.then().spec(respSpec).statusCode(200);

        String jsonString = response.getBody().asString();
        int productsCount = from(jsonString).getInt("products.size()");
        System.out.println("Number of products: " + productsCount);
    }

    @Test
    @Order(2)
    @DisplayName("TC2: Post to All Products List")
    public void testPostToAllProductsListShouldFail() {

        JSONObject jsonObject = generateClothesJsonObject();
        Response response = given(reqSpec).body(jsonObject.toString()).when().post(Endpoint.PRODUCTS_LIST.value);
        response.then().spec(respSpec).statusCode(200);  // This is strange
        response.then().body(containsString("405"));
    }

    @Test
    @Order(3)
    @DisplayName("TC3: Get All Brands List")
    public void testGetAllBrandsListShouldSuccess() {

        Response response = given(reqSpec).when().get(Endpoint.BRANDS_LIST.value);
        response.then().spec(respSpec).statusCode(200);

        String jsonString = response.getBody().asString();
        int brandsCount = from(jsonString).getInt("brands.size()");
        System.out.println("Number of brands: " + brandsCount);
    }

    @Test
    @Order(4)
    @DisplayName("TC4: Put to All Brands List")
    public void testPutAllBrandsListShouldFail() {

        JSONObject jsonObject = generateClothesJsonObject();
        Response response = given(reqSpec).body(jsonObject.toString()).when().put(Endpoint.BRANDS_LIST.value);
        response.then().spec(respSpec).statusCode(200);  // This is strange
        response.then().body(containsString("405"));
    }

    // I don't understand TC5 and difference with TC6

    @Test
    @Order(6)
    @DisplayName("TC6: Post to Search Product without product parameter")
    public void testPostSearchProductShouldSuccess() {

        JSONObject jsonObject = generateClothesJsonObject();
        Response response = given(reqSpec).queryParam("search_product", "top").body(jsonObject.toString()).when().post(Endpoint.SEARCH_PRODUCT.value);
        response.then().spec(respSpec).statusCode(200);  // This is strange
        response.then().body(containsString("400"));
    }

    private JSONObject generateClothesJsonObject() {

        String type =  ReqresHelper.faker.options().option("trousers", "t-shirt", "skirt", "shirt");
        String brand = ReqresHelper.faker.options().option("Adidas", "Nike", "Puma");
        String size = ReqresHelper.faker.options().option("S", "M", "L", "XL");

        Map jsonMap = Map.of("type", type, "brand", brand, "size", size);
        return new JSONObject(jsonMap);
    }
}
