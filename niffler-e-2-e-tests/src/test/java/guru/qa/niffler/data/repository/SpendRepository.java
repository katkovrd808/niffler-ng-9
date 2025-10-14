package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface SpendRepository {

    @Nonnull
    SpendEntity createSpend(SpendEntity spend);

    @Nonnull
    SpendEntity update(SpendEntity spend);

    @Nonnull
    CategoryEntity createCategory(CategoryEntity category);

    @Nonnull
    Optional<CategoryEntity> findCategoryById(UUID id);

    @Nonnull
    Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name);

    @Nonnull
    List<CategoryEntity> findCategoriesByUsername(String username, boolean excludeArchived);

    @Nonnull
    Optional<SpendEntity> findById(UUID id);

    @Nonnull
    List<SpendEntity> findAllByUsername(String username, CurrencyValues currencyValues, Date from, Date to);

    @Nonnull
    List<SpendEntity> findAllByUsername(String username);

    @Nonnull
    CategoryEntity updateCategory(CategoryEntity category);

    @Nonnull
    Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description);

    void delete(SpendEntity spend);

    void deleteCategory(CategoryEntity category);
}
