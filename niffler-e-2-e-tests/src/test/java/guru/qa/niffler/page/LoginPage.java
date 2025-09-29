package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.base.BasePage;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.type;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class LoginPage {
  private final SelenideElement
      usernameInput = $("input[name='username']"),
      passwordInput = $("input[name='password']"),
      submitButton = $("button[type='submit']"),
      loginForm = $("#login-form"),
      registrationBtn = $("#register-button"),
      formError = $("#login-form p");

  private final static Config CFG = Config.getInstance();

  public final static String URL = CFG.authUrl() + "login";

  @Step("Logging in user profile")
  public LoginPage fillLoginPage(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    return this;
  }

  @Step("Submitting log in and asserting the error")
  public LoginPage submitAndCheckErrorText(String error){
    submitButton.click();
    formError.shouldHave(text(error));
    return this;
  }

  @Step("Display password")
  public LoginPage showPassword(){
    loginForm.$$("label").find(text("Password"))
            .$("button").click();
    return this;
  }

  @Step("Asserting password input data type")
  public LoginPage checkPasswordInputType(){
    loginForm.$$("label").find(text("Password"))
            .$("input").shouldHave(type("text"));
    return this;
  }

  @Step("Opening registration form")
  public RegistrationPage openRegistrationForm(){
    registrationBtn.click();
    return new RegistrationPage();
  }

  @Step("Logging in")
  public MainPage submit() {
    submitButton.click();
    return new MainPage();
  }
}
