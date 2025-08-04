package guru.qa.niffler.page.element;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CalendarElement {
    private final SelenideElement self;

    public CalendarElement(SelenideElement self) {
        this.self = self;
    }
}
