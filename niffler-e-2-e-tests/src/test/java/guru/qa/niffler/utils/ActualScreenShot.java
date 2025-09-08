package guru.qa.niffler.utils;

import com.codeborne.selenide.SelenideElement;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import static com.codeborne.selenide.Selenide.$;

public class ActualScreenShot {
  @SneakyThrows
  public BufferedImage makeScreenshot(SelenideElement element) {
    Thread.sleep(2500);
    return ImageIO.read($(element).screenshot());
  }
}
