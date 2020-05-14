package AutomationFramework.exceptions;

/**
 * Exception thrown when driver is requested but not initialized
 */
public class DriverNotInitializedException extends RuntimeException {
    public DriverNotInitializedException(String msg) {
        super("ERROR - APP: " + msg);
    }
}
