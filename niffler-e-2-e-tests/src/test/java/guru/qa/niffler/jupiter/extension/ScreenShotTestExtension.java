package guru.qa.niffler.jupiter.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.api.allure.ScreenDiff;
import guru.qa.niffler.exception.ImageComparisonException;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import io.qameta.allure.Allure;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

public class ScreenShotTestExtension implements ParameterResolver, TestExecutionExceptionHandler {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ScreenShotTestExtension.class);

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final Base64.Encoder encoder = Base64.getEncoder();

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return AnnotationSupport.isAnnotated(extensionContext.getRequiredTestMethod(), ScreenShotTest.class) &&
      parameterContext.getParameter().getType().isAssignableFrom(BufferedImage.class);
  }

  @SneakyThrows
  @Override
  public BufferedImage resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    String expectedRoot = extensionContext.getRequiredTestMethod().getDeclaredAnnotation(ScreenShotTest.class).value();
    return ImageIO.read(new ClassPathResource(expectedRoot).getInputStream());
  }

  @Override
  public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
    boolean overwrite = context.getRequiredTestMethod().getDeclaredAnnotation(ScreenShotTest.class).rewriteExpected();
    try {
      ScreenDiff screenDif = new ScreenDiff(
        "data:image/png;base64," + encoder.encodeToString(imageToBytes(getExpected())),
        "data:image/png;base64," + encoder.encodeToString(imageToBytes(getActual())),
        "data:image/png;base64," + encoder.encodeToString(imageToBytes(getDiff()))
      );

      Allure.addAttachment(
        "Screenshot diff",
        "application/vnd.allure.image.diff",
        objectMapper.writeValueAsString(screenDif)
      );
    } catch (IOException e) {
      if (overwrite) {
        var overwriteRoot = context.getRequiredTestMethod().getDeclaredAnnotation(ScreenShotTest.class).value();
        ImageIO.write(getActual(), "png", new File("src/test/resources", overwriteRoot));
      }
      throw new ImageComparisonException("Image comparison exception: ", e);
    }

    throw throwable;
  }

  public static void setExpected(BufferedImage expected) {
    TestMethodContextExtension.context().getStore(NAMESPACE).put("expected", expected);
  }

  public static BufferedImage getExpected() {
    return Objects.requireNonNull(
      TestMethodContextExtension.context().getStore(NAMESPACE).get("expected", BufferedImage.class),
      "Expected buffered image was not found in TestMethodContext"
    );
  }

  public static void setActual(BufferedImage actual) {
    TestMethodContextExtension.context().getStore(NAMESPACE).put("actual", actual);
  }

  public static BufferedImage getActual() {
    return Objects.requireNonNull(
      TestMethodContextExtension.context().getStore(NAMESPACE).get("actual", BufferedImage.class),
      "Actual buffered image was not found in TestMethodContext"
    );
  }

  public static void setDiff(BufferedImage diff) {
    TestMethodContextExtension.context().getStore(NAMESPACE).put("diff", diff);
  }

  public static BufferedImage getDiff() {
    return TestMethodContextExtension.context().getStore(NAMESPACE).get("diff", BufferedImage.class);
  }

  private static byte[] imageToBytes(BufferedImage image) {
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      ImageIO.write(image, "png", outputStream);
      return outputStream.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
