package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
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

  @Nonnull
  @Step("Logging in user profile")
  public LoginPage fillLoginPage(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    return this;
  }

  @Nonnull
  @Step("Submitting log in and asserting the error")
  public LoginPage submitAndCheckErrorText(String error){
    submitButton.click();
    formError.shouldHave(text(error));
    return this;
  }

  @Nonnull
  @Step("Display password")
  public LoginPage showPassword(){
    loginForm.$$("label").find(text("Password"))
            .$("button").click();
    return this;
  }

  @Nonnull
  @Step("Asserting password input data type")
  public LoginPage checkPasswordInputType(){
    loginForm.$$("label").find(text("Password"))
            .$("input").shouldHave(type("text"));
    return this;
  }

  @Nonnull
  @Step("Opening registration form")
  public RegistrationPage openRegistrationForm(){
    registrationBtn.click();
    return new RegistrationPage();
  }

  @Nonnull
  @Step("Logging in")
  public MainPage submit() {
    submitButton.click();
    return new MainPage();
  }
}
