package uiHelpers;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import executor.Executor;

public class Drivers {
	public WebDriver driver;

    public String driverBasepath = Executor.PROJECT_PATH + "/GridFiles/drivers/";
    
    public WebDriver OpenBrowser()
    {
        // Check current browser name and assign constant to it
    	/* Setting WindowsChrome for assessment purposes but we can fetch the browser name from the configuration
    	file if need be */
        String browserCode = Executor.browser;
        
        // Setting the download directory to the Reports folder
        String downloadDir = Executor.PROJECT_PATH + File.separator + "Reports" + File.separator
				+ Executor.uniqueID + File.separator + "UIReport";

        HashMap<String, Object> preferences = new HashMap<String, Object>();
        
        try {
        	
            switch (browserCode) {
            	
            	case "GoogleChrome":
            		
            		// Setting some basic preferences that are always needed for a smooth execution on Google Chrome
		        	preferences.put("profile.default_content_settings.popups", 0);
		        	preferences.put("download.default_directory", downloadDir + File.separator + "WindowsChrome");
		        	preferences.put("download.prompt_for_download", false);
		        	preferences.put("download.directory_upgrade", true);
		        	preferences.put("safebrowsing.enabled", true);
		        	preferences.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
            	
		        	ChromeOptions options = new ChromeOptions();
		        	options.setExperimentalOption("prefs", preferences);
		        	
		        	// Setting up the desired capabilities required
		            DesiredCapabilities chromeCapabilities = DesiredCapabilities.chrome();
		            chromeCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
		            chromeCapabilities.setCapability("chrome.switches", Arrays.asList("--ignore-certificate-errors"));
		            chromeCapabilities.setCapability("profile.default_content_settings.popups", 0);
		            chromeCapabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
                	
                    System.setProperty("webdriver.chrome.driver", driverBasepath + "chromedriver.exe");
                    driver = new ChromeDriver(options);

	                driver.manage().window().maximize();
	                break;
            	
            	case "EdgeChromium":
            		break;

                case "InternetExplorer":
                    break;

                case "Firefox":
                	break;

                case "LinuxChrome":
                	break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return driver;
    }
}
