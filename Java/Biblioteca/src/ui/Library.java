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

    public static void main(String args[]) {
         /*   System.out.println("LIBROS");
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
         System.out.println("FOTO:"+listBooks.get(i).getImage());
         }*/
         System.out.println("COPIAS");
          CopyDAL objCopyDAL = new CopyDAL();
        ArrayList<Copy> listCopies = objCopyDAL.getCopies();
        for (int i = 0; i < listCopies.size(); i++) {
            System.out.println("Código: " + listCopies.get(i).getBookCode());
            System.out.println("Estado: " + listCopies.get(i).getState());
            System.out.println("Libro: " + listCopies.get(i).getBook());
            //System.out.println("foto: " + listCopies.get(i).getBook().getImage());
            //System.out.println(listCopies.get(i).getBook().getAuthor().get(0).getNationality());
        /*}/*
        MemberDAL objmemberDAL = new MemberDAL();
        ArrayList<Member> memberList = objmemberDAL.getMembers();
        for (int i = 0; i < memberList.size(); i++) {
            System.out.println(memberList.get(i).getPersonName());
            System.out.println(memberList.get(i).getPersonLastName());
            System.out.println(memberList.get(i).getMemberID());
            System.out.println(memberList.get(i).getBorrowedCopies().size());
            BorrowDAL objBorrowDAL = new BorrowDAL();
            ArrayList<Borrow> borrowList = objBorrowDAL.getBorrows();
            System.out.println(borrowList.size());*/
            BorrowDAL newb=new BorrowDAL();
            ArrayList<Borrow> listb = newb.getBorrows();
            System.out.println(listb.size());
                    MemberDAL objmemberDAL = new MemberDAL();
        ArrayList<Member> memberList = objmemberDAL.getMembers();
            System.out.println(memberList.get(0).getBorrowedCopies().size());
        }
    }
}
