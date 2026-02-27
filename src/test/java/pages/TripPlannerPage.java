package pages;

import components.CreateTripModal;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.stream.Collectors;

public class TripPlannerPage extends BasePage {

    private final By deleteTripButton = By.xpath("//button[@class='rt-button tertiary-filled small circle']");
    private final By confirmDeletion = By.xpath("//button[contains(text(),'Delete Trip')]");
    private final By secondConfirmDelete = By.cssSelector(".new-button.default.large.red");
    private final By launchTripButton = By.cssSelector(".sidebar-actions .sidebar-actions-container .primary-filled");
    private final By exploreSection = By.id("onboard-modal-view");
    private final By tripPoints = By.className("waypoint-primary-label");
    private final By createTripButton = bySweetchuckId("map-details__label--itinerary-empty-state-create-trip");
    private final By myTripsButton = bySweetchuckId("map-action-bar__button--my-trips");
    private final By addWayPointButton = bySweetchuckId("map-action-bar__button--add-waypoint");
    private final By pageReady = bySweetchuckId("map-action-bar__button--my-trips");

    @Step("Wait Trip Planner page to be ready")
    public TripPlannerPage waitUntilReady() {
        waitClickable(pageReady);

        return this;
    }

    @Step("Delete existing trip if present")
    public TripPlannerPage deleteTripIfExist() {
        if (isDisplayed(deleteTripButton)) {
            safeClick(deleteTripButton);
            waitClickable(confirmDeletion).click();
            waitClickable(secondConfirmDelete).click();
        }

        return this;
    }

    @Step("Open 'Create Trip' modal")
    public CreateTripModal openCreateTripModal() {
        safeClick(createTripButton);

        return new CreateTripModal();
    }

    @Step("Open 'My Trips' section")
    public TripPlannerPage goMyTripsSection() {
        safeClick(myTripsButton);

        return this;
    }

    @Step("Launch trip")
    public TripPlannerPage launchTrip() {
        waitClickable(launchTripButton).click();

        return waitUntilReady();
    }

    @Step("Open 'Add Waypoint' modal")
    public CreateTripModal clickAddWayPoint() {
        waitClickable(addWayPointButton).click();

        return at(CreateTripModal.class);
    }

    @Step("Close 'Explore' onboarding section")
    public TripPlannerPage closeExploreSection() {
        waitVisible(exploreSection);

        new Actions(driver)
                .click()
                .perform();

        return this;
    }

    @Step("Get trip itinerary points list")
    public List<String> getTripPoints() {
        List<WebElement> elements = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(tripPoints));

        return elements
                .stream()
                .map(WebElement::getText)
                .map(s -> s.replace("\n", " ").trim())
                .filter(s -> !s.isBlank())
                .collect(Collectors.toList());
    }
}