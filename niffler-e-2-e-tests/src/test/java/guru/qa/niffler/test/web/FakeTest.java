package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.service.impl.AuthApiClient;
import guru.qa.niffler.utils.converter.Browser;
import guru.qa.niffler.utils.converter.BrowserConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.UnsupportedEncodingException;

import static com.codeborne.selenide.Condition.text;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FakeTest {

  private static final String FRONT_URL = Config.getInstance().frontUrl();

  private final AuthApiClient authApiClient = new AuthApiClient();

  @ParameterizedTest(name = "Test should run with browser {0} in parallel with other browser")
  @EnumSource(Browser.class)
  void browsersTest(@ConvertWith(BrowserConverter.class) SelenideDriver driver) {
    driver.open(FRONT_URL);
    driver.$(".logo-section__text").shouldHave(text("Niffler"));
  }

  @Test
  void oauthTest() throws UnsupportedEncodingException {
    final String username = "test1";
    final String password = "secret";

    final String token = authApiClient.login(username, password);
    assertNotNull(token);
  }
}
