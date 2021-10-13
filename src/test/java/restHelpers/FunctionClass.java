package restHelpers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Base64;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uiHelpers.Reporting;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class FunctionClass {
	
	DateFormat dateFormat	= new SimpleDateFormat("yyyy-mm-dd");
    public static boolean Allnod;
    Reporting reportCBT;
    
	
	public String getAttributeValue(String xml, String tagName,String attr, String index) throws IOException {
		
		System.out.println(xml);
		DOMParser parser = new DOMParser();
		try {
		    parser.parse(new InputSource(new java.io.StringReader(xml)));
		    Document doc = parser.getDocument();
		    Node val = doc.getElementsByTagName(tagName).item(Integer.valueOf(index)).getAttributes().getNamedItem(attr);
		    
		    //System.out.println("Attribute Value is:" +val.getNodeValue());
		    
		    return val.getNodeValue();
		} catch (SAXException e) {
			e.printStackTrace();
		    // handle SAXException 
		} catch (IOException e) {
			e.printStackTrace();
		    // handle IOException 
		}
		return null;
	}
    
    
    /**
	 * FUNCTION 		- Set headers for a request
	 * @param headers	- Header names separated by ':' to be set for a request in String format
	 * @param values	- Values separated by ':' corresponding to the headers in  String format
	 * @return			- Request specification of request 
	 */

    public RequestSpecification setHeader(String headers, String values) {
        String delim			= ":";
        Pattern pr				= Pattern.compile(delim);
        String[] headerKeys 	= pr.split(headers);
        String[] headerValues 	= pr.split(values);
		/*
		 * for (int i = 0; i < headerKeys.length; i++) { System.out.println("Key is: " +
		 * headerKeys[i] + " and value is: " + headerValues[i]); }
		 */
        RequestSpecification requestSpec = null;
        
        HashMap<String, String> headVal = new HashMap<>();

        RequestSpecBuilder builder 		= new RequestSpecBuilder();
        for (int i = 0; i < headerKeys.length; i++) {

            String key		= headerKeys[i];
            String value 	= headerValues[i];
            
            //System.out.println("Key is: " + key + " and value is: " + value);
			
            switch (key) {
			  
            	case "Content-Type": 
            		builder.setContentType(value);
            		break;
			  
            	case "Cookie": 
            		String []allCookiePair = value.split(";");
            		for(int j=0;j<allCookiePair.length;j++) 
            		{
            			headVal.put(allCookiePair[j].split("=")[0],allCookiePair[j].split("=")[1]); 
            		}
            		builder.addCookies(headVal);
            		break;
            	case "Accept":
            		builder.setAccept(value);
            		break;
            	case "Authorization":
            		if(value.startsWith("zwe") || value.startsWith("vwfs")) {
            			String user = value.split(",")[0];
            			String pass = value.split(",")[1];
            			String authBasic = Base64.getEncoder().encodeToString((user + ":" + pass).getBytes());
            			builder.addHeader(key, String.format("Basic %s", authBasic));
            			break;
            		} else {
            			builder.addHeader(key, value);
            			break;
            		}
			  default:  
				  builder.addHeader(key, value);
				  break; 
            }	         
        }
        requestSpec = builder.build();
        return requestSpec;
    }
    
    
    public boolean responseCodeVerify(Response resp, int ResponseCode,Reporting reporter) throws ArithmeticException {

        boolean flag = false;

        try {
        	
            if (ResponseCode == resp.getStatusCode()) {
                flag = true;
            }

            reporter.verify("Response Code", ResponseCode, resp.getStatusCode());

            long resptime = resp.getTimeIn(TimeUnit.MILLISECONDS);
            
            if(resptime>60000) {
            	reporter.warn("Response Time (Milliseconds): ", Long.toString(resptime));
            }
            else {
            	reporter.info("Response Time (Milliseconds): ", Long.toString(resptime));
            }

        } catch (Exception e) {
        	reporter.fail("Web Services not responding with exception - ", e.getMessage());
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return flag;
    }

    
    /**
     * FUNCTION		- Regular Expression Matcher: Takes two string variables as input.
     * @param regex - Regular Expression as expected value in String format 
     * @param exp	- Expression to be matched with the regular expression in String format
     * @return		- True, if 'exp' satisfies the 'regex', else false
     */
    
    public boolean regexMatcher(String regex,String exp) {
        
        try {
        	
            Pattern p = Pattern.compile(regex);               
            Matcher m = p.matcher(exp);  
            return m.matches();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
