/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
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
public class BookDAL {

    private static String getNodeValue(String strTag, Element eBook) {
        Node nValue = (Node) eBook.getElementsByTagName(strTag).item(0).getFirstChild();
        return nValue.getNodeValue();
    }

    public ArrayList<Book> getBooks() {
        AuthorDAL objAuthorDAL = new AuthorDAL();
        ArrayList<Author> listAuthors = objAuthorDAL.getAuthors();
        ArrayList<Book> bookList = new ArrayList();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("src/db/DBbooks.xml"));
            doc.getDocumentElement().normalize();
            NodeList bookNondes = doc.getElementsByTagName("book");

            for (int i = 0; i < bookNondes.getLength(); i++) {
                Node book = bookNondes.item(i);
                if (book.getNodeType() == Node.ELEMENT_NODE) {
                    Element anElement = (Element) book;
                    Book objBook = new Book();
                    objBook.setISBN(getNodeValue("isbn", anElement));
                    objBook.setTitle(getNodeValue("title", anElement));
                    String type = getNodeValue("type", anElement);
                    switch (type) {
                        case "CONOCIMIENTOS":
                            objBook.setType(Book.BookType.CONOCIMIENTOS);
                            break;
                        case "CUENTO":
                            objBook.setType(Book.BookType.CUENTO);
                            break;
                        case "HISTORIA":
                            objBook.setType(Book.BookType.HISTORIA);
                            break;
                        case "MISTERIO":
                            objBook.setType(Book.BookType.MISTERIO);
                            break;
                        case "NARRATIVA":
                            objBook.setType(Book.BookType.NARRATIVA);
                            break;
                        case "NOTICIAS":
                            objBook.setType(Book.BookType.NOTICIAS);
                            break;
                        case "NOVELA":
                            objBook.setType(Book.BookType.NOVELA);
                            break;
                        default:
                            throw new AssertionError();
                    }
                    objBook.setEditorial(getNodeValue("editorial", anElement));
                    objBook.setEdition(Integer.parseInt(getNodeValue("edition", anElement)));
                    objBook.setYear(Integer.parseInt(getNodeValue("year", anElement)));
                    String authors = getNodeValue("author", anElement);
                    StringTokenizer st = new StringTokenizer(authors, ",");
                    for (int j = 0; j <= st.countTokens(); j++) {
                        int authorid = Integer.parseInt(st.nextToken());
                        Author myAuthor = new Author();
                        myAuthor.setId(authorid);
                        int comparation = listAuthors.indexOf(myAuthor);
                        if (comparation != -1) {
                            Author finalAuthor = new Author();
                            finalAuthor.setId(listAuthors.get(comparation).getId());
                            finalAuthor.setName(listAuthors.get(comparation).getName());
                            finalAuthor.setLastname(listAuthors.get(comparation).getLastname());
                            finalAuthor.setNationality(listAuthors.get(comparation).getNationality());
                            objBook.setAuthor(finalAuthor);
                        }
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
