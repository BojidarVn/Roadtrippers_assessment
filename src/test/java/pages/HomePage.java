package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import utils.Config;

public class HomePage extends BasePage {

    private static final String URL = Config.getOrDefault("BASE_URL", "https://roadtrippers.com/");

    private final By acceptCookiesBtn = By.xpath("//button[contains(.,'Accept')]");
    private final By headerYourTrip = By.cssSelector("#menu-item-85140 > a");


    @Step("Open Roadtrippers homepage")
    public HomePage open() {
        openUrl(URL);
        closePromoPopupIfPresent();

        return this;
    }

    @Step("Accept cookies if present")
    public HomePage acceptCookiesIfPresent() {
        if (isDisplayed(acceptCookiesBtn)) {
            safeClick(acceptCookiesBtn);
        }

        return this;
    }

    @Step("Go to Trip Planner")
    public TripPlannerPage goToTripPlanner() {
        safeClick(headerYourTrip);
        closePromoPopupIfPresent();

        return at(TripPlannerPage.class);
    }
}