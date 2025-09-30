package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.page.base.BasePage;
import guru.qa.niffler.page.element.CalendarElement;
import guru.qa.niffler.page.element.SpendingTable;
import guru.qa.niffler.utils.RandomDataUtils;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Date;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class CreateSpendingPage extends BasePage<CreateSpendingPage> {
  private final CalendarElement calendarElement = new CalendarElement();

  public final static String URL = CFG.frontUrl() + "spending";

  private final SelenideElement self = $("form"),
    amountInput = self.$("#amount"),
    currencySelector = self.$("#currency"),
    categoryInput = self.$("#category"),
    descriptionInput = self.$("#description"),
    saveBtn = $("#save");

  @Nonnull
  @Step("Creating spending")
  public SpendingTable createSpending(SpendJson spend, CurrencyValues currency) {
    amountInput.val(spend.amount().toString());
    currencySelector.click();
    $("[role='listbox']").$$("span").find(text(currency.name())).click();
    categoryInput.val(spend.category().name());
    calendarElement.selectDateInCalendar(spend.spendDate());
    descriptionInput.val(spend.description());
    saveBtn.click();
    checkAlert("New spending is successfully created");
    return new SpendingTable();
  }

  @Nonnull
  @Step("Creating spending with description {description}")
  public SpendingTable createSpending(String description) {
    amountInput.val("100");
    currencySelector.click();
    $("[role='listbox']").$$("span").find(text("RUB")).click();
    categoryInput.val(RandomDataUtils.randomCategoryName());
    calendarElement.selectDateInCalendar(new Date());
    descriptionInput.val(description);
    saveBtn.click();
    checkAlert("New spending is successfully created");
    return new SpendingTable();
  }
}