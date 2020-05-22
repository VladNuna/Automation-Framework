import AutomationFramework.runner.WebDriverManager;
import AutomationFramework.utils.Logger;
import org.testng.annotations.Test;

public class TestWebdriverClass {

    @Test
    public void testWebdriverStart(){
        WebDriverManager.startWebDriver();
        System.out.println(WebDriverManager.getCurrentUrl());
        WebDriverManager.resetDriver(true);

        Logger.debug("debug");
        Logger.error("Error");
        Logger.info("Info");
        Logger.success("Scuccess");
        Logger.warn("warn");
    }
}
