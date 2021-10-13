package testcases;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONArray;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.LogStatus;

import executor.Executor;
import executor.TestInterface;
import uiHelpers.Drivers;
import uiHelpers.Reporting;
import pagesObject.IMDBHomePage;
import pagesObject.IMDBSearchResultPage;

import restHelpers.APIExecutor;

public class IMDBTest001 extends Drivers implements TestInterface {
	
	public Reporting report;
	
	public List<Movies> expectedMoviesList;
	
	public IMDBHomePage objIMDBHomePage;
	public IMDBSearchResultPage objIMDBSearchPage;
	
	public String status = "";

	/** Implementation of Interface-TCSelection's method */
	
	public void testCasesSelection(Map<String, String> testData, Reporting reportObj, WebDriver driver,
			Map<String, Map<String, String>> testData_API) {
		
		this.driver = driver;
		this.report = reportObj;
		
		// Initializing Page Objects for IMDB application webpages
		objIMDBHomePage = new IMDBHomePage(driver, report);
		objIMDBSearchPage = new IMDBSearchResultPage(driver, report);
		
		try {
			String testMethod = testData.get("TestCaseName");
			System.out.println("Test Case ID - " + testMethod);
			
			// STEP 1 - Search API call for type=movie, s='lord of the rings'
			makeAPICall(testData, report, testData_API);
			
			// STEP 2 - Navigate to IMDB website
			System.out.println("NAVIGATING TO URL - " + Executor.URL);
			navigateToURL(Executor.URL);
			
			// Step 3 - Search for 'lord of the rings' in lookup input search box
			searchIMDB(testData);
			
			// Step 4 - Check/assert that displaying results table contains movie titles from filtered response
			verifySearchResults(testData, report, testData_API);
			
			
		} catch(Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			report.assertThat(false, String.format("Test Case %s should not fail", testData.get("TestCaseID")), e.getMessage());
		}
		
	}
	
	
	// STEP 1 - Making the API call to get the expected data
	public void makeAPICall(Map<String, String> testData, Reporting report,
			Map<String, Map<String, String>> testData_API) throws Exception {
		
		report.sectionInfo("MAKE SEARCH API CALL FOR type=movie, s='lord of the rings'");
		
		// Calling the API
		String titleResponse = APIExecutor.executeAPI(testData_API.get("IMDB001"), report, true);
		System.out.println(titleResponse);
		System.out.println("\n* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
		
		// Parsing the JSON string
		JSONObject jsonObject = new JSONObject(titleResponse);
		System.out.println(jsonObject.getJSONArray("Search"));
		
		// expectedMoviesList list of type Movies created
		expectedMoviesList = new ArrayList<Movies>();
		JSONArray jsonArr = jsonObject.getJSONArray("Search");
		
		// Filtering the response and adding the needed data in the list
		for (int i = 0; i < jsonArr.length(); ++i) {
            JSONObject obj = jsonArr.getJSONObject(i);
            String title = obj.getString("Title");
            if (title.equals("The Lord of the Rings: The Fellowship of the Ring") || 
            		title.equals("The Lord of the Rings: The Two Towers") ||
            		title.equals("The Lord of the Rings: The Return of the King")) {
            	expectedMoviesList.add(new Movies(obj.getString("Title"), 
            			obj.getString("Year"), 
            			obj.getString("imdbID"),
            			obj.getString("Type"), 
            			obj.getString("Poster")));
            }
        }
		
	}
	
	// STEP - 2 Go to IMDB and search for 'lord of the rings'
	public void searchIMDB(Map<String, String> testData) {
		
		report.sectionInfo("GO TO IMDB AND SEARCH FOR 'LORD OF THE RINGS'");
		
		objIMDBHomePage.enterSearchTerm(testData.get("SearchData"));
		objIMDBHomePage.clickOnCategoryButton();
		objIMDBHomePage.selectTitlesFromCategory();
		objIMDBHomePage.clickOnSearchButton();
	}
	
	// STEP - 3 Check that displayed results contain movie titles from filtered response
	public void verifySearchResults(Map<String, String> testData, Reporting report,
			Map<String, Map<String, String>> testData_API) {
		
		report.sectionInfo("CHECK THAT RESULTS TABLE CONTAINS MOVIE TITLES FROM FILTERED RESPONSE");
		
		List<String> searchResults = objIMDBSearchPage.returnSearchResult();
		System.out.println("Total number of records searched = " + searchResults.size());
		
		for(Movies m : expectedMoviesList) {
			report.logStepToReport(LogStatus.INFO, "Verify the presence of movie - <b>" + m.getTitle(), "");
			String expectedTitle = m.getTitle();
			for(String actualTitle : searchResults) {
				if(actualTitle.equalsIgnoreCase(expectedTitle)) {
					report.logStepToReport(LogStatus.PASS, "<b>Expected</b> : " + expectedTitle + "<br /> <b>Actual</b> : <b><font color='green'>" + actualTitle, "");
					break;
				}
			}
		}
	}
	
	//********************* PRIVATE FUNCTIONS *********************************
	
	private void navigateToURL(String URL) {
		try {
			report.logStepToReport(LogStatus.INFO,
					"Navigated to URL - <font color='#990099'>" + URL + "</font>", "");
			driver.navigate().to(URL);
		} catch (Exception e) {
			System.out.println("EXCEPTION IN NAVIGATION TO URL - " + e.getMessage());
		}
	}

}

// Defining a POJO class to store the expected Movies
class Movies {
	
	public String title;
	public String year;
	public String imdbID;
	public String type;
	public String poster;
	
	public Movies(String title, String year, String imdbID, String type, String poster) {
		this.title = title;
		this.year = year;
		this.imdbID = imdbID;
		this.type = type;
		this.poster = poster;
		
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getYear() {
		return year;
	}
	
	public String getimdbID() {
		return imdbID;
	}
	
	public String getType() {
		return type;
	}
	
	public String getPoster() {
		return poster;
	}
}
