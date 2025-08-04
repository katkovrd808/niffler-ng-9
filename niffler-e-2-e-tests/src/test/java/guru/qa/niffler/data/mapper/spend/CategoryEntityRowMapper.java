package guru.qa.niffler.data.mapper.spend;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class CategoryEntityRowMapper implements RowMapper<CategoryEntity> {

  public static final CategoryEntityRowMapper instance = new CategoryEntityRowMapper();

  private CategoryEntityRowMapper() {
  }

  @Nonnull
  @Override
  public CategoryEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
    CategoryEntity result = new CategoryEntity();
    result.setId(rs.getObject("id", UUID.class));
    result.setName(rs.getString("name"));
    result.setUsername(rs.getString("username"));
    result.setArchived(rs.getBoolean("archived"));
    return result;
  }
}
