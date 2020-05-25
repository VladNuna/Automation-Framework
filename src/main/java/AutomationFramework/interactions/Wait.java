package AutomationFramework.interactions;

import AutomationFramework.runner.WebDriverManager;
import AutomationFramework.utils.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Date;

public abstract class Wait {
    private static By by;

    /**
     * Utility method to wait for Java Script to fully load on page
     */
    static void waitUntilJSReady() {
        WebDriver drv = WebDriverManager.getWebDriver();
        WebDriverWait wait = new WebDriverWait(drv, 15);
        JavascriptExecutor jsExec = (JavascriptExecutor) drv;

        // Wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = driver -> {
            assert driver != null;
            return ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState").toString().equals("complete");  // Verify if the DOM has the Complete state (readyState)
        };

        // Wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = driver -> {
            assert driver != null;
            return ((Long) ((JavascriptExecutor) driver)
                    .executeScript("return jQuery.active") == 0);
        };

        // Get JS is Ready
        boolean jsReady = jsExec.executeScript("return document.readyState").toString().equals("complete");

        // Wait Javascript until it is Ready!
        if (!jsReady) {
            System.out.println("JS in NOT Ready!");
            // Wait for Javascript to load
            wait.until(jsLoad);
        }
    }

    /***
     * Waits for the page to load
     * This method can wait a default timeout of 10000 ms or you can specify a duration in seconds
     *
     * @param condition         - Condition to be met in order to validate if the element is visible or not
     * @param throwException    - Throws an exception if the page is not loading.
     * @param timeout           - Time to wait in seconds
     * @return                  - True if page is loaded otherwise false.
     */
    static boolean waitNoMsg(ExpectedCondition condition, boolean throwException, int timeout) {
        //Initialize timeout for waiting for the page to load.
        Duration waitForLoadTimeout = timeout <= 0 ? Duration.ofSeconds(10) : Duration.ofSeconds(timeout);

        try {
            new WebDriverWait(WebDriverManager.getWebDriver(), waitForLoadTimeout.getSeconds()).until(condition);
            waitUntilJSReady();
        }
        catch (Exception e) {
            if (throwException) {
                throw e;
            }
            return false;
        }
        return true;
    }

    /**
     * Awaits for the provided element to be visible within a provided timeout.
     * If element is not visible, throws exception but only if throwException == true
     *
     * @param locator        - element locator
     * @param timeout        - time to wait
     * @param throwException - if true, it throws ElementNotVisibleException if element is not visible within the provided timeout
     * @return
     * @throws ElementNotVisibleException - throw
     */
    public static boolean waitForVisible(String locator, int timeout, boolean throwException) throws Exception {
        Wait.by = Elements.by(locator);
        return wait(by, ExpectedConditions.visibilityOfElementLocated(by), throwException, timeout);
    }

    /**
     * Awaits for the provided element to be visible within a default timeout.
     *
     * @param by - selenium By
     */
    public static void waitForVisible(By by) {
        wait(Wait.by = by, ExpectedConditions.visibilityOfElementLocated(by));
    }

    /**
     * Waits for the page to load.
     * Waits for up to a specified timeout.
     * Throws an exception if the page does not load within the specified timeout.
     *
     * @param by        - The By to validate exists to signify the page loaded.
     * @param condition - condition to be met in order to validate existence of the element on page
     */
    public static void wait(By by, ExpectedCondition condition) {
        wait(by, condition, true, 0);
    }

    /**
     * Awaits for the provided element to be visible within a provided timeout.
     *
     * @param by      - selenium By
     * @param timeout - time to wait for
     * @return - returns true if element becomes visible, false otherwise
     */
    public static boolean waitForVisible(By by, int timeout) {
        return wait(Wait.by = by, ExpectedConditions.visibilityOfElementLocated(by), timeout);
    }



    /**
     * Waits for the page to load.
     * Waits for up to a specified timeout.
     * Does not throw an exception if the page does not load within the specified timeout.
     *
     * @param by        - The By to validate exists to signify the page loaded.
     * @param condition - condition to be met in order to validate existence of the element on page
     * @param timeout   - Milliseconds to wait for the page to load. If timeOut &lt;= 0 then the default timeout
     *                  is used (5000 milliseconds).
     * @return - True if page loaded successfully, false otherwise.
     */
    public static boolean wait(By by, ExpectedCondition condition, int timeout) {
        return wait(by, condition, false, timeout);
    }

    /**
     * Awaits for the provided element to be visible within a provided timeout.
     * It throws ElementNotVisibleException but only if throwException is true
     *
     * @param by      - selenium By
     * @param timeout - time to wait for
     * @return - returns true if element becomes visible, false otherwise
     * @throws ElementNotVisibleException - throws exception
     */
    public static boolean waitForVisible(By by, int timeout, boolean throwException) {
        return wait(Wait.by = by, ExpectedConditions.visibilityOfElementLocated(by), throwException, timeout);
    }

    /**
     * Awaits for the provided element to be visible within a default timeout.
     * It throws ElementNotVisibleException but only if throwException is true
     *
     * @param by - selenium By
     * @return - returns true if element becomes visible, false otherwise
     * @throws ElementNotVisibleException - throws exception
     */
    public static boolean waitForVisible(By by, boolean throwException) {
        return wait(Wait.by = by, ExpectedConditions.visibilityOfElementLocated(by), throwException, 0);
    }

    /**
     * Awaits for the provided element to be visible within a default timeout.
     * It throws ElementNotVisibleException but only if throwException is true
     *
     * @param webElement - webElement
     * @return - returns true if element becomes visible, false otherwise
     * @throws ElementNotVisibleException - throws exception
     */
    public static boolean waitForVisible(WebElement webElement, boolean throwException) {
        return wait(Wait.by, ExpectedConditions.visibilityOf(webElement), throwException, 0);
    }

    /**
     * Waits for the page to load.
     * Waits for up to a specified timeout.
     * Optionally throws an exception if the page does not load within the specified timeout.
     *
     * @param by             - The By to validate exists to signify the page loaded.
     * @param condition      - condition to be met in order to validate existence of the element on page
     * @param throwException - If true, throws an excpetion if the page does not load. False, does not.
     * @param timeout        - Milliseconds to wait for the page to load. If timeOut &lt;= 0 then the default timeout
     *                       is used (5000 milliseconds).
     * @return - True if page loaded successfully, false otherwise.
     */
    private static boolean wait(By by, ExpectedCondition condition, boolean throwException, int timeout) {
        //Initialize timeout for waiting for the page to load.
        Duration waitForLoadTimeout = timeout <= 0 ? Duration.ofSeconds(5) : Duration.ofSeconds(timeout);

        Logger.debug(String.format("Waiting for page to load; waiting for the following to exist: %s.\n" +
                "Waiting for load timeout: %s milliseconds", by.toString(), waitForLoadTimeout.toMillis()));

        Date dateTimeBeforeWaitForLoad;
        try {
            dateTimeBeforeWaitForLoad = new Date();

            waitUntilJSReady();
            new WebDriverWait(WebDriverManager.getWebDriver(), waitForLoadTimeout.getSeconds()).until(condition);
        }
        catch (Exception e) {
            if (throwException) {
                Logger.error(String.format("Failed to wait for page to load since failed to validate existence of %s within %d milliseconds!",
                        by.toString(), waitForLoadTimeout.toMillis()));
                throw e;
            }

            Logger.info(String.format("Element %s not found within %d milliseconds!", by.toString(), waitForLoadTimeout.toMillis()));
            return false;
        }

        long elapsed = new Date().getTime() - dateTimeBeforeWaitForLoad.getTime();
        Logger.debug(String.format("Page loaded; the following exists: %s. \nFound in %d milliseconds", by.toString(), elapsed));
        return true;
    }

    /**
     * Waits for an element to no longer be present
     * Waits for up to a specified timeout.
     * Optionally throws an exception if the page does not load / element still present within the specified timeout.
     *
     * @param locator        - The locator to validate no longer exists to signify the page loaded.
     * @param throwException - If true, throws an excpetion if the page does not load. False, does not.
     * @param timeout        - Milliseconds to wait for the page to load. If timeOut &lt;= 0 then the default timeout
     *                       is used (5000 milliseconds).
     * @return - True if page loaded successfully, false otherwise.
     * @throws Exception - throws exception
     */
    public static boolean waitForNotVisible(String locator, int timeout, boolean throwException) throws Exception {
        Wait.by = Elements.by(locator);

        //Initialize timeout for waiting for the page to load.
        Duration waitForLoadTimeout = timeout <= 0 ? Duration.ofSeconds(5) : Duration.ofSeconds(timeout);

        Logger.debug(String.format("Waiting for page to load; waiting for the following to not be visible: %s.\n" +
                "Waiting for load timeout: %s milliseconds", by.toString(), waitForLoadTimeout.toMillis()));

        Date dateTimeBeforeWaitForLoad;
        try {
            dateTimeBeforeWaitForLoad = new Date();
            waitUntilJSReady();
            new WebDriverWait(WebDriverManager.getWebDriver(), waitForLoadTimeout.getSeconds()).until(ExpectedConditions.invisibilityOfElementLocated(by));
        }
        catch (Exception e) {
            Logger.error(String.format("Failed to wait for page to load since failed to validate invisibility of %s " +
                    "within %d milliseconds.", by.toString(), waitForLoadTimeout.toMillis()));

            if (throwException) {
                throw e;
            }
            return false;
        }

        long elapsed = new Date().getTime() - dateTimeBeforeWaitForLoad.getTime();
        Logger.debug(String.format("Page loaded; the following not visible: %s. \nFound in %d milliseconds", by.toString(), elapsed));
        return true;
    }
}
