package guru.qa.niffler.data.repository.impl.hibernate;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.ACCEPTED;
import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.PENDING;
import static guru.qa.niffler.data.jpa.EntityManagers.em;

@ParametersAreNonnullByDefault
public class UserdataUserRepositoryHibernate implements UserdataUserRepository {

  private static final Config CFG = Config.getInstance();

  private final EntityManager entityManager = em(CFG.userdataJdbcUrl());

  @Nonnull
  @Override
  public UdUserEntity create(UdUserEntity user) {
    entityManager.joinTransaction();
    entityManager.persist(user);
    return user;
  }

  @Nonnull
  @Override
  public Optional<UdUserEntity> findById(UUID id) {
    return Optional.ofNullable(
      entityManager.find(UdUserEntity.class, id)
    );
  }

  @Nonnull
  @Override
  public List<UdUserEntity> findAll() {
    return entityManager.createQuery("select u from UdUserEntity u", UdUserEntity.class)
      .getResultList();
  }

  @Nonnull
  @Override
  public Optional<UdUserEntity> findByUsername(String username) {
    try {
      return Optional.of(
        entityManager.createQuery("select u from UdUserEntity u where u.username =: username", UdUserEntity.class)
          .setParameter("username", username)
          .getSingleResult()
      );
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  @Nonnull
  @Override
  public UdUserEntity update(UdUserEntity user) {
    entityManager.joinTransaction();
    return entityManager.merge(user);
  }

  @Nonnull
  @Override
  public List<UdUserEntity> findFriends(String username) {
    return findByUsername(username).map(current -> {
        List<UdUserEntity> friendsAsRequesters = entityManager.createQuery(
            """
              select u
              from UdUserEntity u
              join u.friendshipRequests f
              where f.addressee.id = :addressee
              and f.status = :status
              """,
            UdUserEntity.class
          )
          .setParameter("addressee", current.getId())
          .setParameter("status", ACCEPTED)
          .getResultList();

        List<UdUserEntity> friendsAsAddressees = entityManager.createQuery(
            """
              select u
              from UdUserEntity u
              join u.friendshipAddressees f
              where f.requester.id = :requester
              and f.status = :status
              """,
            UdUserEntity.class
          )
          .setParameter("requester", current.getId())
          .setParameter("status", ACCEPTED)
          .getResultList();

        Set<UdUserEntity> allFriends = new LinkedHashSet<>();
        allFriends.addAll(friendsAsRequesters);
        allFriends.addAll(friendsAsAddressees);

        return new ArrayList<>(allFriends);
      }
    ).orElseGet(ArrayList::new);
  }

  /*SELECT u.*
  FROM "user" u
  JOIN friendship f
  ON u.id = f.addressee_id
  WHERE f.requester_id = ?
  AND f.status = ?
  UNION
  SELECT u.*
  FROM "user" u
  JOIN friendship f
  ON u.id = f.requester_id
  WHERE f.addressee_id = ?
  AND f.status = ?*/

  @Nonnull
  @Override
  public List<UdUserEntity> findFriends(String username, String searchQuery) {
    throw new UnsupportedOperationException("SearchQuery param is not applicable for DB requests!");
  }

  @Nonnull
  @Override
  public List<UdUserEntity> findIncomeInvitations(String username) {
    return findByUsername(username)
      .map(current ->
        entityManager.createQuery(
            """
              select u
              from UdUserEntity u
              join u.friendshipRequests f
              where f.addressee.id = :addressee
              and f.status = :status
              """,
            UdUserEntity.class
          )
          .setParameter("addressee", current.getId())
          .setParameter("status", PENDING)
          .getResultList()
      ).orElseGet(ArrayList::new);
  }

  @Nonnull
  @Override
  public List<UdUserEntity> findOutcomeInvitations(String username) {
    return findByUsername(username)
      .map(current ->
        entityManager.createQuery(
            """
              select u
              from UdUserEntity u
              join u.friendshipAddressees f
              where f.requester.id = :requester
              and f.status = :status
              """,
            UdUserEntity.class
          )
          .setParameter("requester", current.getId())
          .setParameter("status", PENDING)
          .getResultList()
      ).orElseGet(ArrayList::new);
  }

  @Override
  public void sendInvitation(UdUserEntity requester, UdUserEntity addressee) {
    entityManager.joinTransaction();
    addressee.addFriends(PENDING, requester);
  }

  @Override
  public void addFriend(UdUserEntity requester, UdUserEntity addressee) {
    entityManager.joinTransaction();
    requester.addFriends(ACCEPTED, addressee);
    addressee.addFriends(ACCEPTED, requester);
  }

  @Override
  public void delete(UdUserEntity user) {
    entityManager.joinTransaction();
    entityManager.remove(user);
  }
}