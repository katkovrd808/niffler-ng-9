package guru.qa.niffler.utils;

import com.codeborne.selenide.SelenideConfig;
import guru.qa.niffler.utils.converter.Browser;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SelenideUtils {

  @Nonnull
  public static SelenideConfig getConfig(Browser browser) {
    return new SelenideConfig().browser(browser.getName()).pageLoadStrategy("eager").timeout(5000L);
  }
}
