package guru.qa.niffler.page.element;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.base.BaseElement;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class SearchFieldElement {
  private final SelenideElement
    searchInput = $("form input"),
    inputButton = $("form button");

  public SearchFieldElement find(String query) {
    searchInput.val(query).pressEnter();
    return this;
  }

  public SearchFieldElement clearIfNotEmpty() {
    if (!searchInput.getOptions().isEmpty()) {
      inputButton.click();
    }
    return this;
  }
}
