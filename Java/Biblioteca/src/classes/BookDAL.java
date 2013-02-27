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
public class BookDAL {

    private static String getNodeValue(String strTag, Element eBook) {
        Node nValue = (Node) eBook.getElementsByTagName(strTag).item(0).getFirstChild();
        return nValue.getNodeValue();
    }

    public ArrayList<Book> getBooks() {
        AuthorDAL objAuthorDAL= new AuthorDAL();
        ArrayList<Author> listAuthors=objAuthorDAL.getAuthors();
        ArrayList<Book> bookList = new ArrayList();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("src/db/DataBase.xml"));
            doc.getDocumentElement().normalize();
            NodeList authorNodes = doc.getElementsByTagName("book");

            for (int i = 0; i < authorNodes.getLength(); i++) {
                Node author = authorNodes.item(i);
                if (author.getNodeType() == Node.ELEMENT_NODE) {
                    Element anElement = (Element) author;
                    Book objBook = new Book();
                    objBook.setISBN(getNodeValue("isbn", anElement));
                    objBook.setTitle(getNodeValue("title", anElement));
                    int authorid=Integer.parseInt(getNodeValue("author", anElement));
                    Author myAuthor=new Author();
                    myAuthor.setId(authorid);
                    int comparation=listAuthors.indexOf(myAuthor);
                    if (comparation!=-1)
                    {   Author finalAuthor=new Author();
                    finalAuthor.setId(listAuthors.get(comparation).getId());
                    finalAuthor.setName(listAuthors.get(comparation).getName());
                    objBook.setAuthor(finalAuthor);
                    }
                    bookList.add(objBook);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException parseE) {
            JOptionPane.showMessageDialog(null, parseE.getMessage(), "" + "Error", JOptionPane.ERROR_MESSAGE);
        }
        return bookList;
    }
}
