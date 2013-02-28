/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

/**
 *
 * @author berik
 */
public class Member extends Person {

    private int memberID;
    private String address;
    private int phone;

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
