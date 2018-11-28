/***********************************Header Start*********************************************************************************
 * Application/ Module Name                      	   : eFollett/FMS
 * Test/ Function Name                                 : All reusable methods
 * Owner                                               : AutomationTeam
 ***********************************************************************
 * Creation/Modification Log: 
 * Date                     By                                Notes                                    
 ---------                ----------                      ---------
 * 
 ***********************************Header End*********************************************************************************/
package com.org.core;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.decorist.businessrules.Decorist_Constants;
public class ModifyXMLFile {
	static Logger LOG = Logger.getLogger(ModifyXMLFile.class);
	/**
	 * Modify XML File
	 * 
	 * @Author SAcharya
	 */
	public static void main(String argv[]) {
		try {
			String xmlfile = "C:\\Users\\TestData\\TestData.xml";
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(xmlfile);
			Node staff = doc.getElementsByTagName("staff").item(0);
			NamedNodeMap attr = staff.getAttributes();
			Node nodeAttr = attr.getNamedItem("id");
			nodeAttr.setTextContent("2");
			Element age = doc.createElement("age");
			age.appendChild(doc.createTextNode("28"));
			staff.appendChild(age);
			NodeList list = staff.getChildNodes();
			for (int index = 0; index < list.getLength(); index++) {
				Node node = list.item(index);
				if ("salary".equals(node.getNodeName())) {
					node.setTextContent("2000000");
				}
				if ("firstname".equals(node.getNodeName())) {
					staff.removeChild(node);
				}
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(xmlfile));
			transformer.transform(source, result);
			LOG.info("Done");
		} catch (ParserConfigurationException pce) {
			LOG.info(Decorist_Constants.ERROR, pce);
		} catch (TransformerException tfe) {
			LOG.info(Decorist_Constants.ERROR, tfe);
		} catch (IOException ioe) {
			LOG.info(Decorist_Constants.ERROR, ioe);
		} catch (SAXException sae) {
		}
	}
}
