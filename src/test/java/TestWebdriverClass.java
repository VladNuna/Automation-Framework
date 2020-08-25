import AutomationFramework.runner.WebDriverManager;
import Pages.TQA_TextBox;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestWebdriverClass {
    TQA_TextBox elementsPage;

    @BeforeMethod
    public void beforeSuite() {
        WebDriverManager.startWebDriver();
        WebDriverManager.openURLPage("https://demoqa.com/elements");
        elementsPage = PageFactory.initElements(WebDriverManager.getWebDriver(), TQA_TextBox.class);
    }

    @Test
    public void clickOnCheckboxAndCheckIfInputsAreVisible() throws Exception{
        elementsPage.clickOnCheckboxAndCheckIfInputsAreVisible();
    }

    @AfterSuite
    public void closeWebDriver() throws Exception {
        WebDriverManager.stop();
    }
}
