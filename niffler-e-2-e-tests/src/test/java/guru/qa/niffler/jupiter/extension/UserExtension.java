package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.userdata.TestData;
import guru.qa.niffler.model.userdata.UdUserJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.UsersDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.List;

import static guru.qa.niffler.jupiter.extension.TestMethodContextExtension.context;

public class UserExtension implements BeforeEachCallback, ParameterResolver {

    public static final
    ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    public static final String DEFAULT_PASSWORD = "12345";

    private final UsersClient usersClient = new UsersDbClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                    if ("".equals(userAnno.username())) {
                        final String username = RandomDataUtils.randomUsername();
                        UdUserJson created = usersClient.create(username, DEFAULT_PASSWORD);
                        final List<UdUserJson> incomes = usersClient.addInvitation(created, userAnno.incomeInvitations());
                        final List<UdUserJson> outcomes = usersClient.addInvitation(created, userAnno.outcomeInvitations());
                        final List<UdUserJson> friends = usersClient.addFriend(created, userAnno.friends());

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
                                created.addTestData(testData)
                        );
                    }
                });
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
