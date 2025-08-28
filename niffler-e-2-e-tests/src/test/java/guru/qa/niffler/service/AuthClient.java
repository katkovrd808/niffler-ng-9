package guru.qa.niffler.service;

import guru.qa.niffler.model.userdata.UdUserJson;

import javax.annotation.Nullable;

public interface AuthClient {

  @Nullable
  UdUserJson registerUser(String username, String password);

}
