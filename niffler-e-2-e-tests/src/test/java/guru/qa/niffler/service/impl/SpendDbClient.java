package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.hibernate.SpendRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.service.SpendClient;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
public class SpendDbClient implements SpendClient {
  private static final Config CFG = Config.getInstance();

  private final SpendRepository spendRepository = new SpendRepositoryHibernate();

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
    CFG.spendJdbcUrl()
  );

  @Nonnull
  @Override
  public SpendJson createSpend(SpendJson spend) {
    SpendEntity spendEntity = SpendEntity.fromJson(spend);
    return xaTransactionTemplate.execute(() -> {
        return SpendJson.fromEntity(spendRepository.createSpend(spendEntity));
      }
    );
  }

  @Nonnull
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

  @Nonnull
  @Override
  public CategoryJson createCategory(CategoryJson category) {
    CategoryEntity ce = CategoryEntity.fromJson(category);
    return xaTransactionTemplate.execute(() -> {
        return CategoryJson.fromEntity(spendRepository.createCategory(ce));
      }
    );
  }

  @Nonnull
  @Override
  public Optional<CategoryJson> findCategoryByUsernameAndId(String username, UUID id) {
    return spendRepository.findCategoryById(id)
      .map(CategoryJson::fromEntity);
  }

  @Nonnull
  @NotNull
  @Override
  public List<CategoryJson> findCategories(String username, @Nullable Boolean excludeArchived) {
    return spendRepository.findCategoriesByUsername(username, excludeArchived)
      .stream()
      .map(CategoryJson::fromEntity)
      .collect(Collectors.toList());
  }

  @Nonnull
  @Override
  public Optional<CategoryJson> findCategoryByUsernameAndSpendName(String username, String name) {
    return spendRepository.findCategoryByUsernameAndSpendName(username, name)
      .map(CategoryJson::fromEntity);
  }

  @Nonnull
  @Override
  public Optional<SpendJson> findById(UUID id) {
    return spendRepository.findById(id)
      .map(SpendJson::fromEntity);
  }

  @Nonnull
  @Override
  public Optional<SpendJson> findByUsernameAndSpendDescription(String username, String description) {
    return spendRepository.findByUsernameAndSpendDescription(username, description)
      .map(SpendJson::fromEntity);
  }

  @Nonnull
  @Override
  public List<SpendJson> findSpendings(String username, CurrencyValues currency, Date from, Date to) {
    return spendRepository.findAllByUsername(username, currency, from, to)
      .stream()
      .map(SpendJson::fromEntity)
      .collect(Collectors.toList());
  }

  @Nonnull
  @Override
  public List<SpendJson> findSpendings(String username) {
    return spendRepository.findAllByUsername(username)
      .stream()
      .map(SpendJson::fromEntity)
      .collect(Collectors.toList());
  }

  @Nonnull
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
  public void deleteCategory(CategoryJson category) {
    xaTransactionTemplate.execute(() -> {
        CategoryEntity ce = CategoryEntity.fromJson(category);
        spendRepository.deleteCategory(ce);
        return null;
      }
    );
  }
}