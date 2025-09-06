package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.ActualScreenShot;
import guru.qa.niffler.utils.RandomDataUtils;
import guru.qa.niffler.utils.ScreenDiffResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.page.element.DateRange.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Tags({@Tag("WEB")})
@ParametersAreNonnullByDefault
public class SpendingTest {
  private static final String FRONT_URL = Config.getInstance().frontUrl();

  @User(
    spendings = @Spending(
      amount = 89990.00,
      description = "Advanced 9 поток!",
      category = "Обучение"
    )
  )
  @Test
  @DisplayName("Spending description should be changed after editing in spending table")
  void spendingDescriptionShouldBeChangedAfterEditing(UdUserJson user) {
    final String newDescription = ":)";

    open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .checkThatPageLoaded()
      .editSpending(user.testData().spendings().getFirst().description())
      .setNewSpendingDescription(newDescription)
      .save()
      .checkTableContains(newDescription);
  }

  @User()
  @Test
  @DisplayName("Spending should be created")
  void spendingShouldBeCreated(UdUserJson user) {
    final String description = RandomDataUtils.randomSentence(2);

    open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .checkThatPageLoaded()
      .openCreateSpendingPageFromHeader()
      .createSpending(description)
      .checkTableContains(description);
  }

  @User(
    spendings = @Spending(
      amount = 1000,
      description = "Spending should be deleted"
    )
  )
  @Test
  @DisplayName("Spending should be deleted")
  void spendingShouldBeDeletedFromTable(UdUserJson user) {
    final String description = user.testData().spendings().getFirst().description();

    open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .checkThatPageLoaded()
      .spendingTableShouldBeLoaded()
      .deleteSpending(description)
      .checkThatTableNotContainsSpending(description);
  }

  @User(
    spendings = @Spending(
      amount = 1000,
      description = "Spending should be displayed"
    )
  )
  @Test
  @DisplayName("All Time period value should display all users spendings")
  void allTimePeriodShouldDisplayAllUsersSpendings(UdUserJson user) {
    open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .checkThatPageLoaded()
      .spendingTableShouldBeLoaded()
      .selectPeriod(ALL_TIME)
      .checkTableContains(user.testData().spendings().getFirst().description());
  }

  @User(
    spendings = @Spending(
      amount = 1000,
      description = "Spending should be displayed",
      spendDate = LAST_MONTH
    )
  )
  @Test
  @DisplayName("Last month period value should display all users spendings created at last month")
  void lastMonthPeriodShouldDisplayAllUsersSpendingsCreatedAtLastMonth(UdUserJson user) {
    open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .checkThatPageLoaded()
      .spendingTableShouldBeLoaded()
      .selectPeriod(LAST_MONTH)
      .checkTableContains(user.testData().spendings().getFirst().description());
  }

  @User(
    spendings = @Spending(
      amount = 1000,
      description = "Spending should be displayed",
      spendDate = LAST_WEEK
    )
  )
  @Test
  @DisplayName("Last week period value should display all users spendings created at last week")
  void lastWeekPeriodShouldDisplayAllUsersSpendingsCreatedAtLastWeek(UdUserJson user) {
    open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .checkThatPageLoaded()
      .spendingTableShouldBeLoaded()
      .selectPeriod(LAST_WEEK)
      .checkTableContains(user.testData().spendings().getFirst().description());
  }

  @User(
    spendings = @Spending(
      amount = 1000,
      description = "Spending should be displayed"
    )
  )
  @Test
  @DisplayName("Today period value should display all users spendings created at current day")
  void todayPeriodShouldDisplayAllUsersSpendingsCreatedAtThisDay(UdUserJson user) {
    open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .checkThatPageLoaded()
      .spendingTableShouldBeLoaded()
      .selectPeriod(TODAY)
      .checkTableContains(user.testData().spendings().getFirst().description());
  }

