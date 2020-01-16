package nyeblock.Core.ServerCoreTest.Menus;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import nyeblock.Core.ServerCoreTest.Main;

public class XMLToMenu {
//	public XMLToMenu(Main mainInstance) {		
//		//Get Document Builder
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		try {
//			DocumentBuilder builder = factory.newDocumentBuilder();
//			
//			for (File menuFile : new File("./plugins/ServerCoreTest/menus").listFiles()) {
//				Document document = builder.parse(menuFile);
//				document.getDocumentElement().normalize();
//				
//				Element menu = document.getDocumentElement();
//				
//				//Menu
//				if (menu.getNodeName().equalsIgnoreCase("menu")) {
//					NodeList menuChildren = menu.getChildNodes();
//					
//					MenuBase new MenuBase();
//					
//					for (int i = 0; i < menuChildren.getLength(); i++) {
//						Node subMenu = menuChildren.item(i);
//						
//						//SubMenu
//						if (subMenu.getNodeType() == Node.ELEMENT_NODE && subMenu.getNodeName().equalsIgnoreCase("submenu")) {
//							NodeList subMenuChildren = subMenu.getChildNodes();
//							
//							for (int k = 0; k < subMenuChildren.getLength(); k++) {
//								Node option = subMenuChildren.item(k);
//								
//								//Option
//								if (subMenu.getNodeType() == Node.ELEMENT_NODE && option.getNodeName().equalsIgnoreCase("option")) {									
//									
//								}
//							}
//						}
//					}
//				}
//			}
//		} catch (ParserConfigurationException | SAXException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
////		//Get all employees
////		NodeList nList = document.getElementsByTagName("employee");
////		System.out.println("============================");
////		 
////		for (int temp = 0; temp < nList.getLength(); temp++)
////		{
////			Node node = nList.item(temp);
////			System.out.println("");    //Just a separator
////			if (node.getNodeType() == Node.ELEMENT_NODE)
////			{
////				//Print each employee's detail
////				Element eElement = (Element) node;
////				System.out.println("Employee id : "    + eElement.getAttribute("id"));
////				System.out.println("First Name : "  + eElement.getElementsByTagName("firstName").item(0).getTextContent());
////				System.out.println("Last Name : "   + eElement.getElementsByTagName("lastName").item(0).getTextContent());
////				System.out.println("Location : "    + eElement.getElementsByTagName("location").item(0).getTextContent());
////			}
////		}
//	}
}
