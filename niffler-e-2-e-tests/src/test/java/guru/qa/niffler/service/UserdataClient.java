package guru.qa.niffler.service;

import guru.qa.niffler.model.userdata.UdUserJson;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public interface UserdataClient {

  @Nullable
  UdUserJson currentUser(String username);

  @Nullable
  List<UdUserJson> allUsersExceptCurrent(String username, String searchQuery);

}
