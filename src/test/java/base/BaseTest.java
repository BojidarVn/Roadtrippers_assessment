package base;

import core.DriverManager;
import listeners.TestListener;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import utils.AllureUtils;
import utils.Config;

import java.time.Duration;

@Listeners(TestListener.class)
public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverManager.initDriver();
        driver = DriverManager.getDriver();

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(45));
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);

        String baseUrl = Config.getOrDefault("BASE_URL", "https://roadtrippers.com/");
        String headless = Config.getOrDefault("HEADLESS", "false");

        AllureUtils.attachText(
                "Test Context",
                "Base URL: " + baseUrl + "\nHeadless: " + headless + "\nBrowser: Chrome"
        );
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }
}