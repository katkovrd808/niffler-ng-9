package guru.qa.niffler.page.element;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.page.element.DateRange.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class SpendingTable {
  private final SelenideElement
    spendingsTable = $("#spendings"),
    deleteBtn = $("#delete"),
    deleteModal = $("[role='dialog']"),
    periodBtn = $("#period");

  @Step("Selecting spending period")
  public SpendingTable selectPeriod(DateRange period) {
    periodBtn.click();
    final ElementsCollection periodValues = $("[role='listbox']").$$("li");
    periodValues.find(text(period.getValue())).click();
    return this;
  }

  @Step("Asserting that spending table is loaded")
  public final void shouldBeLoaded() {
    spendingsTable.should(visible);
  }

  @Step("Opening edit spend table")
  public EditSpendingPage editSpending(String description) {
    spendingsTable.$$("tbody tr").find(text(description))
      .$$("td")
      .get(5)
      .click();
    return new EditSpendingPage();
  }

  @Step
  public SpendingTable checkThatTableNotContainsSpending(String description) {
    spendingsTable.$$("tbody tr").find(text(description))
      .shouldNot(visible);
    return this;
  }

  @Step("Deleting spend")
  public SpendingTable deleteSpending(String description) {
    spendingsTable.$$("tbody tr").find(text(description))
      .$$("td").get(0).click();
    deleteBtn.click();
    deleteModal.$$("button").get(1).click();
    return this;
  }

  @Step("Asserting spendings by description")
  public SpendingTable checkTableContains(String... expectedSpendsDescription) {
    ElementsCollection spendingElements = spendingsTable.$$("tbody tr");
    for (String description : expectedSpendsDescription) {
      spendingElements.findBy(text(description))
        .shouldHave(text(description));
    }
    return this;
  }

  @Step("Asserting spendings table size")
  public SpendingTable checkTableSize(int expectedSize) {
    int totalCount = 0;
    SelenideElement nextButton = $("#page-next");
    while (true) {
      ElementsCollection spendings = spendingsTable.$$("tbody tr");
      totalCount += spendings.size();
      if (nextButton.has(attribute("disabled"))) {
        break;
      }
      nextButton.click();
    }
    assertEquals(expectedSize, totalCount);
    return this;
  }
}