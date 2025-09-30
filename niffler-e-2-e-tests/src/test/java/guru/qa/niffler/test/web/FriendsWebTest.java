package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.page.AllPeoplePage;
import guru.qa.niffler.page.FriendsPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.open;

@Tags({@Tag("WEB")})
@ParametersAreNonnullByDefault
public class FriendsWebTest {

  @Test
  @User
  @ApiLogin
  @DisplayName("Friends table should be empty for new user")
  void friendsTableShouldBeEmptyForNewUser() {
    open(FriendsPage.URL, FriendsPage.class)
      .checkThatPageOpen()
      .checkThatFriendsTableIsEmpty();
  }

  @Test
  @User(
    username = "test1"
    //friends = 1
  )
  @ApiLogin
  @DisplayName("Friend should be present in friends table")
  void friendShouldBePresentInFriendsTable(UdUserJson user) {
    open(FriendsPage.URL, FriendsPage.class)
      .checkThatPageOpen()
      .findFriend(user.testData().friends().getFirst().username()); //TODO FIX LOCATOR
  }

  @Test
  @User(
    incomeInvitations = 1
  )
  @ApiLogin
  @DisplayName("Income invitation should be present in friends table")
  void incomeInvitationShouldBePresentInFriendsTable(UdUserJson user) {
    open(FriendsPage.URL, FriendsPage.class)
      .checkThatPageOpen()
      .checkFriendshipRequest(user.testData().incomeInvitations().getFirst().username());
  }

  @Test
  @User(
    outcomeInvitations = 1
  )
  @ApiLogin
  @DisplayName("Outcome invitation should be present in friends table")
  void outcomeInvitationShouldBePresentInAllPeopleTable(UdUserJson user) {
    open(AllPeoplePage.URL, AllPeoplePage.class)
      .checkThatPageOpen()
      .findPeopleWithSearchInput(user.testData().outcomeInvitations().getFirst().username());
  }

  @Test
  @User(
    incomeInvitations = 1
  )
  @ApiLogin
  @DisplayName("User should be able to accept friendship request")
  void userShouldBeAbleToAcceptFriendshipRequest(UdUserJson user) {
    open(FriendsPage.URL, FriendsPage.class)
      .checkThatPageOpen()
      .acceptFriendshipRequest(user.testData().incomeInvitations().getFirst().username());
  }

  @Test
  @User(
    incomeInvitations = 1
  )
  @ApiLogin
  @DisplayName("User should be able to decline friendship request")
  void userShouldBeAbleToDeclineFriendshipRequest(UdUserJson user) {
    open(FriendsPage.URL, FriendsPage.class)
      .checkThatPageOpen()
      .declineFriendshipRequest(user.testData().incomeInvitations().getFirst().username());
  }
}