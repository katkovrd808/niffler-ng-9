package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.CategoryExtension;
import guru.qa.niffler.jupiter.extension.SpendingExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@ExtendWith({
        CategoryExtension.class,
        SpendingExtension.class
})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface User {
    String username();
    Category[] categories() default {};
    Spending[] spendings() default {};
}


