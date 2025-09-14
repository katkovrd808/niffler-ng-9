package guru.qa.niffler.page.element;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.page.base.BaseElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.StatConditions.color;

@ParametersAreNonnullByDefault
public class StatElement extends BaseElement<StatElement> {

  public StatElement() {
    super($("#stat"));
  }

  private final ElementsCollection bubbles = self.$("#legend-container").$$("li");
  private final SelenideElement chart = $("canvas[role='img']");

  @Step("Check that statistic bubbles contain texts {0}")
  @Nonnull
  public StatElement checkStatisticBubblesContains(String... texts) {
    bubbles.should(CollectionCondition.texts(texts));
    return this;
  }

  @Step("Check that stat bubbles contains colors {expectedColors}")
  @Nonnull
  public StatElement checkBubbles(Color... expectedColors) {
    bubbles.should(color(expectedColors));
    return this;
  }
}
