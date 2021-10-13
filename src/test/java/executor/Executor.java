package executor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import uiHelpers.PropertiesReader;

public class Executor {
	
	public static String uniqueID, URL, browser, PROJECT_PATH;
	public static PropertiesReader propReader = PropertiesReader.getInstance();
	
	static String status = "";
	String overAllStatus = "";
	
	public static void main(String[] args) {
		
		// Setting the name of the REPORT FOLDER with the date and time of execution to make it unique
		uniqueID = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		
		PROJECT_PATH = new File("").getAbsolutePath();
		
		try {
				
			// Check TimeStamp folder
			new File(PROJECT_PATH + File.separator + "Reports" + File.separator + uniqueID + File.separator + "UIReport").mkdirs();
			
			// Storing the URL
			URL = propReader.getProperty("URL");
			
			// Storing the browser name on which the execution is required
			browser = propReader.getProperty("browserName");
					
			// Start the test case execution
			startExecution();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void startExecution() {
		
		try {
			Controller controller = new Controller();
			controller.controllerMethod();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
