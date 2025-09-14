package guru.qa.niffler.page.base;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.utils.ActualScreenShot;
import guru.qa.niffler.utils.ScreenDiffResult;
import lombok.Getter;

import java.awt.image.BufferedImage;

import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class BaseElement <T extends BaseElement<?>>{

  @Getter
  protected final SelenideElement self;

  public BaseElement(SelenideElement self) {
    this.self = self;
  }

  public void assertDiff(BufferedImage expected, SelenideElement element) {
    assertFalse(new ScreenDiffResult(
      expected,
      new ActualScreenShot().makeScreenshot($(element))
    ));
  }
}
