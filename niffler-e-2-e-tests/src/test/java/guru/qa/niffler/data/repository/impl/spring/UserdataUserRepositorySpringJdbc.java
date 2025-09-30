package guru.qa.niffler.data.repository.impl.spring;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.data.jdbc.DataSources;
import guru.qa.niffler.data.mapper.userdata.UserdataUserEntityMapRowMapper;
import guru.qa.niffler.data.mapper.userdata.UserdataUserEntityRowMapper;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.ACCEPTED;
import static guru.qa.niffler.data.entity.userdata.FriendshipStatus.PENDING;

@ParametersAreNonnullByDefault
public class UserdataUserRepositorySpringJdbc implements UserdataUserRepository {

  private static final Config CFG = Config.getInstance();

  @Nonnull
  @Override
  public UdUserEntity create(UdUserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    KeyHolder kh = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
        "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
          "VALUES (?,?,?,?,?,?,?)",
        Statement.RETURN_GENERATED_KEYS
      );
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getCurrency().name());
      ps.setString(3, user.getFirstname());
      ps.setString(4, user.getSurname());
      ps.setBytes(5, user.getPhoto());
      ps.setBytes(6, user.getPhotoSmall());
      ps.setString(7, user.getFullname());
      return ps;
    }, kh);

    final UUID generatedKey = (UUID) kh.getKeys().get("id");
    user.setId(generatedKey);
    return user;
  }

  @Nonnull
  @Override
  public Optional<UdUserEntity> findById(UUID id) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    return Optional.ofNullable(
      jdbcTemplate.queryForObject(
        "SELECT * FROM \"user\" WHERE id = ?",
        UserdataUserEntityRowMapper.instance,
        id
      )
    );
  }

  @Nonnull
  @Override
  public List<UdUserEntity> findAll() {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    return jdbcTemplate.queryForList(
        "SELECT * FROM \"user\""
      ).stream()
      .map(UserdataUserEntityMapRowMapper.instance::mapRow)
      .collect(Collectors.toList());
  }

  @Nonnull
  @Override
  public Optional<UdUserEntity> findByUsername(String username) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    return Optional.ofNullable(
      jdbcTemplate.queryForObject(
        "SELECT * FROM \"user\" WHERE username = ?",
        UserdataUserEntityRowMapper.instance,
        username
      )
    );
  }

  @Override
  public void delete(UdUserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    jdbcTemplate.update(
      "DELETE FROM \"user\" WHERE id = ?",
      user.getId()
    );
  }

  @Nonnull
  @Override
  public UdUserEntity update(UdUserEntity user) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    jdbcTemplate.update("""
                      UPDATE "user"
                        SET currency    = ?,
                            firstname   = ?,
                            surname     = ?,
                            photo       = ?,
                            photo_small = ?
                        WHERE id = ?
        """,
      user.getCurrency().name(),
      user.getFirstname(),
      user.getSurname(),
      user.getPhoto(),
      user.getPhotoSmall(),
      user.getId());

    jdbcTemplate.batchUpdate("""
                         INSERT INTO friendship (requester_id, addressee_id, status)
                         VALUES (?, ?, ?)
                         ON CONFLICT (requester_id, addressee_id)
                             DO UPDATE SET status = ?
        """,
      new BatchPreparedStatementSetter() {
        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
          ps.setObject(1, user.getId());
          ps.setObject(2, user.getFriendshipRequests().get(i).getAddressee().getId());
          ps.setString(3, user.getFriendshipRequests().get(i).getStatus().name());
          ps.setString(4, user.getFriendshipRequests().get(i).getStatus().name());
        }

        @Override
        public int getBatchSize() {
          return user.getFriendshipRequests().size();
        }
      });
    return user;
  }

  @Nonnull
  @Override
  public List<UdUserEntity> findFriends(String username) {
    return findByUsername(username).map(current -> {
      final UUID userId = current.getId();
      JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
      return jdbcTemplate.query(
        """
            SELECT DISTINCT u.*
              FROM "user" u
              JOIN friendship f
                ON (u.id = f.addressee_id AND f.requester_id = ?)
                OR (u.id = f.requester_id AND f.addressee_id = ?)
              WHERE f.status = ?;
          """,
        UserdataUserEntityRowMapper.instance,
        userId,
        userId,
        ACCEPTED.name()
      );
    }).orElseGet(ArrayList::new);
  }

  @Nonnull
  @Override
  public List<UdUserEntity> findFriends(String username, String searchQuery) {
    throw new UnsupportedOperationException("SearchQuery param is not applicable for DB requests!");
  }

  @Nonnull
  @Override
  public List<UdUserEntity> findIncomeInvitations(String username) {
    return findByUsername(username).map(current -> {
      final UUID userId = current.getId();
      JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
      return jdbcTemplate.query(
        """
            SELECT u.*
            FROM "user" u
            JOIN friendship f ON u.id = f.requester_id
            WHERE f.addressee_id = ?
            AND f.status = ?
          """,
        UserdataUserEntityRowMapper.instance,
        userId,
        PENDING.name()
      );
    }).orElseGet(ArrayList::new);
  }

  @Nonnull
  @Override
  public List<UdUserEntity> findOutcomeInvitations(String username) {
    return findByUsername(username).map(current -> {
      final UUID userId = current.getId();
      JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
      return jdbcTemplate.query(
        """
            SELECT u.*
            FROM "user" u
            JOIN friendship f ON u.id = f.addressee_id
            WHERE f.requester_id = ?
            AND f.status = ?
          """,
        UserdataUserEntityRowMapper.instance,
        userId,
        PENDING.name()
      );
    }).orElseGet(ArrayList::new);
  }

  @Override
  public void sendInvitation(UdUserEntity requester, UdUserEntity addressee) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
        """
          INSERT INTO friendship (addressee_id, requester_id, created_date, status) VALUES (?, ?, ?, ?)
          """
      );
      ps.setObject(1, requester.getId());
      ps.setObject(2, addressee.getId());
      ps.setDate(3, new Date(System.currentTimeMillis()));
      ps.setString(3, "PENDING");
      ps.execute();
      return null;
    });
  }

  @Override
  public void addFriend(UdUserEntity requester, UdUserEntity addressee) {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement(
        """
          INSERT INTO friendship (addressee_id, requester_id, created_date, status) VALUES (?, ?, ?, ?)
          """
      );
      ps.setObject(1, requester.getId());
      ps.setObject(2, addressee.getId());
      ps.setDate(3, new Date(System.currentTimeMillis()));
      ps.setString(3, "ACCEPTED");
      ps.execute();
      return null;
    });
  }
}
