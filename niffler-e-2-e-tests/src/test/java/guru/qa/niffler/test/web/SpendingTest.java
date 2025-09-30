package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.spend.Bubble;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
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
import static guru.qa.niffler.model.CurrencyValues.RUB;
import static guru.qa.niffler.page.element.DateRange.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Tags({@Tag("WEB")})
@ParametersAreNonnullByDefault
public class SpendingTest {

  @Test
  @User(
    spendings = @Spending(
      amount = 89990.00,
      description = "Advanced 9 поток!",
      category = "Обучение"
    )
  )
  @ApiLogin
  @DisplayName("Spending description should be changed after editing in spending table")
  void spendingDescriptionShouldBeChangedAfterEditing(UdUserJson user) {
    final String newDescription = ":)";

    open(MainPage.URL, MainPage.class)
      .editSpending(user.testData().spendings().getFirst().description())
      .setNewSpendingDescription(newDescription)
      .save()
      .checkTableContains(newDescription);
  }

  @Test
  @User
  @ApiLogin
  @DisplayName("Spending should be created")
  void spendingShouldBeCreated() {
    final String description = RandomDataUtils.randomSentence(2);

    open(MainPage.URL, MainPage.class)
      .openCreateSpendingPageFromHeader()
      .createSpending(description)
      .checkTableContains(description);
  }

  @Test
  @User(
    username = "test1",
    spendings = @Spending(
      amount = 1000,
      description = "Spending should be deleted"
    )
  )
  @ApiLogin
  @DisplayName("Spending should be deleted")
  void spendingShouldBeDeletedFromTable(UdUserJson user) {
    final String description = user.testData().spendings().getFirst().description();

    open(MainPage.URL, MainPage.class)
      .spendingTableShouldBeLoaded()
      .deleteSpending(description)
      .checkThatTableNotContainsSpending(description);
  }

  @Test
  @User(
    spendings = @Spending(
      amount = 1000,
      description = "Spending should be displayed"
    )
  )
  @ApiLogin
  @DisplayName("All Time period value should display all users spendings")
  void allTimePeriodShouldDisplayAllUsersSpendings(UdUserJson user) {
    open(MainPage.URL, MainPage.class)
      .spendingTableShouldBeLoaded()
      .selectPeriod(ALL_TIME)
      .checkTableContains(user.testData().spendings().getFirst().description());
  }

  @Test
  @User(
    spendings = @Spending(
      amount = 1000,
      description = "Spending should be displayed",
      spendDate = LAST_MONTH
    )
  )
  @ApiLogin
  @DisplayName("Last month period value should display all users spendings created at last month")
  void lastMonthPeriodShouldDisplayAllUsersSpendingsCreatedAtLastMonth(UdUserJson user) {
    open(MainPage.URL, MainPage.class)
      .spendingTableShouldBeLoaded()
      .selectPeriod(LAST_MONTH)
      .checkTableContains(user.testData().spendings().getFirst().description());
  }

  @Test
  @User(
    spendings = @Spending(
      amount = 1000,
      description = "Spending should be displayed",
      spendDate = LAST_WEEK
    )
  )
  @ApiLogin
  @DisplayName("Last week period value should display all users spendings created at last week")
  void lastWeekPeriodShouldDisplayAllUsersSpendingsCreatedAtLastWeek(UdUserJson user) {
    open(MainPage.URL, MainPage.class)
      .spendingTableShouldBeLoaded()
      .selectPeriod(LAST_WEEK)
      .checkTableContains(user.testData().spendings().getFirst().description());
  }

  @Test
  @User(
    spendings = @Spending(
      amount = 1000,
      description = "Spending should be displayed"
    )
  )
  @ApiLogin
  @DisplayName("Today period value should display all users spendings created at current day")
  void todayPeriodShouldDisplayAllUsersSpendingsCreatedAtThisDay(UdUserJson user) {
    open(MainPage.URL, MainPage.class)
      .spendingTableShouldBeLoaded()
      .selectPeriod(TODAY)
      .checkTableContains(user.testData().spendings().getFirst().description());
  }

  @Test
  @User(
    spendings = @Spending(
      amount = 100,
      description = "Spending should be displayed"
    )
  )
  @ApiLogin
  @DisplayName("Created spending should be displayed in table")
  void createdSpendingShouldBeVisibleInTable(UdUserJson user) {
    open(MainPage.URL, MainPage.class)
      .spendingTableShouldBeLoaded()
      .checkTableContains(user.testData().spendings().getFirst().description());
  }

  @Test
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
  @ApiLogin
  @DisplayName("Created spendings should be displayed in table")
  void createdSpendingsShouldBeVisibleInTable(UdUserJson user) {
    var spendings = user.testData().spendings();
    final String[] spendingDescriptions = {
      spendings.getFirst().description(),
      spendings.getLast().description()
    };

    open(MainPage.URL, MainPage.class)
      .spendingTableShouldBeLoaded()
      .checkTableContains(spendingDescriptions);
  }

  @Test
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
  @ApiLogin
  @DisplayName("Table size should be equal to amount of user spendings")
  void tableSizeShouldBeEqualToAmountOfSpendings(UdUserJson user) {
    var spendings = user.testData().spendings();

    open(MainPage.URL, MainPage.class)
      .spendingTableShouldBeLoaded()
      .checkTableSize(spendings.size());
  }

