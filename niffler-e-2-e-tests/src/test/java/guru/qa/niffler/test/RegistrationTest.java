package guru.qa.niffler.test;

import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

public class RegistrationTest {
  private static final String FRONT_URL = Config.getInstance().frontUrl();
  private final Faker faker = new Faker();

  @Test
  @DisplayName("User should be registered with valid username and password")
  void userWithUniqueUsernameAndValidPasswordShouldBeRegistered() {
    final String password = "12345";

    open(FRONT_URL, LoginPage.class)
      .openRegistrationForm()
      .fillRegistrationForm(randomUsername(), password, password)
      .submitRegistration()
      .checkSucceedRegistrationPageTitle();
  }

  @Test
  @DisplayName("User should be registered with valid uppercase username and password")
  void userWithUpperCaseUsernameShouldBeRegistered() {
    final String password = "12345";

    open(FRONT_URL, LoginPage.class)
      .openRegistrationForm()
      .fillRegistrationForm(randomUsername().toUpperCase(), password, password)
      .submitRegistration()
      .checkSucceedRegistrationPageTitle();
  }

  @Test
  @DisplayName("User should be registered with valid username which contains digits and password")
  void userUsernameContainsDigitsShouldBeRegistered() {
    final String password = "12345";

    open(FRONT_URL, LoginPage.class)
      .openRegistrationForm()
      .fillRegistrationForm(randomUsername() + "1", password, password)
      .submitRegistration()
      .checkSucceedRegistrationPageTitle();
  }

  @Test
  @DisplayName("User can open login page and login to account after registration")
  void userCanMakeLoginAfterRegistration() {
    final String password = "12345";

    open(FRONT_URL, LoginPage.class)
      .openRegistrationForm()
      .fillRegistrationForm(randomUsername(), password, password)
      .submitRegistration()
      .checkSucceedRegistrationPageTitle()
      .loginAfterRegistration()
      .fillLoginPage(randomUsername(), password)
      .submit()
      .checkThatPageLoaded();
  }

  @Test
  @DisplayName("User with taken username should be not registered")
  @User()
  void userShouldBeNotRegisteredIfUsernameAlreadyTaken(UdUserJson user) {
    open(FRONT_URL, LoginPage.class)
      .openRegistrationForm()
      .fillRegistrationForm(user.username(), "12345", "12345")
      .completeRegistrationAndCheckFormError(String.format("Username `%s` already exists"
        , user.username()));
  }

  @Test
  @DisplayName("User with short username should be not registered")
  void userShouldBeNotRegisteredIfUsernameIsShort() {
    final String password = "12345";

    open(FRONT_URL, LoginPage.class)
      .openRegistrationForm()
      .fillRegistrationForm("us", password, password)
      .submitRegistrationAndCheckUsernameError("Allowed username length should be from 3 to 50 characters");
  }

  @Test
  @DisplayName("User with username more than 50 characters should be not registered")
  void userShouldBeNotRegisteredIfUsernameIsLong() {
    final String username = faker.lorem().sentence(20)
      .replaceAll("\\s", "");
    final String password = "12345";

    open(FRONT_URL, LoginPage.class)
      .openRegistrationForm()
      .fillRegistrationForm(username, password, password)
      .submitRegistrationAndCheckUsernameError("Allowed username length should be from 3 to 50 characters");
  }

  @Test
  @DisplayName("User with blank username should be not registered")
  void userShouldBeNotRegisteredIfUsernameIsBlank() {
    final String password = "12345";

    open(FRONT_URL, LoginPage.class)
      .openRegistrationForm()
      .fillRegistrationForm("     ", password, password)
      .submitRegistrationAndCheckUsernameError("Username can not be blank");
  }

  @Test
  @DisplayName("User with username contains whitespaces should be not registered")
  void userShouldBeNotRegisteredIfUsernameContainsWhitespaces() {
    final String username = "test username";
    final String password = "12345";

    open(FRONT_URL, LoginPage.class)
      .openRegistrationForm()
      .fillRegistrationForm(username, password, password)
      .submitRegistrationAndCheckUsernameError("Username must not contain whitespaces");
  }

  @Test
  @DisplayName("Error should be present if passwords don't match")
  void errorShouldBeShownWhenPasswordsDontMatch() {
    final String password = "12345";

    open(FRONT_URL, LoginPage.class)
      .openRegistrationForm()
      .fillRegistrationForm(randomUsername(), password, password + "1")
      .submitRegistrationAndCheckPasswordError("Passwords should be equal");
  }

  @Test
  @DisplayName("Error should be present if password shorter than 3 characters")
  void errorShouldBeShownWhenPasswordTooShort() {
    final String password = "12";

    open(FRONT_URL, LoginPage.class)
      .openRegistrationForm()
      .fillRegistrationForm(randomUsername(), password, password)
      .submitRegistrationAndCheckPasswordError("Allowed password length should be from 3 to 12 characters");
  }

  @Test
  @DisplayName("Error should be present if password longer than 12 characters")
  void errorShouldBeShownWhenPasswordTooLong() {
    final String password = "1234567890123";

    open(FRONT_URL, LoginPage.class)
      .openRegistrationForm()
      .fillRegistrationForm(randomUsername(), password, password)
      .submitRegistrationAndCheckPasswordError("Allowed password length should be from 3 to 12 characters");
  }
}