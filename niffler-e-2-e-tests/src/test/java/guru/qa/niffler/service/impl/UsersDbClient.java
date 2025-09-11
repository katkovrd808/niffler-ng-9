package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.hibernate.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.hibernate.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.service.UsersClient;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@ParametersAreNonnullByDefault
public class UsersDbClient implements UsersClient {

  private static final Config CFG = Config.getInstance();
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
  private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryHibernate();

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
    CFG.authJdbcUrl(),
    CFG.userdataJdbcUrl()
  );

  @Override
  public UdUserJson create(String username, String password) {
    return xaTransactionTemplate.execute(() -> {
        AuthUserEntity authUser = authUserEntity(username, password);
        authUserRepository.create(authUser);
        return UdUserJson.fromEntity(
          userdataUserRepository.create(userEntity(username)),
          null
        );
      }
    );
  }

  @Override
  public Optional<UdUserJson> findById(UUID id) {
    return userdataUserRepository.findById(id)
      .map(user -> UdUserJson.fromEntity(user, null));
  }

  @Override
  public List<UdUserJson> findAll() {
    List<UdUserEntity> userEntities = userdataUserRepository.findAll();
    return userEntities.stream().map(
        user -> UdUserJson.fromEntity(user, null))
      .toList();
  }

  @Override
  public Optional<UdUserJson> findByUsername(String username) {
    return userdataUserRepository.findByUsername(username)
      .map(user -> UdUserJson.fromEntity(user, null));
  }

  @Override
  public UdUserJson update(UdUserJson user) {
    UdUserEntity ue = UdUserEntity.fromJson(user);
    return UdUserJson.fromEntity(userdataUserRepository.update(ue), null);
  }

  @Override
  public List<UdUserJson> addInvitation(UdUserJson targetUser, int count) {
    final List<UdUserJson> result = new ArrayList<>();
    if (count > 0) {
      UdUserEntity targetEntity = userdataUserRepository.findById(
        targetUser.id()
      ).orElseThrow();

      for (int i = 0; i < count; i++) {
        xaTransactionTemplate.execute(() -> {
            String username = randomUsername();
            AuthUserEntity authUser = authUserEntity(username, "12345");
            authUserRepository.create(authUser);
            UdUserEntity adressee = userdataUserRepository.create(userEntity(username));
            userdataUserRepository.sendInvitation(targetEntity, adressee);
            result.add(UdUserJson.fromEntity(
              adressee,
              FriendshipStatus.PENDING
            ));
            return null;
          }
        );
      }
    }
    return result;
  }

  @Override
  public List<UdUserJson> addFriend(UdUserJson targetUser, int count) {
    final List<UdUserJson> result = new ArrayList<>();
    if (count > 0) {
      UdUserEntity targetEntity = userdataUserRepository.findById(
        targetUser.id()
      ).orElseThrow();
      for (int i = 0; i < count; i++) {
        xaTransactionTemplate.execute(() -> {
            String username = randomUsername();
            AuthUserEntity authUser = authUserEntity(username, "12345");
            authUserRepository.create(authUser);
            UdUserEntity adressee = userdataUserRepository.create(userEntity(username));
            userdataUserRepository.addFriend(adressee, targetEntity);
            result.add(UdUserJson.fromEntity(
              adressee,
              FriendshipStatus.ACCEPTED
            ));
            return null;
          }
        );
      }
    }
    return result;
  }

  @Override
  public void delete(UdUserJson user) {
    xaTransactionTemplate.execute(() -> {
        userdataUserRepository.delete(
          UdUserEntity.fromJson(user));
      }
    );
  }

  private UdUserEntity userEntity(String username) {
    UdUserEntity ue = new UdUserEntity();
    ue.setUsername(username);
    ue.setCurrency(CurrencyValues.RUB);
    return ue;
  }

  private AuthUserEntity authUserEntity(String username, String password) {
    AuthUserEntity authUser = new AuthUserEntity();
    authUser.setUsername(username);
    authUser.setPassword(pe.encode(password));
    authUser.setEnabled(true);
    authUser.setAccountNonExpired(true);
    authUser.setAccountNonLocked(true);
    authUser.setCredentialsNonExpired(true);
    authUser.setAuthorities(
      Arrays.stream(Authority.values()).map(
        e -> {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setUser(authUser);
          ae.setAuthority(e);
          return ae;
        }
      ).toList()
    );
    return authUser;
  }
}