package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import utils.Config;

public class HomePage extends BasePage {

    private static final String URL = Config.getOrDefault("BASE_URL", "https://roadtrippers.com/");

    private final By acceptCookiesBtn = By.xpath("//button[contains(.,'Accept')]");
    private final By headerYourTrip = By.cssSelector("#menu-item-85140 > a");
    private final By iframeLocator = By
            .xpath("//div[contains(@class,'gist-background') and contains(@class,'gist-visible')]//iframe");
    private final By closeBtn = By.xpath("//button[contains(@onclick,'message.dismiss')]");


    @Step("Open Roadtrippers homepage")
    public HomePage open() {
        openUrl(URL);
        closeIframePopupIfPresent(iframeLocator, closeBtn);

        return this;
    }

    @Step("Accept cookies if present")
    public HomePage acceptCookiesIfPresent() {
        if (isDisplayed(acceptCookiesBtn)) {
            click(acceptCookiesBtn);
        }

        return this;
    }

    @Step("Go to Trip Planner")
    public TripPlannerPage goToTripPlanner() {
        click(headerYourTrip);
        closeIframePopupIfPresent(iframeLocator, closeBtn);

        return new TripPlannerPage();
    }
}