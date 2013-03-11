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
}
