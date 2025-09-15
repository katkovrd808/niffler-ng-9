package guru.qa.niffler.page.element;

import com.codeborne.selenide.ElementsCollection;
import guru.qa.niffler.model.spend.Bubble;
import guru.qa.niffler.page.base.BaseElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.StatConditions.*;

@ParametersAreNonnullByDefault
public class StatElement extends BaseElement<StatElement> {

  public StatElement() {
    super($("#stat"));
  }

  private final ElementsCollection bubbles = self.$("#legend-container").$$("li");

  @Nonnull
  public StatElement checkBubbles(Bubble... expectedBubbles) {
    bubbles.should(statBubbles(expectedBubbles));
    return this;
  }

  @Nonnull
  public StatElement checkStatisticBubblesInAnyOrder(Bubble... expectedBubbles) {
    bubbles.should(statBubblesInAnyOrder(expectedBubbles));
    return this;
  }

  @Nonnull
  public StatElement checkStatisticBubblesContains(Bubble... expectedBubbles) {
    bubbles.should(statBubblesContains(expectedBubbles));
    return this;
  }
}
