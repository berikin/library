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
}
