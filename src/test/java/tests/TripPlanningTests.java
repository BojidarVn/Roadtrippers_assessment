package tests;

import base.BaseTest;
import components.CreateTripModal;
import components.LoginModal;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.TripPlannerPage;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static testdata.TripData.*;

public class TripPlanningTests extends BaseTest {


    @Test(description = "Create trip, add waypoint, attempt save")
    public void createTrip_allowAddingWaypoint() {
        TripPlannerPage planner = goToSecondPageWithoutTrip()
                .openCreateTripModal()
                .createTrip(ORIGIN, DESTINATION)
                .launchTrip()
                .closeExploreSection()
                .clickAddWayPoint()
                .addWaypoint(WAYPOINT_1);

        List<String> expected = List.of(ORIGIN, DESTINATION, WAYPOINT_1);
        List<String> actual = planner.getTripPoints();

        assertEquals(actual, expected, "Trip points mismatch");
    }

    @Test(description = "Error: cannot create trip without destination (modal stays open)")
    public void createTrip_showError_when_destinationMissing() {
        CreateTripModal modal = goToSecondPageWithoutTrip()
                .openCreateTripModal()
                .setOriginOnly(ORIGIN)
                .clickCreateTripExpectingFailure();

        Assert.assertTrue(modal.isDestinationErrorVisible(), "Modal should remain open when destination is missing.");
    }

    @Test(description = "Edge: add multiple waypoints consecutively and verify they appear")
    public void addWaypoints_handleMultipleSequentialAdds() {
        List<String> waypoints = List.of(
                WAYPOINT_1,
                WAYPOINT_2,
                WAYPOINT_3,
                WAYPOINT_4);

        TripPlannerPage planner = goToSecondPageWithoutTrip()
                .openCreateTripModal()
                .createTrip(ORIGIN, DESTINATION)
                .launchTrip()
                .closeExploreSection();

        for (String wp : waypoints) {
            planner = planner
                    .clickAddWayPoint()
                    .addWaypoint(wp);
        }

        List<String> actualPoints = planner.getTripPoints();

        Assert.assertTrue(actualPoints.contains(ORIGIN), "Origin missing");
        Assert.assertTrue(actualPoints.contains(DESTINATION), "Destination missing");

        for (String wp : waypoints) {
            Assert.assertTrue(actualPoints.contains(wp), "Missing waypoint: " + wp);
        }
    }

    private TripPlannerPage goToSecondPageWithoutTrip() {
        TripPlannerPage planner = new HomePage()
                .open()
                .acceptCookiesIfPresent()
                .goToTripPlanner();

        new LoginModal().loginIfPresent();

        planner = planner
                .goMyTripsSection()
                .deleteTripIfExist();

        return planner;
    }
}