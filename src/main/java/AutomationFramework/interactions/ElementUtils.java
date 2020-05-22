package AutomationFramework.interactions;

import AutomationFramework.runner.WebDriverManager;
import AutomationFramework.utils.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

 /***
  * ElementUtils class responsible for element refresh whenever it gets stale and also to avoid StaleElementReferenceException
  *  Source: http://www.amitrawat.tech/post/mystery-of-stale-element-reference-exception/
  */
public class ElementUtils {

    /***
     * Refresh a specified web element and returns it if possible.
     *
     * @param elem              - Web element object
     * @param params            - Optional parameters for filtering/position
     * @return                  - The refreshed web element
     */
    public static WebElement refreshElement(WebElement elem, Object... params) {
        Object refreshedElem = null;
        try {
            String[] locators = elem.toString().split("->");
            for (String s : locators) {
                String newLocator = s.trim().replaceAll("^\\[+", "").replaceAll("]+$", "");
                String[] parts = newLocator.split(": ");
                String key = parts[0];
                String value = parts[1];
                int leftBracketsCount = value.length() - value.replace("[", "").length();
                int rightBracketscount = value.length() - value.replace("]", "").length();
                if (leftBracketsCount - rightBracketscount == 1) { value = value + "]"; }
                if (refreshedElem == null) {
                    refreshedElem = WebDriverManager.getWebDriver();
                } else {
                    refreshedElem = getWebElement(refreshedElem, key, value, params);
                }
            }
        } catch (Exception e) {
            Logger.error("Can not refresh element: \n EXCEPTION: " + e.getMessage());
        }

        Logger.info("Refreshed element: " + elem.toString());
        return (WebElement) refreshedElem;
    }

    /***
     * Method used to refresh a webElement selected by “By”
     * Returns the refreshed element.
     *
     * @param by                - By selector to use
     * @param params            - Optional parameters for filtering/position
     * @return                  - The refreshed web element
     */
    public static WebElement refreshElement(By by, Object... params) {
        Object refreshedElem = null;
        try {
            String[] locators = Elements.findElement(by).toString().split("->");
            for (String s : locators) {
                String newLocator = s.trim().replaceAll("^\\[+", "").replaceAll("]+$", "");
                String[] parts = newLocator.split(": ");
                String key = parts[0];
                String value = parts[1];
                int leftBracketsCount = value.length() - value.replace("[", "").length();
                int rightBracketscount = value.length() - value.replace("]", "").length();
                if (leftBracketsCount - rightBracketscount == 1) { value = value + "]"; }
                if (refreshedElem == null) {
                    refreshedElem = WebDriverManager.getWebDriver();
                } else {
                    refreshedElem = getWebElement(refreshedElem, key, value, params);
                }
            }
        } catch (Exception e) {
            Logger.error("Can not refresh element: \n EXCEPTION: " + e.getMessage());
        }

        assert refreshedElem != null;
        Logger.info("Refreshed element: " + refreshedElem.toString());
        return (WebElement) refreshedElem;
    }

    /***
     * Checks if is element stale.
     *
     * @param element           - Optional parameters for filtering/position
     * @return                  - Returns true, if is element stale
     */
    public static boolean isElementStale(WebElement element) {
        try {
            element.isDisplayed();
            return false;
        } catch (StaleElementReferenceException ex) {
            return true;
        }
    }

    /***
     * Gets the web element based on key and value.
     *
     * @param lastObject        - The last object provided
     * @param key               - the key used in getBy method
     * @param value             - the value same as above
     * @param params            - optional parameters for
     * @return                  - return the actual webElement
     */
    private static WebElement getWebElement(Object lastObject, String key, String value, Object... params) {
        List<WebElement> elements = null;
        try {
            By by = getBy(key, value);
            Method m = getCaseInsensitiveDeclaredMethod(lastObject, "findElements");
            elements = (List<WebElement>) m.invoke(lastObject, by);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        assert elements != null;
        WebElement element = params.length <= 0 ? elements.get(0) : elements.get((Integer) params[0]);
        if (params.length > 1) {
            return (Boolean)params[1] ? element.findElement(By.xpath("..")) : element;
        }
        return element;
    }

    /***
     * Gets the by selector based on provided key and value.
     *
     * @param key                           - the key used in getBy method
     * @param value                         - the value same as above
     * @return                              - the by
     * @throws InvocationTargetException    - the invocation target exception
     * @throws IllegalAccessException       - the illegal access exception
     *
     */
    private static By getBy(String key, String value) throws InvocationTargetException, IllegalAccessException {
        Class cls = By.class;
        String methodName = key.replace(" ", "");
        Method mth = getCaseInsensitiveStaticDeclaredMethod(cls, methodName);
        return (By) mth.invoke(null, value);
    }

    /***
     * Gets the case insensitive declared method.
     *
     * @param obj               - The object where we need to find the method name
     * @param methodName        - The method name that we need to find
     * @return                  - the case insensitive declared method
     */
    private static Method getCaseInsensitiveDeclaredMethod(Object obj, String methodName) {
        Method[] methods = obj.getClass().getMethods();
        Method method = null;
        for (Method m : methods) {
            if (m.getName().equalsIgnoreCase(methodName)) {
                method = m;
                break;
            }
        }
        if (method == null) {
            throw new IllegalStateException(String.format("%s Method name is not found for this Class %s",
                    methodName, obj
                            .getClass().toString()));
        }
        return method;
    }

    /***
     * Gets the case insensitive static declared method.
     *
     * @param cls               - The class
     * @param methodName        - The method name
     * @return                  - The case insensitive static declared method
     */
    private static Method getCaseInsensitiveStaticDeclaredMethod(Class cls, String methodName) {
        Method[] methods = cls.getMethods();
        Method method = null;
        for (Method m : methods) {
            if (m.getName().equalsIgnoreCase(methodName)) {
                method = m;
                break;
            }
        }
        if (method == null) {
            throw new IllegalStateException(String.format("%s Method name is not found for this Class %s",
                    methodName, cls
                            .toString()));
        }
        return method;
    }
}
