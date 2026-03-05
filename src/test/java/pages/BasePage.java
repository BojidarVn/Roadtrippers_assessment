package pages;

import core.DriverManager;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.AllureUtils;

import java.time.Duration;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected WebElement waitVisible(By locator) {

        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitClickable(By locator) {

        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected By bySweetchuckId(String id) {

        return By.cssSelector("[data-sweetchuck-id='" + id + "']");
    }

    @Step("Click on element: {0}")
    protected void click(By locator) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                WebElement el = waitClickable(locator);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
                el.click();
                return;
            } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
                attempts++;

                if (attempts == 3) {
                    AllureUtils.attachScreenshot(driver, "Click failed - " + locator);
                    throw e;
                }
            }
        }
    }

    @Step("Clear and type '{1}' into element: {0}")
    protected void clearAndType(By locator, String text) {
        WebElement el = waitVisible(locator);
        el.click();
        el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        el.sendKeys(Keys.DELETE);
        el.sendKeys(text);
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waitVisible(locator).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Step("Navigate to: {0}")
    protected void openUrl(String url) {
        driver.get(url);

    }

    @Step("Close iframe popup if present")
    protected void closeIframePopupIfPresent(By iframeLocator, By closeButtonInFrame) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.presenceOfElementLocated(iframeLocator));
        } catch (TimeoutException ignored) {
            return;
        }

        try {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframeLocator));

            if (!driver.findElements(closeButtonInFrame).isEmpty()) {
                click(closeButtonInFrame);
            }
        } catch (TimeoutException ignored) {
        } finally {
            driver.switchTo().defaultContent();
        }
    }
}