  @User(
    spendings = @Spending(
      amount = 100,
      description = "Spending should be displayed"
    )
  )
  @Test
  @DisplayName("Created spending should be displayed in table")
  void createdSpendingShouldBeVisibleInTable(UdUserJson user) {
    open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .checkThatPageLoaded()
      .spendingTableShouldBeLoaded()
      .checkTableContains(user.testData().spendings().getFirst().description());
  }

  @User(
    spendings = {@Spending(
      amount = 100,
      description = "First spending should be displayed"
    ),
      @Spending(
        amount = 90,
        description = "Second spending should be displayed"
      )}
  )
  @Test
  @DisplayName("Created spendings should be displayed in table")
  void createdSpendingsShouldBeVisibleInTable(UdUserJson user) {
    var spendings = user.testData().spendings();
    final String[] spendingDescriptions = {
      spendings.getFirst().description(),
      spendings.getLast().description()
    };

    open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .checkThatPageLoaded()
      .spendingTableShouldBeLoaded()
      .checkTableContains(spendingDescriptions);
  }

  @User(
    spendings = {@Spending(
      amount = 80,
      description = "First spending should be displayed"
    ),
      @Spending(
        amount = 90,
        description = "Second spending should be displayed"
      )}
  )
  @Test
  @DisplayName("Table size should be equal to amount of user spendings")
  void tableSizeShouldBeEqualToAmountOfSpendings(UdUserJson user) {
    var spendings = user.testData().spendings();

    open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .checkThatPageLoaded()
      .spendingTableShouldBeLoaded()
      .checkTableSize(spendings.size());
  }

  @User(
    spendings = @Spending(
      category = "Обучение",
      description = "Обучение Advanced 2.0",
      amount = 79990
    )
  )
  @ScreenShotTest("img/expected/expected-stat.png")
  @DisplayName("Created spending should be displayed in chart")
  void checkStatComponent(UdUserJson user, BufferedImage expected) throws IOException {
    Selenide.open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit();

    assertFalse(new ScreenDiffResult(
      expected,
      new ActualScreenShot().makeScreenshot($("canvas[role='img']"))
    ));
  }

  @User(
    spendings = @Spending(
      category = "Обучение",
      description = "Обучение Advanced 2.0",
      amount = 79990
    )
  )
  @ScreenShotTest("img/expected/expected-stat-edited.png")
  @DisplayName("Spending amount should change in chart after editing")
  void checkStatComponentAfterEdit(UdUserJson user, BufferedImage expected) throws IOException {
    Selenide.open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .editSpending(user.testData().spendings().getFirst().description())
      .setNewSpendingPrice(1000)
      .save();

    assertFalse(new ScreenDiffResult(
      expected,
      new ActualScreenShot().makeScreenshot($("canvas[role='img']"))
    ));
  }

  @User(
    spendings = @Spending(
      category = "Обучение",
      description = "Обучение Advanced 2.0",
      amount = 79990
    )
  )
  @ScreenShotTest("img/expected/expected-stat-deleted.png")
  @DisplayName("Deleted spendings should not be displayed in chart")
  void checkStatComponentAfterDelete(UdUserJson user, BufferedImage expected) throws IOException {
    Selenide.open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .deleteSpending(user.testData().spendings().getFirst().description())
      .checkAlert("Spendings succesfully deleted");

    assertFalse(new ScreenDiffResult(
      expected,
      new ActualScreenShot().makeScreenshot($("canvas[role='img']"))
    ));
  }

  @User(
    spendings = @Spending(
      category = "Обучение",
      description = "Обучение Advanced 2.0",
      amount = 79990
    )
  )
  @ScreenShotTest(("img/expected/expected-stat-archived.png"))
  @DisplayName("Archived spendings should be displayed in chart")
  void checkStatComponentAfterArchive(UdUserJson user, BufferedImage expected) throws IOException {
    final SpendJson spending = user.testData().spendings().getFirst();

    Selenide.open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .openProfilePageFromHeader()
      .archiveCategoryAndReturn(spending.category().name())
      .assertArchivedSpendingHasSum(spending.amount());

    assertFalse(new ScreenDiffResult(
      expected,
      new ActualScreenShot().makeScreenshot($("canvas[role='img']"))
    ));
  }
}