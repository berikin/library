/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.util.ArrayList;

/**
 *
 * @author berik
 */
public class Member extends Person {

    private int memberID;
    private String address, userid, pwd;
    private int phone;
    private ArrayList<Borrow> borrowedCopies=new ArrayList();
    private ArrayList<Fine> fines=new ArrayList();

    public Member() {
    }

    public Member(Member member) {
        this.memberID = member.getMemberID();
        this.address = member.getAddress();
        this.userid = member.getUserid();
        this.pwd = member.getPwd();
        this.phone = member.getPhone();
        this.borrowedCopies=member.getBorrowedCopies();
        this.fines=member.getFines();
        super.name=member.getPersonName();
        super.lastname=member.getPersonLastName();
    }
    
    public ArrayList<Fine> getFines() {
        return fines;
    }

    public void setFines(Fine fines) {
        this.fines.add(fines);
    }
    public void setFines(ArrayList<Fine> fines) {
        this.fines.clear();
        for (int i=0; i<fines.size();i++)
        {
        this.fines.add(fines.get(i));
        }
    }
    public ArrayList<Borrow> getBorrowedCopies() {
        return borrowedCopies;
    }

    public void setBorrowedCopies(Borrow borrowedCopies) {
        this.borrowedCopies.add(borrowedCopies);
    }
    public void setBorrowedCopies(ArrayList<Borrow> borrowedCopies) {
        this.borrowedCopies.clear();
        for (int i=0; i<borrowedCopies.size();i++)
        {
        this.borrowedCopies.add(borrowedCopies.get(i));
        }
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public void setPersonName(String name) {
        super.name = name;
    }

    public void setPersonLastName(String lastname) {
        super.lastname = lastname;
    }

    public String getPersonName() {
        return super.name;
    }

    public String getPersonLastName() {
        return super.lastname;
    }
}
