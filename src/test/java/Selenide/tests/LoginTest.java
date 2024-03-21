package Selenide.tests;

import Selenide_books_pages.LoginPage;
import Selenide_books_pages.MainPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;

public class LoginTest extends BaseTest{

    MainPage mainpage = page(MainPage.class);
    LoginPage loginPage = page(LoginPage.class);

    @Test
    @DisplayName("Log in with correct credentials")
    public void shouldLoginWithCorrectCredentials() throws InterruptedException {
        mainpage.goToLogin.click();
        loginPage.name.setValue(configUserName);
        loginPage.password.setValue(configPassword);
        loginPage.submitButton.scrollIntoView(true).click();
    }


    // Register test cannot be performed due to blocked Captcha button
//    @Test
//    @DisplayName("Register new user")
//    public void shouldRegisterUserWithCorrectCredentials() {
//        mainpage.goToLogin.click();
//        loginPage.newUserButton.click();
//        RegisterPage registerPage = page(RegisterPage.class);
//        registerPage.firstName.setValue("Katarzyna");
//        registerPage.lastname.setValue("Pierzyna");
//        registerPage.userName.setValue(configUserName);
//        registerPage.password.setValue(configPassword);
//
//        switchTo().frame(registerPage.iframe);
//        registerPage.captchaButton.click();  // this is not clickable
//        switchTo().defaultContent();
//
//        registerPage.submitButton.click();
//    }
}
