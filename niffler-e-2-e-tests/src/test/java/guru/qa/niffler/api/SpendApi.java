package guru.qa.niffler.api;

import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.spend.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface SpendApi {

  @POST("internal/spends/add")
  Call<SpendJson> addSpending(@Body SpendJson spending);

  @PATCH("internal/spends/edit")
  Call<SpendJson> editSpending(@Body SpendJson spending);

  @GET("internal/spends/{id}")
  Call<SpendJson> getSpendingById(@Path("id") UUID spendingId);

  @GET("internal/spends/all")
  Call<List<SpendJson>> getSpendingList(@Query("username") String username,
                                        @Query("currencyValues") CurrencyValues currencyValues,
                                        @Query("from") Date from,
                                        @Query("to") Date to);

  @GET("internal/spends/all")
  Call<List<SpendJson>> getSpendingList(@Query("username") String username);

  @DELETE("internal/spends/remove")
  Call<Void> deleteSpendings(@Query("ids") List<UUID> ids);

  @POST("internal/categories/add")
  Call<CategoryJson> addCategory(@Body CategoryJson category);

  @PATCH("internal/categories/update")
  Call<CategoryJson> editCategory(@Body CategoryJson category);

  @GET("/internal/categories/all")
  Call<List<CategoryJson>> getCategoriesList(@Query("username") String username,
                                             @Query("excludeArchived") Boolean excludeArchived);
}
