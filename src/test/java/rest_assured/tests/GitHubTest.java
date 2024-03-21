package rest_assured.tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/*
This is class for github operations on owner repos
TOKEN is valid for 30 days from generation date (profil > settings > developer settings)
It is very probable that you have to update your token to run this class
*/
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GitHubTest {

    public static final String TEXT_TO_ACCEPT = "application/vnd.github.v3+json";
    private final static String OWNER = "kwietrzyk";
    private final static String TOKEN = "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";
    private final static String EXISTING_REPO_NAME = "Training_cpp";
    private final static String REPO_NAME = "Nowe_repo";
    private final static String NEW_NAME = "UpdatedRepoName";
    private static RequestSpecification reqSpec;
    private static ResponseSpecification respSpec;

    @BeforeAll
    public static void setup() {

        RestAssured.baseURI = "https://api.github.com/";
        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "token " + TOKEN)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
        respSpec = new ResponseSpecBuilder()
                .expectHeader("Server", "GitHub.com")
                .log(LogDetail.ALL)
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("Getting all user repos")
    public void testGetRepos() {
        Response response = given(reqSpec).pathParam("userName", OWNER).when().get("/users/{userName}/repos");
        response.then().spec(respSpec).statusCode(200);
        response.then().body("name", hasItem(equalTo(EXISTING_REPO_NAME)));  // dowolny name (przynajmniej jeden) ma miec zadaną nazwe

        String existingRepoName = response.then().extract().body().jsonPath().getList("name").get(0).toString();
        System.out.println("Existing repo name: " + existingRepoName);
    }

    @Test
    @Order(2)
    @DisplayName("Creating new repo")
    public void testNewRepo() {
        JSONObject attributes =new JSONObject();
        attributes.put("name", REPO_NAME);
        attributes.put("description", "Very good project");
        attributes.put("homepage", "https://github.com/");
        attributes.put("private", false);

        given(reqSpec)
                //.header("Authorization", "token " + TOKEN)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .body(attributes.toString())
                .post("user/repos")
                .then().statusCode(201).spec(respSpec);
    }

    @Test
    @Order(3)
    @DisplayName("Get new repo")
    public void testGetRepo() {
        Response response = given(reqSpec).pathParam("userName", OWNER).when().get("/repos/{userName}/" + REPO_NAME);
        response.then().spec(respSpec).statusCode(200);
    }

    @Test
    @Order(4)
    @DisplayName("Change repo name")
    public void testPatchRepoName() {
        //REQUEST PAYLOAD
        JSONObject requestParams = new JSONObject();
        requestParams.put("name", NEW_NAME);

        given()
                .header("Authorization", "token " + TOKEN)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .body(requestParams.toString())
                .log().all()
                .patch("/repos/" + OWNER + "/" + REPO_NAME)
                .then()
                .assertThat()
                .statusCode(200)
                .body("name", equalTo(NEW_NAME))
                .body("full_name", containsString(NEW_NAME))
                .body("html_url", containsString(NEW_NAME))
                .header("Content-Encoding", notNullValue())
                .statusLine(containsString("OK"));
    }


    @Test
    @Order(5)
    @DisplayName("Update topics")
    public void testPostTopics() {
        List<String> topics = List.of("cpp", "training", "exercises");
        JSONObject top = new JSONObject();
        top.put("names", topics);

        given(reqSpec)
                .body(top.toString())
                .accept(TEXT_TO_ACCEPT)
                .pathParam("userName", OWNER)
                .when()
                .put("/repos/{userName}/" + NEW_NAME + "/topics")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(6)
    @DisplayName("Deleting new repo")
    public void testDeleteRepo() {
        String repoToDeleteName = NEW_NAME;
        given(reqSpec)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .delete("repos/kwietrzyk/" + repoToDeleteName)
                .then().spec(respSpec).statusCode(either(is(200)).or(is(204)));  // we receive 204 in this case
    }
}
