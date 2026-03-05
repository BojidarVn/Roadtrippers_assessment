package components;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import pages.BasePage;
import pages.TripPlannerPage;

public class CreateTripModal extends BasePage {

    private final By startingPointInput = By.id("origin");
    private final By destinationPointInput = By.id("destination");
    private final By waypointInput = By.cssSelector("section#waypoint-add-modal-view #destination");
    private final By createTripButton = By
            .cssSelector("#create-trip-manually-modal-view .sticky-footer button.rt-button.primary-filled.large.default");
    private final By destinationError = By.cssSelector("#create-trip-manually-modal-view .destination-input.has-error");
    private final By firstItemFromDropdown = By.cssSelector("div.rt-autocomplete-list button.rt-autocomplete-list-item-view");

    @Step("Create trip: from '{0}' to '{1}'")
    public TripPlannerPage createTrip(String startingPoint, String destination) {
        addingLocation(startingPointInput, startingPoint);
        addingLocation(destinationPointInput, destination);

        click(createTripButton);

        return new TripPlannerPage().waitUntilReady();
    }

    @Step("Add waypoint: '{0}'")
    public TripPlannerPage addWaypoint(String waypoint) {
        addingLocation(waypointInput, waypoint);
        waitWaypointVisible(waypoint);

        return new TripPlannerPage().waitUntilReady();
    }

    @Step("Select location '{0}' in field {1}")
    private void addingLocation(By locatorOfPoint, String locationPoint) {
        clearAndType(locatorOfPoint, locationPoint);
        click(firstItemFromDropdown);
    }

    @Step("Wait waypoint '{0}' to be visible in itinerary")
    public void waitWaypointVisible(String name) {
        By locator = By.xpath(String
                .format("//button[contains(@class,'waypoint-primary-label')][contains(normalize-space(.),'%s')]", name));

        waitVisible(locator).isDisplayed();
    }

    @Step("Set starting point only: '{0}'")
    public CreateTripModal setOriginOnly(String startingPoint) {
        addingLocation(startingPointInput, startingPoint);

        return this;
    }

    @Step("Click 'Create Trip' expecting validation error")
    public CreateTripModal clickCreateTripExpectingFailure() {
        click(createTripButton);

        return this;
    }

    @Step("Check destination error is visible")
    public boolean isDestinationErrorVisible() {

        return isDisplayed(destinationError);
    }
}