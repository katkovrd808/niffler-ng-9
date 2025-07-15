package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.util.Optional;
import java.util.UUID;

public interface AuthAuthorityDao {
  void create(AuthorityEntity... authority);

  Optional<AuthorityEntity> findById(UUID id);

  Optional<AuthorityEntity> findByUserId(UUID userId);

  void delete(AuthorityEntity authority);
}
