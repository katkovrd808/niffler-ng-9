package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.repository.AuthAuthorityRepository;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.impl.hibernate.AuthAuthorityRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.hibernate.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.auth.AuthUserJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
public class AuthDbClient {
    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
    private final AuthAuthorityRepository authorityRepository = new AuthAuthorityRepositoryHibernate();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    public AuthUserJson createUser(AuthUserJson json) {
        AuthUserEntity userEntity = AuthUserEntity.fromJson(json);
        return xaTransactionTemplate.execute(() -> {
                    List<AuthorityEntity> authorities = Arrays.stream(Authority.values())
                            .map(authority -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setAuthority(authority);
                                ae.setUser(userEntity);
                                return ae;
                            })
                            .collect(Collectors.toList());
                    userEntity.setAuthorities(authorities);
                    authUserRepository.create(userEntity);

                    return AuthUserJson.fromEntity(userEntity);
                }
        );
    }

    public Optional<AuthUserJson> findById(AuthUserJson json) {
        Optional<AuthUserEntity> ue = authUserRepository.findById(json.id());
        return ue.map(AuthUserJson::fromEntity);
    }

    public Optional<AuthUserJson> findByUsername(AuthUserJson json) {
        Optional<AuthUserEntity> ue = authUserRepository.findByUsername(json.username());
        return ue.map(AuthUserJson::fromEntity);
    }

    public List<AuthUserEntity> findAll() {
        return authUserRepository.findAll();
    }

    public List<AuthorityEntity> findAllAuthorities() {
        return authorityRepository.findAll();
    }

    public List<AuthorityEntity> findAuthoritiesByUserId(UUID id) {
        return authorityRepository.findByUserId(id);
    }

    public void deleteUser(AuthUserJson json) {
        AuthUserEntity ue = AuthUserEntity.fromJson(json);
        authUserRepository.delete(ue);

    }
}