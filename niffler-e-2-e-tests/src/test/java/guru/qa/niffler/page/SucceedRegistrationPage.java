package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class SucceedRegistrationPage {
    private final SelenideElement
        congratulationText = $(".form__paragraph_success"),
        loginBtn = $(".form_sign-in");

    public SucceedRegistrationPage checkSucceedRegistrationPageTitle(){
        congratulationText.shouldHave(text("Congratulations! You've registered!"));
        return this;
    }

    public LoginPage loginAfterRegistration(){
        loginBtn.click();
        return new LoginPage();
    }
}
