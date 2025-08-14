package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.element.AlertElement;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

@ParametersAreNonnullByDefault
public class ProfilePage {
  private final AlertElement alert = new AlertElement();

  private final SelenideElement
    profileInfoForm = $x("//*/main/div/form"),
    saveBtn = $("[type='submit']"),
    categoriesForm = $x("//*/main/div/div");

  public ProfilePage checkUsername(String username) {
    profileInfoForm.$("#username").shouldHave(attribute("value", username))
      .should(disabled);
    return this;
  }

  public ProfilePage setFullName(String name) {
    profileInfoForm.$("#name").val(name);
    saveBtn.click();
    alert.shouldHaveText("Profile successfully updated");
    return this;
  }

  public ProfilePage checkFullName(String fullName) {
    profileInfoForm.$("#name").shouldHave(attribute("value", fullName));
    return this;
  }

  public ProfilePage uploadProfileImage(String path) {
    profileInfoForm.$("#image__input").uploadFromClasspath(path);
    saveBtn.click();
    return this;
  }

  public ProfilePage createNewCategory(String categoryName) {
    categoriesForm.$("#category").val(categoryName)
      .pressEnter();
    return this;
  }

  public ProfilePage showArchivedCategories() {
    categoriesForm.$("div")
      .$("label").click();
    return this;
  }

  //TODO check locator when test will be written
  public ProfilePage editCategoryName(String categoryName, String newCategoryName) {
    categoriesForm.$$("div").find(text(categoryName))
      .$("[aria-label='Edit category']").click();
    categoriesForm.$$("div").find(text(categoryName))
      .val(newCategoryName).pressEnter();
    return this;
  }

  //TODO check locator when test will be written
  public ProfilePage archiveCategory(String categoryName) {
    categoriesForm.$$("div").find(text(categoryName))
      .$("[aria-label='Archive category']").click();
    return this;
  }

  public ProfilePage checkCategory(String categoryName) {
    categoriesForm.$$("div").find(text(categoryName))
      .should(visible);
    return this;
  }

  public ProfilePage checkArchivedCategory(String categoryName) {
    categoriesForm.$$("div").find(text(categoryName))
      .should(visible);
    return this;
  }
}
