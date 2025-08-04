package guru.qa.niffler.model.userdata;

import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public record TestData(
        String password,
        List<UdUserJson> friends,
        List<UdUserJson> incomeInvitations,
        List<UdUserJson> outcomeInvitations,
        List<CategoryJson> categories,
        List<SpendJson> spendings
) {

    public TestData addCategories(List<CategoryJson> categories) {
        return new TestData(
                this.password,
                this.friends,
                this.incomeInvitations,
                this.outcomeInvitations,
                categories,
                this.spendings
        );
    }

    public TestData addSpendings(List<SpendJson> spendings) {
        return new TestData(
                this.password,
                this.friends,
                this.incomeInvitations,
                this.outcomeInvitations,
                this.categories,
                spendings
        );
    }
}