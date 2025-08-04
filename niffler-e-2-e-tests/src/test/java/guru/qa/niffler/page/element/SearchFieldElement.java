package guru.qa.niffler.page.element;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Selenide.$;

public class SearchFieldElement {
    private final SelenideElement
        searchInput = $("form input"),
        clearButton = $("");

    public SearchFieldElement find(@Nonnull String query) {
        searchInput.val(query).pressEnter();
        return this;
    }

    public SearchFieldElement clearIfNotEmpty() {
        if (!searchInput.getOptions().isEmpty()) {
            clearButton.click();
        }
        return this;
    }
}
