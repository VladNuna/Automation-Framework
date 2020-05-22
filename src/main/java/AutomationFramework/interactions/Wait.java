package AutomationFramework.interactions;

import AutomationFramework.runner.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

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
}
