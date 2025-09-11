package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.userdata.TestData;
import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.impl.UsersApiClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.List;

import static guru.qa.niffler.jupiter.extension.TestMethodContextExtension.context;
import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

public class UserExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
  public static final String DEFAULT_PASSWORD = "secret";

  private final UsersClient usersClient = new UsersApiClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
      .ifPresent(userAnno -> {
        UdUserJson user = "".equals(userAnno.username())
          ? usersClient.create(randomUsername(), DEFAULT_PASSWORD)
          : usersClient.findByUsername(userAnno.username()).orElseThrow(
          () -> new IllegalArgumentException("User is empty")
        );
        final List<UdUserJson> incomes = usersClient.addInvitation(user, userAnno.incomeInvitations());
        final List<UdUserJson> outcomes = usersClient.addInvitation(user, userAnno.outcomeInvitations());
        final List<UdUserJson> friends = usersClient.addFriend(user, userAnno.friends());

        TestData testData = new TestData(
          DEFAULT_PASSWORD,
          friends,
          incomes,
          outcomes,
          new ArrayList<>(),
          new ArrayList<>()
        );

        context.getStore(NAMESPACE).put(
          context.getUniqueId(),
          user.addTestData(testData)
        );
      });
  }

  @Override
  public void afterTestExecution(ExtensionContext context) throws Exception {
    context.getStore(NAMESPACE).remove(context.getUniqueId());
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
    ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(UdUserJson.class);
  }

  @Override
  public UdUserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws
    ParameterResolutionException {
    return createdUser();
  }

  public static UdUserJson createdUser() {
    final ExtensionContext methodContext = context();
    return methodContext.getStore(NAMESPACE)
      .get(methodContext.getUniqueId(), UdUserJson.class);
  }
}
