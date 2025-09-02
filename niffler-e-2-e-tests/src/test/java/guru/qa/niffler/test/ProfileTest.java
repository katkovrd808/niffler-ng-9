package guru.qa.niffler.test;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
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
import static guru.qa.niffler.utils.RandomDataUtils.randomName;

@Tags({@Tag("WEB")})
@ParametersAreNonnullByDefault
public class ProfileTest {
  private static final String FRONT_URL = Config.getInstance().frontUrl();

  @User(
    categories = @Category(
      archived = true
    )
  )
  @Test
  @DisabledByIssue("3")
  @DisplayName("Archived category should be present in categories list on profile page")
  void archivedCategoryShouldPresentInCategoriesList(UdUserJson user) {
    open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .checkThatPageLoaded()
      .openProfilePageFromHeader()
      .showArchivedCategories()
      .checkArchivedCategory(user.testData().categories().getFirst().name());
  }

  @User(
    categories = @Category()
  )
  @Test
  @DisplayName("Active category should be present in categories list on profile page")
  void activeCategoryShouldPresentInCategoriesList(UdUserJson user) {
    open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .checkThatPageLoaded()
      .openProfilePageFromHeader()
      .checkCategory(user.testData().categories().getFirst().name());
  }

  @User()
  @Test
  void userShouldBeAbleToUploadProfileImage(UdUserJson user) {
    open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .checkThatPageLoaded()
      .openProfilePageFromHeader()
      .uploadProfileImage("img/cat.jpeg");
  }

  @User()
  @Test
  void userShouldBeNotAbleToChangeUsername(UdUserJson user) {
    open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .checkThatPageLoaded()
      .openProfilePageFromHeader()
      .checkUsername(user.username());
  }

  @User()
  @Test
  void userShouldBeAbleToChangeName(UdUserJson user) {
    final String fullName = randomName();

    open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .checkThatPageLoaded()
      .openProfilePageFromHeader()
      .setFullName(fullName)
      .checkFullName(fullName);
  }
}