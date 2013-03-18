/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import classes.Member;
import classes.MemberDAL;
import java.util.ArrayList;

/**
 *
 * @author berik
 */
public class TryMembers {
      public static void main(String args[]) {

                    MemberDAL objmemberDAL = new MemberDAL();
        ArrayList<Member> memberList = objmemberDAL.getMembers();
        for (int i=0; i<memberList.size();i++)
        {
            System.out.println("MIEMBRO NÚMERO "+(i+1)+":");
            System.out.println("Nombre: "+memberList.get(i).getPersonName());
            System.out.println("Apellidos: "+memberList.get(i).getPersonLastName());
            System.out.println("Dirección: "+memberList.get(i).getAddress());
            System.out.println("Nombre de usuario: "+memberList.get(i).getUserid());
            System.out.println("Contraseña: "+memberList.get(i).getPwd());
            if (!memberList.get(i).getBorrowedCopies().isEmpty())
            {
                System.out.println("Copias prestadas actualmente:");
                for (int j=0; j<memberList.get(i).getBorrowedCopies().size();j++)
                {
                    System.out.println("ID de copia: "+memberList.get(i).getBorrowedCopies().get(j).getBorrowedCopy().getBookCode());
                    System.out.println("Título de copia: "+memberList.get(i).getBorrowedCopies().get(j).getBorrowedCopy().getBook().getTitle());
                    System.out.println("Fecha límite de entrega: "+memberList.get(i).getBorrowedCopies().get(j).getLimitDate().getLongFormat());
                }
            }
        }
    }
}
