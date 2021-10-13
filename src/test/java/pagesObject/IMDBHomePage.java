package pagesObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import uiHelpers.BrowserActions;
import uiHelpers.Reporting;

public class IMDBHomePage {
	
	WebDriver driver = null;

    Reporting report = null;

    BrowserActions browserAction = null;

    String imgPath = "";
    
    public IMDBHomePage(WebDriver driver, Reporting report) {
    	this.driver = driver;
    	this.report = report;
    	
    	PageFactory.initElements(driver, this);
    	
    	browserAction = new BrowserActions(driver, report);
        imgPath = report.imagePath;
    }
    
    
    /** *-*-*-*-* IMDB HOME PAGE OBJECTS *-*-*-*-* */
    
    // Search box - input element
    @FindBy(how = How.XPATH, using = "//input[@type='text' and @id='suggestion-search']")
    private WebElement searchBox;
    
    // Search button - button element
    @FindBy(how = How.XPATH, using = "//button[@id='suggestion-search-button']")
    private WebElement searchButton;
    
    // Category drop down - div element
    @FindBy(how = How.XPATH, using = "//div[@class='SearchCategorySelector__StyledContainer-sc-18f40f7-0 fEgMct search-category-selector']")
    private WebElement categoryButton;
    
    // Category Selection list
    @FindBy(how = How.XPATH, using = "(//ul[@class='ipc-list _2crW0ewf49BFHCKEEUJ_9o ipc-list--baseAlt']/a)[2]")
    private WebElement categoryTitles;
    
    
    
    /** *-*-*-*-* IMDB HOME PAGE METHODS *-*-*-*-* */
	
    // Enter text in Search field
    public void enterSearchTerm(String search) {
    	try {
    		browserAction.setText(searchBox, search, imgPath, "Searching for movie - <font color='green'>"+ search);
    	} catch(Exception e) {
    		throw e;
    	}
    }
    
    // Click on Search button
    public void clickOnSearchButton() {
    	try {
    		browserAction.click(searchButton, imgPath, "Search Button clicked");
    	} catch(Exception e) {
    		throw e;
    	}
    }
    
    //Click on Category button
    public void clickOnCategoryButton() {
    	try {
    		browserAction.click(categoryButton, imgPath, "Category Button clicked");
    	} catch(Exception e) {
    		throw e;
    	}
    }
    
    // Select Titles from Category
    public void selectTitlesFromCategory() {
    	try {
    		browserAction.click(categoryTitles, imgPath, "Titles category selected");
    	} catch(Exception e) {
    		throw e;
    	}
    }
}
