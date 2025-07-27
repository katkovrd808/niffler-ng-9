package guru.qa.niffler.test;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.model.auth.AuthUserJson;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.service.AuthDbClient;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;

import java.util.*;

import static guru.qa.niffler.model.CurrencyValues.RUB;
import static guru.qa.niffler.model.CurrencyValues.USD;
import static guru.qa.niffler.utils.RandomDataUtils.*;

public class JdbcTest {

    private final SpendDbClient spendDbClient = new SpendDbClient();
    private final AuthDbClient authDbClient = new AuthDbClient();
    private final UsersDbClient usersDbClient = new UsersDbClient();

    @Test
    void authRepositoryShouldReturnAllUsersWithAuthorities() {
        List<AuthUserEntity> users = authDbClient.findAll();
        System.out.println(users);
    }

    @Test
    void userShouldBeCreatedWithAuthorities() {
        AuthUserJson json = new AuthUserJson(
                null,
                "test934597865877621548124",
                "12345",
                true,
                true,
                true,
                true
        );
        AuthUserJson ue = authDbClient.createUser(json);
        System.out.println(ue);
    }

    @Test
    void udUserShouldBeCreated() {
        UdUserJson user = usersDbClient.createUser("test2286", "12345");
        System.out.println(user);
    }

    @Test
    void findByIdShouldReturnUserWithAllAuthorities() {
        AuthUserJson json = new AuthUserJson(
                UUID.fromString("c9cb463e-8b90-402b-9240-5b4f39c7ffec"),
                "test93",
                "12345",
                true,
                true,
                true,
                true
        );
        Optional<AuthUserJson> ue = authDbClient.findById(json);
        System.out.println(ue);
    }

    @Test
    void findAllShouldReturnAllUsersWithAllAuthorities() {
        List<AuthUserEntity> ue = authDbClient.findAll();
        System.out.println(ue);
    }

    @Test
    void findAllShouldReturnAllAuthoritiesSpring() {
        List<AuthorityEntity> ae = authDbClient.findAllAuthorities();
        System.out.println(ae);
    }

    @Test
    void findByUserIdShouldReturnListOfUserAuthorities() {
        List<AuthorityEntity> ae = authDbClient.findAuthoritiesByUserId(UUID.fromString("c9cb463e-8b90-402b-9240-5b4f39c7ffec"));
        System.out.println(ae);
    }

    @Test
    void udFindByIdMethodShouldReturnAllFriendships() {
        Optional<UdUserJson> ue = usersDbClient.findById(UUID.fromString("1579929d-e4c0-4fd7-9870-684e5f426535"));
        System.out.println(ue);
    }

    @Test
    void udFindByUsernameMethodShouldReturnAllFriendships() {
        Optional<UdUserEntity> ue = usersDbClient.findByUsername("test1");
        System.out.println(ue);
    }

    @Test
    void udFindAllMethodShouldReturnAllUsersWithFriendships() {
        List<UdUserEntity> ue = usersDbClient.findAll();
        System.out.println(ue);
    }

    @Test
    void incomeFriendshipShouldBeCreatedSpring() {
        UdUserJson requester = new UdUserJson(
                UUID.fromString("9828191e-2e48-4b3c-a372-c782601e2476"),
                "boris123",
                RUB,
                randomName() + randomSurname(),
                randomName(),
                randomSurname(),
                new byte[]{},
                new byte[]{}
        );
        usersDbClient.addInvitation(requester, 1);
    }

    @Test
    void spendShouldBeCreated() {
        SpendJson spend = spendDbClient.createSpend(new SpendJson(
                null,
                new Date(System.currentTimeMillis()),
                new CategoryJson(
                        null,
                        "Test category 12345124214",
                        "test1",
                        false
                ),
                RUB,
                100.00,
                "Test desc",
                "test1"
        ));
        System.out.println(spend);
    }

    @Test
    void categoryCreationJdbcTest() {
        CategoryJson json = spendDbClient.createCategory(
                new CategoryJson(
                        null,
                        "Test category 12089071241241249",
                        "test1",
                        false
                )
        );
        System.out.println(json);
    }

    @Test
    void categoryShouldBeUpdated() {
        SpendJson spend = spendDbClient.updateSpend(
                new SpendJson(
                        UUID.fromString("4cad4d0d-afdc-4e2c-9823-116808ed773c"),
                        new Date(),
                        new CategoryJson(
                                UUID.fromString("eee8f23d-e57d-4e27-b0fb-47802a996e32"),
                                "Test category 12089071241241249",
                                "test1",
                                false
                        ),
                        USD,
                        100.00,
                        "test",
                        "test1"));
        System.out.println(spend);
    }
}
