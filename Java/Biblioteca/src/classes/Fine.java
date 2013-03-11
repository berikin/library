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
public class Fine {
    private Date startDate, endDate;
    private int memberID, fineID;
    private double tax;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public int getFineID() {
        return fineID;
    }

    public void setFineID(int fineID) {
        this.fineID = fineID;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }
        @Override
    public boolean equals(Object obj)
    {
        Fine a=(Fine)obj;
        return (fineID==a.getFineID());
    }
}
