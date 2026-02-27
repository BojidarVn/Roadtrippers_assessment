# Part 4 – CI/CD Integration Strategy (CircleCI)

## Overview

The automated UI tests (Selenium + TestNG + Maven) are integrated into CircleCI to run on every Pull Request.  
The goal is to ensure early detection of regressions in the trip planning feature.


## CI Pipeline Configuration

The pipeline is defined in:

.circleci/config.yml

### Key Configuration Points

- Docker image: `cimg/openjdk:17.0-browsers`
- Tests executed via: `mvn test`
- Chrome runs in a virtual display (Xvfb) when headless mode is disabled
- Environment variables are used for credentials (no hardcoded secrets)


## Test Execution Flow

1. Checkout repository
2. Restore Maven dependencies (cached)
3. Execute TestNG test suite
4. Collect test results
5. Store artifacts (reports and logs)


## Test Reporting

The following artifacts are collected:

- `target/surefire-reports` (JUnit XML reports)
- `target/allure-results` (Allure raw results, screenshots, attachments)

CircleCI displays test results in the "Tests" tab and allows downloading artifacts for debugging.


## Handling Test Failures

- The pipeline fails automatically if any test fails.
- Screenshots and logs are attached via Allure for debugging.
- Failures are investigated before merge to maintain test stability.


## Flaky Test Management Strategy

- Retry logic is handled in the test framework (safe click & synchronization strategy).
- Flaky tests should be identified and fixed promptly.
- If necessary, unstable tests may be temporarily isolated from blocking CI runs.


## Metrics to Track

- Test pass rate
- Pipeline success rate
- Test execution duration
- Frequency of flaky failures

These metrics help maintain test reliability and pipeline health over time.


## Security Considerations

- Credentials are stored as CircleCI Environment Variables.
- No sensitive data is committed to the repository.
- Headless mode is configurable via environment variable.


## Conclusion

This CI/CD setup ensures automated validation of the core trip planning functionality on every pull request, improving code quality, stability, and delivery confidence.