package uiHelpers;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

public class BrowserActions {
	
	public WebDriver driver;

    public JavascriptExecutor jse;

    public Reporting report;

    public WebDriverWait wait;

    //public Actions actions;
    
    public BrowserActions(WebDriver driver, Reporting report)
    {
        this.driver = driver;
        this.report = report;
        //actions = new Actions(driver);
        jse = (JavascriptExecutor) driver;
    }
    
    
    // Wait for Page Load
    public void WaittoPageLoad()
    {
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>()
        {
            public Boolean apply(WebDriver driver)
            {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
                    .equals("complete");
            }
        };
        try {
            Thread.sleep(1000);
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(expectation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    // Click on Web Element
    public void click(WebElement element)
    {
        try {
            element.click();
        } catch (Exception e) {
            System.err.println("Exception in click - " + e.getLocalizedMessage());
        }
    }
    
    
    // Click on Web Element and log step in report
    public void click(WebElement element, String imagePath, String message)
    {
        try {
            element.click();
            report.logStepToReport(LogStatus.INFO, message, imagePath);
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    
    // Scroll to WebElement
    public void scrollToElement(WebElement element)
    {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            Actions actions = new Actions(driver);
            actions.moveToElement(element);
            actions.perform();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    // Get text of Web element
    public String getText(WebElement element)
    {
        String elementText = null;

        try {
            elementText = element.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return elementText;
    }
    
    
    // Set Text of Web element
    public void setText(WebElement element, String value)
    {
        try {
            element.sendKeys(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    // Set Text of Web element and log step in report
    public void setText(WebElement element, String value, String imagePath, String message)
    {
        try {
            element.sendKeys(value);
            report.logStepToReport(LogStatus.INFO, message, imagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    // Clear text from the field
    public void clearText(WebElement element)
    {
        try {
            element.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    // Wait for Web Element visibility and log step in report if element is not visible
    public void waitForElementVisibility(WebElement element)
    {
        try {
            Thread.sleep(500);
            wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    // Wait for Web element Clickable
    public void waitForElement(WebElement element)
    {
        try {
            Thread.sleep(500);
            wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    
    // Check for presence of web element
    public boolean isElementPresent(WebElement element, int timeout)
    {
    	boolean presence=false;
        for (int i = 0; i < timeout; i++) {
            try {
                Thread.sleep(1000);
                presence = element.isEnabled() || element.isDisplayed();
                if (presence) {
                    return presence;
                }
            } catch (Exception Ex) {
            }
        }

        System.err.println("Timeout Exception for element -" + Thread.currentThread().getStackTrace());
        
        return presence;
    }
    
    
    // Quit current browser window
    public void quitBrowser() {
    	driver.quit();
    }
    
    
    // Refresh the webpage
    public void pageRefresh() {
    	driver.navigate().refresh();
    }
    
    
    // Get any attribute of a web element
    public String getAttribute(WebElement element, String value) {
    	
    	String attributeValue = "";
    	try {
    		
    		attributeValue = element.getAttribute(value);
    		
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return attributeValue;
    }
    
    
    // Check if element is enabled
    public boolean isElementEnabled(WebElement element) {
    	
    	boolean enabled = false;
    	
    	try {
    		enabled = element.isEnabled(); 
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    	return enabled;
    }
	
}
