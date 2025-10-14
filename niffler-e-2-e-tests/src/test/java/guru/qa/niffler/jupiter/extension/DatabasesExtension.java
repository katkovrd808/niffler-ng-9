package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.jdbc.Connections;
import guru.qa.niffler.jupiter.internal.DatabaseCleaner;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class DatabasesExtension implements SuiteExtension {

    private static final Config CFG = Config.getInstance();

    private final DatabaseCleaner dbCleaner = new DatabaseCleaner();

    @Override
    /*@Override
    public void beforeSuite(ExtensionContext context) {
        dbCleaner.cleanDatabase(
          CFG.userdataJdbcUrl(),
          CFG.authJdbcUrl(),
          CFG.spendJdbcUrl()
        );
    }*/

    public void afterSuite() {
        Connections.closeAllConnections();
    }
}
