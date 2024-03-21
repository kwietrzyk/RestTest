package Selenide_books_pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    public SelenideElement name = $("#userName");
    public SelenideElement password = $("#password");
    public SelenideElement submitButton = $("#login");
    public SelenideElement newUserButton = $("#newUser");  //.shouldBe(visible); - to ma jakis problem
}
