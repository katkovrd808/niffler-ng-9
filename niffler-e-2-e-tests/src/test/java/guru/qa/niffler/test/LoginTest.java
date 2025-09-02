package guru.qa.niffler.test;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.utils.RandomDataUtils.randomPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@Tags({@Tag("WEB")})
@ParametersAreNonnullByDefault
public class LoginTest {
  private static final String FRONT_URL = Config.getInstance().frontUrl();

  private static final String CREDENTIALS_ERROR_TEXT = "Неверные учетные данные пользователя";

  @User()
  @Test
  @DisplayName("Main page should be present after successful login")
  void mainPageShouldBeDisplayedAfterSuccessLogin(UdUserJson user) {
    open(FRONT_URL, LoginPage.class)
        .fillLoginPage(user.username(), user.testData().password())
        .submit()
        .checkThatPageLoaded();
  }

  @Test
  @DisplayName("Unregistered user should get error when trying to login")
  void errorShouldBeShownIfUserNotRegistered(){
    open(FRONT_URL, LoginPage.class)
            .fillLoginPage(randomUsername(), "12345")
            .submitAndCheckErrorText(CREDENTIALS_ERROR_TEXT);
  }

  @Test
  @DisplayName("Error should be present if user trying to login with incorrect password")
  void errorShouldBeShownWithIncorrectRegisteredUserPassword(){
    open(FRONT_URL, LoginPage.class)
            .fillLoginPage("test1", randomPassword())
            .submitAndCheckErrorText(CREDENTIALS_ERROR_TEXT);
  }

  @User()
  @Test
  @DisplayName("Password should be visible after changing visibility")
  void passwordShouldBeShownAfterVisibilityChanging(UdUserJson user){
    open(FRONT_URL, LoginPage.class)
            .fillLoginPage(user.username(), user.testData().password())
            .showPassword()
            .checkPasswordInputType();
  }
}
