package AutomationFramework.utils;

public class Utils {
    /**
     * Sleeps for a given time
     *
     * @param sleepTime time to sleep in millis
     * @param msg       info message to display
     * @return true if sleep interrupted
     */
    public static boolean threadSleep(long sleepTime, String msg) {
        Thread cur = Thread.currentThread();
        try {
            if (msg != null) { Logger.debug("Thread \"" + cur.getName() + "\" sleeping for: " + sleepTime + "ms"); }
            Thread.sleep(sleepTime);
            if (msg != null) { Logger.debug("Thread \"" + cur.getName() + " is awake"); }
            return false;
        } catch (InterruptedException e) {
            if (msg != null) { Logger.debug("Thread \"" + cur.getName() + " was interrupted:" + e.getMessage()); }
            return true;
        }
    }
}
