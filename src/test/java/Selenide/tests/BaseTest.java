package Selenide.tests;

import com.codeborne.selenide.Selenide;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.io.FileInputStream;

import static com.codeborne.selenide.Selenide.*;

public class BaseTest {
    protected static final String configFilePath = "src/test/resources/configuration.properties";
    protected static Properties properties = new Properties();
    protected static String baseUrl;
    protected static String configUserName;
    protected static String configPassword;

    @BeforeAll
    public static void baseSetup() throws IOException {
        properties.load(new FileInputStream(configFilePath));
        baseUrl = properties.getProperty("app.url");
        configUserName = properties.getProperty("userName");
        configPassword = properties.getProperty("password");

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        RestAssured.responseSpecification = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    @BeforeEach
    public void setup() {
        open(baseUrl);
    }

    @AfterEach
    public void clearBrowser() {
        clearBrowserCookies();
        clearBrowserLocalStorage();
    }
}
