package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.ScreenShotTestExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junitpioneer.jupiter.RetryingTest;
import org.opentest4j.AssertionFailedError;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@RetryingTest(maxAttempts = 2, onExceptions = {AssertionFailedError.class})
@ExtendWith({ScreenShotTestExtension.class})
public @interface ScreenShotTest {
  String value();

  boolean rewriteExpected() default false;
}
