package guru.qa.niffler.page.element;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DateRange {
  ALL_TIME("All time"),
  LAST_MONTH("Last month"),
  LAST_WEEK("Last week"),
  TODAY("Today");

  private final String value;
}
