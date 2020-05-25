package AutomationFramework.interactions;

import AutomationFramework.runner.WebDriverManager;
import AutomationFramework.utils.Logger;
import AutomationFramework.utils.Utils;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.Dimension;
import java.awt.*;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Elements {

    /***
     * Creates the By selector based on a predefined names (class, classname, id, css, xpath or name)
     *
     * @param selector          - The unique identifier of the object (id name, class name etc)
     * @return                  - Returns the selector based on given (By.id, By.className etc)
     * @throws Exception        - Throws exception if element is null or if we do not have a valid selector (classname, id, css etc)
     */
    public static By by(String selector) throws Exception {
        PageElement pageElement = new PageElement(selector);
        if (pageElement.elementBy == null) {
            Logger.exception("null selector provided!");
        }

        if (!isValidLocator(pageElement.elementBy)) {
            Logger.exception("Not a valid selector ! Please check the exception log.");
        }

        switch (pageElement.elementBy.toLowerCase()) {
            case "class":
            case "classname": {
                return By.className(pageElement.elementSelector);
            }
            case "id": {
                return By.id(pageElement.elementSelector);
            }
            case "css": {
                return By.cssSelector(pageElement.elementSelector);
            }
            case "xpath": {
                return By.xpath(pageElement.elementSelector);
            }
            case "name": {
                return By.name(pageElement.elementSelector);
            }
            default:
                throw new Exception("This case not implemented yet for By: " + pageElement.elementBy);
        }
    }

    /***
     * Method that returns true if the specified selector is a valid / supported otherwise returns false;
     * Note: Currently I have implemented only the basic ones but this will be changed based on project needs
     *
     * @param selector          - The name of the selector id, name, xpath etc (String)
     * @return                  - return true if specified selector is within our list otherwise returns false
     */
    private static boolean isValidLocator(String selector) {
        switch (selector) {
            case "id":
            case "name":
            case "xpath":
            case "class":
            case "cssSelector":
            case "css":
                return true;
            default:
                return false;
        }
    }

    /***
     * Get an attribute value "attr" from element with a specific name
     *
     * @param elementName       - name of the element
     * @param attr              - attribute name to retrieve
     * @return                  - requested attribute value if it exists, otherwise empty string ""
     */
    public static String getElementAttribute(String elementName, String attr) throws Exception {
        return getElementAttribute(elementName, attr, true);
    }

    /***
     * Get an attribute value "attr" from element with a specific name
     *
     * @param selector          - name of the object/element
     * @param attr              - attribute name to retrieve
     * @param throwException    - if true, throws exception if the attribute is not found
     * @return                  - requested attribute value if it exists, otherwise empty string ""
     * @throws Exception        - NullPointerException
     */
    public static String getElementAttribute(String selector, String attr, boolean throwException) throws Exception {
        try {
            WebElement el = findElement(selector);

            String attribute = el.getAttribute(attr);
            if (attribute == null) {
                if (throwException) {
                    throw new NullPointerException();
                }
                Logger.debug(String.format("Element has no %s attribute", attr));
            }
            return attribute;
        }
        catch (NullPointerException e) {
            return "";
        }
    }

    /***
     *  Gets an attribute value "attr" from the provided web element
     *
     * @param element           - name of the object/element
     * @param attr              - attribute name to retrieve
     * @param throwException    - if true, throws NullPointerException exception if the attribute is not found
     * @return                  - requested attribute value if it exists, otherwise empty string ""
     * @throws                  - NullPointerException it the attribute does not exist
     */
    public static String getElementAttribute(WebElement element, String attr, boolean throwException) {
        try {
            String attribute = element.getAttribute(attr);
            if (attribute == null) {
                if (throwException) {
                    throw new NullPointerException();
                }
                Logger.debug(String.format("Element has no %s attribute", attr));
            }
            return attribute;
        }
        catch (NullPointerException e) {
            return "";
        }
    }

    /***
     * Find the element on the webpage using the provided locator name
     *
     * @param locator           - element locator name
     * @return                  - returns the webelement
     * @throws                  - throws NoSuchElementException if element does not exist
     */
    public static WebElement findElement(String locator) throws Exception {
        return findElement(locator, true);
    }

    /***
     * Find the element on the webpage using the provided locator name
     *
     * @param locator           - element locator name
     * @param throwException    - if true and element is not found it throws NoSuchElementException
     * @return                  - returns the web element
     * @throws                  - throws NoSuchElementException if element does not exist
     */
    public static WebElement findElement(String locator, boolean throwException) throws Exception {
        return findElement(by(locator), throwException);
    }

    /***
     * Find the element on the webpage using the provided By (By.css("element"))
     *
     * @param by                - element by
     * @return                  - returns the web element
     * @throws                  - throws NoSuchElementException if element does not exist
     */
    public static WebElement findElement(By by) throws Exception {
        return findElement(by, true);
    }

    /***
     * Find the element on the web page using the provided By (By.xpath("element"))
     *
     * @param by                - from the element type of by
     * @param throwException    - if true and element is not found it throws NoSuchElementException
     * @return                  - returns the webelement
     * @throws                  - throws NoSuchElementException if element does not exist
     */
    public static WebElement findElement(By by, boolean throwException) throws Exception {
        Logger.info("Find element using selector : " + by.toString());
        try {

            try {
                //Waiting just a bit for the element to show up
                Wait.waitUntilJSReady();
                new WebDriverWait(WebDriverManager.getWebDriver(), 10).until(ExpectedConditions.presenceOfElementLocated(by));
            }
            catch (Exception e) {
                //nothing to do here
            }

            List<WebElement> elements = WebDriverManager.getWebDriver().findElements(by);
            if (elements == null || elements.size() == 0) {
                throw new NoSuchElementException("Unable to locate an element using selector : " + by.toString());
            }
            List<WebElement> visible = elements.stream().filter(WebElement::isDisplayed)
                    .collect(Collectors.toList());
            if (!visible.isEmpty()) {
                elements = visible;
            }

            return elements.get(0);
        }
        /**
         * StaleElementReferenceException - A stale element reference exception is thrown in one of two cases, the first being more common than the second:
         *
         * The element has been deleted entirely.
         * The element is no longer attached to the DOM.
         *
         * Note: If the element has been replaced with an identical one, a useful strategy is to look up the element again.
         * More details can be found here: https://www.selenium.dev/exceptions/#stale_element_reference.html
         */
        catch (StaleElementReferenceException e) {
            Logger.warn(e.getMessage());
            ElementUtils.refreshElement(by);
            findElement(by, throwException);
        }
        catch (NoSuchElementException ex) {
            if (throwException) {
                Logger.exception("No element found with selector: " + by.toString());
            }
            Logger.warn("No element found with selector: " + by.toString());
            Logger.warn("Unable to find element: " + ex);
        }

        return null;
    }

    /***
     * Retrieves a list of elements using a selector and filters them with the given Predicate if provided.
     * If no element is found it will not throw an exception, just logs an error and returns null
     *
     * @param locator           - name of the item
     * @param filter            - Predicate to filter results with
     * @return                  - list of WebElements selected by el after filter is applied
     */
    public static WebElement findElement(String locator, Predicate<WebElement> filter) throws Exception {
        return findElements(by(locator), filter, true).get(0);
    }

    /***
     * Return a web element using a selector and filter it based on the given Predicate if provided.
     * Return null if no object is find
     *
     * @param locator           - name of the item
     * @param filter            - Predicate to filter results with
     * @param throwException    - if true and element is not found it will return null
     * @return                  - A WebElements selected after filter is applied
     */
    public static WebElement findElement(String locator, Predicate<WebElement> filter, boolean throwException) throws Exception {
        return findElements(by(locator), filter, throwException).size() > 0 ? findElements(by(locator), filter, throwException).get(0) : null;
    }

    /***
     * Retrieves all elements using the provided selector name.
     *
     * @param locator           - name of the item
     * @return                  - list of WebElements selected by locator
     * @throws                  - throws an exception if no element is find
     */
    public static List<WebElement> findElements(String locator) throws Exception {
        return findElements(locator, null);
    }

    /***
     * Retrieves all elements using a selector and filters them with the given Predicate if provided.
     * If no element is found it will not throw an exception, just logs an error and returns null
     *
     * @param locator           - name of the item
     * @param filter            - Predicate to filter results with
     * @return                  - list of WebElements selected by el after filter is applied
     * @throws                  - throws an exception if no element is find
     */
    public static List<WebElement> findElements(String locator, Predicate<WebElement> filter) throws Exception {
        return findElements(by(locator), filter, true);
    }

    /***
     * Retrieves all visible elements using a given selector
     * This will return null if no elements are found, or an empty list if elements are found but not currently displayed.
     *
     * @param locator           - name of the item
     * @param throwException    - if true throws an Exception if no element is found. Otherwise it returns null
     * @return                  - list of WebElements selected by el
     * @throws                  - throws an exception if the element does not exist
     */
    public static List<WebElement> findElements(String locator, boolean throwException) throws Exception {
        return findElements(locator, null, throwException);
    }

    /***
     * Retrieves all elements using a selector and filters them with the given Predicate if provided
     *
     * @param locator           - name of the item
     * @param filter            - Predicate to filter results with
     * @param throwException    - if true throws an Exception if no element is found. Otherwise it returns null
     * @return                  - list of WebElements selected by el after filter is applied
     * @throws                  - throws an exception if the element does not exist
     */
    public static List<WebElement> findElements(String locator, Predicate<WebElement> filter, boolean throwException) throws Exception {
        return findElements(by(locator), filter, throwException);
    }

    /***
     * Retrieves all elements using a selector and filters them with the given Predicate if provided
     *
     * @param by                - from the element type of by and it's selector
     * @param filter            - Predicate to filter results with
     * @param throwException    - if true throws an Exception if no element is found. Otherwise it returns null
     * @return                  - list of WebElements selected by el after filter is applied
     * @throws                  - throws an exception if the element does not exist
     */
    public static List<WebElement> findElements(By by, Predicate<WebElement> filter, boolean throwException) throws Exception {
        List<WebElement> elements = null;
        Wait.waitNoMsg(ExpectedConditions.presenceOfElementLocated(by), false, 10);
        for (int i = 0; i < 3; i++) {
            try {
                elements = filter != null ?
                        WebDriverManager.getWebDriver().findElements(by).stream().filter(filter).collect(Collectors.toList()) :
                        WebDriverManager.getWebDriver().findElements(by);
            }
            catch (Exception ex) {
                if (throwException) {
                    Logger.exception("No elements found for selector: " + by.toString());
                }
                Utils.threadSleep(100, null);
            }
        }

        if (elements == null || elements.size() == 0) {
            if (throwException) {
                Logger.exception("No elements found with selector : " + by.toString());
            }
            Logger.error("No elements found with selector : " + by.toString());
        }
        return elements;
    }

    /**
     * Get a list of WebElements using chained elements
     *
     * @param parent            - parent element locator
     * @param selector          - child element locator
     * @return                  - list of found WebElements
     * @throws                  - throws NoSuchElementException if the element does not exist
     */
    public static List<WebElement> chainedElements(String parent, String selector) throws Exception {
        ByChained chained = new ByChained(by(parent), by(selector));
        return WebDriverManager.getWebDriver().findElements(chained);
    }

    /**
     * Method that checks if the element received by the selector has the given attribute
     *
     * @param locator           - element locator from repo
     * @param attribute         - attribute to check for
     * @return                  - returns true if found, false otherwise
     */
    public static boolean hasAttribute(String locator, String attribute) {
        boolean result = false;
        try {
            String value = Elements.findElement(Elements.by(locator)).getAttribute(attribute);
            if (value != null) {
                result = true;
            }
        }
        catch (Exception ignored) {
        }

        return result;
    }

    /**
     * Method that checks if the specified webelement has the given attribute
     *
     * @param element           - element
     * @param attribute         - attribute to check for
     * @return                  - returns true if found, false otherwise
     */
    public static boolean hasAttribute(WebElement element, String attribute) {
        boolean result = false;
        try {
            String value = element.getAttribute(attribute);
            if (value != null) {
                result = true;
            }
        }
        catch (Exception e) {
            Logger.error(e.getMessage());
        }

        return result;
    }

    /**
     * Check if element is displayed on the page
     *
     * @param locator           - element path in format "page_name.element_name"
     * @return                  - true if element is displayed on the page
     */
    public static boolean elementPresent(String locator, int... timeout) throws Exception {
        return elementPresent(by(locator), timeout);
    }

    /**
     * Checks if element is displayed on the page
     *
     * @param by                - By selector to use
     * @param timeout
     * @return                  - true if element is displayed on the page
     */


    public static boolean elementPresent(By by, int... timeout) { // ... Variable Arguments or varargs
        Duration waitForLoadTimeout = Duration.ofSeconds(timeout == null || timeout.length < 1 ? 5 : timeout[0]);

        Logger.info(String.format("Waiting %s milliseconds for '%s' element to be present in DOM...", waitForLoadTimeout.toMillis(), by.toString()));

        Date dateTimeBeforeWaitForLoad;
        try {
            new WebDriverWait(WebDriverManager.getWebDriver(), timeout == null || timeout.length < 1 ? 5 : timeout[0]).until(ExpectedConditions.presenceOfElementLocated(by));

            dateTimeBeforeWaitForLoad = new Date();
            Wait.waitUntilJSReady();
            new WebDriverWait(WebDriverManager.getWebDriver(), waitForLoadTimeout.getSeconds()).until(ExpectedConditions.visibilityOfElementLocated(by));
        }
        catch (Exception e) {
            Logger.error(String.format("Failed to wait for the element to be visible on the page since failed to validate visibility of %s within %d milliseconds.",
                    by.toString(), waitForLoadTimeout.toMillis()));
            return false;
        }

        long elapsed = new Date().getTime() - dateTimeBeforeWaitForLoad.getTime();
        Logger.debug(String.format("Element displayed: %s. Found in %d milliseconds", by.toString(), elapsed));
        return true;
    }



    /**
     * Uses the Robot class in order to move mouse cursor to the element
     * regardless of the used screen resolution or browser header configuration
     *
     * @param selector          - element to move to/hover
     * @throws Exception        - throws exception
     */
    public static void moveToElement(String selector)
            throws Exception {
        moveToElement(Elements.findElement(selector));
    }

    /**
     * Uses the Robot class in order to move mouse cursor to the element
     * regardless of the used screen resolution or browser header configuration
     *
     * @param locator           - element to move to/hover
     * @throws Exception        - throws exception
     */
    public static void moveToElement(By locator)
            throws Exception {
        moveToElement(Elements.findElement(locator));
    }

    /**
     * Uses the Robot class in order to move mouse cursor to the element
     * regardless of the used screen resolution or browser header configuration
     *
     * @param element           - element to move to/hover
     * @throws Exception        - throws exception
     */
    public static void moveToElement(WebElement element) throws Exception {
        try {
            Actions action = new Actions(WebDriverManager.getWebDriver());
            action.moveToElement(element).build().perform();
        }
        catch (Exception ex) {
            Logger.error(ex.getMessage());
            Robot robot = new Robot();
            //take screen resolution

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = (int) screenSize.getWidth();
            int screenHeight = (int) screenSize.getHeight();

            robot.mouseMove(screenWidth / 2, screenHeight / 2);
            //take webpage dimensions
            int webpageWidth = ((Number) (((JavascriptExecutor) WebDriverManager.getWebDriver())
                    .executeScript("return window.innerWidth"))).intValue();
            int webpageHeight = ((Number) (((JavascriptExecutor) WebDriverManager.getWebDriver())
                    .executeScript("return window.innerHeight"))).intValue();

            //take only the x axis point to calculate the middle of the element
            //browser header most likely to change, but not the horizontal part
            //and Point will take x coord of the left side of the element not middle
            int xMid = element.getSize().width / 2;

            //get element location relative to the webpage (not screen)
            Point coordinates = element.getLocation();

            //calculate x and y based on screen resolution and webpage dimension
            //then get to the middle of it
            int coordinatesX = (screenWidth - webpageWidth) + coordinates.x + xMid;
            int coordinatesY = (screenHeight - webpageHeight) + coordinates.y;

            //tries 10 times to set the cursor at correct position
            int tries = 10;
            while (MouseInfo.getPointerInfo().getLocation().x != coordinatesX &&
                    MouseInfo.getPointerInfo().getLocation().y != coordinatesY &&
                    tries > 0) {
                new Robot().mouseMove(0, 0);
                Thread.sleep(1000);

                new Robot().mouseMove(coordinatesX / 2, coordinatesY / 2);
                tries--;
            }
        }
    }
}
