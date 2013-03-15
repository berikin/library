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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author berik
 */
public class MemberDAL {

    private static String getNodeValue(String strTag, Element eMember) {
        Node nValue = (Node) eMember.getElementsByTagName(strTag).item(0).getFirstChild();
        return nValue.getNodeValue();
    }
public static void changeFineBorrow(int memberID, int itemID,String item) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("db/DBmembers.xml"));
            doc.getDocumentElement().normalize();
            int myItem = 0;
            NodeList copyNodes = doc.getElementsByTagName("member");
            for (int i = 0; i < copyNodes.getLength(); i++) {
                Element a = (Element) copyNodes.item(i);
                if (Integer.parseInt(getNodeValue("id", a)) == memberID) {
                    myItem = i;
                    break;
                }
            }
            Node copy = doc.getElementsByTagName(item).item(myItem);

            NodeList list = copy.getChildNodes();

            for (int i = 0; i < list.getLength(); i++) {

                Node node = list.item(i);

                // get the salary element, and update the value
                String a=node.getNodeValue();
                if (a.equals("none"))
                {
                node.setTextContent(Integer.toString(itemID));
                }
                else
                {
                a+=","+itemID;
                node.setTextContent(a);
                }
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("db/DBmembers.xml"));
            transformer.transform(source, result);

            System.out.println("Done");

        } catch (ParserConfigurationException | SAXException | IOException | NumberFormatException | DOMException | AssertionError | TransformerFactoryConfigurationError | TransformerException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "" + "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public ArrayList<Member> getMembers() {
        BorrowDAL objBorrowDAL = new BorrowDAL();
        ArrayList<Borrow> borrowList = objBorrowDAL.getBorrows();
        FineDAL objFineDAL = new FineDAL();
        ArrayList<Fine> fineList = objFineDAL.getFines();
        ArrayList<Member> memberList = new ArrayList();
        try {
            StringTokenizer st;
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("db/DBmembers.xml"));
            doc.getDocumentElement().normalize();
            NodeList memberNodes = doc.getElementsByTagName("member");
            for (int i = 0; i < memberNodes.getLength(); i++) {
                Node member = memberNodes.item(i);
                if (member.getNodeType() == Node.ELEMENT_NODE) {
                    Element anElement = (Element) member;
                    Member objMember = new Member();
                    objMember.setMemberID(Integer.parseInt(getNodeValue("id", anElement)));
                    objMember.setPersonName(getNodeValue("name", anElement));
                    objMember.setPersonLastName(getNodeValue("lastname", anElement));
                    objMember.setAddress(getNodeValue("address", anElement));
                    objMember.setPhone(Integer.parseInt(getNodeValue("phone", anElement)));
                    objMember.setUserid(getNodeValue("userid", anElement));
                    objMember.setPwd(getNodeValue("pwd", anElement));
                    String borrows = getNodeValue("borrows", anElement);
                    if (!borrows.equals("none")) {
                        st = new StringTokenizer(borrows, ",");
                        for (int j = 0; j <= st.countTokens(); j++) {
                            int borrowid = Integer.parseInt(st.nextToken());
                            Borrow myBorrow = new Borrow();
                            myBorrow.setBorrowID(borrowid);
                            int comparation = borrowList.indexOf(myBorrow);
                            if (comparation != -1) {
                                myBorrow.setBorrowID(borrowList.get(comparation).getBorrowID());
                                myBorrow.setBorrowedCopy(borrowList.get(comparation).getBorrowedCopy());
                                myBorrow.setBorrowDate(borrowList.get(comparation).getBorrowDate());
                                myBorrow.setLimitDate(borrowList.get(comparation).getLimitDate());
                                objMember.setBorrowedCopies(myBorrow);
                            }
                        }
                    }
                    String fines = getNodeValue("finelist", anElement);
                    if (!fines.equals("none")) {
                        st = new StringTokenizer(fines, ",");
                        for (int j = 0; j <= st.countTokens(); j++) {
                            int fineid = Integer.parseInt(st.nextToken());
                            Fine myFine = new Fine();
                            myFine.setFineID(fineid);
                            int comparation = fineList.indexOf(myFine);
                            if (comparation != -1) {
                                myFine.setFineID(fineList.get(comparation).getFineID());
                                myFine.setMemberID(fineList.get(comparation).getMemberID());
                                myFine.setStartDate(fineList.get(comparation).getStartDate());
                                myFine.setEndDate(fineList.get(comparation).getEndDate());
                                myFine.setTax(fineList.get(comparation).getTax());
                                objMember.setFines(myFine);
                            }
                        }
                    }
                    memberList.add(objMember);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException parseE) {
            JOptionPane.showMessageDialog(null, parseE.getMessage(), "" + "Error", JOptionPane.ERROR_MESSAGE);
        }
        return memberList;
    }

    public void addMember(Member member) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File("db/DBmembers.xml"));
            doc.getDocumentElement().normalize();
            Node rootNode = doc.getDocumentElement();
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // PREPARING THE NEW MEMBER
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            Element newMember = doc.createElement("member");
            Element newID = doc.createElement("id");
            newID.setTextContent(Integer.toString(member.getMemberID()));
            Element newName = doc.createElement("name");
            newName.setTextContent(member.getPersonName());
            Element newLastName = doc.createElement("lastname");
            newLastName.setTextContent(member.getPersonLastName());
            Element newAddress = doc.createElement("address");
            newAddress.setTextContent(member.getAddress());
            Element newPhone = doc.createElement("phone");
            newPhone.setTextContent(Integer.toString(member.getPhone()));
            String fines = "";
            if (member.getFines().isEmpty()) {
                fines = "none";
            } else {
                for (int i = 0; i < member.getFines().size(); i++) {
                    fines += member.getFines().get(i).getFineID() + "";
                    if (i != (member.getFines().size() - 1)) {
                        fines += ",";
                    }
                }
            }
            Element newFineList = doc.createElement("finelist");
            newFineList.setTextContent(fines);
            String borrows = "";
            if (member.getBorrowedCopies().isEmpty()) {
                borrows = "none";
            } else {
                for (int i = 0; i < member.getBorrowedCopies().size(); i++) {
                    borrows += member.getBorrowedCopies().get(i).getBorrowID() + "";
                    if (i != (member.getBorrowedCopies().size() - 1)) {
                        borrows += ",";
                    }
                }
            }
            Element newBorrowList = doc.createElement("borrows");
            newBorrowList.setTextContent(borrows);
            Element newUser = doc.createElement("userid");
            newUser.setTextContent(member.getUserid());
            Element newPWD = doc.createElement("pwd");
            newPWD.setTextContent(member.getPwd());
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // ADDING NODES
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            newMember.appendChild(newID);
            newMember.appendChild(newName);
            newMember.appendChild(newLastName);
            newMember.appendChild(newAddress);
            newMember.appendChild(newPhone);
            newMember.appendChild(newFineList);
            newMember.appendChild(newBorrowList);
            newMember.appendChild(newUser);
            newMember.appendChild(newPWD);
            rootNode.appendChild(newMember);
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // SENDING TO XML FILE
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("db/DBmembers.xml"));
            transformer.transform(source, result);
        } catch (TransformerConfigurationException parseE) {
            JOptionPane.showMessageDialog(null, parseE.getMessage(), "" + "Error", JOptionPane.ERROR_MESSAGE);
        } catch (TransformerException parseE) {
            JOptionPane.showMessageDialog(null, parseE.getMessage(), "" + "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParserConfigurationException | SAXException | IOException parseE) {
            JOptionPane.showMessageDialog(null, parseE.getMessage(), "" + "Error", JOptionPane.ERROR_MESSAGE);
        }


    }
}