  @ScreenShotTest("img/expected/expected-stat.png")
  @User(
    spendings = @Spending(
      category = "Обучение",
      description = "Обучение Advanced 2.0",
      amount = 79990
    )
  )
  @ApiLogin
  @DisplayName("Created spending should be displayed in chart")
  void checkStatComponent(BufferedImage expected) throws IOException {
    assertFalse(new ScreenDiffResult(
      expected,
      new ActualScreenShot().makeScreenshot($("canvas[role='img']"))
    ));
  }

  @ScreenShotTest("img/expected/expected-stat-edited.png")
  @User(
    spendings = @Spending(
      category = "Обучение",
      description = "Обучение Advanced 2.0",
      amount = 79990
    )
  )
  @ApiLogin
  @DisplayName("Spending amount should change in chart after editing")
  void checkStatComponentAfterEdit(UdUserJson user, BufferedImage expected) throws IOException {
    Selenide.open(MainPage.URL, MainPage.class)
      .editSpending(user.testData().spendings().getFirst().description())
      .setNewSpendingPrice(1000)
      .save()
      .assertDiff(expected, $("canvas[role='img']"));
  }

  @ScreenShotTest("img/expected/expected-stat-deleted.png")
  @User(
    spendings = @Spending(
      category = "Обучение",
      description = "Обучение Advanced 2.0",
      amount = 79990
    )
  )
  @ApiLogin
  @DisplayName("Deleted spendings should not be displayed in chart")
  void checkStatComponentAfterDelete(UdUserJson user, BufferedImage expected) throws IOException {
    Selenide.open(MainPage.URL, MainPage.class)
      .deleteSpending(user.testData().spendings().getFirst().description())
      .checkAlert("Spendings succesfully deleted")
      .assertDiff(expected, $("canvas[role='img']"));
  }

  @ScreenShotTest(("img/expected/expected-stat-archived.png"))
  @User(
    spendings = @Spending(
      category = "Обучение",
      description = "Обучение Advanced 2.0",
      amount = 79990
    )
  )
  @ApiLogin
  @DisplayName("Archived spendings should be displayed in chart")
  void checkStatComponentAfterArchive(UdUserJson user, BufferedImage expected) throws IOException {
    final SpendJson spending = user.testData().spendings().getFirst();

    Selenide.open(ProfilePage.URL, ProfilePage.class)
      .archiveCategoryAndReturn(spending.category().name())
      .assertArchivedSpendingHasSum(spending.amount())
      .assertDiff(expected, $("canvas[role='img']"));
  }

  @Test
  @User(
    spendings = @Spending(
      category = "Рыбалка",
      description = "Рыбалка на неве",
      amount = 1000
    )
  )
  @ApiLogin
  @DisplayName("Created spending category should be displayed at chart bubbles")
  void statBubblesContentShouldContainValidTextAndColor(UdUserJson user) {
    Selenide.open(MainPage.URL, MainPage.class)
      .checkStatBubbles(
        Bubble.from(user, RUB, Color.yellow, 0)
      );
  }

  @Test
  @User(
    spendings = {@Spending(
      category = "Рыбалка",
      description = "Рыбалка на неве",
      amount = 1000
    ),
      @Spending(
        category = "Автомобиль",
        description = "Ремонт автомобиля",
        amount = 10000
      ),
      @Spending(
        category = "Динозавр",
        description = "Покупка динозавра",
        amount = 200
      )}
  )
  @ApiLogin
  @DisplayName("Created spending category should be displayed at chart bubbles")
  void statBubblesContentShouldBeAssertedInAnyOrder(UdUserJson user) {
    Selenide.open(MainPage.URL, MainPage.class)
      .checkStatBubblesAnyOrder(
        Bubble.from(user, RUB, Color.yellow, 1),
        Bubble.from(user, RUB, Color.green, 0),
        Bubble.from(user, RUB, Color.blue100, 2)
      );
  }

  @Test
  @User(
    spendings = {@Spending(
      category = "Рыбалка",
      description = "Рыбалка на неве",
      amount = 1000
    ),
      @Spending(
        category = "Автомобиль",
        description = "Ремонт автомобиля",
        amount = 10000
      )}
  )
  @ApiLogin
  @DisplayName("Created spending category should be displayed at chart bubbles")
  void statBubblesContentShouldContain(UdUserJson user) {
    Selenide.open(MainPage.URL, MainPage.class)
      .checkStatBubblesContains(
        Bubble.from(user, RUB, Color.green, 0)
      );
  }

  @Test
  @User(
    spendings = {@Spending(
      amount = 90,
      description = "First spending should be displayed"
    ),
      @Spending(
        amount = 100,
        description = "Second spending should be displayed"
      )}
  )
  @ApiLogin
  @DisplayName("Created spendings should be displayed in table and asserted by custom assertion")
  void createdSpendingsShouldBeVisibleInTableAssertedByCustomAssertion(UdUserJson user) {
    var spendings = user.testData().spendings();

    open(MainPage.URL, MainPage.class)
      .checkThatPageLoaded()
      .spendingTableShouldBeLoaded()
      .checkTableContains(spendings.toArray(SpendJson[]::new));
  }
}