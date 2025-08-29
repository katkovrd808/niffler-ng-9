package guru.qa.niffler.api;

import guru.qa.niffler.model.userdata.UdUserJson;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserdataApi {

  @GET("internal/users/current")
  Call<UdUserJson> currentUser(@Query("username") String username);

}
