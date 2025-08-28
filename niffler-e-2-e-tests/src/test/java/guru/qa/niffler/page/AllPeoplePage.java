package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.base.BasePage;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class AllPeoplePage extends BasePage<AllPeoplePage> {
    private final SelenideElement
        pageTab = $("[aria-label='People tabs']"),
        searchInput = $("input[aria-label='search']"),
        peopleTable = $("#simple-tabpanel-all table"),
        nextPageBtn = $("#page-next"),
        prevPageBtn = $("#page-prev");

    public AllPeoplePage checkThatPageOpen(){
        pageTab.$$("a").find(href("/people/all")).
                shouldHave(attribute("aria-selected", "true"));
        return this;
    }

    public AllPeoplePage findPeopleWithSearchInput(String username){
        searchInput.val(username).pressEnter();
        peopleTable.$$("td").find(text(username))
                .shouldBe(visible);
        return this;
    }

    public AllPeoplePage addFriendFromTable(String username){
        searchInput.val(username).pressEnter();
        peopleTable.$$("tr").find(text(username))
                .$$("td").find(text("Add friend")).click();
        checkAlert("Invitation sent to " + username);
        return this;
    }
}
