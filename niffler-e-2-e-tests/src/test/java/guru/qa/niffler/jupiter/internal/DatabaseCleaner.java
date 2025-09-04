package guru.qa.niffler.jupiter.internal;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import jakarta.persistence.EntityManager;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import static guru.qa.niffler.data.jpa.EntityManagers.em;

@ParametersAreNonnullByDefault
public class DatabaseCleaner {

  private static final Config CFG = Config.getInstance();

  private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
    CFG.userdataJdbcUrl(),
    CFG.authJdbcUrl(),
    CFG.spendJdbcUrl()
  );

  public void cleanDatabase(String... jdbcUrl) {
    xaTransactionTemplate.execute(() -> {
      for (String url : jdbcUrl) {
        try (EntityManager entityManager = em(url)) {
          for (String table : sanitizeTables(getProperty())) {
            boolean exists = (boolean) entityManager
              .createNativeQuery("SELECT EXISTS (SELECT 1 FROM information_schema.tables " +
                "WHERE table_schema = 'public' AND table_name = '" + table + "')")
              .getSingleResult();
            if (exists) {
              entityManager.createNativeQuery("DELETE FROM \"" + table + "\"")
                .executeUpdate();
            }
          }
        }
      }
      return null;
    });
  }

  @Nonnull
  private List<String> getProperty() {
    try (InputStream input = DatabaseCleaner.class.getClassLoader()
      .getResourceAsStream("db-cleaner.properties")) {
      Properties props = new Properties();
      props.load(input);
      String tables = props.getProperty("db.cleaner.tables", "");
      return Arrays.stream(tables.split(","))
        .map(String::trim)
        .filter(s -> !s.isBlank())
        .collect(Collectors.toCollection(ArrayList::new));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Nonnull
  private List<String> sanitizeTables(List<String> tables) {
    return tables.stream()
      .filter(ALLOWED_TABLES::contains)
      .toList();
  }

  private final Set<String> ALLOWED_TABLES = Set.of(
    "user", "friendship", "authority",
    "spend", "category"
  );
}
