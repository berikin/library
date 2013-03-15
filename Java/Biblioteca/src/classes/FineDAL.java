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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import util.Date;

/**
 *
 * @author berik
 */
public class FineDAL {

    private static String getNodeValue(String strTag, Element eCopy) {
        Node nValue = (Node) eCopy.getElementsByTagName(strTag).item(0).getFirstChild();
        return nValue.getNodeValue();
    }

    public ArrayList<Fine> getFines() {
        Date myDate;
        ArrayList<Fine> fineList = new ArrayList();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("db/DBfines.xml"));
            doc.getDocumentElement().normalize();
            NodeList fineNodes = doc.getElementsByTagName("fine");
            if (fineNodes.getLength() != -1) {
                for (int i = 0; i < fineNodes.getLength(); i++) {
                    Node fine = fineNodes.item(i);
                    if (fine.getNodeType() == Node.ELEMENT_NODE) {
                        Element anElement = (Element) fine;
                        Fine objFine = new Fine();
                        objFine.setFineID(Integer.parseInt(getNodeValue("fineid", anElement)));
                        objFine.setMemberID(Integer.parseInt(getNodeValue("memberid", anElement)));
                        String dateTxt = getNodeValue("startdate", anElement);
                        myDate = new Date(dateTxt);
                        objFine.setStartDate(myDate);
                        dateTxt = getNodeValue("enddate", anElement);
                        myDate = new Date(dateTxt);
                        objFine.setEndDate(myDate);
                        objFine.setTax(Double.parseDouble(getNodeValue("tax", anElement)));
                        fineList.add(objFine);
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException parseE) {
            JOptionPane.showMessageDialog(null, parseE.getMessage(), "" + "Error", JOptionPane.ERROR_MESSAGE);
        }
        return fineList;
    }
    
          public static void addFine(Fine fine) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("db/DBfines.xml"));
            doc.getDocumentElement().normalize();
            Node rootNode = doc.getDocumentElement();
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // PREPARING THE NEW MEMBER
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            Element newFine = doc.createElement("fine");
            Element newID = doc.createElement("fineid");
            newID.setTextContent(Integer.toString(fine.getFineID()));
            Element newMemberID = doc.createElement("memberid");
            newMemberID.setTextContent(Integer.toString(fine.getMemberID()));
            Element newStartDate = doc.createElement("startdate");
            newStartDate.setTextContent(fine.getStartDate().getShortFormat());
            Element newEndDate = doc.createElement("enddate");
            newEndDate.setTextContent(fine.getEndDate().getShortFormat());
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // ADDING NODES
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            newFine.appendChild(newID);
            newFine.appendChild(newMemberID);
            newFine.appendChild(newStartDate);
            newFine.appendChild(newEndDate);
            rootNode.appendChild(newFine);
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // SENDING TO XML FILE
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("db/DBfines.xml"));
            transformer.transform(source, result);
        } catch (TransformerConfigurationException parseE) {
            JOptionPane.showMessageDialog(null, parseE.getMessage(), "" + "Error", JOptionPane.ERROR_MESSAGE);
        } catch (TransformerException parseE) {
            JOptionPane.showMessageDialog(null, parseE.getMessage(), "" + "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParserConfigurationException | SAXException | IOException parseE) {
            JOptionPane.showMessageDialog(null, parseE.getMessage(), "" + "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
