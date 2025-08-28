package guru.qa.niffler.page.base;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class BasePage <T extends BasePage<?>>{

  protected final SelenideElement alert = $("#root [role='presentation']");

  @SuppressWarnings("unchecked")
  @Step("Asserting that alert should have text {text}")
  public T checkAlert(String text) {
    alert.shouldHave(text(text));
    return (T) this;
  }

}
