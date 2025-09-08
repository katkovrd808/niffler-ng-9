package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.base.BasePage;
import guru.qa.niffler.page.element.SpendingTable;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class EditSpendingPage extends BasePage<EditSpendingPage> {
  private final SelenideElement descriptionInput = $("#description");
  private final SelenideElement priceInput = $("#amount");
  private final SelenideElement submitButton = $("#save");

  @Step("Setting new description to spend")
  public EditSpendingPage setNewSpendingDescription(String description) {
    descriptionInput.clear();
    descriptionInput.setValue(description);
    return this;
  }

  @Step("Setting new price {price} to spend")
  public EditSpendingPage setNewSpendingPrice(int amount) {
    priceInput.clear();
    priceInput.setValue(String.valueOf(amount));
    return this;
  }

  @Step("Saving edited spend")
  public SpendingTable save() {
    submitButton.click();
    checkAlert("Spending is edited successfully");
    return new SpendingTable();
  }
}
