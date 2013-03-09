/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
public class MemberDAL {

    private static String getNodeValue(String strTag, Element eMember) {
        Node nValue = (Node)eMember.getElementsByTagName(strTag).item(0).getFirstChild();
        return nValue.getNodeValue();
    }

    public ArrayList<Member> getMembers() {
        ArrayList<Member> memberList = new ArrayList();
        try {
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
                    memberList.add(objMember);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException parseE) {
            JOptionPane.showMessageDialog(null, parseE.getMessage(), "" + "Error", JOptionPane.ERROR_MESSAGE);
        }
        return memberList;
    }
}
