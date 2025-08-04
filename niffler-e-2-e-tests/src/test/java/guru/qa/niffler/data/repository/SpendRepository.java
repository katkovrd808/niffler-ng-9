package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.spend.CategoryJson;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
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
    Optional<SpendEntity> findById(UUID id);

    @Nonnull
    CategoryEntity updateCategory(CategoryEntity category);

    @Nonnull
    Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description);

    void delete(SpendEntity spend);

    void deleteCategory(CategoryEntity category);
}
