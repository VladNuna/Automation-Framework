package AutomationFramework.utils;

/**
 * Class responsible for custom assert logs.
 */
public class AFCustomAsserts {
    /**
     * Validates that two objects are equal.
     *
     * @param actual   Actual result
     * @param expected Expected result
     * @throws Exception throws exception if not equal
     */
    public static <E> void VerifyEqual(E actual, E expected) throws Exception {
        try {
            if (actual.toString().trim().equals(expected.toString().trim())) {
                Logger.success(String.format("Values are correct. Actual: %s Expected: %s", actual.toString().trim(), expected.toString().trim()));
            } else {
                Logger.exception("Values are not equal! \n" +
                        String.format("Actual: %s \n", actual.toString().trim()) +
                        String.format("Expected: %s", expected.toString().trim())
                );
            }
        } catch (Exception e) {
            Logger.exception(e.getMessage());
        }
    }
}
