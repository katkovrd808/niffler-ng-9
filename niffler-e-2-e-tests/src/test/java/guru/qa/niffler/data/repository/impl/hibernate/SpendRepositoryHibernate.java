package guru.qa.niffler.data.repository.impl.hibernate;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.model.CurrencyValues;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jpa.EntityManagers.em;

@ParametersAreNonnullByDefault
public class SpendRepositoryHibernate implements SpendRepository {

  private static final Config CFG = Config.getInstance();

  private final EntityManager entityManager = em(CFG.spendJdbcUrl());

  @Nonnull
  @Override
  public SpendEntity createSpend(SpendEntity spend) {
    entityManager.joinTransaction();
    entityManager.persist(spend);
    return spend;
  }

  @Nonnull
  @Override
  public SpendEntity update(SpendEntity spend) {
    entityManager.joinTransaction();
    entityManager.merge(spend);
    return spend;
  }

  @Nonnull
  @Override
  public CategoryEntity createCategory(CategoryEntity category) {
    entityManager.joinTransaction();
    entityManager.persist(category);
    return category;
  }

  @Nonnull
  @Override
  public Optional<CategoryEntity> findCategoryById(UUID id) {
    return Optional.ofNullable(
      entityManager.find(CategoryEntity.class, id)
    );
  }

  @Nonnull
  @Override
  public Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name) {
    try {
      return Optional.of(
        entityManager.createQuery("select c from CategoryEntity c where c.username =: username and c.name =: name", CategoryEntity.class)
          .setParameter("username", username)
          .setParameter("name", name)
          .getSingleResult()
      );
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @NotNull
  @Override
  public List<CategoryEntity> findCategoriesByUsername(String username, boolean excludeArchived) {
    return entityManager.createQuery("select c from CategoryEntity c where c.username =: username and archived =: excludeArchived", CategoryEntity.class)
      .setParameter("username", username)
      .setParameter("excludeArchived", excludeArchived)
      .getResultList();
  }

  @Nonnull
  @Override
  public Optional<SpendEntity> findById(UUID id) {
    return Optional.ofNullable(
      entityManager.find(SpendEntity.class, id)
    );
  }

  @NotNull
  @Override
  public List<SpendEntity> findAllByUsername(String username, CurrencyValues currency, Date from, Date to) {
    return entityManager.createQuery("" +
          "select s from SpendEntity s where s.username =: username and s.currency =: currency and s.spendDate between :from and :to",
        SpendEntity.class)
      .setParameter("username", username)
      .setParameter("currency", currency)
      .setParameter("from", from)
      .setParameter("to", to)
      .getResultList();
  }

  @NotNull
  @Override
  public List<SpendEntity> findAllByUsername(String username) {
    return entityManager.createQuery("" +
        "select s from SpendEntity s where s.username =: username", SpendEntity.class)
      .setParameter("username", username)
      .getResultList();
  }

  @Nonnull
  @Override
  public CategoryEntity updateCategory(CategoryEntity category) {
    entityManager.joinTransaction();
    entityManager.merge(category);
    return category;
  }

  @Override
  public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
    try {
      return Optional.of(
        entityManager.createQuery("select s from SpendEntity s where s.username =: username and s.description =: description", SpendEntity.class)
          .setParameter("username", username)
          .setParameter("description", description)
          .getSingleResult()
      );
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Override
  public void delete(SpendEntity spend) {
    entityManager.joinTransaction();
    entityManager.remove(spend);
  }

  @Override
  public void deleteCategory(CategoryEntity category) {
    entityManager.joinTransaction();
    entityManager.remove(category);
  }
}
