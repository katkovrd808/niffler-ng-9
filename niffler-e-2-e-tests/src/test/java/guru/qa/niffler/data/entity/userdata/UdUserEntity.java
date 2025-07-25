package guru.qa.niffler.data.entity.userdata;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.userdata.UdUserJson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Stream;

@Getter
@Setter
@Entity
@Table(name = "\"user\"")
public class UdUserEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
  private UUID id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private CurrencyValues currency;

  @Column()
  private String firstname;

  @Column()
  private String surname;

  @Column(name = "full_name")
  private String fullname;

  @Column(name = "photo", columnDefinition = "bytea")
  private byte[] photo;

  @Column(name = "photo_small", columnDefinition = "bytea")
  private byte[] photoSmall;

  @OneToMany(mappedBy = "requester", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<FriendshipEntity> friendshipRequests = new ArrayList<>();

  @OneToMany(mappedBy = "addressee", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<FriendshipEntity> friendshipAddressees = new ArrayList<>();

  public UdUserEntity(UUID id, String username, CurrencyValues currency, String firstname, String surname, String fullname, byte[] photo, byte[] photoSmall) {
    this.id = id;
    this.username = username;
    this.currency = currency;
    this.firstname = firstname;
    this.surname = surname;
    this.fullname = fullname;
    this.photo = photo;
    this.photoSmall = photoSmall;
  }

  public UdUserEntity(){
  }

  public void addFriends(FriendshipStatus status, UdUserEntity... friends) {
    List<FriendshipEntity> friendsEntities = Stream.of(friends)
        .map(f -> {
          FriendshipEntity fe = new FriendshipEntity();
          fe.setRequester(this);
          fe.setAddressee(f);
          fe.setStatus(status);
          fe.setCreatedDate(new Date());
          return fe;
        }).toList();
    this.friendshipRequests.addAll(friendsEntities);
  }

  public void addInvitations(UdUserEntity... invitations) {
    List<FriendshipEntity> invitationsEntities = Stream.of(invitations)
        .map(i -> {
          FriendshipEntity fe = new FriendshipEntity();
          fe.setRequester(i);
          fe.setAddressee(this);
          fe.setStatus(FriendshipStatus.PENDING);
          fe.setCreatedDate(new Date());
          return fe;
        }).toList();
    this.friendshipAddressees.addAll(invitationsEntities);
  }

  public void removeFriends(UdUserEntity... friends) {
    List<UUID> idsToBeRemoved = Arrays.stream(friends).map(UdUserEntity::getId).toList();
    for (Iterator<FriendshipEntity> i = getFriendshipRequests().iterator(); i.hasNext(); ) {
      FriendshipEntity friendsEntity = i.next();
      if (idsToBeRemoved.contains(friendsEntity.getAddressee().getId())) {
        friendsEntity.setAddressee(null);
        i.remove();
      }
    }
  }

  public void removeInvites(UdUserEntity... invitations) {
    List<UUID> idsToBeRemoved = Arrays.stream(invitations).map(UdUserEntity::getId).toList();
    for (Iterator<FriendshipEntity> i = getFriendshipAddressees().iterator(); i.hasNext(); ) {
      FriendshipEntity friendsEntity = i.next();
      if (idsToBeRemoved.contains(friendsEntity.getRequester().getId())) {
        friendsEntity.setRequester(null);
        i.remove();
      }
    }
  }

  public static UdUserEntity fromJson(UdUserJson json) {
    UdUserEntity ue = new UdUserEntity();
    ue.setId(json.id());
    ue.setUsername(json.username());
    ue.setCurrency(json.currency());
    ue.setFullname(json.fullname());
    ue.setFirstname(json.firstname());
    ue.setSurname(json.surname());
    ue.setPhoto(json.photo());
    ue.setPhotoSmall(json.photoSmall());
    return ue;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    UdUserEntity that = (UdUserEntity) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
  }
}