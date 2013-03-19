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
 * MEMBER DATA ACCESS LAYER FOR MEMBER CLASS
 *
 * @author berik
 */
public class MemberDAL {
    
    private static String getNodeValue(String strTag, Element eMember) {
        Node nValue = (Node) eMember.getElementsByTagName(strTag).item(0).getFirstChild();
        return nValue.getNodeValue();
    }

    /**
     * Método que nos permite tanto agregar como eliminar multas y préstamos a
     * un determinado miembro.
     *
     * @param memberID ID del miembro al que queremos modificar datos
     * @param itemID ID de lo que queremos modificar
     * @param item Especifica lo que queremos modificar (borrow/fine)
     * @param method Especifica si añadimos (0) o eliminamos (1)
     */
    public static void changeFineBorrow(int memberID, int itemID, String item, int method) {
        try {
            //*********************************************************************************//
            // INSTANCIAMOS UN OBJETO QUE NOS PERMITA ALMACENAR TODA LA INFORMACIÓN DE NUESTRO //
            // FICHERO XML DE FORMA ODENADA                                                    //
            //*********************************************************************************//
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            //*********************************************************************************//
            // CARGAMOS EL FICHERO XML EN NUESTRO OBJETO                                       //
            //*********************************************************************************//
            Document doc = docBuilder.parse(new File("db/DBmembers.xml"));
            //*********************************************************************************//
            // NORMALIZAMOS NUESTRO OBJETO PARA ELIMINAR NODOS VACIOS                          //
            //*********************************************************************************//
            doc.getDocumentElement().normalize();
            int myItem = 0;
            //*********************************************************************************//
            // CARGAMOS LOS NODOS MIEMBRO PARA BUSCAR EL NODO QUE NOS INTERESA                 //
            //*********************************************************************************//
            NodeList memberNodes = doc.getElementsByTagName("member");
            for (int i = 0; i < memberNodes.getLength(); i++) {
                //*********************************************************************************//
                // EL ELEMENTO QUE INSTANCIAMOS CONTIENE UN NODO INDIVIDUAL DE UN MIEMBRO          //
                //*********************************************************************************//
                Element anElement = (Element) memberNodes.item(i);
                //*********************************************************************************//
                // COMPROBAMOS SI LA ID DE NUESTRO ELEMENTO ES LA QUE BUSCAMOS                     //
                //*********************************************************************************//
                if (Integer.parseInt(getNodeValue("id", anElement)) == memberID) {
                    myItem = i;
                    break;
                }
            }
            //*********************************************************************************//
            // CARGAMOS EL NODO ESPECÍFICO QUE ESTAMOS BUSCANDO                                //
            //*********************************************************************************//
            Node itemNode = doc.getElementsByTagName(item).item(myItem);
            NodeList list = itemNode.getChildNodes();
            String a = list.item(0).getNodeValue();
            //*********************************************************************************//
            // EDITAMOS EL CONTENIDO DEL NODO                                                  //
            //*********************************************************************************//
            switch (method) {
                case 0://CASOS EN LOS QUE AÑADIMOS ALGO A LA BASE DE DATOS
                    if (a.equals("none")) {
                        list.item(0).setTextContent(Integer.toString(itemID));
                    } else {
                        a += "," + itemID;
                        list.item(0).setTextContent(a);
                    }
                    break;
                case 1://CASOS EN LOS QUE ELIMINAMOS ALGO DE LA BASE DE DATOS
                    String finalNode;
                    String nodeVal = list.item(0).getTextContent();
                    int position = nodeVal.indexOf(item);
                    if (nodeVal.length() > 1) {
                        finalNode = nodeVal.substring(0, (position - 2));
                        finalNode += nodeVal.substring((position + 1), nodeVal.length());
                    } else {
                        finalNode = "none";
                    }
                    list.item(0).setTextContent(finalNode);
                    break;
                default:
                    throw new AssertionError();
            }
            //*********************************************************************************//
            // ESCRIBIMOS NUESTRO OBJETO CON LOS CAMBIOS DE NUEVO EN EL FICHERO XML            //
            //*********************************************************************************//
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("db/DBmembers.xml"));
            transformer.transform(source, result);
        } catch (ParserConfigurationException | SAXException | IOException | NumberFormatException | DOMException | AssertionError | TransformerFactoryConfigurationError | TransformerException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "" + "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método que genera un ArrayList de miembros. Carga la información a partir
     * de diversos ficheros XML y llamando a otros métodos similares a este que
     * generan ArrayList de multas, préstamos, etc.
     *
     * @return ArrayList con todos los miembros de la base de datos
     */
    public ArrayList<Member> getMembers() {
        //*********************************************************************************//
        // CREAMOS LOS ARRAYLIST EXTERNOS QUE NECESITAREMOS PARA NUESTROS MIEMBROS         //
        //*********************************************************************************//
        BorrowDAL objBorrowDAL = new BorrowDAL();
        ArrayList<Borrow> borrowList = objBorrowDAL.getBorrows();
        FineDAL objFineDAL = new FineDAL();
        ArrayList<Fine> fineList = objFineDAL.getFines();
        //*********************************************************************************//
        // CREAMOS EL ARRAYLIST QUE FINALMENTE DEVOLVEREMOS                                //
        //*********************************************************************************//
        ArrayList<Member> memberList = new ArrayList();
        try {
            //*********************************************************************************//
            // INSTANCIAMOS UN OBJETO QUE NOS PERMITA ALMACENAR TODA LA INFORMACIÓN DE NUESTRO //
            // FICHERO XML DE FORMA ODENADA                                                    //
            //*********************************************************************************//
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            //*********************************************************************************//
            // CARGAMOS EL FICHERO XML EN NUESTRO OBJETO                                       //
            //*********************************************************************************//
            Document doc = docBuilder.parse(new File("db/DBmembers.xml"));
            //*********************************************************************************//
            // NORMALIZAMOS NUESTRO OBJETO PARA ELIMINAR NODOS VACIOS                          //
            //*********************************************************************************//
            doc.getDocumentElement().normalize();
            //*********************************************************************************//
            // CARGAMOS LOS NODOS MIEMBRO                                                      //
            //*********************************************************************************//
            NodeList memberNodes = doc.getElementsByTagName("member");
            for (int i = 0; i < memberNodes.getLength(); i++) {
                Node member = memberNodes.item(i);
                if (member.getNodeType() == Node.ELEMENT_NODE) {
                    StringTokenizer st;
                    //*********************************************************************************//
                    // INSTANCIAMOS UN OBJETO ELEMENT QUE RELLENAMOS CON EL ELEMENTO                   //
                    // QUE NOS MANDA NUESTRO BUCLE.                                                    //
                    // TAMBIÉN CREAMOS NUESTRO OBJETO MIEMBRO QUE FINALMENTE AÑADIREMOS A NUESTRO      //
                    // ARRAYLIST                                                                       //
                    //*********************************************************************************//
                    Element anElement = (Element) member;
                    Member objMember = new Member();
                    //*********************************************************************************//
                    // RELLENAMOS NUESTRO OBJETO MEMBER (PODRÍA HACERSE IGUALMENTE CON UN CONSTRUCTOR) //
                    //*********************************************************************************//
                    objMember.setMemberID(Integer.parseInt(getNodeValue("id", anElement)));
                    objMember.setPersonName(getNodeValue("name", anElement));
                    objMember.setPersonLastName(getNodeValue("lastname", anElement));
                    objMember.setAddress(getNodeValue("address", anElement));
                    objMember.setPhone(Integer.parseInt(getNodeValue("phone", anElement)));
                    objMember.setUserid(getNodeValue("userid", anElement));
                    objMember.setPwd(getNodeValue("pwd", anElement));
                    if (!getNodeValue("dnie", anElement).equals("none")) {
                        objMember.setDnie(getNodeValue("dnie", anElement));
                    }
                    String borrows = getNodeValue("borrows", anElement);
                    //*********************************************************************************//
                    // AL UTILIZAR NORMALIZE() NOS VEMOS OBLIGADOS A INTRODUCIR ALGÚN VALOR EN LOS     //
                    // NODOS DEL FICHERO XML QUE QUEREMOS QUE EXISTAN PERO QUE NO CONTENGAN NADA.      //
                    // EN ESTE CASO PARA ESOS NODOS ESCRIBIMOS LA CADENA "NONE"                        //
                    //*********************************************************************************//
                    if (!borrows.equals("none")) {
                        //*********************************************************************************//
                        // STRINGTOKENIZER NOS PERMITE RECOGER UN STRING Y DIVIDIRLO EN SEGMENTOS          //
                        // (TOKENS) CON LOS QUE OPERAR. EL MÉTODO NEXTTOKEN ES EL QUE NOS PERMITE          //
                        // AVANZAR EN EL LISTADO DE SEGMENTOS QUE FORMAN LA CADENA OBJETIVO.               //
                        //*********************************************************************************//
                        st = new StringTokenizer(borrows, ",");
                        for (int j = 0; j <= st.countTokens(); j++) {
                            int borrowid = Integer.parseInt(st.nextToken());
                            Borrow myBorrow = new Borrow();
                            myBorrow.setBorrowID(borrowid);
                            //*********************************************************************************//
                            // BUSCAMOS EL PRÉSTAMO QUE COINCIDE CON LA ID DE PRÉSTAMO QUE TENEMOS ALMACENADA  //
                            // EN NUESTRO XML. UNA VEZ LO ENCONTRAMOS INTRODUCIMOS EL OBJETO EN NUESTRO        //
                            // OBJETO MIEMBRO.               //
                            //*********************************************************************************//
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
                    //*********************************************************************************//
                    // EL PROCESO ES EL MISMO QUE CON LOS PRÉSTAMOS                                    //
                    //*********************************************************************************//
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
                    //*********************************************************************************//
                    // UNA VEZ TENEMOS COMPLETO NUESTRO OBJETO MIEMBRO PROCEDEMOS A AGREGARLO          //
                    // AL ARRAYLIST Y REPETIMOS EL PROCESO HASTA LLEGAR AL LÍMITE DEL BUCLE            //
                    //*********************************************************************************//
                    memberList.add(objMember);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException parseE) {
            JOptionPane.showMessageDialog(null, parseE.getMessage(), "" + "Error", JOptionPane.ERROR_MESSAGE);
        }
        return memberList;
    }

    /**
     * Método que permite agregar un miembro nuevo al fichero XML.
     *
     * @param member miembro que será agregado al fichero.
     */
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
            Element newDnie = doc.createElement("dnie");
            if (member.getDnie().isEmpty()) {
                newDnie.setTextContent("none");
            } else {
                newDnie.setTextContent(member.getDnie());
            }
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
            newMember.appendChild(newDnie);
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
