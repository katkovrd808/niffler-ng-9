package guru.qa.niffler.page.element;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.*;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class HeaderElement {
  private final SelenideElement
    openProfileBtn = $("header button"),
    logo = $("header h1"),
    newSpendingBtn = $$("header a").find(text("New spending")),
    profileMenu = $("[role='menu']");

  @Step("Going to Profile page from header")
  public ProfilePage openProfilePageFromHeader() {
    openProfileBtn.click();
    profileMenu.$$("li").find(text("Profile")).click();
    return new ProfilePage();
  }

  @Step("Going to Create spend page from header")
  public CreateSpendingPage openCreateSpendPage() {
    newSpendingBtn.click();
    return new CreateSpendingPage();
  }

  @Step("Going to All People page from header")
  public AllPeoplePage openAllPeoplePageFromHeader() {
    openProfileBtn.click();
    profileMenu.$$("li").find(text("All People")).click();
    return new AllPeoplePage();
  }

  @Step("Going to Friends page from header")
  public FriendsPage openFriendsPageFromHeader() {
    openProfileBtn.click();
    profileMenu.$$("li").find(text("Friends")).click();
    return new FriendsPage();
  }

  @Step("Signing out from profile")
  public LoginPage signOut() {
    openProfileBtn.click();
    profileMenu.$$("li").find(text("Sign out")).click();
    return new LoginPage();
  }

  @Step("Going to Main Page from header")
  public MainPage toMainPage() {
    logo.click();
    return new MainPage();
  }
}
