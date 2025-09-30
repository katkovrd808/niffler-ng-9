package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.spend.Bubble;
import guru.qa.niffler.page.base.BasePage;
import guru.qa.niffler.page.element.HeaderElement;
import guru.qa.niffler.page.element.SpendingTable;
import guru.qa.niffler.page.element.StatElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {
  private final SpendingTable spendingTable = new SpendingTable();
  private final HeaderElement headerElement = new HeaderElement();
  private final StatElement statElement = new StatElement();

  private final SelenideElement statistics = $("#stat h2");
  private final SelenideElement legend = $("#legend-container");

  public final static String URL = CFG.frontUrl() + "main";

  @Nonnull
  @Step("Asserting that main page is loaded")
  public MainPage checkThatPageLoaded() {
    statistics.should(visible);
    return this;
  }

  @Nonnull
  @Step("Deleting spend with description {description}")
  public MainPage deleteSpending(String description) {
    spendingTable.deleteSpending(description);
    return this;
  }

  @Nonnull
  @Step("Asserting that ")
  public MainPage assertArchivedSpendingHasSum(Double sum) {
    legend.$("ul").$$("li").get(0)
      .shouldHave(text(String.format("Archived %d ₽", sum.intValue())));
    return this;
  }

  @Nonnull
  @Step("Asserting that spending page is loaded")
  public SpendingTable spendingTableShouldBeLoaded() {
    spendingTable.shouldBeLoaded();
    return new SpendingTable();
  }

  @Nonnull
  @Step("Editing spend with description {description}")
  public EditSpendingPage editSpending(String description) {
    spendingTable.editSpending(description);
    return new EditSpendingPage();
  }

  @Nonnull
  @Step("Opening Profile page from header")
  public ProfilePage openProfilePageFromHeader() {
    headerElement.openProfilePageFromHeader();
    return new ProfilePage();
  }

  @Nonnull
  @Step("Opening Friends page from header")
  public FriendsPage openFriendsPageFromHeader() {
    headerElement.openFriendsPageFromHeader();
    return new FriendsPage();
  }

  @Nonnull
  @Step("Opening All People page from header")
  public AllPeoplePage openAllPeoplePageFromHeader() {
    headerElement.openAllPeoplePageFromHeader();
    return new AllPeoplePage();
  }

  @Nonnull
  @Step("Opening Create Spend page from header")
  public CreateSpendingPage openCreateSpendingPageFromHeader() {
    headerElement.openCreateSpendPage();
    return new CreateSpendingPage();
  }

  @Nonnull
  @Step("Asserting statistic bubbles content and colors")
  public MainPage checkStatBubbles(Bubble... bubbles) {
    statElement.checkBubbles(bubbles);
    return this;
  }

  @Nonnull
  @Step("Asserting statistic bubbles content and colors in any order")
  public MainPage checkStatBubblesAnyOrder(Bubble... bubbles) {
    statElement.checkStatisticBubblesInAnyOrder(bubbles);
    return this;
  }

  @Nonnull
  @Step("Asserting statistic bubbles contains {bubbles}")
  public MainPage checkStatBubblesContains(Bubble... bubbles) {
    statElement.checkStatisticBubblesContains(bubbles);
    return this;
  }
}
