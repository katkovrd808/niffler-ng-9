package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.base.BasePage;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class FriendsPage extends BasePage<FriendsPage> {
    private final SelenideElement
        pageTab = $("[aria-label='People tabs']"),
        searchInput = $("input[aria-label='search']"),
        requestsTable = $("#requests"),
        friendsTable = $("#friends"),
        modal = $("[role='dialog']"),
        nextPageBtn = $("#page-next"),
        prevPageBtn = $("#page-prev");

    public FriendsPage checkThatPageOpen(){
        pageTab.$$("a").find(href("/people/friends")).
                shouldHave(attribute("aria-selected", "true"));
        return this;
    }

    public FriendsPage findFriend(String friendUsername){
        searchInput.val(friendUsername).pressEnter();
        friendsTable.$$("td").find(text(friendUsername)).shouldBe(visible);
        return this;
    }

    public FriendsPage checkFriendshipRequest(String friendUsername){
        searchInput.val(friendUsername).pressEnter();
        requestsTable.$$("td").find(text(friendUsername)).shouldBe(visible);
        return this;
    }

    public FriendsPage checkThatFriendsTableIsEmpty(){
        friendsTable.$("tr").shouldNotBe(exist);
        return this;
    }

    public FriendsPage acceptFriendshipRequest(String friendUsername){
        searchInput.val(friendUsername).pressEnter();
        requestsTable.$$("tr").find(text(friendUsername))
                .$$("button").find(text("Accept")).click();
        checkAlert("Invitation of " + friendUsername + " accepted");
        return this;
    }

    public FriendsPage declineFriendshipRequest(String friendUsername){
        searchInput.val(friendUsername).pressEnter();
        requestsTable.$$("tr").find(text(friendUsername))
                .$$("button").find(text("Decline")).click();
        modal.$("h2").shouldHave(text("Decline friendship"));
        modal.$$("button").find(text("Decline")).click();
        checkAlert("Invitation of " + friendUsername + " is declined");
        return this;
    }
}
