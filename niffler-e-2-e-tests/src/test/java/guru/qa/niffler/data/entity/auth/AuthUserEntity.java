package guru.qa.niffler.data.entity.auth;

import guru.qa.niffler.model.auth.AuthUserJson;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.FetchType.EAGER;

@Getter
@Setter
@Entity
@Table(name = "\"user\"")
public class AuthUserEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, columnDefinition = "UUID default gen_random_uuid()")
  private UUID id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private Boolean enabled;

  @Column(name = "account_non_expired", nullable = false)
  private Boolean accountNonExpired;

  @Column(name = "account_non_locked", nullable = false)
  private Boolean accountNonLocked;

  @Column(name = "credentials_non_expired", nullable = false)
  private Boolean credentialsNonExpired;

  @OneToMany(fetch = EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
  private List<AuthorityEntity> authorities = new ArrayList<>();

  public void addAuthorities(AuthorityEntity... authorities) {
    for (AuthorityEntity authority : authorities) {
      this.authorities.add(authority);
      authority.setUser(this);
    }
  }

  public void removeAuthority(AuthorityEntity authority) {
    this.authorities.remove(authority);
    authority.setUser(null);
  }

  public static AuthUserEntity fromJson(AuthUserJson json) {
    AuthUserEntity ue = new AuthUserEntity();
    ue.setId(json.id());
    ue.setUsername(json.username());
    ue.setPassword(json.password());
    ue.setEnabled(json.enabled());
    ue.setAccountNonLocked(json.accountNonLocked());
    ue.setAccountNonExpired(json.accountNonExpired());
    ue.setCredentialsNonExpired(json.credentialsNonExpired());
    return ue;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    AuthUserEntity that = (AuthUserEntity) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
  }
}
