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
public class BorrowDAL {

    private static String getNodeValue(String strTag, Element eBook) {
        Node nValue = (Node) eBook.getElementsByTagName(strTag).item(0).getFirstChild();
        return nValue.getNodeValue();
    }

    public ArrayList<Borrow> getBorrows() {
        CopyDAL objCopyDAL = new CopyDAL();
        ArrayList<Copy> copyList = objCopyDAL.getCopies();
        ArrayList<Borrow> borrowList = new ArrayList();
        try {
            
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("db/DBborrows.xml"));
            doc.getDocumentElement().normalize();
            NodeList borrowNodes = doc.getElementsByTagName("borrow");
            if (borrowNodes.getLength()!=-1) {
                for (int i = 0; i < borrowNodes.getLength(); i++) {
                    Node borrow = borrowNodes.item(i);
                    if (borrow.getNodeType() == Node.ELEMENT_NODE) {
                        Element anElement = (Element) borrow;
                        Borrow objBorrow = new Borrow();
                        objBorrow.setBorrowID(Integer.parseInt(getNodeValue("borrowid", anElement)));
                        int copyCode=Integer.parseInt(getNodeValue("copycode", anElement));
                        Copy myCopy=new Copy();
                        myCopy.setBookCode(copyCode);
                        int comparation = copyList.indexOf(myCopy);
                        if (comparation!=-1)
                        {
                        myCopy.setState(copyList.get(comparation).getState());
                        myCopy.setBook(copyList.get(comparation).getBook());
                        objBorrow.setBorrowedCopy(myCopy);
                        }
                        String myDate = getNodeValue("datefirst", anElement);
                        Date theDate = new Date(myDate);
                        objBorrow.setBorrowDate(theDate);
                        myDate = getNodeValue("datelast", anElement);
                        theDate = new Date(myDate);
                        objBorrow.setLimitDate(theDate);
                        borrowList.add(objBorrow);
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException parseE) {
            //JOptionPane.showMessageDialog(null, parseE.getMessage(), "" + "Error", JOptionPane.ERROR_MESSAGE);
        }
        return borrowList;
    }
    
       public static void addBorrow(Borrow borrow) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("db/DBborrows.xml"));
            doc.getDocumentElement().normalize();
            Node rootNode = doc.getDocumentElement();
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // PREPARING THE NEW MEMBER
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            Element newBorrow = doc.createElement("borrow");
            Element newID = doc.createElement("borrowid");
            newID.setTextContent(Integer.toString(borrow.getBorrowID()));
            Element newCopyCode = doc.createElement("copycode");
            newCopyCode.setTextContent(Integer.toString(borrow.getBorrowedCopy().getBookCode()));
            Element newDateFirst = doc.createElement("datefirst");
            newDateFirst.setTextContent(borrow.getBorrowDate().getShortFormat());
            Element newDateLast = doc.createElement("datelast");
            newDateLast.setTextContent(borrow.getLimitDate().getShortFormat());
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // ADDING NODES
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            newBorrow.appendChild(newID);
            newBorrow.appendChild(newCopyCode);
            newBorrow.appendChild(newDateFirst);
            newBorrow.appendChild(newDateLast);
            rootNode.appendChild(newBorrow);
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // SENDING TO XML FILE
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("db/DBborrows.xml"));
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
