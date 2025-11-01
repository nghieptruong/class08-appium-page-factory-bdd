package hooks;

import drivers.DriverManager;
import io.cucumber.java.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import utils.ConfigManager;

public class Hook {

    private static final Logger logger = LogManager.getLogger(Hook.class);

    @BeforeAll
    public static void beforeAllScenarios() {
        logger.info("beforeAllScenarios executed");
        ConfigManager.loadProperties();
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        logger.info("Scenario: " + scenario.getName());
        String platform = ConfigManager.getProperty("platform","android");
        DriverManager.getDriver(platform);
    }

    @After
    public void afterScenario(Scenario scenario) {
        logger.info("Finished: Scenario - " + scenario.getName());
        WebDriver driver = DriverManager.getCurrentDriver();
        if (driver != null) {
            if (scenario.isFailed()) {
                //capture screenshot
                byte[] screenshot = ((TakesScreenshot) DriverManager.getCurrentDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", scenario.getName());
            }
            DriverManager.quitDriver();
        }
    }

    @AfterAll
    public static void afterAllScenarios() {
        logger.info("After All Scenarios executed");
    }
}
