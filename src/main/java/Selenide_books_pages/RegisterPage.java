package Selenide_books_pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    public SelenideElement firstName = $("#firstname");
    public SelenideElement lastname = $("#lastname");
    public SelenideElement userName = $("#userName");
    public SelenideElement password = $("#password");
    public SelenideElement submitButton = $("#register");
    public SelenideElement backToLoginButton = $("#gotologin");
    public SelenideElement iframe = $("iframe[title='reCAPTCHA']");

    public SelenideElement captchaButton = $(".recaptcha-checkbox-borderAnimation");
}
