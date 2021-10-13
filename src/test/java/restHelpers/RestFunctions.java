package restHelpers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.poi.util.IOUtils;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestFunctions {
	DateFormat dateFormat	= new SimpleDateFormat("hhmmss");
	public RestFunctions() {

	}

	// Global Setup Variables

	public String path; // Rest request path

	/**
	 * FUNCTION - Sets Base URI: Before starting the test, we should set the
	 * RestAssured.baseURI
	 * 
	 * @param baseURI - Base URI to be set in String format
	 */
	
	public void setBaseURI(String baseURI) {
		RestAssured.baseURI = baseURI;
	}

	/**
	 * FUNCTION - Sets base path: Before starting the test, we should set the
	 * RestAssured.basePath
	 * 
	 * @param basePathTerm - Base Path to be used in String format
	 */
	
	public void setBasePath(String basePathTerm) {
		RestAssured.basePath = basePathTerm;
	}

	/**
	 * FUNCTION 	- Returns response
	 * @param req 	- Request specification for the request 
	 * @return		- Response for the request sent
	 */
	
	public Response getQueryResponse(RequestSpecification req) {
		return RestAssured.given().spec(req).get();
	}

	/**
	 * FUNCTION					- Returns response
	 * @param ParameterName2	- Parameter names as ',' separated values
	 * @param ParameterValue2	- Parameter values as ',' separated values
	 * @return					- Response for the request sent
	 */
	
	public Response getQueryResponse(String ParameterName2, String ParameterValue2, RequestSpecification req) {

		String[] parameterNameArray 	= ParameterName2.split(",");
		String[] parameterValueArray 	= ParameterValue2.split(",");
		
		Map<String, String> parameterNameValuePairs = new LinkedHashMap<>();

		for (int i = 0; i < parameterNameArray.length; i++) {
			
			if(parameterValueArray[i].startsWith("xmlAttributeValue")) {
				parameterValueArray[i] = parameterValueArray[i].replace("xmlAttributeValue(", "");
				parameterValueArray[i] = parameterValueArray[i].replace(")", "");
				String []temp = parameterValueArray[i].split(";");
				try {
					parameterValueArray[i] = new FunctionClass().getAttributeValue(temp[0], temp[1], temp[2],temp[3]);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (parameterNameArray[i].trim().length() > 0) {
				parameterNameValuePairs.put(parameterNameArray[i], parameterValueArray[i]);
			}
			//System.out.println(parameterNameValuePairs);
		}

		return RestAssured.given().spec(req).params(parameterNameValuePairs).when().get().then().extract().response();

	}

	
	/**
	 * FUNCTION			- Save response in a file
	 * @param res		- Response object
	 * @param filePath	- Path of the file where response is to be stored
	 */
	
	public void saveResponseToFile(Response res, String filePath) {

		try {

			String responseBody = res.getBody().asString();
			InputStream input = new ByteArrayInputStream(responseBody.getBytes());
			byte[] SWFByteArray = IOUtils.toByteArray(input);
			FileOutputStream fos = new FileOutputStream(new File(filePath));

			fos.write(SWFByteArray);
			fos.flush();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
