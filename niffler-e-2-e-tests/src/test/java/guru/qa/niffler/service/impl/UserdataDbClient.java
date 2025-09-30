package guru.qa.niffler.service.impl;

import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.hibernate.UserdataUserRepositoryHibernate;
import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.service.UserdataClient;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.ACCEPTED;
import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.PENDING;

@ParametersAreNonnullByDefault
public class UserdataDbClient implements UserdataClient {

  private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryHibernate();

  @Nonnull
  @Override
  public UdUserJson currentUser(String username) {
    return userdataUserRepository.findByUsername(username)
      .map(user -> UdUserJson.fromEntity(user, null))
      .orElseGet(() -> new UdUserJson(username));
  }

  @Nonnull
  @Override
  public List<UdUserJson> allUsersExceptCurrent(String username, @Nullable String searchQuery) {
    return userdataUserRepository.findAll()
      .stream()
      .filter(u -> !u.getUsername().equals(username))
      .map(u -> UdUserJson.fromEntity(u, null))
      .collect(Collectors.toList());
  }

  @Nonnull
  @Override
  public UdUserJson sendInvitation(String username, String targetUsername) {
    throw new UnsupportedOperationException("Userdata DB is not supporting friendships operation yet. Use UsersDbClient!");
  }

  @Nonnull
  @Override
  public UdUserJson acceptInvitation(String username, String targetUsername) {
    throw new UnsupportedOperationException("Userdata DB is not supporting friendships operation yet. Use UsersDbClient!");
  }

  @Nonnull
  @Override
  public List<UdUserJson> findAllFriends(String username, @Nullable String searchQuery) {
    return userdataUserRepository.findFriends(username, searchQuery)
      .stream()
      .map(user -> UdUserJson.fromEntity(user, ACCEPTED))
      .collect(Collectors.toList());
  }

  @Nonnull
  @Override
  public List<UdUserJson> findAllFriends(String username) {
    return userdataUserRepository.findFriends(username)
      .stream()
      .map(user -> UdUserJson.fromEntity(user, ACCEPTED))
      .collect(Collectors.toList());
  }

  @Nonnull
  @Override
  public List<UdUserJson> findIncomeInvitations(String username) {
    return userdataUserRepository.findIncomeInvitations(username)
      .stream()
      .map(user -> UdUserJson.fromEntity(user, PENDING))
      .collect(Collectors.toList());
  }

  @Nonnull
  @Override
  public List<UdUserJson> findOutcomeInvitations(String username) {
    return userdataUserRepository.findOutcomeInvitations(username)
      .stream()
      .map(user -> UdUserJson.fromEntity(user, PENDING))
      .collect(Collectors.toList());
  }
}
