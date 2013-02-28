/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import classes.*;
import java.util.ArrayList;
/**
 *
 * @author berik
 */
public class Library {
    public static void main (String args[])
    {
        System.out.println("LIBROS");
        BookDAL objBookDAL= new BookDAL();
        ArrayList<Book> listBooks=objBookDAL.getBooks();
    for(int i=0;i<listBooks.size();i++)
    {
        System.out.println("ISBN:" +listBooks.get(i).getISBN());
        System.out.println("Título:" +listBooks.get(i).getTitle());
        System.out.println("Tipo:" +listBooks.get(i).getType());
        System.out.println("Editorial:" +listBooks.get(i).getEditorial());
        System.out.println("Edición:" +listBooks.get(i).getEdition());
        System.out.println("Anyo:" +listBooks.get(i).getYear());
        System.out.println("Autor:" +listBooks.get(i).getAuthor());
    }
        System.out.println("COPIAS");
        CopyDAL objCopyDAL= new CopyDAL();
        ArrayList<Copy> listCopies=objCopyDAL.getCopies();
    for(int i=0;i<listCopies.size();i++)
    {
        System.out.println("Código: "+listCopies.get(i).getBookCode());
        System.out.println("Estado: "+listCopies.get(i).getState());
        System.out.println("Libro: "+listCopies.get(i).getBook());
        System.out.println(listCopies.get(i).getBook().getAuthor().get(0).getNationality());
    }
    Member miembro=new Member();
    miembro.setMemberID(1);
    miembro.setPersonName("Pepe");
    miembro.setPersonLastName("Perez");
        System.out.println(miembro.getPersonName());
        System.out.println(miembro.getPersonLastName());
        System.out.println(miembro.getMemberID());
    }
}
