package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.LifecycleMethodExecutionExceptionHandler;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.ByteArrayInputStream;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BrowserExtension implements
  BeforeEachCallback,
  AfterEachCallback,
  TestExecutionExceptionHandler,
  LifecycleMethodExecutionExceptionHandler {

  private final static Set<String> REQUIRED_TAGS = Set.of("WEB");

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    context.getElement()
      .map(this::extractTags)
      .filter(tags -> tags.stream().anyMatch(REQUIRED_TAGS::contains))
      .ifPresent(tags ->
        SelenideLogger.addListener("Allure-selenide", new AllureSelenide()
          .savePageSource(false)
          .screenshots(false)
        )
      );
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    if (WebDriverRunner.hasWebDriverStarted()) {
      Selenide.closeWebDriver();
    }
  }

  @Override
  public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
    doScreenshot();
    throw throwable;
  }

  @Override
  public void handleBeforeEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
    doScreenshot();
    throw throwable;
  }

  @Override
  public void handleAfterEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
    doScreenshot();
    throw throwable;
  }

  private static void doScreenshot() {
    if (WebDriverRunner.hasWebDriverStarted()) {
      Allure.addAttachment(
        "Screen on fail",
        new ByteArrayInputStream(
          ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES)
        )
      );
    }
  }

  private Set<String> extractTags(AnnotatedElement element) {
    Set<String> singleTags = Arrays.stream(element.getAnnotationsByType(Tag.class))
      .map(Tag::value)
      .collect(Collectors.toSet());

    Set<String> multipleTags = Arrays.stream(element.getAnnotationsByType(Tags.class))
      .flatMap(tags -> Arrays.stream(tags.value()))
      .map(Tag::value)
      .collect(Collectors.toSet());

    return Set.copyOf(
      Arrays.asList(singleTags, multipleTags).stream()
        .flatMap(Set::stream)
        .collect(Collectors.toSet())
    );
  }
}