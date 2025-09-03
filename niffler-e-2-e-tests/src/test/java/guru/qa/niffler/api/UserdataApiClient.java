package guru.qa.niffler.api;

import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.service.RestClient;
import guru.qa.niffler.service.UserdataClient;
import retrofit2.Response;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class UserdataApiClient extends RestClient implements UserdataClient {

  private final UserdataApi userdataApi;

  public UserdataApiClient() {
    super(CFG.userdataUrl());
    userdataApi = create(UserdataApi.class);
  }

  @Override
  @Nullable
  public UdUserJson currentUser(String username) {
    final Response<UdUserJson> response;
    try {
      response = userdataApi.currentUser(username).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Override
  @Nullable
  public List<UdUserJson> allUsersExceptCurrent(String username, String searchQuery) {
    final Response<List<UdUserJson>> response;
    try {
      response = userdataApi.allUsers(username, searchQuery).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

}
