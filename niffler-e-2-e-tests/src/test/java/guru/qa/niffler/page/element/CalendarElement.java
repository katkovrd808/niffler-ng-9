package guru.qa.niffler.page.element;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.base.BaseElement;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class CalendarElement extends BaseElement<CalendarElement> {

  public CalendarElement() {
    super($("[placeholder='MM/DD/YYYY']"));
  }

  public CalendarElement selectDateInCalendar(Date date) {
    LocalDate targetDate = date.toInstant()
      .atZone(ZoneId.systemDefault())
      .toLocalDate();
    int targetDay = targetDate.getDayOfMonth();
    int targetMonth = targetDate.getMonthValue();
    int targetYear = targetDate.getYear();

    String targetDateString = String.format("%d/%d/%d", targetDay, targetMonth, targetYear);
    self.val(targetDateString);
    return this;
  }
}
