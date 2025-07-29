package guru.qa.niffler.test;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.DisabledByIssue;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;

public class ProfileTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @DisabledByIssue("3")
    @DisplayName("Archived category should be present in categories list on profile page")
    @User(
            categories = @Category(
                    archived = true
            )
    )
    void archivedCategoryShouldPresentInCategoriesList(UdUserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit()
                .checkThatPageLoaded()
                .openProfilePageFromHeader()
                .showArchivedCategories()
                .checkArchivedCategory(user.testData().categories().getFirst().name());
    }

    @User(
            categories = @Category(
                    archived = false
            )
    )
    @Test
    @DisplayName("Active category should be present in categories list on profile page")
    void activeCategoryShouldPresentInCategoriesList(UdUserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.testData().password())
                .submit()
                .checkThatPageLoaded()
                .openProfilePageFromHeader()
                .checkCategory(user.testData().categories().getFirst().name());
    }

}
