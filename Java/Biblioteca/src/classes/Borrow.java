/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import util.Date;

/**
 *
 * @author berik
 */
public class Borrow {
    private int borrowID;
    Copy copy;
    private Date borrowDate, limitDate;

    public int getBorrowID() {
        return borrowID;
    }

    public void setBorrowID(int borrowID) {
        this.borrowID = borrowID;
    }

    public Date getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(Date limitDate) {
        this.limitDate = limitDate;
    }


    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Copy getBorrowedCopy() {
        return copy;
    }

    public void setBorrowedCopy(Copy copy) {
        this.copy = copy;
    }

        
    @Override
    public boolean equals(Object obj)
    {
        Borrow a=(Borrow)obj;
        return (borrowID==a.getBorrowID());
    }
}
