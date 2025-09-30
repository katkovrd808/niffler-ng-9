package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.service.UsersClient;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.StopWatch;
import org.jetbrains.annotations.NotNull;
import retrofit2.Response;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.naming.OperationNotSupportedException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class UsersApiClient extends RestClient implements UsersClient {

  private static final String DEFAULT_PASSWORD = "secret";

  private final AuthApi authApi;
  private final UserdataApiClient userdataApi;

  public UsersApiClient() {
    super(CFG.authUrl());
    this.authApi = create(AuthApi.class);
    userdataApi = new UserdataApiClient();
  }

  @Nonnull
  @Override
  public UdUserJson create(String username, String password) {
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
          return userJson.addTestData(new TestData(DEFAULT_PASSWORD));
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

  @SneakyThrows
  @Nonnull
  @Override
  public Optional<UdUserJson> findById(UUID id) {
    throw new OperationNotSupportedException("Operation FIND BY ID is not supported in current API version");
  }

  @SneakyThrows
  @Nonnull
  @Override
  public List<UdUserJson> findAll() {
    throw new OperationNotSupportedException("Operation FIND ALL is not supported in current API version");
  }

  @Nonnull
  @Override
  public Optional<UdUserJson> findByUsername(String username) {
    return Optional.ofNullable(userdataApi.currentUser(username));
  }

  @Nonnull
  @Override
  public UdUserJson update(UdUserJson user) {
    final Response<UdUserJson> response;
    try {
      response = authApi.update(user).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Nonnull
  @Override
  public List<UdUserJson> addInvitation(UdUserJson targetUser, int count) {
    final List<UdUserJson> result = new ArrayList<>();
    if (count > 0) {
      for (int i = 0; i < count; i++) {
        final UdUserJson newUser = create(randomUsername(), defaultPassword);
        result.add(newUser);
        UdUserJson invited = userdataApi.sendInvitation(
          newUser.username(),
          targetUser.username()
        );

        targetUser.testData()
          .outcomeInvitations()
          .add(invited);
      }
    }
    return result;
  }

  @Nonnull
  @Override
  public List<UdUserJson> addFriend(UdUserJson targetUser, int count) {
    final List<UdUserJson> result = new ArrayList<>();
    if (count > 0) {
      for (int i = 0; i < count; i++) {
        final UdUserJson newUser = create(randomUsername(), defaultPassword);
        result.add(newUser);
        userdataApi.sendInvitation(
          newUser.username(),
          targetUser.username()
        );
        UdUserJson friend = userdataApi.acceptInvitation(
          newUser.username(),
          targetUser.username()
          );

        targetUser.testData()
          .friends()
          .add(friend);
      }
    }
    return result;
  }

  @SneakyThrows
  @Override
  public void delete(UdUserJson user) {
    throw new OperationNotSupportedException("Operation DELETE is not supported in current API version");
  }
}
