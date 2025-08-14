package guru.qa.niffler.page.element;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class CalendarElement {
  public CalendarElement selectDateInCalendar(Date date) {
    LocalDate targetDate = date.toInstant()
      .atZone(ZoneId.systemDefault())
      .toLocalDate();
    int targetDay = targetDate.getDayOfMonth();
    int targetMonth = targetDate.getMonthValue();
    int targetYear = targetDate.getYear();

    String targetDateString = String.format("%d/%d/%d", targetDay, targetMonth, targetYear);
    $("[placeholder='MM/DD/YYYY']").val(targetDateString);
    return this;
  }
}
