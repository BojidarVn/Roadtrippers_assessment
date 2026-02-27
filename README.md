# Roadtrippers Trip Planning – UI Automation (Part 1)

This project contains automated UI tests created for the Roadpass Digital QA technical assessment.
The tests validate the core trip planning functionality on https://roadtrippers.com using Selenium WebDriver and TestNG.


## Project Structure

src/test/java
├── base        → Base test setup
├── components  → Reusable UI components (modals)
├── core        → Driver management
├── listeners   → Test listeners (Allure integration)
├── pages       → Page Object Model classes
├── tests       → Test scenarios
└── utils       → Helpers (Config, AllureUtils)


## Tech Stack

Java 21
Selenium WebDriver 4
TestNG
Allure Reporting
WebDriverManager


## Prerequisites

Make sure the following tools are installed:

Java 21+
Maven 3.9+
Google Chrome

Check versions:

java -version
mvn -v


## Running the Tests

Run the full test suite:

mvn clean test

Tests execute sequentially because the demo environment allows only one active trip per account.


## Credentials Configuration

Login credentials are loaded from environment variables or JVM properties.

Environment variables:

RT_USERNAME=your-email  
RT_PASSWORD=your-password

Or via Maven:

mvn clean test -DRT_USERNAME=your-email -DRT_PASSWORD=your-password


## Test Coverage

The test suite includes:

Happy Path: Create trip → Launch trip → Add waypoint → Verify itinerary
Error Scenario: Attempt to create trip without destination
Edge Case: Add multiple waypoints consecutively


## Reporting

Allure is used for reporting and screenshots on failure.

Generate Allure report:

mvn allure:report  
mvn allure:serve

Results are stored in:

target/allure-results


## Notes

Page Object Model (POM) is used to keep tests modular and maintainable.
Explicit waits and retry logic are implemented for stability.
Marketing popups are handled dynamically to avoid flaky failures.