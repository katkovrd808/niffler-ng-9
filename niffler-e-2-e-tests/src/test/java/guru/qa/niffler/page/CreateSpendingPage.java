package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.element.CalendarElement;
import guru.qa.niffler.page.element.SpendingTable;
import guru.qa.niffler.utils.RandomDataUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Date;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class CreateSpendingPage {
  private final CalendarElement calendarElement = new CalendarElement();

  private final SelenideElement self = $("form"),
  amountInput = self.$("#amount"),
  currencySelector = self.$("#currency"),
  categoryInput = self.$("#category"),
  descriptionInput = self.$("#description"),
  saveBtn = $("#save");

  public SpendingTable createSpending(String description) {
    amountInput.val("100");
    currencySelector.click();
    $("[role='listbox']").$$("span").find(text("RUB")).click();
    categoryInput.val(RandomDataUtils.randomCategoryName());
    calendarElement.selectDateInCalendar(new Date());
    descriptionInput.val(description);
    saveBtn.click();
    return new SpendingTable();
  }
}