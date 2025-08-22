package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.element.HeaderElement;
import guru.qa.niffler.page.element.SpendingTable;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class MainPage {
  private final SpendingTable spendingTable = new SpendingTable();
  private final HeaderElement headerElement = new HeaderElement();

  private final SelenideElement statistics = $("#stat h2");

  @Step("Asserting that main page is loaded")
  public MainPage checkThatPageLoaded() {
    statistics.should(visible);
    return this;
  }

  @Step("Asserting that spending page is loaded")
  public SpendingTable spendingTableShouldBeLoaded() {
    spendingTable.shouldBeLoaded();
    return new SpendingTable();
  }

  @Step("Opening edit spend page")
  public EditSpendingPage editSpending(String description) {
    spendingTable.editSpending(description);
    return new EditSpendingPage();
  }

  public ProfilePage openProfilePageFromHeader() {
    headerElement.openProfilePageFromHeader();
    return new ProfilePage();
  }

  public FriendsPage openFriendsPageFromHeader() {
    headerElement.openFriendsPageFromHeader();
    return new FriendsPage();
  }

  public AllPeoplePage openAllPeoplePageFromHeader() {
    headerElement.openAllPeoplePageFromHeader();
    return new AllPeoplePage();
  }

  public CreateSpendingPage openCreateSpendingPageFromHeader() {
    headerElement.openCreateSpendPage();
    return new CreateSpendingPage();
  }
}
