package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.UserdataApi;
import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.service.UserdataClient;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class UserdataApiClient extends RestClient implements UserdataClient {

  private final UserdataApi userdataApi;

  public UserdataApiClient() {
    super(CFG.userdataUrl());
    userdataApi = create(UserdataApi.class);
  }

  @Override
  @Nonnull
  public UdUserJson currentUser(String username) {
    final Response<UdUserJson> response;
    try {
      response = userdataApi.currentUser(username)
        .execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Override
  @Nonnull
  public List<UdUserJson> allUsersExceptCurrent(String username, @Nullable String searchQuery) {
    final Response<List<UdUserJson>> response;
    try {
      response = userdataApi.allUsers(username, searchQuery)
        .execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Override
  @Nonnull
  public UdUserJson sendInvitation(String username, String targetUsername) {
    final Response<UdUserJson> response;
    try {
      response = userdataApi.sendInvitation(username, targetUsername)
        .execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Override
  @Nonnull
  public UdUserJson acceptInvitation(String username, String targetUsername) {
    final Response<UdUserJson> response;
    try {
      response = userdataApi.acceptFriendship(username, targetUsername)
        .execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Nonnull
  @Override
  public List<UdUserJson> findAllFriends(String username, @Nullable String searchQuery) {
    final Response<List<UdUserJson>> response;
    try {
      response = userdataApi.findFriends(username, searchQuery)
        .execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  @Nonnull
  @Override
  public List<UdUserJson> findAllFriends(String username) {
    final Response<List<UdUserJson>> response;
    try {
      response = userdataApi.findFriends(username)
        .execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals(200, response.code());
    List<UdUserJson> users = response.body();
    if (users != null && !users.isEmpty()) {
      return users.stream()
        .filter(
          u -> Objects.equals(u.friendshipStatus(), FRIEND)
        ).collect(Collectors.toList());
    }
    return new ArrayList<>();
  }

  @Nonnull
  @Override
  public List<UdUserJson> findIncomeInvitations(String username) {
    final Response<List<UdUserJson>> response;
    try {
      response = userdataApi.findFriends(username)
        .execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals(200, response.code());
    List<UdUserJson> users = response.body();
    if (users != null && !users.isEmpty()) {
      return users.stream()
        .filter(
          u -> Objects.equals(u.friendshipStatus(), INVITE_RECEIVED)
        ).collect(Collectors.toList());
    }
    return new ArrayList<>();
  }

  @Nonnull
  @Override
  public List<UdUserJson> findOutcomeInvitations(String username) {
    final Response<List<UdUserJson>> response;
    try {
      response = userdataApi.allUsers(username)
        .execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals(200, response.code());
    List<UdUserJson> users = response.body();
    if (users != null && !users.isEmpty()) {
      return users.stream()
        .filter(
          u -> Objects.equals(u.friendshipStatus(), INVITE_SENT)
        ).toList();
    }
    return new ArrayList<>();
  }

  @Nonnull
  public List<UdUserJson> findOutcomeInvitations(String username, @Nullable String searchQuery) {
    final Response<List<UdUserJson>> response;
    try {
      response = userdataApi.allUsers(username, searchQuery)
        .execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    assertEquals(200, response.code());
    List<UdUserJson> users = response.body();
    if (users != null && !users.isEmpty()) {
      return users.stream()
        .filter(
          u -> Objects.equals(u.friendshipStatus(), INVITE_SENT)
        ).toList();
    }
    return new ArrayList<>();
  }
}
