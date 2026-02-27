package components;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import pages.BasePage;
import utils.Config;

public class LoginModal extends BasePage {

    private final By loginFormButton = bySweetchuckId("register-form__button--log-in-footer");
    private final By usernameField = bySweetchuckId("login-form__input--username-or-email-address");
    private final By passwordField = bySweetchuckId("login-form__input--password");
    private final By loginButton = bySweetchuckId("login-form__button--submit");
    private final By headerLoginButton = bySweetchuckId("top-bar__button--log-in");
    private final By footerCloseButton = By.cssSelector("button[onclick*='dismiss']");
    private final By iframeFooter = By.cssSelector("iframe.gist-frame");

    @Step("Login if modal is present")
    public void loginIfPresent() {
        if (isDisplayed(headerLoginButton)) {
            waitClickable(headerLoginButton).click();
        }

        closeIframePopupIfPresent(iframeFooter, footerCloseButton);
        String user = Config.get("RT_USERNAME");
        String pass = Config.get("RT_PASSWORD");

        if (user == null || user.isBlank() || pass == null || pass.isBlank()) {
            throw new IllegalStateException("Login modal is open but RT_USERNAME / RT_PASSWORD are missing (env vars or -D).");
        }

        clearAndType(usernameField, user);
        clearAndType(passwordField, pass);

        safeClick(loginButton);

        waitInvisible(loginFormButton, 10);

        waitVisible(bySweetchuckId("header__image--user-profile-auth"));
    }
}