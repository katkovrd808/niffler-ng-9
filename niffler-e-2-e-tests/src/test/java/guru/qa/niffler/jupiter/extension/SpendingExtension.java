package guru.qa.niffler.jupiter.extension;

import com.github.jknack.handlebars.internal.lang3.ArrayUtils;
import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.utils.DateResolverFactory;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static guru.qa.niffler.jupiter.extension.TestMethodContextExtension.context;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

  private final SpendApiClient spendApiClient = new SpendApiClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
      .ifPresent(userAnno -> {
          if (ArrayUtils.isNotEmpty(userAnno.spendings())) {
            final @Nullable UdUserJson createdUser = UserExtension.createdUser();

            final List<SpendJson> result = new ArrayList<>();
            for (Spending spendAnno : userAnno.spendings()) {
              final String username = createdUser != null ? createdUser.username() : userAnno.username();
              if (!"".equals(username)) {
                SpendJson spendJson = new SpendJson(
                  null,
                  DateResolverFactory.resolveDate(spendAnno.spendDate()),
                  new CategoryJson(
                    null,
                    spendAnno.category(),
                    username,
                    false
                  ),
                  spendAnno.currency(),
                  spendAnno.amount(),
                  spendAnno.description(),
                  username
                );
                result.add(spendApiClient.createSpend(spendJson));
              }
            }

            if (createdUser != null) {
              createdUser.testData().spendings().addAll(result);
            } else {
              context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                result.stream().toArray(SpendJson[]::new)
              );
            }
          }
        }
      );
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
    ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(SpendJson[].class);
  }

  @Override
  public SpendJson[] resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
    ParameterResolutionException {
    return createdSpending();
  }

  public static SpendJson[] createdSpending() {
    final ExtensionContext methodContext = context();
    return methodContext.getStore(NAMESPACE)
      .get(methodContext.getUniqueId(), SpendJson[].class);
  }
}
