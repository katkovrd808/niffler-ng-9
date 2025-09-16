package guru.qa.niffler.model.spend;

import guru.qa.niffler.condition.Color;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.userdata.UdUserJson;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record Bubble(
  Color color,
  String text
) {

  @Nonnull
  public static Bubble from(UdUserJson user, CurrencyValues currency, Color color, int index) {
    final String expectedCategoryName = user.testData().spendings().get(index).category().name();
    final String expectedAmount = String.format("%s %s", user.testData().spendings().get(index).amount().intValue(), currency.symbol);

    return new Bubble(color, String.format("%s %s", expectedCategoryName, expectedAmount));
  }
}
