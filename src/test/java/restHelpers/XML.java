package restHelpers;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XML {
	String path = "";
	
	/**
	 * CONSTRUCTOR 		- Set the value of 'path' variable
	 * @param xmlPath	- file path of the xml file
	 */
	
	public XML(String xmlPath) {
		path = xmlPath;
	}
	
	/**
	 * FUNCTION 		- Find the tag in the xml file and return the tag's value 
	 * @param tagName	- Name of the tag whose value is to be returned
	 * @return			- Value of the tag
	 */
	
	public String readTagVal(String tagName) {	
		
		String tagVal 							= null;
		File file 								= new File(path);
		
		try {
			
			DocumentBuilder builder 			= DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc 						= builder.parse(file);
			tagVal 								= doc.getElementsByTagName(tagName).item(0).getTextContent();
		} catch(Exception e) {
			System.err.println("Could not get data from config.XML file for the tag "+tagName);
		}
		
		return tagVal;
	}
}
