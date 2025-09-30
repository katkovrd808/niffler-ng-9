package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.*;
import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.utils.RandomDataUtils.randomName;

@Tags({@Tag("WEB")})
@ParametersAreNonnullByDefault
public class ProfileTest {

  @Test
  @User(
    categories = @Category(
      archived = true
    )
  )
  @ApiLogin
  @DisabledByIssue("3")
  @DisplayName("Archived category should be present in categories list on profile page")
  void archivedCategoryShouldPresentInCategoriesList(UdUserJson user) {
    open(ProfilePage.URL, ProfilePage.class)
      .showArchivedCategories()
      .checkArchivedCategory(user.testData().categories().getFirst().name());
  }

  @Test
  @User(
    categories = @Category()
  )
  @ApiLogin
  @DisplayName("Active category should be present in categories list on profile page")
  void activeCategoryShouldPresentInCategoriesList(UdUserJson user) {
    open(ProfilePage.URL, ProfilePage.class)
      .checkCategory(user.testData().categories().getFirst().name());
  }

  @Test
  @User
  @ApiLogin
  @DisplayName("User should be able to upload profile image")
  void userShouldBeAbleToUploadProfileImage() {
    open(ProfilePage.URL, ProfilePage.class)
      .uploadProfileImage("img/cat.jpeg");
  }

  @Test
  @User
  @ApiLogin
  @DisplayName("User should be able to change username")
  void userShouldBeNotAbleToChangeUsername(UdUserJson user) {
    open(ProfilePage.URL, ProfilePage.class)
      .checkUsername(user.username());
  }

  @Test
  @User
  @ApiLogin
  @DisplayName("User should be able to change name")
  void userShouldBeAbleToChangeName() {
    final String fullName = randomName();

    open(ProfilePage.URL, ProfilePage.class)
      .setFullName(fullName)
      .checkFullName(fullName);
  }

  @ScreenShotTest(
    value = "img/expected/expected-profile.png",
    rewriteExpected = true
  )
  @User
  @ApiLogin
  @DisplayName("Profile image should be changed after setting")
  void checkUserProfileImage(BufferedImage expected) throws IOException {
    open(ProfilePage.URL, ProfilePage.class)
      .uploadProfileImage("img/cat.png")
      .assertDiff(expected, $("form img"));
  }
}