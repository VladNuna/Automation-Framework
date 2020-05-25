/**
 * Clicks class
 *
 * This class is used to for any click interaction. This implements many types of clicks for webdriver
 *
 * @author Vlad Nuna
 * @date  14/May/2020
 */
package AutomationFramework.interactions;

import AutomationFramework.runner.WebDriverManager;
import AutomationFramework.utils.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Class responsible with click type events on the page: click, double click, move and click etc
	@FindBy(css="")
 */
public abstract class Clicks {

    /**
     * Clicks an element
     *
     * @param locator string locator
     * @throws NoSuchElementException thrown if no element is found
     */
    public static void click(String locator) throws Exception {
        click(Elements.findElement(locator));
    }

    /**
     * Clicks an element
     *
     * @param by                    - By selector to use
     * @throws                      - NoSuchElementException thrown if no element is found
     */
    public static void click(By by) throws Exception {
        click(Elements.findElement(by));
    }

    /**
     * Runs a pre-condition lambda, then clicks an element
     *
     * @param preCondition          - code to run before element is clicked
     * @param by                    - By selector to use
     * @throws                      - NoSuchElementException thrown if no element is found
     */
    public static void click(Runnable preCondition, By by) throws Exception {
        preCondition.run();
        click(Elements.findElement(by));
    }

    /**
     * Clicks an element, then runs an exit condition lambda
     *
     * @param by                    - By selector to use
     * @param exitCondition         - code to run after element is clicked
     * @throws                      - NoSuchElementException thrown if no element is found
     */
    public static void click(By by, Runnable exitCondition) throws Exception {
        click(Elements.findElement(by));
        exitCondition.run();
    }

    /**
     * clicks a WebElement
     *
     * @param el                    - By selector to use
     * @throws                      - NoSuchElementException thrown if no element is found
     */
    public static void click(WebElement el) throws Exception {
        int timeout = 10;
        if (el == null) {
            throw new NoSuchElementException("Unable to click null element");
        }

        WebDriver driver = WebDriverManager.getWebDriver();
        Actions actions = new Actions(driver);
        try {
            el = new WebDriverWait(driver, timeout).until(ExpectedConditions.elementToBeClickable(el));
        } catch (Exception ex) {
            try {
                throw new NoSuchElementException("Element not clickable: " + el.getTagName() + ": " + el
                        .getText() + ": " + ex.getMessage());
            } catch (StaleElementReferenceException exc) {
                throw new NoSuchElementException("Element not clickable: " + exc.getMessage());
            }
        }

        try {
            actions.moveToElement(el).build().perform();
            actions.click(el).build().perform();
            Logger.debug("Clicked element: " + el.toString());
        } catch (WebDriverException ex) {
            Logger.warn("Error while clicking, trying JS: " + ex.getMessage());
        }
    }

    /**
     * Moves to a specified web element and clicks on a specified WebElement
     * @param elementToMoveTo       - Web element to move to
     * @param elementToClick        - Web element to click on
     * @throws Exception            - NoSuchElementException - if element not found, throw exception
     */
    public static void moveToAndClickOn(WebElement elementToMoveTo, WebElement elementToClick) throws Exception {
        Elements.moveToElement(elementToMoveTo);
        click(elementToClick);

        //get the cursor off the dropdown
        Elements.moveToElement(By.className("logo"));
    }

    /**
     * Double clicks on the given element
     *
     * @param locator               - element to double-click on
     * @throws Exception            - if element not found, throw NoSuchElementException exception
     */
    public static void doubleClick(String locator) throws Exception {
        WebElement element = Elements.findElement(locator);
        doubleClick(element);
    }

    /**
     * Double clicks on the given element
     *
     * @param element               - element to double-click on
     */
    public static void doubleClick(WebElement element) {
        Actions actions = new Actions(WebDriverManager.getWebDriver());
        actions.moveToElement(element).click().click().build().perform();
    }

    /**
     * Clicks on an element
     * Waits for up to a specified timeout to see if the clicked element is no longer visible.
     *
     * @param locator               - The locator to validate no longer exists to signify the page loaded.
     * @param tries                 - (optional)  - Number of tries for clicking the element and waiting for it to be no longer visible.
     *                              - default executes the loop for two times.
     * @param timeout               - Milliseconds to wait for the page to load. If timeOut &lt;= 0 then the default timeout
     *                                is used (1000 milliseconds).
     * @throws Exception            - throws NoSuchElementException exception if element was not found
     */
    public static void clickAndRetry(String locator, int timeout, int... tries) throws Exception {

        int numberOfTries = tries.length > 0 ? tries[0] : 1;
        do {
            if (Wait.waitForVisible(locator, 1, false)) {
                click(locator);
            } else break;
            Wait.waitForNotVisible(locator, timeout, false);
            numberOfTries--;
        }
        while (numberOfTries == 0);
    }
}
