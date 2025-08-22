package guru.qa.niffler.page.element;

import lombok.Getter;

import javax.annotation.Nullable;

@Getter
public enum DateRange {
  ALL_TIME("All time"),
  LAST_MONTH("Last month"),
  LAST_WEEK("Last week"),
  TODAY("Today");

  private final String value;

  DateRange(@Nullable String value) {
    this.value = value;
  }
}
