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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(7));
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void waitInvisible(By locator, int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException ignored) {
        }
    }

    protected <T> T at(Class<T> pageClass) {
        try {
            return pageClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot create page: " + pageClass.getSimpleName(), e);
        }
    }

    protected By bySweetchuckId(String id) {
        return By.cssSelector("[data-sweetchuck-id='" + id + "']");
    }

    @Step("Safe click on element: {0}")
    protected void safeClick(By locator) {
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
                    AllureUtils.attachScreenshot(driver, "SafeClick failed - " + locator);
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

    @Step("Close promo popup if present")
    protected void closePromoPopupIfPresent() {
        By iframeLocator = By
                .xpath("//div[contains(@class,'gist-background') and contains(@class,'gist-visible')]//iframe[contains(@src,'gist')]");
        By closeBtn = By.xpath("//button[contains(@onclick,'message.dismiss')]");
        waitVisible(iframeLocator);
        try {
            driver.switchTo().frame(driver
                    .findElement(iframeLocator));

            if (driver.findElements(closeBtn).size() > 0) {
                driver.findElement(closeBtn).click();
            }

            driver.switchTo().defaultContent();
        } catch (Exception ignored) {
            driver.switchTo().defaultContent();
        }
    }

    @Step("Close iframe popup if present (iframe: {0}, close: {1})")
    protected void closeIframePopupIfPresent(By iframeLocator, By closeButtonInFrame) {
        if (driver.findElements(iframeLocator).isEmpty()) return;

        try {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframeLocator));

            if (!driver.findElements(closeButtonInFrame).isEmpty()) {
                waitClickable(closeButtonInFrame).click();
                driver.switchTo().defaultContent();
                wait.until(ExpectedConditions.invisibilityOfElementLocated(iframeLocator));
                return;
            }
        } catch (TimeoutException ignored) {
        } finally {
            driver.switchTo().defaultContent();
        }
    }
}