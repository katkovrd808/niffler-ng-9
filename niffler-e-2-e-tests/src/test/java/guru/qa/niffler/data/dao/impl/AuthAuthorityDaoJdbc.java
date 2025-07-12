package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.Authority;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

  private final Connection connection;

  public AuthAuthorityDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void create(AuthorityEntity... authority) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)",
        PreparedStatement.RETURN_GENERATED_KEYS)) {
      for (AuthorityEntity a : authority) {
        ps.setObject(1, a.getUserId());
        ps.setString(2, a.getAuthority().name());
        ps.addBatch();
        ps.clearParameters();
      }
      ps.executeBatch();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthorityEntity> findById(UUID id) {
    try (PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM authority WHERE id = ?"
    )) {
      ps.setObject(1, id);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setId(rs.getObject("id", UUID.class));
          ae.setUserId(rs.getObject("user_id", UUID.class));
          ae.setAuthority(rs.getObject("authority", Authority.class));
          return Optional.of(ae);
        } else return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<AuthorityEntity> findByUsername(String username) {
    try (PreparedStatement ps = connection.prepareStatement(
            "SELECT * FROM authority WHERE username = ?"
    )) {
      ps.setString(1, username);
      ps.execute();
      try (ResultSet rs = ps.getResultSet()) {
        if (rs.next()) {
          AuthorityEntity ae = new AuthorityEntity();
          ae.setId(rs.getObject("id", UUID.class));
          ae.setUserId(rs.getObject("user_id", UUID.class));
          ae.setAuthority(rs.getObject("authority", Authority.class));
          return Optional.of(ae);
        } else return Optional.empty();
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteUser(AuthorityEntity authority) {
    try (PreparedStatement ps = connection.prepareStatement(
            "DELETE FROM user WHERE id = ?"
    )) {
      ps.setObject(1, authority.getId());
      ps.execute();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
