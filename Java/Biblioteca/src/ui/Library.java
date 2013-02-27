/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import classes.Author;
import classes.AuthorDAL;
import java.util.ArrayList;
/**
 *
 * @author berik
 */
public class Library {
    public static void main (String args[])
    {
        System.out.println("Info");
        AuthorDAL objAuthorDAL= new AuthorDAL();
        ArrayList<Author> listAuthors=objAuthorDAL.getAuthors();
    for(int i=0;i<listAuthors.size();i++)
    {
        System.out.println("Nombre:" +listAuthors.get(i).getName());
        System.out.println("Apellidos:" +listAuthors.get(i).getLastname());
        System.out.println("Nacionalidad:" +listAuthors.get(i).getNationality());
    }
    }
}
