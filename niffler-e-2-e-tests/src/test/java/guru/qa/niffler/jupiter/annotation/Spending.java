package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.page.element.DateRange;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static guru.qa.niffler.page.element.DateRange.TODAY;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Spending {
  DateRange spendDate() default TODAY;
  String description();
  double amount();
  CurrencyValues currency() default CurrencyValues.RUB;
  String category() default "Tests";
}
