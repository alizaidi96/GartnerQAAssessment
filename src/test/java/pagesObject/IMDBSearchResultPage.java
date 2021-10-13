package pagesObject;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.relevantcodes.extentreports.LogStatus;

import uiHelpers.BrowserActions;
import uiHelpers.Reporting;

public class IMDBSearchResultPage {
	
	WebDriver driver = null;

    Reporting report = null;

    BrowserActions browserAction = null;

    String imgPath = "";
    
    public IMDBSearchResultPage(WebDriver driver, Reporting report) {
    	this.driver = driver;
    	this.report = report;
    	
    	PageFactory.initElements(driver, this);
    	
    	browserAction = new BrowserActions(driver, report);
        imgPath = report.imagePath;
    }
    
    
    /** *-*-*-*-* IMDB SEARCH RESULT PAGE OBJECTS *-*-*-*-* */
    
    // Table containing the list of movies fetched after search
    @FindBy(how = How.XPATH, using = "//table[@class='findList']/tbody/tr/td[2]/a")
    private List<WebElement> searchResults;
    
    
    
    /** *-*-*-*-* IMDB HOME PAGE METHODS *-*-*-*-* */
	
    // Enter text in Search field
    public List<String> returnSearchResult() {
    	List<String> searchResult = new ArrayList<String>();
    	try {
    		for(WebElement webElement : searchResults)
    			searchResult.add(browserAction.getText(webElement));
    		report.logStepToReport(LogStatus.INFO, "API response filtered to get the required data", imgPath);
    	} catch(Exception e) {
    		throw e;
    	}
    	return searchResult;
    }

}
