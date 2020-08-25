/**
 * Web Driver Manager class
 *
 * This class is responsible for any web driver actions. Initializations, restart, open or close
 * Currently this supports only ChromeDriver
 *
 * @author Vlad Nuna
 * @date  14/May/2020
 */

package AutomationFramework.runner;

import AutomationFramework.exceptions.DriverNotInitializedException;
import AutomationFramework.utils.Logger;
import AutomationFramework.utils.Utils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebDriverManager {
    private static final String userDirProperty = System.getProperty("user.dir");
    private static WebDriver   driver;

    /***
     * Initialize the webDriver (for now we support only chromeDriver
     * but in the future it is intended to add support for other browsers.
     */
    public static void startWebDriver() {

        System.setProperty("webdriver.chrome.driver", userDirProperty + "/src/main/resources/chromedriver");
        // If driver is not null, close it before we instantiate a new webDriver
        if (driver != null) {
            driver.quit();
        }

        // try to initialize the driver at least twice for now...
        for (int i = 0; i < 2; i++) {
            driver = new ChromeDriver(); // to do: Add support for multiple web browsers, Safari, Firefox, Edge etc

            try {
                driver.manage().window().maximize();
                String windowSize = driver.manage().window().getSize().toString();
                Logger.info("Initialized browser with window size = " + windowSize);
                return;
            }
            catch (Exception ex) {
                Logger.error("Failed initialized webdriver: retry" + i + ":" + ex.getMessage());
                Utils.threadSleep(2000, null);
            }
        }
    }

    /***
     * Open a specific url page provided as string
     * @param url  - url link as string
     */
    public static void openURLPage(String url){
        try {
            driver.get(url);
        }catch (Exception ex){
            Logger.error("Failed to open the following url: "+url);
        }
    }

    /***
     * Close webdriver instance.
     *
     * @throws Exception
     */
    public static void stop() throws Exception {
        driver.quit();
    }

    /**
     * Resets the driver
     *
     * @param quit parameter used if we want to close or not the driver
     */
    public static void resetDriver(boolean quit) {
        try {
            if (quit) {
                driver.quit();
                Logger.info("WebDriver quit successful");
            }
        }
        catch (Exception e) {
            Logger.warn("Driver could not be reset. See debug log for details");
            Logger.debug(e.toString());
        }
        finally {
            driver = null;
        }
    }

    /**
     * Checks if the web driver exists
     *
     * @return true if a valid web driver is active
     */
    public static Boolean driverInitialized() {
        return driver != null;
    }


    /**
     * Gets the current webDriver instance or tries to create one
     *
     * @return current webDriver instance
     * @throws DriverNotInitializedException if driver is null
     */
    public static WebDriver getWebDriver() throws DriverNotInitializedException {
        if (driver == null) {
            throw new DriverNotInitializedException("Driver is not initialized!");
        }
        return driver;
    }

    /**
     * Get the current URL from browser and/or URL param
     *
     * @return the current url the browser is on
     */
    public static String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
