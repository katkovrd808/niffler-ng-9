package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.base.BasePage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@ParametersAreNonnullByDefault
public class ProfilePage extends BasePage<ProfilePage> {

  private final SelenideElement
    profileInfoForm = $x("//*/main/div/form"),
    saveBtn = $("[type='submit']"),
    categoriesForm = $x("//*/main/div/div"),
    archiveCategoryModal = $("div[role='dialog']");

  public final static String URL = CFG.frontUrl() + "profile";

  @Nonnull
  @Step("Asserting username {username}")
  public ProfilePage checkUsername(String username) {
    profileInfoForm.$("#username").shouldHave(attribute("value", username))
      .should(disabled);
    return this;
  }

  @Nonnull
  @Step("Setting user fullname {name}")
  public ProfilePage setFullName(String name) {
    profileInfoForm.$("#name").val(name);
    saveBtn.click();
    checkAlert("Profile successfully updated");
    return this;
  }

  @Nonnull
  @Step("Asserting user fullname {name}")
  public ProfilePage checkFullName(String name) {
    profileInfoForm.$("#name").shouldHave(attribute("value", name));
    return this;
  }

  @Nonnull
  @Step("Setting user profile image from path {path}")
  public ProfilePage uploadProfileImage(String path) {
    profileInfoForm.$("#image__input").uploadFromClasspath(path);
    saveBtn.click();
    return this;
  }

  @Nonnull
  @Step("Creating new category with name {categoryName}")
  public ProfilePage createNewCategory(String categoryName) {
    categoriesForm.$("#category").val(categoryName)
      .pressEnter();
    return this;
  }

  @Nonnull
  @Step("Displaying archived categories")
  public ProfilePage showArchivedCategories() {
    categoriesForm.$("div")
      .$("label").click();
    return this;
  }

  @Nonnull
  @Step("Editing category name from {categoryName} to {newCategoryName}")
  public ProfilePage editCategoryName(String categoryName, String newCategoryName) {
    categoriesForm.$$("div").find(text(categoryName))
      .$("[aria-label='Edit category']").click();
    categoriesForm.$$("div").find(text(categoryName))
      .val(newCategoryName).pressEnter();
    return this;
  }

  @Nonnull
  @Step("Archiving category with name {categoryName} in user profile")
  public ProfilePage archiveCategory(String categoryName) {
    categoriesForm.$$("div").find(text(categoryName))
      .$("[aria-label='Archive category']").click();
    archiveCategoryModal.$x(".//button[text()='Archive'").click();
    return this;
  }

  @Nonnull
  @Step("Archiving category with name {categoryName} in user profile and return to MainPage")
  public MainPage archiveCategoryAndReturn(String categoryName) {
    categoriesForm.$$("div").find(text(categoryName))
      .$("[aria-label='Archive category']").click();
    archiveCategoryModal.$x(".//button[text()='Archive']").click();
    header.toMainPage();
    return new MainPage();
  }

  @Nonnull
  @Step("Asserting that category with name {categoryName} is visible")
  public ProfilePage checkCategory(String categoryName) {
    categoriesForm.$$("div").find(text(categoryName))
      .should(visible);
    return this;
  }

  @Nonnull
  @Step("Asserting that category with name {categoryName} is archived")
  public ProfilePage checkArchivedCategory(String categoryName) {
    categoriesForm.$$("div").find(text(categoryName))
      .should(visible);
    return this;
  }
}