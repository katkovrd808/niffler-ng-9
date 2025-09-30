package guru.qa.niffler.service;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.service.impl.SpendDbClient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface SpendClient {
    //TODO WRITE FACTORY METHOD WHEN DOCKER WILL BE SET UP
    static SpendClient getInstance() {
        return new SpendDbClient();
    }

    @Nonnull
    SpendJson createSpend(SpendJson spend);

    @Nonnull
    SpendJson update(SpendJson spend);

    @Nonnull
    CategoryJson createCategory(CategoryJson category);

    @Nonnull
    Optional<CategoryJson> findCategoryByUsernameAndId(String username, UUID id);

    @Nonnull
    List<CategoryJson> findCategories(String username, @Nullable Boolean excludeArchived);

    @Nonnull
    Optional<CategoryJson> findCategoryByUsernameAndSpendName(String username, String name);

    @Nonnull
    Optional<SpendJson> findById(UUID id);

    @Nonnull
    Optional<SpendJson> findByUsernameAndSpendDescription(String username, String description);

    @Nonnull
    List<SpendJson> findSpendings(String username, CurrencyValues currency, Date from, Date to);

    @Nonnull
    List<SpendJson> findSpendings(String username);

    @Nonnull
    CategoryJson editCategory(CategoryJson category);

    void delete(SpendJson spend);

    void deleteCategory(CategoryJson category);
}