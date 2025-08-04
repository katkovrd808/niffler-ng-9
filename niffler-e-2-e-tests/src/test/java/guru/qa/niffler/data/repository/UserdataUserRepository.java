package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.userdata.UdUserEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface UserdataUserRepository {
  @Nonnull
  UdUserEntity create(UdUserEntity user);

  @Nonnull
  Optional<UdUserEntity> findById(UUID id);

  @Nonnull
  List<UdUserEntity> findAll();

  @Nonnull
  Optional<UdUserEntity> findByUsername(String username);

  @Nonnull
  UdUserEntity update(UdUserEntity user);

  void sendInvitation(UdUserEntity requester, UdUserEntity addressee);

  void addFriend(UdUserEntity requester, UdUserEntity addressee);

  void delete(UdUserEntity user);
}
