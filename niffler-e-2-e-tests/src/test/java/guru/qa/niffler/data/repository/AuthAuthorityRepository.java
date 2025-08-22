package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface AuthAuthorityRepository {

  @Nonnull
  void create(AuthorityEntity... authorities);

  @Nonnull
  List<AuthorityEntity> findByUserId(UUID userId);

  @Nonnull
  List<AuthorityEntity> findAll();

  void delete(AuthorityEntity authority);
}
