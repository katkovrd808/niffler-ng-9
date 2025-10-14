package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.base.BasePage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
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

    public final static String URL = CFG.frontUrl() + "people/friends";

    @Nonnull
    @Step("Asserting that Friends page is open")
    public FriendsPage checkThatPageOpen(){
        pageTab.$$("a").find(href("/people/friends")).
                shouldHave(attribute("aria-selected", "true"));
        return this;
    }

    @Nonnull
    @Step("Searching for friend with username {friendUsername}")
    public FriendsPage findFriend(String friendUsername){
        searchInput.val(friendUsername).pressEnter();
        friendsTable.$$("td").find(text(friendUsername)).shouldBe(visible);
        return this;
    }

    @Nonnull
    @Step("Asserting that friendship request from user {friendUsername} exists")
    public FriendsPage checkFriendshipRequest(String friendUsername){
        searchInput.val(friendUsername).pressEnter();
        requestsTable.$$("td").find(text(friendUsername)).shouldBe(visible);
        return this;
    }

    @Nonnull
    @Step("Asserting that friends table is empty")
    public FriendsPage checkThatFriendsTableIsEmpty(){
        friendsTable.$("tr").shouldNotBe(exist);
        return this;
    }

    @Nonnull
    @Step("Accepting friendship request from user {friendUsername}")
    public FriendsPage acceptFriendshipRequest(String friendUsername){
        searchInput.val(friendUsername).pressEnter();
        requestsTable.$$("tr").find(text(friendUsername))
                .$$("button").find(text("Accept")).click();
        checkAlert("Invitation of " + friendUsername + " accepted");
        return this;
    }

    @Nonnull
    @Step("Declining friendship request from user {friendUsername}")
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
