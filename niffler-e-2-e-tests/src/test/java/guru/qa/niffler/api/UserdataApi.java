package guru.qa.niffler.api;

import guru.qa.niffler.model.userdata.UdUserJson;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface UserdataApi {

  @GET("internal/users/current")
  Call<UdUserJson> currentUser(@Query("username") String username);

  @GET("internal/users/all")
  Call<List<UdUserJson>> allUsers(@Query("username") String username,
                                  @Nullable @Query("searchQuery") String searchQuery);

}
