/**
 * Logger class
 *
 * This class is used to keep track of any actions across the project
 * and is color based to be easier to spot different types of logs.
 *
 * @author Vlad Nuna
 * @date  14/May/2020
 */

package AutomationFramework.utils;


/**
 * Class responsible for logs across the framework.
 */
public abstract class Logger {

    public static final String ANSI_RESET       = "\u001B[0m";
    public static final String ANSI_RED         = "\u001B[31m";
    public static final String ANSI_GREEN       = "\u001B[32m";
    public static final String ANSI_YELLOW      = "\u001B[33m";
    public static final String ANSI_BLUE        = "\u001B[34m";
    public static final String ANSI_DARK_GREY   = "\u001B[30m";

    /***
     * Log a info message (print line dark grey), requires a message
     * @param message The message that needs to be logged
     */
    public static void info(String message){
        System.out.println(ANSI_DARK_GREY + " : " + message + ANSI_RESET);
    }

    /***
     * Log a success message (print line green), requires a message
     * @param message The message that needs to be logged
     */
    public static void success(String message){
        System.out.println(ANSI_GREEN + " : " + message + ANSI_RESET);
    }

    /***
     * Log a warn message (print line yellow), requires a message
     * @param message The message that needs to be logged
     */
    public static void warn(String message){
        System.out.println(ANSI_YELLOW + " : " + message + ANSI_RESET);
    }

    /***
     * Log a error message (print line red), requires a message
     * @param message The message that needs to be logged
     */
    public static void error(String message){
        System.out.println(ANSI_RED + " : " + message + ANSI_RESET);
    }

    /***
     * Throws a new exception message based on provided string.
     *
     * @param message
     * @throws Exception throws exception if used
     */
    public static void exception(String message) throws Exception {
        message = ANSI_RED + " : " + message + ANSI_RESET;
        throw new Exception(message);
    }

    /***
     * Log a debug message (print line blue), requires a message
     * @param message The message that needs to be logged
     */
    public static void debug(String message){
        System.out.println(ANSI_BLUE + " : " + message + ANSI_RESET);
    }
}
