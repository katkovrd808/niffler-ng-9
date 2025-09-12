package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.service.SpendClient;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class SpendApiClient extends RestClient implements SpendClient {

  private final SpendApi spendApi;

  public SpendApiClient() {
    super(CFG.spendUrl());
    this.spendApi = create(SpendApi.class);
  }

  @Nonnull
  public SpendJson createSpend(SpendJson spendJson) {
    final Response<SpendJson> response;
    try {
      response = spendApi.addSpending(spendJson)
        .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(201, response.code());
    return response.body();
  }

  @Nonnull
  public SpendJson update(SpendJson spendJson) {
    final Response<SpendJson> response;
    try {
      response = spendApi.editSpending(spendJson)
        .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Nonnull
  public Optional<SpendJson> findById(UUID id) {
    final Response<SpendJson> response;
    try {
      response = spendApi.getSpendingById(id)
        .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    if (response.body() == null) {
      return Optional.empty();
    } else {
      return Optional.of(response.body());
    }
  }

  @Nonnull
  public final Optional<SpendJson> findByUsernameAndSpendDescription(String username, String description) {
    throw new UnsupportedOperationException("Unsupported action with Spend API");
  }

  public CategoryJson editCategory(CategoryJson category) {
    final Response<CategoryJson> response;
    try {
      response = spendApi.editCategory(category)
        .execute();
      assertEquals(200, response.code());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return response.body();
  }

  public final void delete(SpendJson spend) {
    final Response<Void> response;
    try {
      response = spendApi.deleteSpendings(Stream.of(spend.id())
          .toList())
        .execute();
      assertEquals(200, response.code());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public final void deleteCategory(CategoryJson category) {
    throw new UnsupportedOperationException("Unsupported action with Spend API");
  }

  @Nonnull
  public CategoryJson createCategory(CategoryJson categoryJson) {
    final Response<CategoryJson> response;
    try {
      response = spendApi.addCategory(categoryJson)
        .execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Nonnull
  public Optional<CategoryJson> findCategoryById(UUID id) {
    final Response<List<CategoryJson>> response;
    try {
      response = spendApi.getCategoriesList()
        .execute();
      assertEquals(200, response.code());
      List<CategoryJson> categories = response.body();
      for (CategoryJson category : categories) {
        if (category.id().equals(id)) {
          return Optional.of(category);
        }
      }
      return Optional.empty();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public final Optional<CategoryJson> findCategoryByUsernameAndSpendName(String username, String name) {
    throw new UnsupportedOperationException("Unsupported action with Spend API");
  }
}
