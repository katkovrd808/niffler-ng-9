package guru.qa.niffler.test.api;

import guru.qa.niffler.service.impl.UserdataApiClient;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.service.UserdataClient;
import org.junit.jupiter.api.*;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ParametersAreNonnullByDefault
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsersApiTest {

  private final UserdataClient userdataClient = new UserdataApiClient();

  @User
  @Test
  @Order(1)
  @DisplayName("Users table should be empty before all tests execution")
  void usersListShouldBeEmptyBeforeTests(UdUserJson user) {
    List<UdUserJson> users = userdataClient.allUsersExceptCurrent(user.username(), null);
    assertTrue(users.isEmpty());
  }

  @User
  @Test
  @Order(Integer.MAX_VALUE)
  @DisplayName("Users table should be not empty after all tests execution")
  void usersListShouldNotBeEmptyAfterTests(UdUserJson user) {
    List<UdUserJson> users = userdataClient.allUsersExceptCurrent(user.username(), null);
    assertTrue(!users.isEmpty());
  }
}
