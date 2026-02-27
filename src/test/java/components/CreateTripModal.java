package components;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;
import pages.TripPlannerPage;

public class CreateTripModal extends BasePage {

    private final By startingPointInput = By.id("origin");
    private final By destinationPointInput = By.id("destination");
    private final By waypointInput = By.cssSelector("section#waypoint-add-modal-view #destination");
    private final By createTripButton = By
            .cssSelector("#create-trip-manually-modal-view .sticky-footer button.rt-button.primary-filled.large.default");
    private final By destinationError = By.cssSelector("#create-trip-manually-modal-view .destination-input.has-error");
    private final By modalRoot = By.cssSelector("section#create-trip-manually-modal-view");

    @Step("Create trip: from '{0}' to '{1}'")
    public TripPlannerPage createTrip(String startingPoint, String destination) {
        addingLocation(startingPoint, startingPointInput);
        addingLocation(destination, destinationPointInput);

        safeClick(createTripButton);

        waitInvisible(modalRoot, 7);

        return at(TripPlannerPage.class).waitUntilReady();
    }

    @Step("Add waypoint: '{0}'")
    public TripPlannerPage addWaypoint(String waypoint) {
        addingLocation(waypoint, waypointInput);
        waitWaypointVisible(waypoint);

        return at(TripPlannerPage.class).waitUntilReady();
    }

    @Step("Select location '{0}' in field {1}")
    private void addingLocation(String locationPoint, By locatorPoint) {
        WebElement origin = waitClickable(locatorPoint);
        origin.click();
        origin.clear();
        origin.sendKeys(locationPoint);

        By firstItem = By.cssSelector("div.rt-autocomplete-list button.rt-autocomplete-list-item-view");

        waitClickable(firstItem).click();
    }

    @Step("Wait waypoint '{0}' to be visible in itinerary")
    public void waitWaypointVisible(String name) {
        By locator = By.xpath(String
                .format("//button[contains(@class,'waypoint-primary-label')][contains(normalize-space(.),'%s')]", name));

        wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
    }

    @Step("Set origin only: '{0}'")
    public CreateTripModal setOriginOnly(String startingPoint) {
        addingLocation(startingPoint, startingPointInput);

        return this;
    }

    @Step("Click 'Create Trip' expecting validation error")
    public CreateTripModal clickCreateTripExpectingFailure() {
        safeClick(createTripButton);

        return this;
    }

    @Step("Check destination error is visible")
    public boolean isDestinationErrorVisible() {

        return isDisplayed(destinationError);
    }
}