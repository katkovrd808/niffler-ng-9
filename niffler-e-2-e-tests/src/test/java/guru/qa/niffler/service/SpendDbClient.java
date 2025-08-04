package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.hibernate.SpendRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class SpendDbClient implements SpendClient{
    private static final Config CFG = Config.getInstance();

    private final SpendRepository spendRepository = new SpendRepositoryHibernate();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    @Override
    public SpendJson createSpend(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        return xaTransactionTemplate.execute(() -> {
                    return SpendJson.fromEntity(spendRepository.createSpend(spendEntity));
                }
        );
    }

    @Override
    public SpendJson update(SpendJson spend) {
        return xaTransactionTemplate.execute(() -> {
                    SpendEntity se = SpendEntity.fromJson(spend);
                    return SpendJson.fromEntity(
                            spendRepository.update(se)
                    );
                }
        );
    }

    @Override
    public CategoryJson createCategory(CategoryJson category) {
        CategoryEntity ce = CategoryEntity.fromJson(category);
        return xaTransactionTemplate.execute(() -> {
                    return CategoryJson.fromEntity(spendRepository.createCategory(ce));
                }
        );
    }

    @Override
    public Optional<CategoryJson> findCategoryById(UUID id) {
        return spendRepository.findCategoryById(id)
                .map(CategoryJson::fromEntity);
    }

    @Override
    public Optional<CategoryJson> findCategoryByUsernameAndSpendName(String username, String name) {
        return spendRepository.findCategoryByUsernameAndSpendName(username, name)
                .map(CategoryJson::fromEntity);
    }

    @Override
    public Optional<SpendJson> findById(UUID id) {
        return spendRepository.findById(id)
                .map(SpendJson::fromEntity);
    }

    @Override
    public Optional<SpendJson> findByUsernameAndSpendDescription(String username, String description) {
        return spendRepository.findByUsernameAndSpendDescription(username, description)
                .map(SpendJson::fromEntity);
    }

    @Override
    public CategoryJson editCategory(CategoryJson category) {
        CategoryEntity ce = CategoryEntity.fromJson(category);
        return CategoryJson.fromEntity(spendRepository.updateCategory(ce));
    }

    @Override
    public void delete(SpendJson spend) {
        xaTransactionTemplate.execute(() -> {
                    SpendEntity se = SpendEntity.fromJson(spend);
                    spendRepository.delete(se);
                    return null;
                }
        );
    }

    @Override
    public @Nonnull void deleteCategory(CategoryJson category) {
        xaTransactionTemplate.execute(() -> {
                    CategoryEntity ce = CategoryEntity.fromJson(category);
                    spendRepository.deleteCategory(ce);
                    return null;
                }
        );
    }
}