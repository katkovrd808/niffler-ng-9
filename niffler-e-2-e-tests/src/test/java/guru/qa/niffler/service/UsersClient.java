package guru.qa.niffler.service;

import guru.qa.niffler.model.userdata.UdUserJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersClient {
    UdUserJson create(String username, String password);

    Optional<UdUserJson> findById(UUID id);

    List<UdUserJson> findAll();

    Optional<UdUserJson> findByUsername(String username);

    UdUserJson update(UdUserJson user);

    List<UdUserJson> addInvitation(UdUserJson targetUser, int count);

    List<UdUserJson> addFriend(UdUserJson targetUser, int count);

    void delete(UdUserJson user);
}
