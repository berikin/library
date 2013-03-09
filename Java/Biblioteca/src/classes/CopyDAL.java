/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.swing.JOptionPane;

/**
 *
 * @author berik
 */
public class CopyDAL {

    private static String getNodeValue(String strTag, Element eCopy) {
        Node nValue = (Node) eCopy.getElementsByTagName(strTag).item(0).getFirstChild();
        return nValue.getNodeValue();
    }

    public ArrayList<Copy> getCopies(String ISBN) {
        BookDAL objBookDAL = new BookDAL();
        ArrayList<Book> listBooks = objBookDAL.getBooks();
        ArrayList<Copy> copyList = new ArrayList();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("db/DBcopies.xml"));
            doc.getDocumentElement().normalize();
            NodeList copyNodes = doc.getElementsByTagName("copy");

            for (int i = 0; i < copyNodes.getLength(); i++) {
                Node copy = copyNodes.item(i);
                if (copy.getNodeType() == Node.ELEMENT_NODE) {
                    Element anElement = (Element) copy;
                    if (getNodeValue("isbn", anElement).equals(ISBN)) {
                        Copy objCopy = new Copy();
                        objCopy.setBookCode(Integer.parseInt(getNodeValue("bookcode", anElement)));
                        String state = getNodeValue("state", anElement);
                        switch (state) {
                            case "BORROWED":
                                objCopy.setState(Copy.CopyState.BORROWED);
                                break;
                            case "STORED":
                                objCopy.setState(Copy.CopyState.STORED);
                                break;
                            default:
                                throw new AssertionError();
                        }
                        String bookid = getNodeValue("isbn", anElement);
                        Book myBook = new Book();
                        myBook.setISBN(bookid);
                        int comparation = listBooks.indexOf(myBook);
                        if (comparation != -1) {
                            Book finalBook = new Book();
                            finalBook.setISBN(listBooks.get(comparation).getISBN());
                            finalBook.setTitle(listBooks.get(comparation).getTitle());
                            finalBook.setType(listBooks.get(comparation).getType());
                            finalBook.setEditorial(listBooks.get(comparation).getEditorial());
                            finalBook.setEdition(listBooks.get(comparation).getEdition());
                            finalBook.setYear(listBooks.get(comparation).getYear());
                            for (int j = 0; j < listBooks.get(comparation).getAuthor().size(); j++) {
                                finalBook.setAuthor(listBooks.get(comparation).getAuthor().get(j));
                            }
                            objCopy.setBook(finalBook);
                        }
                        copyList.add(objCopy);
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException parseE) {
            JOptionPane.showMessageDialog(null, parseE.getMessage(), "" + "Error", JOptionPane.ERROR_MESSAGE);
        }
        return copyList;
    }

    public ArrayList<Copy> getCopies() {
        BookDAL objBookDAL = new BookDAL();
        ArrayList<Book> listBooks = objBookDAL.getBooks();
        ArrayList<Copy> copyList = new ArrayList();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("db/DBcopies.xml"));
            doc.getDocumentElement().normalize();
            NodeList copyNodes = doc.getElementsByTagName("copy");

            for (int i = 0; i < copyNodes.getLength(); i++) {
                Node copy = copyNodes.item(i);
                if (copy.getNodeType() == Node.ELEMENT_NODE) {
                    Element anElement = (Element) copy;
                    Copy objCopy = new Copy();
                    objCopy.setBookCode(Integer.parseInt(getNodeValue("bookcode", anElement)));
                    String state = getNodeValue("state", anElement);
                    switch (state) {
                        case "BORROWED":
                            objCopy.setState(Copy.CopyState.BORROWED);
                            break;
                        case "STORED":
                            objCopy.setState(Copy.CopyState.STORED);
                            break;
                        default:
                            throw new AssertionError();
                    }
                    String bookid = getNodeValue("isbn", anElement);
                    Book myBook = new Book();
                    myBook.setISBN(bookid);
                    int comparation = listBooks.indexOf(myBook);
                    if (comparation != -1) {
                        Book finalBook = new Book();
                        finalBook.setISBN(listBooks.get(comparation).getISBN());
                        finalBook.setTitle(listBooks.get(comparation).getTitle());
                        finalBook.setType(listBooks.get(comparation).getType());
                        finalBook.setEditorial(listBooks.get(comparation).getEditorial());
                        finalBook.setEdition(listBooks.get(comparation).getEdition());
                        finalBook.setYear(listBooks.get(comparation).getYear());
                        for (int j = 0; j < listBooks.get(comparation).getAuthor().size(); j++) {
                            finalBook.setAuthor(listBooks.get(comparation).getAuthor().get(j));
                        }
                        objCopy.setBook(finalBook);
                    }
                    copyList.add(objCopy);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException parseE) {
            JOptionPane.showMessageDialog(null, parseE.getMessage(), "" + "Error", JOptionPane.ERROR_MESSAGE);
        }
        return copyList;
    }
}
