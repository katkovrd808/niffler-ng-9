package guru.qa.niffler.utils;

import guru.qa.niffler.page.element.DateRange;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateResolverFactory {
  private DateResolverFactory() {
  }

  @Nonnull
  public static Date resolveDate(DateRange range) {
    return switch (range) {
      case TODAY -> new Date();
      case LAST_WEEK -> Date.from(Instant.now().minus(7, ChronoUnit.DAYS));
      case LAST_MONTH -> Date.from(Instant.now().minus(30, ChronoUnit.DAYS));
      default -> throw new IllegalStateException("Unexpected value: " + range);
    };
  }
}
