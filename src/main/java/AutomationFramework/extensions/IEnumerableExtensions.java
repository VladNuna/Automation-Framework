package AutomationFramework.extensions;

import AutomationFramework.utils.Logger;
import org.openqa.selenium.WebElement;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

/**
 * Represents IEnumerableExtensions description
 */
public abstract class IEnumerableExtensions {

    /**
     * Returns a randomly chosen element from a List<WebElement>
     *
     * @param elements - list of elements to get the random from
     * @return - randomly chosen WebElement
     */
    public static WebElement randomElement(List<WebElement> elements) {
        int x = new Random().nextInt(elements.size());
        return elements.get(x);
    }

    /**
     * Returns a randomly chosen element from a List<WebElement>
     *
     * @param elements - list of elements to get the random from
     * @return - randomly chosen WebElement
     */
    public static WebElement randomElement(List<WebElement> elements, Predicate condition) {
        int x = new Random().nextInt(elements.size());
        return elements.get(x);
    }

    /**
     * Returns a randomly chosen int value from given limits
     *
     * @param max - max number
     * @param min - optional min number. If this is not provided, then 0 is the min limit
     * @return - randomly chosen int
     */
    public static int randomNumber(int max, int... min) {
        int minVal;
        try {
            minVal = min[0] != -1 ? min[0] : 0;
        } catch (Exception e) {
            minVal = 0;
        }
        int test = (int) ((Math.random() * (max - minVal + 1)) + minVal);
        Logger.success(String.format("Choosing %d between %d and %d", test, max, minVal));
        return test;
    }

    /**
     * Returns a randomly chosen element from a List<WebElement>
     *
     * @param object - generic collection to get the random from
     * @return - randomly chosen object from collection
     */
    public static Object random(Object object) {
        if (object instanceof Collection<?>) {
            return ((Collection) object).toArray()[(new Random().nextInt(((Collection) object).size()))];
        }

        if (object instanceof Map<?, ?>) {
            return ((Map) object).get(new Random().nextInt(((Map) object).size()));
        }

        return null;
    }
}
