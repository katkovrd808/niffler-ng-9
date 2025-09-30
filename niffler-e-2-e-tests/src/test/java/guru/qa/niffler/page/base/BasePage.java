package guru.qa.niffler.page.base;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.element.HeaderElement;
import guru.qa.niffler.utils.ActualScreenShot;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;

import java.awt.image.BufferedImage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BasePage <T extends BasePage<?>>{

  protected final HeaderElement header = new HeaderElement();
  protected final SelenideElement alert = $("#root [role='presentation']");

  protected static final Config CFG = Config.getInstance();

  @SuppressWarnings("unchecked")
  @Step("Asserting that alert should have text {text}")
  public T checkAlert(String text) {
    alert.shouldHave(text(text));
    return (T) this;
  }

  public void assertDiff(BufferedImage expected, SelenideElement element) {
    assertFalse(new ScreenDiffResult(
      expected,
      new ActualScreenShot().makeScreenshot($(element))
    ));
  }
}
