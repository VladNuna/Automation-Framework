package AutomationFramework.interactions;

import java.util.ArrayList;
import java.util.Map;

/***
 * PageElement class responsible for elements organization, this class contains element's selectors and a list of all locators available
 */
public class PageElement {

    public String elementBy = null;
    public String elementSelector = null;
    public String elementName = null;
    public ArrayList<String> elementLocators = new ArrayList<String>();

    /**
     * Setup and read page element data
     *
     * @param locator       - element locator from repo
     * @throws Exception    - throws exeption
     */
    public PageElement(String locator) throws Exception {
        Object object = new Object();

        /***
         * SuppressWarnings -> What is the SuppressWarnings annotation? A standard annotation that suppresses warnings for the annotated part of the program.
         *                     (http://www.angelikalanger.com/GenericsFAQ/FAQSections/TechnicalDetails.html#What%20is%20the%20SuppressWarnings%20annotation?)
         * Source:
         *    http://www.angelikalanger.com/GenericsFAQ/FAQSections/ProgrammingIdioms.html#FAQ300
         *
         */
        @SuppressWarnings(value = "unchecked")
        Map<String, ArrayList<String>> locators = (Map<String, ArrayList<String>>) object.getClass().getMethod("getAll").invoke(object);
        parseValue(locators, locator);
    }

    /**
     * Parses the locators and values for a page element
     *
     * @param locators      - values in format 'id, selector'
     * @param elementName   - element to match
     * @return              - returns the locators
     */
    private ArrayList<String> parseValue(Map<String, ArrayList<String>> locators, String elementName){
        if (locators == null) {
            return elementLocators;
        }

        for(String s : locators.keySet()){
            if(s.equals(elementName)){
                this.elementBy = locators.get(s).get(0);
                this.elementSelector = locators.get(s).get(1);
                this.elementName = s;
                setLocators();
            }
        }

        return elementLocators;
    }

    /***
     * Sets the locators
     */
    private void setLocators(){
        elementLocators.add(elementBy);
        elementLocators.add(elementSelector);
    }

    /***
     * Override the toString() method to print elementBy and element selector while coding.
     *
     * @return          - A string concatenation between this.elementBy and this.elementSelector
     */
    public String toString() {
        return this.elementBy + ", " + this.elementSelector;
    }
}
