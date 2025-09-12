package guru.qa.niffler.data.jdbc;

import com.atomikos.jdbc.AtomikosDataSourceBean;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.p6spy.engine.spy.P6DataSource;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@ParametersAreNonnullByDefault
public class DataSources {
  private DataSources() {
  }

  private static final Map<String, DataSource> dataSources = new ConcurrentHashMap<>();

  @Nonnull
  public static DataSource dataSource(String jdbcUrl) {
    return dataSources.computeIfAbsent(
        jdbcUrl,
        key -> {
          AtomikosDataSourceBean dsBean = new AtomikosDataSourceBean();
          final String uniqueId = StringUtils.substringAfter(jdbcUrl, "5432/");
          dsBean.setUniqueResourceName(uniqueId);
          dsBean.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
          Properties props = new Properties();
          props.put("URL", jdbcUrl);
          props.put("user", "postgres");
          props.put("password", "secret");
          dsBean.setXaProperties(props);
          dsBean.setMaxPoolSize(10);
          P6DataSource p6DataSource = new P6DataSource(
            dsBean
          );
            try {
                InitialContext context = new InitialContext();
                context.bind("java:comp/env/jdbc/" + uniqueId, p6DataSource);
            } catch (NamingException e) {
                throw new RuntimeException(e);
            }
          return dsBean;
        }
    );
  }
}
