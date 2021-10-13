package executor;

import java.util.Map;

import org.openqa.selenium.WebDriver;

import uiHelpers.Reporting;

public interface TestInterface {
	
	public void testCasesSelection(Map <String,String> testData, Reporting report, WebDriver driver, Map<String, Map<String, String>> testData_API);
	
}
