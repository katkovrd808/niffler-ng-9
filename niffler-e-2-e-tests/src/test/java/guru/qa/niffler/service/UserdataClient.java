package guru.qa.niffler.service;

import guru.qa.niffler.model.userdata.UdUserJson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface UserdataClient {

  @Nonnull
  UdUserJson currentUser(String username);

  @Nonnull
  List<UdUserJson> allUsersExceptCurrent(String username, @Nullable String searchQuery);

  @Nonnull
  UdUserJson sendInvitation(String username, String targetUsername);

  @Nullable
  UdUserJson acceptInvitation(String username, String targetUsername);
}
