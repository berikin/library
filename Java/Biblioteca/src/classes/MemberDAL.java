/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author berik
 */
public class MemberDAL {

    private static String getNodeValue(String strTag, Element eMember) {
        Node nValue = (Node)eMember.getElementsByTagName(strTag).item(0).getFirstChild();
        return nValue.getNodeValue();
    }

    public ArrayList<Author> getMembers() {
        ArrayList<Author> authorList = new ArrayList();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("src/db/DBauthors.xml"));
            doc.getDocumentElement().normalize();
            NodeList authorNodes = doc.getElementsByTagName("author");

            for (int i = 0; i < authorNodes.getLength(); i++) {
                Node author = authorNodes.item(i);
                if (author.getNodeType() == Node.ELEMENT_NODE) {
                    Element anElement = (Element) author;
                    Author objAuthor = new Author();
                    objAuthor.setId(Integer.parseInt(getNodeValue("id", anElement)));
                    objAuthor.setName(getNodeValue("name", anElement));
                    objAuthor.setLastname(getNodeValue("lastname", anElement));
                    objAuthor.setNationality(getNodeValue("nationality", anElement));    
                    authorList.add(objAuthor);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException parseE) {
            JOptionPane.showMessageDialog(null, parseE.getMessage(), "" + "Error", JOptionPane.ERROR_MESSAGE);
        }
        return authorList;
    }
}
