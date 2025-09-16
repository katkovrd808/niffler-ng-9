package guru.qa.niffler.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CurrencyValues {
  RUB("₽"),
  USD("$"),
  EUR(""),
  KZT("₸");

  public final String symbol;

  public static CurrencyValues bySymbol(String symbol) {
    for (CurrencyValues currency : values()) {
      if (currency.symbol.equals(symbol)) {
        return currency;
      }
    }
    throw new IllegalArgumentException("No currency with symbol: " + symbol);
  }
}
