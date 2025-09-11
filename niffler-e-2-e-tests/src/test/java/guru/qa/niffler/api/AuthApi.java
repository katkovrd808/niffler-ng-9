package guru.qa.niffler.api;

import guru.qa.niffler.model.userdata.UdUserJson;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApi {

  @GET("/register")
  Call<ResponseBody> requestRegisterForm();

  @POST("/register")
  Call<ResponseBody> register(@Query("username") String username,
                              @Query("password") String password,
                              @Query("passwordSubmit") String passwordSubmit,
                              @Query("_csrf") String token);

  @POST("/update")
  Call<UdUserJson> update(@Body UdUserJson user);
}
