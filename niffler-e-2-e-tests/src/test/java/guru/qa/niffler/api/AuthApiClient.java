package guru.qa.niffler.api;

import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.service.AuthClient;
import guru.qa.niffler.service.RestClient;
import org.apache.commons.lang3.time.StopWatch;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@ParametersAreNonnullByDefault
public class AuthApiClient extends RestClient implements AuthClient {

  private final AuthApi authApi;
  private final UserdataApiClient userdataApi;

  public AuthApiClient() {
    super(CFG.authUrl());
    this.authApi = create(AuthApi.class);
    userdataApi = new UserdataApiClient();
  }

  @Override
  @Nonnull
  public UdUserJson registerUser(String username, String password) {
    try {
      authApi.requestRegisterForm().execute();
      authApi.register(
        username,
        password,
        password,
        ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
      ).execute();

      final StopWatch sw = StopWatch.createStarted();

      while (sw.getTime(TimeUnit.SECONDS) < 30) {
        UdUserJson userJson = userdataApi.currentUser(username);
        if (userJson != null && userJson.id() != null) {
          return userJson;
        } else {
          try {
            Thread.sleep(100);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      }
      throw new AssertionError("Timed out waiting for register");
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }
}
