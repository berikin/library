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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;

/**
 *
 * @author berik
 */
public class CopyDAL {

    private static String getNodeValue(String strTag, Element eCopy) {
        Node nValue = (Node) eCopy.getElementsByTagName(strTag).item(0).getFirstChild();
        return nValue.getNodeValue();
    }

    public static void changeCopyState(int copyID, String state) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("db/DBcopies.xml"));
            doc.getDocumentElement().normalize();
            int myItem = 0;
            NodeList copyNodes = doc.getElementsByTagName("copy");
            for (int i = 0; i < copyNodes.getLength(); i++) {
                Element a = (Element) copyNodes.item(i);
                if (Integer.parseInt(getNodeValue("bookcode", a)) == copyID) {
                    myItem = i;
                    break;
                }
            }
            Node copy = doc.getElementsByTagName("copy").item(myItem);

            NodeList list = copy.getChildNodes();

            for (int i = 0; i < list.getLength(); i++) {

                Node node = list.item(i);

                // get the salary element, and update the value
                if ("state".equals(node.getNodeName())) {
                    switch (state) {
                        case "STORED":
                            node.setTextContent("STORED");
                            break;
                        case "BORROWED":
                            node.setTextContent("BORROWED");
                            break;
                        default:
                            throw new AssertionError();
                    }
                }

            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("db/DBcopies.xml"));
            transformer.transform(source, result);

            System.out.println("Done");

        } catch (ParserConfigurationException | SAXException | IOException | NumberFormatException | DOMException | AssertionError | TransformerFactoryConfigurationError | TransformerException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "" + "Error", JOptionPane.ERROR_MESSAGE);
        }
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
                        myBook.setISBN(listBooks.get(comparation).getISBN());
                        myBook.setTitle(listBooks.get(comparation).getTitle());
                        myBook.setType(listBooks.get(comparation).getType());
                        myBook.setImage(listBooks.get(comparation).getImage());
                        myBook.setEditorial(listBooks.get(comparation).getEditorial());
                        myBook.setEdition(listBooks.get(comparation).getEdition());
                        myBook.setYear(listBooks.get(comparation).getYear());
                        for (int j = 0; j < listBooks.get(comparation).getAuthor().size(); j++) {
                            myBook.setAuthor(listBooks.get(comparation).getAuthor().get(j));
                        }
                        objCopy.setBook(myBook);
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
