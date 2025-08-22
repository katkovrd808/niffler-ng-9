package guru.qa.niffler.data.mapper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@FunctionalInterface
@ParametersAreNonnullByDefault
public interface MapRowMapper<T>{
  T mapRow(Map<String, Object> row);
}
