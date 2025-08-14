package guru.qa.niffler.test;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.codeborne.selenide.Selenide.open;

@ExtendWith({BrowserExtension.class})
public class FriendsWebTest {
  private static final String FRONT_URL = Config.getInstance().frontUrl();

  @Test
  @DisplayName("Friends table should be empty for new user")
  @User
  void friendsTableShouldBeEmptyForNewUser(UdUserJson user) {
    open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .openFriendsPageFromHeader()
      .checkThatPageOpen()
      .checkThatFriendsTableIsEmpty();
  }

  @Test
  @DisplayName("Friend should be present in friends table")
  @User(
    friends = 1
  )
  void friendShouldBePresentInFriendsTable(UdUserJson user) {
    open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .openFriendsPageFromHeader()
      .checkThatPageOpen()
      .findFriend(user.testData().friends().getFirst().username());
  }

  @Test
  @DisplayName("Income invitation should be present in friends table")
  @User(
    incomeInvitations = 1
  )
  void incomeInvitationShouldBePresentInFriendsTable(UdUserJson user) {
    open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .openFriendsPageFromHeader()
      .checkThatPageOpen()
      .checkFriendshipRequest(user.testData().incomeInvitations().getFirst().username());
  }

  @Test
  @DisplayName("Outcome invitation should be present in friends table")
  @User(
    outcomeInvitations = 1
  )
  void outcomeInvitationShouldBePresentInAllPeopleTable(UdUserJson user) {
    open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .openAllPeoplePageFromHeader()
      .checkThatPageOpen()
      .findPeopleWithSearchInput(user.testData().outcomeInvitations().getFirst().username());
  }

  @Test
  @DisplayName("Income invitation should be present in friends table")
  @User(
    incomeInvitations = 1
  )
  void userShouldBeAbleToAcceptFriendshipRequest(UdUserJson user) {
    open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .openFriendsPageFromHeader()
      .checkThatPageOpen()
      .acceptFriendshipRequest(user.testData().incomeInvitations().getFirst().username());
  }

  @Test
  @DisplayName("Income invitation should be present in friends table")
  @User(
    incomeInvitations = 1
  )
  void userShouldBeAbleToDeclineFriendshipRequest(UdUserJson user) {
    open(FRONT_URL, LoginPage.class)
      .fillLoginPage(user.username(), user.testData().password())
      .submit()
      .openFriendsPageFromHeader()
      .checkThatPageOpen()
      .declineFriendshipRequest(user.testData().incomeInvitations().getFirst().username());
  }
}
