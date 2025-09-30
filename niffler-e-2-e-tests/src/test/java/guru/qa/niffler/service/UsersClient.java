package guru.qa.niffler.service;

import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.service.impl.UsersDbClient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface UsersClient {
    //ADD FACTORY LOGIC
    static UsersClient getInstance() {
        return new UsersDbClient();
    }

    @Nonnull
    UdUserJson create(String username, String password);

    @Nonnull
    Optional<UdUserJson> findById(UUID id);

    @Nonnull
    List<UdUserJson> findAll();

    @Nonnull
    Optional<UdUserJson> findByUsername(String username);

    @Nonnull
    UdUserJson update(UdUserJson user);

    @Nonnull
    List<UdUserJson> addInvitation(UdUserJson targetUser, int count);

    @Nonnull
    List<UdUserJson> addFriend(UdUserJson targetUser, int count);

    void delete(UdUserJson user);
}