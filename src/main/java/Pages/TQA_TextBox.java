package Pages;


import AutomationFramework.interactions.Clicks;
import AutomationFramework.interactions.Elements;
import AutomationFramework.utils.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import java.util.List;

enum ListElements{
                                        // ID
    TEXT_BOX                        ("item-0"),
    CHECK_BOX                       ("item-1"),
    RADIO_BUTTON                    ("item-2"),
    WEB_TABLES                      ("item-3"),
    BUTTONS                         ("item-4"),
    LINKS                           ("item-5"),
    UPLOAD_AND_DOWNLOAD             ("item-6"),
    DYNAMIC_PROPERTIES              ("item-7");

    private String id;

    /***
     * Constructor that sets the id based on provided data
     * @param id
     */
    ListElements(String id) {
        this.id = id;
    }

    /***
     * @return Return id as String
     */
    public String getId() {
        return id;
    }
}

public class TQA_TextBox {

    @FindBy(className = "main-header") // Header Text
    private WebElement headerElement;

    private String expectedTitle = "text box";

    public void clickOnCheckboxAndCheckIfInputsAreVisible() throws Exception{
        WebElement textBox = Elements.findElement(By.id(ListElements.TEXT_BOX.getId()));
        Assert.assertNotNull(textBox);
        Clicks.click(textBox);
        if(checkIfHeaderIsTextBox()){
            checkInputsToBeVisible();
        }else{
            Assert.assertTrue(headerElement.getText().equalsIgnoreCase(expectedTitle));
        }
    }

    private boolean checkIfHeaderIsTextBox(){
        return headerElement.getText().equalsIgnoreCase(expectedTitle);
    }

    private void checkInputsToBeVisible() throws Exception{
        WebElement fullName             = Elements.findElement(By.id("userName"));
        WebElement email                = Elements.findElement(By.id("userEmail"));
        WebElement currentAddress       = Elements.findElement(By.id("currentAddress"));
        WebElement permanentAddress     = Elements.findElement(By.id("permanentAddress"));
        WebElement submitButton         = Elements.findElement(By.id("submit"));

        Assert.assertTrue(fullName.isDisplayed());
        Assert.assertTrue(email.isDisplayed());
        Assert.assertTrue(currentAddress.isDisplayed());
        Assert.assertTrue(permanentAddress.isDisplayed());
        Assert.assertTrue(submitButton.isDisplayed());

        List<WebElement> outputVisibleElements = fullName.findElements(By.className("mb-1"));

        Logger.debug("Children Count is: " + outputVisibleElements.size());
        for (WebElement elem: outputVisibleElements) {
            Assert.assertTrue(elem.getText().isEmpty());
        }
    }
}
