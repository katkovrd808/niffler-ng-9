package guru.qa.niffler.page.element;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class AlertElement {
  private final SelenideElement self = $("#root [role='presentation']");

  public void shouldHaveText(String expected) {
    self.shouldHave(text(expected));
  }
}
