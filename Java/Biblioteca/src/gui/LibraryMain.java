/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

//**********************************************************************************************************************
//**********************************************************************************************************************
// <editor-fold defaultstate="collapsed" desc="// IMPORTS">   
import classes.Author;
import classes.Borrow;
import classes.BorrowDAL;
import classes.Copy;
import static classes.Copy.CopyState.BORROWED;
import static classes.Copy.CopyState.STORED;
import classes.CopyDAL;
import classes.Fine;
import classes.Member;
import classes.MemberDAL;
import java.awt.CardLayout;
import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import util.Date;
// </editor-fold> 
//**********************************************************************************************************************
//**********************************************************************************************************************

/**
 *
 * @author berik
 */
public class LibraryMain extends javax.swing.JFrame {

    /**
     * Creates new form LibraryMain
     */
    public LibraryMain() {
        genModelCopies();
        genMembers();
        genBorrows();
        initComponents();
        this.setLocationRelativeTo(null);
    }
//**********************************************************************************************************************
//**********************************************************************************************************************
// <editor-fold defaultstate="collapsed" desc="// GENERATORS MODELCOPIES/MEMBERS/BORROWS/USERBORROWS/FINES/USERFINES & FILLERS FOR USERPANEL">   

    private void genModelCopies() {
        CopyDAL objCopyDAL = new CopyDAL();
        listCopies = objCopyDAL.getCopies();
        modelCopies.clear();
        for (int i = 0; i < listCopies.size(); i++) {
            modelCopies.addElement(listCopies.get(i));
        }
    }

    private void genMembers() {
        MemberDAL objMemberDAL = new MemberDAL();
        listMembers = objMemberDAL.getMembers();
    }

    private void genBorrows() {
        BorrowDAL objBorrowDAL = new BorrowDAL();
        listBorrows = objBorrowDAL.getBorrows();
    }

    private void genUserBorrows() {
        //GregorianCalendar d = new GregorianCalendar();
        if (tableModelBorrow.getRowCount() > 0) {
            for (int i = tableModelBorrow.getRowCount() - 1; i > -1; i--) {
                tableModelBorrow.removeRow(i);
            }
        }
        for (int i = 0; i < loggedMember.getBorrowedCopies().size(); i++) {
            /* d=new GregorianCalendar(loggedMember.getBorrowedCopies().get(i).getBorrowDate().get(2),loggedMember.getBorrowedCopies().get(i).getBorrowDate().get(1),loggedMember.getBorrowedCopies().get(i).getBorrowDate().get(0));
             d.add(Calendar.DAY_OF_YEAR, 18);
             DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
             Date expirationDate=new Date(dateFormat.format(d.getTime()));
             System.out.println(expirationDate.getLongFormat());*/
            Object[] rowData = {loggedMember.getBorrowedCopies().get(i).getBorrowedCopy().getBook().getTitle(), loggedMember.getBorrowedCopies().get(i).getBorrowDate().getLongFormat(), loggedMember.getBorrowedCopies().get(i).getLimitDate().getLongFormat()};
            tableModelBorrow.addRow(rowData);
        }
    }

    private void genFines() {
        if (tableModelFine.getRowCount() > 0) {
            for (int i = tableModelFine.getRowCount() - 1; i > -1; i--) {
                tableModelFine.removeRow(i);
            }
        }
        for (int i = 0; i < loggedMember.getFines().size(); i++) {
            Object[] rowData = {loggedMember.getFines().get(i).getStartDate().getLongFormat(), loggedMember.getFines().get(i).getEndDate().getLongFormat(), loggedMember.getFines().get(i).getTax() + " Euros"};
            tableModelFine.addRow(rowData);
        }
    }

    private void fillMemberPanel() {
        genUserBorrows();
        genFines();
        jTextFieldMemberName.setText(loggedMember.getPersonName());
        jTextFieldMemberLastName.setText(loggedMember.getPersonLastName());
        jTextFieldMemberAddress.setText(loggedMember.getAddress());
        jTextFieldMemberPhone.setText(Integer.toString(loggedMember.getPhone()));
        jTextFieldMemberPWD.setText(loggedMember.getPwd());
    }

    private void fillBorrow() {
        Date a = new Date();
        GregorianCalendar d = new GregorianCalendar(a.get(2), (a.get(1) - 1), a.get(0));
        d.add(Calendar.DAY_OF_YEAR, 18);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date expirationDate = new Date(dateFormat.format(d.getTime()));
        lblBorrowStartDate.setText(a.getLongFormat());
        lblBorrowEndDate.setText(expirationDate.getLongFormat());
        if (validateReachBorrows() || validateFines()) {
            if (validateReachBorrows()) {
                lblBorrowMaxReached.setVisible(true);
                jButtonBorrow.setEnabled(false);
            }
            if (validateFines()) {
                lblBorrowFine.setVisible(true);
                jButtonBorrow.setEnabled(false);
            }
        } else {
            lblBorrowMaxReached.setVisible(false);
            lblBorrowFine.setVisible(false);
            jButtonBorrow.setEnabled(true);
        }
    }
// </editor-fold> 
//**********************************************************************************************************************
//**********************************************************************************************************************

    private boolean validateReachBorrows() {
        if (loggedMember.getBorrowedCopies().size() == 4) {
            return true;
        }
        return false;
    }

    private boolean validateFines() {
        if (!loggedMember.getFines().isEmpty()) {
            Date today = new Date();
            Date fine = loggedMember.getFines().get((loggedMember.getFines().size()) - 1).getEndDate();
            //System.out.println(today.getLongFormat());
            //System.out.println(fine.getLongFormat());
            int check = today.compareTo(fine);
            //System.out.println(check);
            if (check <= 0) {
                return true;
            }
        }
        return false;
    }

//**********************************************************************************************************************
//**********************************************************************************************************************
// <editor-fold defaultstate="collapsed" desc="// CLEAR FOR LOGIN & REGISTRATION PANELS/ MAKE LOGIN">   
    private void clearLogin() {
        jTextFieldUser.setText("");
        lblWrongUPWD.setVisible(false);
        jTextFieldPWD.setText("");
        lblPWD.setForeground(new Color(0, 0, 0));
        lblUser.setForeground(new Color(0, 0, 0));
    }

    private void clearRegistration() {
        jTextFieldRegisterName.setText("Nombre...");
        jTextFieldRegisterLastName.setText("Apellidos...");
        jTextFieldRegisterAddress.setText("Dirección...");
        jTextFieldRegisterPhone.setText("Número de teléfono...");
        jTextFieldRegisterUserName.setText("Nombre de usuario...");
        jTextRegisterPWD.setText("");
        lblFailedName.setVisible(false);
        lblFailedAddress.setVisible(false);
        lblFailedPhone.setVisible(false);
        lblFailedUserName.setVisible(false);
    }

    private void makeLogin() {
        CardLayout cl = (CardLayout) (searchAndBorrow.getLayout());
        CardLayout cl2 = (CardLayout) (coverPanel.getLayout());
        cl2.show(coverPanel, "memberCard");
        fillMemberPanel();
        mainJTabbedPanel.setTitleAt(0, "                         Panel de usuario                         ");
        //lblWelcomeUser.setText("Bienvenid@ " + loggedMember.getPersonName());
        switch (destination) {
            case BORROW:
                fillBorrow();
                cl.show(searchAndBorrow, "cardBorrow");
                mainJTabbedPanel.setTitleAt(1, "                       Procesar el préstamo                       ");
                break;
            case PREFERENCES:
                cl.show(searchAndBorrow, "cardSearchPanel");
                mainJTabbedPanel.setTitleAt(1, "                             Búsqueda                             ");
                mainJTabbedPanel.setSelectedIndex(0);
                memberPanel.requestFocus();
                break;
            case SEARCH:
                cl.show(searchAndBorrow, "cardSearchPanel");
                mainJTabbedPanel.setTitleAt(1, "                             Búsqueda                             ");
                break;
            default:
                throw new AssertionError();
        }
    }
// </editor-fold> 
//**********************************************************************************************************************
//**********************************************************************************************************************

//**********************************************************************************************************************
//**********************************************************************************************************************
// <editor-fold defaultstate="collapsed" desc="// FILTERS (BY STATE & TITLE/AUTHOR/ISBN)">   
    private void filterByState() {
        genModelCopies();
        stateCopies = new ArrayList();
        ArrayList<Copy> auxCopies2 = new ArrayList();
        if (jRadioStored.isSelected()) {
            for (int i = 0; i < listCopies.size(); i++) {
                if (listCopies.get(i).getState().equals(Copy.CopyState.STORED)) {
                    stateCopies.add(listCopies.get(i));
                }
            }
        } else {
            for (int i = 0; i < listCopies.size(); i++) {
                stateCopies.add(listCopies.get(i));
            }
        }
        modelCopies.clear();
        if (!jTextFieldSearch.getText().equals("") && !jTextFieldSearch.getText().startsWith("Introduce tu búsqueda...")) {
            auxCopies2 = filterByTAI(listCopies);
            for (int i = 0; i < auxCopies2.size(); i++) {
                modelCopies.addElement(auxCopies2.get(i));
            }
        } else {
            for (int i = 0; i < stateCopies.size(); i++) {
                modelCopies.addElement(stateCopies.get(i));
            }
        }
    }

    private ArrayList<Copy> filterByTAI(ArrayList<Copy> auxCopies) {
        ArrayList<Copy> auxCopies2 = new ArrayList();
        String auxText = jTextFieldSearch.getText().toLowerCase();
        if (jRadioByTitle.isSelected()) {
            for (int i = 0; i < auxCopies.size(); i++) {
                if (auxCopies.get(i).getBook().getTitle().toLowerCase().contains(auxText)) {
                    auxCopies2.add(auxCopies.get(i));
                }
            }
        } else if (jRadioByAuthor.isSelected()) {
            for (int i = 0; i < auxCopies.size(); i++) {
                for (int j = 0; j < auxCopies.get(i).getBook().getAuthor().size(); j++) {
                    if (auxCopies.get(i).getBook().getAuthor().get(j).getName().toLowerCase().contains(auxText) || auxCopies.get(i).getBook().getAuthor().get(j).getLastname().toLowerCase().contains(auxText)) {
                        auxCopies2.add(auxCopies.get(i));
                    }
                }
            }
        } else if (jRadioByISBN.isSelected()) {
            for (int i = 0; i < auxCopies.size(); i++) {
                if (auxCopies.get(i).getBook().getISBN().toLowerCase().startsWith(auxText)) {
                    auxCopies2.add(auxCopies.get(i));
                }
            }
        }
        return auxCopies2;
    }
// </editor-fold> 
//**********************************************************************************************************************
//**********************************************************************************************************************

//**********************************************************************************************************************
//**********************************************************************************************************************
// <editor-fold defaultstate="collapsed" desc="// REGISTRATION VALIDATORS">   
    private boolean validateAddress() {
        if (!jTextFieldRegisterAddress.getText().equals("Dirección...")) {
            lblFailedAddress.setVisible(false);
            return true;
        } else {
            lblFailedAddress.setVisible(true);
            return false;
        }
    }

    private boolean validateName() {
        if (!jTextFieldRegisterName.getText().equals("Nombre...")
                && !jTextFieldRegisterLastName.getText().equals("Apellidos...")) {
            lblFailedName.setVisible(false);
            return true;
        } else {
            lblFailedName.setVisible(true);
            return false;
        }
    }

    private boolean validatePhone() {
        try {
            String auxNumbStr = jTextFieldRegisterPhone.getText();
            auxNumbStr = auxNumbStr.replace(" ", "");
            int auxNumbInt = Integer.parseInt(auxNumbStr);
            lblFailedPhone.setVisible(false);
            return true;
        } catch (Exception e) {
            lblFailedPhone.setVisible(true);
            return false;
        }
    }

    private boolean validateUserName() {
        boolean userExist = false;
        String tryUser = jTextFieldRegisterUserName.getText();
        if (!tryUser.equals("Nombre de usuario...")) {
            for (int i = 0; i < listMembers.size(); i++) {
                if (listMembers.get(i).getUserid().equals(tryUser)) {
                    userExist = true;
                }
            }
            if (!userExist) {
                lblFailedUserName.setVisible(false);
                return true;
            }
            lblFailedUserName.setVisible(true);
        }
        return false;
    }
// </editor-fold> 
//**********************************************************************************************************************
//**********************************************************************************************************************

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bGroupFilterState = new javax.swing.ButtonGroup();
        bGroupFilterTAI = new javax.swing.ButtonGroup();
        jSeparator2 = new javax.swing.JSeparator();
        jMenuItem2 = new javax.swing.JMenuItem();
        mainJTabbedPanel = new javax.swing.JTabbedPane();
        coverPanel = new javax.swing.JPanel();
        welcomePanel = new javax.swing.JPanel();
        lblWelcomePhoto = new javax.swing.JLabel();
        lblWelcomeLibraryName = new javax.swing.JLabel();
        lblWelcomeLibrary = new javax.swing.JLabel();
        jButtonRegister = new javax.swing.JButton();
        lblWelcomeUser = new javax.swing.JLabel();
        jButtonOpenWelcome = new javax.swing.JButton();
        memberPanel = new javax.swing.JPanel();
        jButtonCloseMemberPanel = new javax.swing.JButton();
        jScrollTableBorrows = new javax.swing.JScrollPane();
        jTableBorrows = new javax.swing.JTable();
        jScrollTableFines = new javax.swing.JScrollPane();
        jTableFines = new javax.swing.JTable();
        jTextFieldMemberAddress = new javax.swing.JTextField();
        jTextFieldMemberPhone = new javax.swing.JTextField();
        jTextFieldMemberPWD = new javax.swing.JPasswordField();
        jTextFieldMemberName = new javax.swing.JTextField();
        jTextFieldMemberLastName = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldMemberPhone1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        registerPanel = new javax.swing.JPanel();
        jButtonGoWelcome = new javax.swing.JButton();
        jTextFieldRegisterName = new javax.swing.JTextField();
        jTextFieldRegisterLastName = new javax.swing.JTextField();
        jTextRegisterPWD = new javax.swing.JPasswordField();
        jTextFieldRegisterAddress = new javax.swing.JTextField();
        jTextFieldRegisterPhone = new javax.swing.JTextField();
        jTextFieldRegisterUserName = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButtonCompleteRegister = new javax.swing.JButton();
        lblFailedName = new javax.swing.JLabel();
        lblFailedAddress = new javax.swing.JLabel();
        lblFailedUserName = new javax.swing.JLabel();
        lblFailedPhone = new javax.swing.JLabel();
        searchAndBorrow = new javax.swing.JPanel();
        searchPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListCopies = new javax.swing.JList();
        jPanelCopyInfo = new javax.swing.JPanel();
        lblBookTitle = new javax.swing.JLabel();
        lblBookName = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lvlISBN = new javax.swing.JLabel();
        lblBookISBN = new javax.swing.JLabel();
        lblType = new javax.swing.JLabel();
        lblBookType = new javax.swing.JLabel();
        lblBookEditorial = new javax.swing.JLabel();
        lblEditorial = new javax.swing.JLabel();
        lblBookEdition = new javax.swing.JLabel();
        lblEdition1 = new javax.swing.JLabel();
        lblBookState = new javax.swing.JLabel();
        lblState = new javax.swing.JLabel();
        lblCopyCode = new javax.swing.JLabel();
        lblBookCopyCode = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jListAuthors = new javax.swing.JList();
        jButtonBorrowRequest = new javax.swing.JButton();
        lblBookImg = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jTextFieldSearch = new javax.swing.JTextField();
        jRadioAll = new javax.swing.JRadioButton();
        jRadioStored = new javax.swing.JRadioButton();
        jRadioByTitle = new javax.swing.JRadioButton();
        jRadioByAuthor = new javax.swing.JRadioButton();
        jRadioByISBN = new javax.swing.JRadioButton();
        loginPanel = new javax.swing.JPanel();
        jButtonBack = new javax.swing.JButton();
        jTabbedPaneLogin = new javax.swing.JTabbedPane();
        jPanelLogin = new javax.swing.JPanel();
        jTextFieldUser = new javax.swing.JTextField();
        lblPWD = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        jTextFieldPWD = new javax.swing.JPasswordField();
        jButtonLogin = new javax.swing.JButton();
        lblWrongUPWD = new javax.swing.JLabel();
        borrowPanel = new javax.swing.JPanel();
        jButtonBackFromBorrow = new javax.swing.JButton();
        lblDateOneDesc = new javax.swing.JLabel();
        lblDateTwoDesc = new javax.swing.JLabel();
        lblBorrowStartDate = new javax.swing.JLabel();
        lblBorrowEndDate = new javax.swing.JLabel();
        jButtonBorrow = new javax.swing.JButton();
        lblBorrowMaxReached = new javax.swing.JLabel();
        lblBorrowFine = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();

        jMenuItem2.setText("jMenuItem2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainJTabbedPanel.setBackground(new java.awt.Color(102, 102, 102));
        mainJTabbedPanel.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        mainJTabbedPanel.setMinimumSize(new java.awt.Dimension(1024, 600));
        mainJTabbedPanel.setSize(new java.awt.Dimension(1024, 600));
        mainJTabbedPanel.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                clearLog(evt);
            }
        });

        coverPanel.setLayout(new java.awt.CardLayout());

        welcomePanel.setBackground(new java.awt.Color(230, 230, 230));
        welcomePanel.setFocusable(false);

        lblWelcomePhoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cover.png"))); // NOI18N
        lblWelcomePhoto.setFocusable(false);

        lblWelcomeLibraryName.setFont(new java.awt.Font("American Typewriter", 1, 48)); // NOI18N
        lblWelcomeLibraryName.setForeground(new java.awt.Color(102, 102, 102));
        lblWelcomeLibraryName.setText("Un universo de papel");
        lblWelcomeLibraryName.setFocusable(false);

        lblWelcomeLibrary.setFont(new java.awt.Font("American Typewriter", 1, 18)); // NOI18N
        lblWelcomeLibrary.setText("Biblioteca");
        lblWelcomeLibrary.setFocusable(false);

        jButtonRegister.setText("<html>¿Aún no eres miembro?<br> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Regístrate </html>");
        jButtonRegister.setFocusable(false);
        jButtonRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRegisterActionPerformed(evt);
            }
        });

        lblWelcomeUser.setFont(new java.awt.Font("American Typewriter", 1, 14)); // NOI18N
        lblWelcomeUser.setForeground(new java.awt.Color(102, 102, 102));
        lblWelcomeUser.setText("Diseñado por José Antonio Yáñez Jiménez");
        lblWelcomeUser.setFocusable(false);

        jButtonOpenWelcome.setText("Inicia sesión");
        jButtonOpenWelcome.setActionCommand("Login");
        jButtonOpenWelcome.setFocusable(false);
        jButtonOpenWelcome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOpenSession(evt);
            }
        });

        org.jdesktop.layout.GroupLayout welcomePanelLayout = new org.jdesktop.layout.GroupLayout(welcomePanel);
        welcomePanel.setLayout(welcomePanelLayout);
        welcomePanelLayout.setHorizontalGroup(
            welcomePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(welcomePanelLayout.createSequentialGroup()
                .addContainerGap(125, Short.MAX_VALUE)
                .add(welcomePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(lblWelcomeLibrary)
                    .add(lblWelcomeLibraryName))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 175, Short.MAX_VALUE)
                .add(welcomePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jButtonRegister, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 189, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButtonOpenWelcome, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 189, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
            .add(welcomePanelLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(welcomePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(lblWelcomePhoto, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblWelcomeUser, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        welcomePanelLayout.setVerticalGroup(
            welcomePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, welcomePanelLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(welcomePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(welcomePanelLayout.createSequentialGroup()
                        .add(lblWelcomeLibraryName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 82, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lblWelcomeLibrary)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 22, Short.MAX_VALUE)
                        .add(lblWelcomeUser)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(lblWelcomePhoto)
                        .addContainerGap(12, Short.MAX_VALUE))
                    .add(welcomePanelLayout.createSequentialGroup()
                        .add(6, 6, 6)
                        .add(jButtonRegister, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(6, 6, 6)
                        .add(jButtonOpenWelcome, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        coverPanel.add(welcomePanel, "welcomeCard");

        memberPanel.setBackground(new java.awt.Color(230, 230, 230));
        memberPanel.setFocusable(false);

        jButtonCloseMemberPanel.setText("Cierra sesión");
        jButtonCloseMemberPanel.setActionCommand("Login");
        jButtonCloseMemberPanel.setFocusable(false);
        jButtonCloseMemberPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCloseSession(evt);
            }
        });

        jScrollTableBorrows.setBackground(new java.awt.Color(230, 230, 230));
        jScrollTableBorrows.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollTableBorrows.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollTableBorrows.setHorizontalScrollBar(null);

        jTableBorrows.setModel(tableModelBorrow);
        jTableBorrows.setFocusTraversalPolicyProvider(true);
        jTableBorrows.setFocusable(false);
        jTableBorrows.setGridColor(new java.awt.Color(230, 230, 230));
        jTableBorrows.setRequestFocusEnabled(false);
        jTableBorrows.setShowGrid(false);
        jTableBorrows.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                returnBook(evt);
            }
        });
        jScrollTableBorrows.setViewportView(jTableBorrows);

        jScrollTableFines.setBackground(new java.awt.Color(230, 230, 230));
        jScrollTableFines.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollTableFines.setHorizontalScrollBar(null);

        jTableFines.setModel(tableModelFine);
        jTableFines.setFocusTraversalKeysEnabled(false);
        jTableFines.setFocusable(false);
        jTableFines.setGridColor(new java.awt.Color(230, 230, 230));
        jTableFines.setRequestFocusEnabled(false);
        jTableFines.setRowSelectionAllowed(false);
        jTableFines.setShowGrid(false);
        jScrollTableFines.setViewportView(jTableFines);

        jTextFieldMemberAddress.setBackground(new java.awt.Color(230, 230, 230));
        jTextFieldMemberAddress.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        jTextFieldMemberAddress.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                colorToWhite(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                colorToBrightGrey(evt);
            }
        });

        jTextFieldMemberPhone.setBackground(new java.awt.Color(230, 230, 230));
        jTextFieldMemberPhone.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        jTextFieldMemberPhone.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                colorToWhite(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                colorToBrightGrey(evt);
            }
        });

        jTextFieldMemberPWD.setBackground(new java.awt.Color(230, 230, 230));
        jTextFieldMemberPWD.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        jTextFieldMemberPWD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PWDColorToWhite(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                PWDColorToGrey(evt);
            }
        });

        jTextFieldMemberName.setEditable(false);
        jTextFieldMemberName.setBackground(new java.awt.Color(230, 230, 230));
        jTextFieldMemberName.setBorder(null);
        jTextFieldMemberName.setFocusable(false);

        jTextFieldMemberLastName.setEditable(false);
        jTextFieldMemberLastName.setBackground(new java.awt.Color(230, 230, 230));
        jTextFieldMemberLastName.setBorder(null);
        jTextFieldMemberLastName.setFocusable(false);

        jLabel1.setText("Histórico de sanciones");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel2.setText("Préstamos actuales");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jTextFieldMemberPhone1.setBackground(new java.awt.Color(230, 230, 230));
        jTextFieldMemberPhone1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        jTextFieldMemberPhone1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                colorToWhite(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                colorToBrightGrey(evt);
            }
        });

        jLabel3.setText("Dirección");

        jLabel4.setText("Teléfono");

        jLabel8.setText("Usuario");

        jLabel9.setText("Clave");

        org.jdesktop.layout.GroupLayout memberPanelLayout = new org.jdesktop.layout.GroupLayout(memberPanel);
        memberPanel.setLayout(memberPanelLayout);
        memberPanelLayout.setHorizontalGroup(
            memberPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(memberPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(memberPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, memberPanelLayout.createSequentialGroup()
                        .add(0, 806, Short.MAX_VALUE)
                        .add(jButtonCloseMemberPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 189, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jScrollTableBorrows)
                    .add(jScrollTableFines)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, memberPanelLayout.createSequentialGroup()
                        .add(0, 178, Short.MAX_VALUE)
                        .add(memberPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel3)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jLabel8))
                        .add(18, 18, 18)
                        .add(memberPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jTextFieldMemberPhone1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 238, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jTextFieldMemberAddress, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 238, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jTextFieldMemberName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 238, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(18, 18, 18)
                        .add(memberPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jTextFieldMemberPWD, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 238, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jTextFieldMemberPhone, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 238, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jTextFieldMemberLastName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 238, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(memberPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel4)
                            .add(jLabel9))
                        .add(0, 178, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, memberPanelLayout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(memberPanelLayout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(jLabel1)
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        memberPanelLayout.setVerticalGroup(
            memberPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, memberPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jButtonCloseMemberPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 93, Short.MAX_VALUE)
                .add(memberPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(memberPanelLayout.createSequentialGroup()
                        .add(jTextFieldMemberName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(memberPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jTextFieldMemberAddress, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel3))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(memberPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jTextFieldMemberPhone1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel8)))
                    .add(memberPanelLayout.createSequentialGroup()
                        .add(jTextFieldMemberLastName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(memberPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jTextFieldMemberPhone, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel4))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(memberPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jTextFieldMemberPWD, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel9))))
                .add(58, 58, 58)
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jScrollTableBorrows, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 81, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(29, 29, 29)
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jScrollTableFines, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                .addContainerGap())
        );

        coverPanel.add(memberPanel, "memberCard");

        registerPanel.setBackground(new java.awt.Color(230, 230, 230));
        registerPanel.setFocusable(false);

        jButtonGoWelcome.setText("De vuelta a Bienvenida");
        jButtonGoWelcome.setActionCommand("Login");
        jButtonGoWelcome.setFocusable(false);
        jButtonGoWelcome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goToWelcome(evt);
            }
        });

        jTextFieldRegisterName.setBackground(new java.awt.Color(230, 230, 230));
        jTextFieldRegisterName.setForeground(new java.awt.Color(153, 153, 153));
        jTextFieldRegisterName.setText("Nombre...");
        jTextFieldRegisterName.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        jTextFieldRegisterName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                removeText(evt);
            }
        });
        jTextFieldRegisterName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                colorToWhite(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                colorToBrightGrey(evt);
            }
        });
        jTextFieldRegisterName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                checkCompleteButton(evt);
            }
        });

        jTextFieldRegisterLastName.setBackground(new java.awt.Color(230, 230, 230));
        jTextFieldRegisterLastName.setForeground(new java.awt.Color(153, 153, 153));
        jTextFieldRegisterLastName.setText("Apellidos...");
        jTextFieldRegisterLastName.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        jTextFieldRegisterLastName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                removeText(evt);
            }
        });
        jTextFieldRegisterLastName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                colorToWhite(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                colorToBrightGrey(evt);
            }
        });
        jTextFieldRegisterLastName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                checkCompleteButton(evt);
            }
        });

        jTextRegisterPWD.setBackground(new java.awt.Color(230, 230, 230));
        jTextRegisterPWD.setForeground(new java.awt.Color(153, 153, 153));
        jTextRegisterPWD.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        jTextRegisterPWD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                checkCompleteButton(evt);
            }
        });

        jTextFieldRegisterAddress.setBackground(new java.awt.Color(230, 230, 230));
        jTextFieldRegisterAddress.setForeground(new java.awt.Color(153, 153, 153));
        jTextFieldRegisterAddress.setText("Dirección...");
        jTextFieldRegisterAddress.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        jTextFieldRegisterAddress.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                removeText(evt);
            }
        });
        jTextFieldRegisterAddress.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                colorToWhite(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                colorToBrightGrey(evt);
            }
        });
        jTextFieldRegisterAddress.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                checkCompleteButton(evt);
            }
        });

        jTextFieldRegisterPhone.setBackground(new java.awt.Color(230, 230, 230));
        jTextFieldRegisterPhone.setForeground(new java.awt.Color(153, 153, 153));
        jTextFieldRegisterPhone.setText("Número de teléfono...");
        jTextFieldRegisterPhone.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        jTextFieldRegisterPhone.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                removeText(evt);
            }
        });
        jTextFieldRegisterPhone.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                colorToWhite(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                colorToBrightGrey(evt);
            }
        });
        jTextFieldRegisterPhone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                checkCompleteButton(evt);
            }
        });

        jTextFieldRegisterUserName.setBackground(new java.awt.Color(230, 230, 230));
        jTextFieldRegisterUserName.setForeground(new java.awt.Color(153, 153, 153));
        jTextFieldRegisterUserName.setText("Nombre de usuario...");
        jTextFieldRegisterUserName.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        jTextFieldRegisterUserName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                removeText(evt);
            }
        });
        jTextFieldRegisterUserName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                colorToWhite(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                colorToBrightGrey(evt);
            }
        });
        jTextFieldRegisterUserName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                checkCompleteButton(evt);
            }
        });

        jLabel5.setText("Escribe tu nombre y apellidos");

        jLabel6.setText("Escribe tu dirección y número de teléfono");

        jLabel7.setText("Para finalizar, escribe un nombre de usuario y una contraseña");

        jButtonCompleteRegister.setText("Completa el registro");
        jButtonCompleteRegister.setActionCommand("Login");
        jButtonCompleteRegister.setEnabled(false);
        jButtonCompleteRegister.setFocusable(false);
        jButtonCompleteRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tryRegistration(evt);
            }
        });

        lblFailedName.setBackground(new java.awt.Color(230, 230, 230));
        lblFailedName.setForeground(new java.awt.Color(102, 102, 0));
        lblFailedName.setText("Introduce tu nombre y apellidos");
        lblFailedName.setVisible(false);

        lblFailedAddress.setBackground(new java.awt.Color(230, 230, 230));
        lblFailedAddress.setForeground(new java.awt.Color(102, 102, 0));
        lblFailedAddress.setText("Introduce tu dirección");
        lblFailedAddress.setVisible(false);

        lblFailedUserName.setBackground(new java.awt.Color(230, 230, 230));
        lblFailedUserName.setForeground(new java.awt.Color(204, 0, 51));
        lblFailedUserName.setText("Nombre de usuario en uso");
        lblFailedUserName.setVisible(false);

        lblFailedPhone.setBackground(new java.awt.Color(230, 230, 230));
        lblFailedPhone.setForeground(new java.awt.Color(102, 102, 0));
        lblFailedPhone.setText("Introduce un número de teléfono válido (solo números)");
        lblFailedPhone.setVisible(false);

        org.jdesktop.layout.GroupLayout registerPanelLayout = new org.jdesktop.layout.GroupLayout(registerPanel);
        registerPanel.setLayout(registerPanelLayout);
        registerPanelLayout.setHorizontalGroup(
            registerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(registerPanelLayout.createSequentialGroup()
                .addContainerGap(254, Short.MAX_VALUE)
                .add(registerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(registerPanelLayout.createSequentialGroup()
                        .add(registerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, registerPanelLayout.createSequentialGroup()
                                .add(0, 0, Short.MAX_VALUE)
                                .add(jButtonGoWelcome, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 189, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(registerPanelLayout.createSequentialGroup()
                                .add(registerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                    .add(registerPanelLayout.createSequentialGroup()
                                        .add(jTextFieldRegisterAddress, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 238, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(18, 18, 18)
                                        .add(jTextFieldRegisterPhone, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 238, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(registerPanelLayout.createSequentialGroup()
                                        .add(jTextFieldRegisterUserName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 238, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(18, 18, 18)
                                        .add(jTextRegisterPWD, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 238, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(registerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(registerPanelLayout.createSequentialGroup()
                                            .add(jTextFieldRegisterName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 238, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                            .add(18, 18, 18)
                                            .add(jTextFieldRegisterLastName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 238, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                        .add(jLabel5)
                                        .add(lblFailedName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 494, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                    .add(jLabel6))
                                .add(0, 253, Short.MAX_VALUE)))
                        .addContainerGap())
                    .add(registerPanelLayout.createSequentialGroup()
                        .add(registerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 396, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(registerPanelLayout.createSequentialGroup()
                                .add(lblFailedAddress, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 238, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(18, 18, 18)
                                .add(lblFailedPhone, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 353, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(lblFailedUserName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 238, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(0, 0, Short.MAX_VALUE))))
            .add(registerPanelLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jButtonCompleteRegister, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 189, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(0, 0, Short.MAX_VALUE))
        );
        registerPanelLayout.setVerticalGroup(
            registerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, registerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jButtonGoWelcome, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 69, Short.MAX_VALUE)
                .add(jLabel5)
                .add(18, 18, 18)
                .add(registerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextFieldRegisterName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldRegisterLastName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(lblFailedName)
                .add(46, 46, 46)
                .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(28, 28, 28)
                .add(registerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextFieldRegisterAddress, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextFieldRegisterPhone, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(registerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblFailedAddress)
                    .add(lblFailedPhone))
                .add(42, 42, 42)
                .add(jLabel7)
                .add(18, 18, 18)
                .add(registerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jTextFieldRegisterUserName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextRegisterPWD, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(lblFailedUserName)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 48, Short.MAX_VALUE)
                .add(jButtonCompleteRegister, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 36, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(42, 42, 42))
        );

        coverPanel.add(registerPanel, "registerCard");

        mainJTabbedPanel.addTab("                            Bienvenida                            ", coverPanel);

        searchAndBorrow.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                ClearLogFromCard(evt);
            }
        });
        searchAndBorrow.setLayout(new java.awt.CardLayout());

        searchPanel.setBackground(new java.awt.Color(230, 230, 230));
        searchPanel.setFocusable(false);

        jListCopies.setModel(modelCopies);
        jListCopies.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                displayItem(evt);
            }
        });
        jScrollPane1.setViewportView(jListCopies);

        jPanelCopyInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Información de copia", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Trebuchet MS", 1, 14))); // NOI18N

        lblBookTitle.setBackground(new java.awt.Color(204, 204, 204));

        lblTitle.setText("Título:");

        lvlISBN.setText("ISBN:");

        lblBookISBN.setBackground(new java.awt.Color(204, 204, 204));

        lblType.setText("Tipo:");

        lblBookType.setBackground(new java.awt.Color(204, 204, 204));

        lblBookEditorial.setBackground(new java.awt.Color(204, 204, 204));

        lblEditorial.setText("Editorial:");

        lblBookEdition.setBackground(new java.awt.Color(204, 204, 204));

        lblEdition1.setText("Edición:");

        lblBookState.setBackground(new java.awt.Color(204, 204, 204));

        lblState.setText("Estado:");

        lblCopyCode.setText("Nº de Copia:");

        lblBookCopyCode.setBackground(new java.awt.Color(204, 204, 204));

        jListAuthors.setBackground(new java.awt.Color(238, 238, 238));
        jListAuthors.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Autor(es)", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));
        jListAuthors.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListAuthors.setToolTipText("");
        jListAuthors.setFocusable(false);
        jListAuthors.setSelectionBackground(new java.awt.Color(238, 238, 238));
        jListAuthors.setSelectionForeground(new java.awt.Color(0, 0, 0));
        jScrollPane3.setViewportView(jListAuthors);

        jButtonBorrowRequest.setText("Solicitar préstamo");
        jButtonBorrowRequest.setVisible(false);
        jButtonBorrowRequest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBorrowRequestActionPerformed(evt);
            }
        });

        lblBookImg.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Imagen", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));
        lblBookImg.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblBookImg.setIconTextGap(1);

        org.jdesktop.layout.GroupLayout jPanelCopyInfoLayout = new org.jdesktop.layout.GroupLayout(jPanelCopyInfo);
        jPanelCopyInfo.setLayout(jPanelCopyInfoLayout);
        jPanelCopyInfoLayout.setHorizontalGroup(
            jPanelCopyInfoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelCopyInfoLayout.createSequentialGroup()
                .add(jPanelCopyInfoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanelCopyInfoLayout.createSequentialGroup()
                        .add(242, 242, 242)
                        .add(lblBookName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 205, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanelCopyInfoLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanelCopyInfoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblEditorial)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, lvlISBN)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, lblTitle)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, lblType)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, lblEdition1))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanelCopyInfoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblBookTitle, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 250, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(lblBookISBN, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 250, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(lblBookType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 250, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(lblBookEditorial, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 250, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(lblBookEdition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 250, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(18, 18, 18)
                        .add(jPanelCopyInfoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanelCopyInfoLayout.createSequentialGroup()
                                .add(lblState)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(lblBookState, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 123, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(18, 18, 18)
                                .add(jButtonBorrowRequest, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 133, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 329, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jPanelCopyInfoLayout.createSequentialGroup()
                                .add(lblCopyCode)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(lblBookCopyCode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 250, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(27, 27, 27)
                        .add(lblBookImg, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 108, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(73, Short.MAX_VALUE))
        );
        jPanelCopyInfoLayout.setVerticalGroup(
            jPanelCopyInfoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelCopyInfoLayout.createSequentialGroup()
                .add(jPanelCopyInfoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanelCopyInfoLayout.createSequentialGroup()
                        .add(13, 13, 13)
                        .add(jPanelCopyInfoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(lblTitle, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                            .add(lblBookTitle, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(lblState, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(lblBookState, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jButtonBorrowRequest, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jPanelCopyInfoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanelCopyInfoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(lvlISBN, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(lblBookISBN, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanelCopyInfoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(lblCopyCode, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(lblBookCopyCode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jPanelCopyInfoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanelCopyInfoLayout.createSequentialGroup()
                                .add(jPanelCopyInfoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(lblType, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(lblBookType, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(jPanelCopyInfoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                    .add(lblEditorial, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(lblBookEditorial, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(jPanelCopyInfoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(lblBookEdition, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(lblEdition1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                            .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 91, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(lblBookImg, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 178, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(223, 223, 223)
                .add(lblBookName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 47, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTextFieldSearch.setForeground(new java.awt.Color(204, 204, 204));
        jTextFieldSearch.setText("Introduce tu búsqueda...");
        jTextFieldSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                removeSearchText(evt);
            }
        });
        jTextFieldSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                doFilterSearchK(evt);
            }
        });

        bGroupFilterState.add(jRadioAll);
        jRadioAll.setSelected(true);
        jRadioAll.setText("Mostrar todos");
        jRadioAll.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jRadioAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doFilterSearch(evt);
            }
        });

        bGroupFilterState.add(jRadioStored);
        jRadioStored.setText("Filtrar prestados");
        jRadioStored.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doFilterSearch(evt);
            }
        });

        bGroupFilterTAI.add(jRadioByTitle);
        jRadioByTitle.setSelected(true);
        jRadioByTitle.setText("Título");

        bGroupFilterTAI.add(jRadioByAuthor);
        jRadioByAuthor.setText("Autor");

        bGroupFilterTAI.add(jRadioByISBN);
        jRadioByISBN.setText("ISBN");
        jRadioByISBN.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        org.jdesktop.layout.GroupLayout searchPanelLayout = new org.jdesktop.layout.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
            searchPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(searchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(searchPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparator1)
                    .add(jScrollPane1)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, searchPanelLayout.createSequentialGroup()
                        .add(240, 240, 240)
                        .add(searchPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(searchPanelLayout.createSequentialGroup()
                                .add(jRadioByTitle)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(jRadioByAuthor)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(jRadioByISBN))
                            .add(jTextFieldSearch))
                        .add(240, 240, 240))
                    .add(searchPanelLayout.createSequentialGroup()
                        .add(0, 327, Short.MAX_VALUE)
                        .add(jRadioAll)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 110, Short.MAX_VALUE)
                        .add(jRadioStored)
                        .add(0, 303, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, searchPanelLayout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(jPanelCopyInfo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        searchPanelLayout.setVerticalGroup(
            searchPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, searchPanelLayout.createSequentialGroup()
                .add(24, 24, 24)
                .add(jTextFieldSearch, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(searchPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jRadioByTitle)
                    .add(jRadioByAuthor)
                    .add(jRadioByISBN))
                .add(18, 18, 18)
                .add(searchPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jRadioAll)
                    .add(jRadioStored))
                .add(69, 69, 69)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanelCopyInfo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 206, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );

        searchPanel.setVisible(false);

        searchAndBorrow.add(searchPanel, "cardSearchPanel");

        loginPanel.setBackground(new java.awt.Color(230, 230, 230));

        jButtonBack.setText("Volver a búsqueda");
        jButtonBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackActionPerformed(evt);
            }
        });

        jPanelLogin.setBackground(new java.awt.Color(222, 222, 222));

        jTextFieldUser.setBackground(new java.awt.Color(222, 222, 222));
        jTextFieldUser.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        jTextFieldUser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                colorToWhite(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                colorToGrey(evt);
            }
        });

        lblPWD.setText("Contraseña");
        lblPWD.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lblUser.setText("Usuario");
        lblUser.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jTextFieldPWD.setBackground(new java.awt.Color(222, 222, 222));
        jTextFieldPWD.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 153), 1, true));
        jTextFieldPWD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                colorToWhite(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                colorToGrey(evt);
            }
        });

        jButtonLogin.setText("Aceptar");
        jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLoginActionPerformed(evt);
            }
        });

        lblWrongUPWD.setForeground(new java.awt.Color(204, 0, 0));
        lblWrongUPWD.setText("Combinación de usuario y contraseña no válida");
        lblWrongUPWD.setVisible(false);

        org.jdesktop.layout.GroupLayout jPanelLoginLayout = new org.jdesktop.layout.GroupLayout(jPanelLogin);
        jPanelLogin.setLayout(jPanelLoginLayout);
        jPanelLoginLayout.setHorizontalGroup(
            jPanelLoginLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelLoginLayout.createSequentialGroup()
                .add(jPanelLoginLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanelLoginLayout.createSequentialGroup()
                        .add(230, 230, 230)
                        .add(jTextFieldUser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 238, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanelLoginLayout.createSequentialGroup()
                        .add(205, 205, 205)
                        .add(lblWrongUPWD))
                    .add(jPanelLoginLayout.createSequentialGroup()
                        .add(314, 314, 314)
                        .add(lblPWD))
                    .add(jPanelLoginLayout.createSequentialGroup()
                        .add(325, 325, 325)
                        .add(lblUser))
                    .add(jPanelLoginLayout.createSequentialGroup()
                        .add(230, 230, 230)
                        .add(jTextFieldPWD, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 238, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanelLoginLayout.createSequentialGroup()
                        .add(263, 263, 263)
                        .add(jButtonLogin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 175, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(195, Short.MAX_VALUE))
        );
        jPanelLoginLayout.setVerticalGroup(
            jPanelLoginLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanelLoginLayout.createSequentialGroup()
                .add(48, 48, 48)
                .add(lblUser)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTextFieldUser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 39, Short.MAX_VALUE)
                .add(lblWrongUPWD)
                .add(39, 39, 39)
                .add(lblPWD)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTextFieldPWD, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(66, 66, 66)
                .add(jButtonLogin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(32, 32, 32))
        );

        jTabbedPaneLogin.addTab("                                Acceso de Miembros                                ", jPanelLogin);

        org.jdesktop.layout.GroupLayout loginPanelLayout = new org.jdesktop.layout.GroupLayout(loginPanel);
        loginPanel.setLayout(loginPanelLayout);
        loginPanelLayout.setHorizontalGroup(
            loginPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(loginPanelLayout.createSequentialGroup()
                .add(loginPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(loginPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(jButtonBack, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 169, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(loginPanelLayout.createSequentialGroup()
                        .addContainerGap(139, Short.MAX_VALUE)
                        .add(jTabbedPaneLogin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 719, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(149, Short.MAX_VALUE))
        );
        loginPanelLayout.setVerticalGroup(
            loginPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, loginPanelLayout.createSequentialGroup()
                .addContainerGap(46, Short.MAX_VALUE)
                .add(jTabbedPaneLogin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 403, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 83, Short.MAX_VALUE)
                .add(jButtonBack, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        searchAndBorrow.add(loginPanel, "cardLogin");

        borrowPanel.setBackground(new java.awt.Color(230, 230, 230));

        jButtonBackFromBorrow.setText("Volver a búsqueda");
        jButtonBackFromBorrow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBackActionPerformed(evt);
            }
        });

        lblDateOneDesc.setText("Fecha inicial del préstamo:");

        lblDateTwoDesc.setText("Fecha límite de entrega:");

        jButtonBorrow.setText("Finalizar proceso de préstamo");
        jButtonBorrow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonBorrowActionPerformed(evt);
            }
        });

        lblBorrowMaxReached.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lblBorrowMaxReached.setForeground(new java.awt.Color(102, 102, 0));
        lblBorrowMaxReached.setText("<html>Lamentablemente ya has alcanzado tu límite de préstamos.<br>Devuelve un libro para poder procesar el préstamo</html>");
        lblBorrowMaxReached.setVisible(false);

        lblBorrowFine.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        lblBorrowFine.setForeground(new java.awt.Color(153, 0, 0));
        lblBorrowFine.setText("<html>Actualmente estás siendo sancionado debido a un retraso en tu devolución de préstamos.<br>Consulta tu panel de usuario para obtener más información</html>");
        lblBorrowFine.setVisible(false);

        org.jdesktop.layout.GroupLayout borrowPanelLayout = new org.jdesktop.layout.GroupLayout(borrowPanel);
        borrowPanel.setLayout(borrowPanelLayout);
        borrowPanelLayout.setHorizontalGroup(
            borrowPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(borrowPanelLayout.createSequentialGroup()
                .add(borrowPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(lblDateTwoDesc)
                    .add(borrowPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(borrowPanelLayout.createSequentialGroup()
                            .addContainerGap()
                            .add(jButtonBackFromBorrow, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 169, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(borrowPanelLayout.createSequentialGroup()
                            .add(119, 119, 119)
                            .add(lblDateOneDesc))))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(borrowPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblBorrowStartDate)
                    .add(lblBorrowEndDate))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, borrowPanelLayout.createSequentialGroup()
                .add(0, 351, Short.MAX_VALUE)
                .add(jButtonBorrow, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 305, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(351, Short.MAX_VALUE))
            .add(borrowPanelLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(borrowPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lblBorrowFine, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lblBorrowMaxReached, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        borrowPanelLayout.setVerticalGroup(
            borrowPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, borrowPanelLayout.createSequentialGroup()
                .addContainerGap(84, Short.MAX_VALUE)
                .add(lblBorrowMaxReached, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(31, 31, 31)
                .add(lblBorrowFine, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(92, 92, 92)
                .add(borrowPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblDateOneDesc)
                    .add(lblBorrowStartDate))
                .add(28, 28, 28)
                .add(borrowPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblDateTwoDesc)
                    .add(lblBorrowEndDate))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 69, Short.MAX_VALUE)
                .add(jButtonBorrow, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 36, Short.MAX_VALUE)
                .add(jButtonBackFromBorrow, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 37, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        searchAndBorrow.add(borrowPanel, "cardBorrow");

        mainJTabbedPanel.addTab("                             Búsqueda                             ", searchAndBorrow);

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 1007, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 575, Short.MAX_VALUE)
        );

        mainJTabbedPanel.addTab("                          Administración                          ", jPanel3);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainJTabbedPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainJTabbedPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //**********************************************************************************************************************
    //**********************************************************************************************************************
    // <editor-fold defaultstate="collapsed" desc="// SEARCH CARD METHODS">   
    private void removeSearchText(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeSearchText
        // TODO add your handling code here:
        // TODO add your handling code here:
        if (jTextFieldSearch.getText().startsWith("Introduce tu búsqueda...")) {
            jTextFieldSearch.setForeground(Color.BLACK);
            jTextFieldSearch.setText("");
        }
    }//GEN-LAST:event_removeSearchText

    private void doFilterSearch(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doFilterSearch
        // TODO add your handling code here:
        filterByState();
    }//GEN-LAST:event_doFilterSearch

    private void doFilterSearchK(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_doFilterSearchK
        // TODO add your handling code here:
        filterByState();
    }//GEN-LAST:event_doFilterSearchK

    private void displayItem(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_displayItem
        // TODO add your handling code here:
        Object a = jListCopies.getSelectedValue();
        if (a == null) {
            lblBookTitle.setText("");
            lblBookISBN.setText("");
            lblBookType.setText("");
            lblBookEditorial.setText("");
            lblBookEdition.setText("");
            lblBookState.setText("");
            lblBookCopyCode.setText("");
            modelAuthors.clear();
            jButtonBorrowRequest.setVisible(false);
            lblBookImg.setIcon(null);
        } else {
            Copy b = (Copy) (a);
            lblBookTitle.setText(b.getBook().getTitle());
            lblBookISBN.setText(b.getBook().getISBN());
            lblBookType.setText(b.getBook().getType().toString());
            lblBookEditorial.setText(b.getBook().getEditorial());
            lblBookEdition.setText(Integer.toString(b.getBook().getEdition()));
            switch (b.getState()) {
                case BORROWED:
                    lblBookState.setText("Prestado");
                    lblBookState.setForeground(Color.red);
                    jButtonBorrowRequest.setVisible(false);
                    break;
                case STORED:
                    lblBookState.setText("Disponible");
                    lblBookState.setForeground(Color.green);
                    jButtonBorrowRequest.setVisible(true);
                    break;
                default:
                    throw new AssertionError();
            }
            lblBookCopyCode.setText(Integer.toString(b.getBookCode()));
            //System.out.println(b.getBook().getImage());
            lblBookImg.setIcon(new ImageIcon(getClass().getResource(b.getBook().getImage())));
            listAuthors = new ArrayList();
            for (int i = 0; i < b.getBook().getAuthor().size(); i++) {
                listAuthors.add(b.getBook().getAuthor().get(i));
            }
            modelAuthors.clear();
            for (int i = 0; i < listAuthors.size(); i++) {
                modelAuthors.addElement(listAuthors.get(i));
            }
            jListAuthors.setModel(modelAuthors);
        }
    }//GEN-LAST:event_displayItem

    private void jButtonBorrowRequestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBorrowRequestActionPerformed
        // TODO add your handling code here:
        borrowCopy = (Copy) jListCopies.getSelectedValue();
        CardLayout cl = (CardLayout) (searchAndBorrow.getLayout());
        if (loggedMember.getUserid() == null) {
            cl.show(searchAndBorrow, "cardLogin");
            mainJTabbedPanel.setTitleAt(1, "                        Acceso de usuarios                        ");
        } else {
            fillBorrow();
            cl.show(searchAndBorrow, "cardBorrow");
            mainJTabbedPanel.setTitleAt(1, "                       Procesar el préstamo                       ");
        }
        destination = SwitchPanel.BORROW;
    }//GEN-LAST:event_jButtonBorrowRequestActionPerformed
    // </editor-fold> 
    //**********************************************************************************************************************
    //**********************************************************************************************************************

    private void jButtonBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackActionPerformed
        // TODO add your handling code here:
        clearLogin();
        CardLayout cl = (CardLayout) (searchAndBorrow.getLayout());
        cl.show(searchAndBorrow, "cardSearchPanel");
        mainJTabbedPanel.setTitleAt(1, "                             Búsqueda                             ");
    }//GEN-LAST:event_jButtonBackActionPerformed

    private void jButtonLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLoginActionPerformed
        // TODO add your handling code here:
        loggedMember = new Member();
        genMembers();
        for (int i = 0; i < listMembers.size(); i++) {
            if (jTextFieldUser.getText().equals(listMembers.get(i).getUserid()) && jTextFieldPWD.getText().equals(listMembers.get(i).getPwd())) {
                loggedMember = new Member(listMembers.get(i));
                break;
            }

        }
        if (loggedMember.getUserid() == null) {
            lblWrongUPWD.setVisible(true);
            jTextFieldPWD.setText("");
            lblPWD.setForeground(new Color(204, 0, 0));
            lblUser.setForeground(new Color(204, 0, 0));
        } else {
            makeLogin();
            validateFines();
        }
    }//GEN-LAST:event_jButtonLoginActionPerformed

    private void jButtonOpenSession(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenSession
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) (searchAndBorrow.getLayout());
        cl.show(searchAndBorrow, "cardLogin");
        mainJTabbedPanel.setTitleAt(1, "                        Acceso de usuarios                        ");
        mainJTabbedPanel.setSelectedIndex(1);
        destination = SwitchPanel.PREFERENCES;
    }//GEN-LAST:event_jButtonOpenSession

    private void jButtonRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRegisterActionPerformed
        // TODO add your handling code here:
        CardLayout cl = (CardLayout) (coverPanel.getLayout());
        cl.show(coverPanel, "registerCard");
        mainJTabbedPanel.setTitleAt(0, "                        Proceso de registro                       ");
    }//GEN-LAST:event_jButtonRegisterActionPerformed

    private void jButtonCloseSession(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCloseSession
        // TODO add your handling code here:+
        CardLayout cl = (CardLayout) (searchAndBorrow.getLayout());
        CardLayout cl2 = (CardLayout) (coverPanel.getLayout());
        loggedMember = new Member();
        cl.show(searchAndBorrow, "cardSearchPanel");
        mainJTabbedPanel.setTitleAt(1, "                             Búsqueda                             ");

        cl2.show(coverPanel, "welcomeCard");
        mainJTabbedPanel.setTitleAt(0, "                            Bienvenida                            ");
    }//GEN-LAST:event_jButtonCloseSession

    private void clearLog(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_clearLog
        // TODO add your handling code here:
        clearLogin();
    }//GEN-LAST:event_clearLog

    private void ClearLogFromCard(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_ClearLogFromCard
        // TODO add your handling code here:
        clearLogin();
    }//GEN-LAST:event_ClearLogFromCard

    private void colorToWhite(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_colorToWhite
        // TODO add your handling code here:
        JTextField txf = (JTextField) (evt.getComponent());
        txf.setBackground(new java.awt.Color(255, 255, 255));
    }//GEN-LAST:event_colorToWhite

    private void colorToGrey(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_colorToGrey
        // TODO add your handling code here:
        JTextField txf = (JTextField) (evt.getComponent());
        txf.setBackground(new java.awt.Color(222, 222, 222));
    }//GEN-LAST:event_colorToGrey

    private void PWDColorToGrey(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PWDColorToGrey
        // TODO add your handling code here:
        JPasswordField txf = (JPasswordField) (evt.getComponent());
        txf.setBackground(new java.awt.Color(230, 230, 230));
        txf.setEchoChar('•');

    }//GEN-LAST:event_PWDColorToGrey

    private void PWDColorToWhite(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PWDColorToWhite
        // TODO add your handling code here:
        JPasswordField txf = (JPasswordField) (evt.getComponent());
        txf.setBackground(new java.awt.Color(255, 255, 255));
        txf.setEchoChar((char) 0);
    }//GEN-LAST:event_PWDColorToWhite

    private void colorToBrightGrey(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_colorToBrightGrey
        // TODO add your handling code here:
        JTextField txf = (JTextField) (evt.getComponent());
        txf.setBackground(new java.awt.Color(230, 230, 230));
    }//GEN-LAST:event_colorToBrightGrey

    private void goToWelcome(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goToWelcome
        // TODO add your handling code here:
        clearRegistration();
        CardLayout cl = (CardLayout) (coverPanel.getLayout());
        cl.show(coverPanel, "welcomeCard");
        mainJTabbedPanel.setTitleAt(0, "                            Bienvenida                            ");
    }//GEN-LAST:event_goToWelcome

    private void tryRegistration(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tryRegistration
        // TODO add your handling code here:
        Member newMember = new Member();
        int aux = listMembers.size();
        newMember.setMemberID(aux);
        newMember.setAddress(jTextFieldRegisterAddress.getText());
        newMember.setBorrowedCopies(new ArrayList<Borrow>());
        newMember.setFines(new ArrayList<Fine>());
        newMember.setPersonLastName(jTextFieldRegisterLastName.getText());
        newMember.setPersonName(jTextFieldRegisterName.getText());
        String auxNumbStr = jTextFieldRegisterPhone.getText();
        auxNumbStr = auxNumbStr.replace(" ", "");
        int auxNumbInt = Integer.parseInt(auxNumbStr);
        newMember.setPhone(auxNumbInt);
        newMember.setUserid(jTextFieldRegisterUserName.getText());
        newMember.setPwd(jTextRegisterPWD.getText());
        MemberDAL newMemberDAL = new MemberDAL();
        newMemberDAL.addMember(newMember);
        genMembers();
        loggedMember = new Member(newMember);
        destination = SwitchPanel.PREFERENCES;
        makeLogin();
    }//GEN-LAST:event_tryRegistration

    private void removeText(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeText
        // TODO add your handling code here:
        JTextField aux = (JTextField) evt.getSource();
        switch (aux.getText()) {
            case "Nombre...":
                jTextFieldRegisterName.setText("");
                break;
            case "Apellidos...":
                jTextFieldRegisterLastName.setText("");
                break;
            case "Dirección...":
                jTextFieldRegisterAddress.setText("");
                break;
            case "Número de teléfono...":
                jTextFieldRegisterPhone.setText("");
                break;
            case "Nombre de usuario...":
                jTextFieldRegisterUserName.setText("");
                break;
            default:
                break;
        }
    }//GEN-LAST:event_removeText

    private void checkCompleteButton(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_checkCompleteButton
        // TODO add your handling code here:
                /*        System.out.println(!jTextFieldRegisterName.getText().equals("Nombre..."));
         System.out.println(!jTextFieldRegisterLastName.getText().equals("Apellidos..."));
         System.out.println(!jTextFieldRegisterAddress.getText().equals("Dirección..."));
         System.out.println(validatePhone());
         System.out.println(validateUserName());
         System.out.println(jTextRegisterPWD.getText().equals(""));*/
        if (validateName()
                && validateAddress()
                && validatePhone()
                && validateUserName()
                && !jTextRegisterPWD.getText().equals("")) {
            jButtonCompleteRegister.setEnabled(true);
        } else {
            jButtonCompleteRegister.setEnabled(false);
        }
    }//GEN-LAST:event_checkCompleteButton

    //**********************************************************************************************************************
    //**********************************************************************************************************************
    // <editor-fold defaultstate="collapsed" desc="// APPLY BORROW">   
    private void jButtonBorrowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBorrowActionPerformed
        // TODO add your handling code here:
        Borrow newBorrow = new Borrow();
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        //FECHAS
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        Date startDate = new Date();
        GregorianCalendar d = new GregorianCalendar(startDate.get(2), (startDate.get(1) - 1), startDate.get(0));
        //d.add(Calendar.DAY_OF_YEAR, 18);
        //d.add(Calendar.DATE, 18);
        d.add(Calendar.DAY_OF_MONTH, 18);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println(d.getTime());
        Date expirationDate = new Date(dateFormat.format(d.getTime()));
        newBorrow.setBorrowDate(startDate);
        newBorrow.setLimitDate(expirationDate);
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        // ID
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        int newID = 0;
        for (int i = 0; i < listBorrows.size(); i++) {
            if (listBorrows.get(i).getBorrowID() > newID) {
                newID = listBorrows.get(i).getBorrowID();
            }
        }
        newID++;
        newBorrow.setBorrowID(newID);
        newBorrow.setBorrowedCopy(borrowCopy);
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        // AÑADIMOS EL PRÉSTAMO A LA LISTA Y MODIFICAMOS EL ESTADO DE LA COPIA
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        BorrowDAL.addBorrow(newBorrow);
        MemberDAL.changeFineBorrow(loggedMember.getMemberID(), newBorrow.getBorrowID(), "borrows");
        CopyDAL.changeCopyState(borrowCopy.getBookCode(), "BORROWED");
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        // Recargamos desde la base de datos
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        loggedMember.setBorrowedCopies(newBorrow);
        genBorrows();
        genModelCopies();
        genMembers();
        fillMemberPanel();
        borrowCopy = new Copy();
        destination = SwitchPanel.PREFERENCES;
        CardLayout cl = (CardLayout) (searchAndBorrow.getLayout());
        cl.show(searchAndBorrow, "cardSearchPanel");
        mainJTabbedPanel.setTitleAt(1, "                             Búsqueda                             ");
        mainJTabbedPanel.setSelectedIndex(0);
        memberPanel.requestFocus();
    }//GEN-LAST:event_jButtonBorrowActionPerformed

    private void returnBook(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_returnBook
        // TODO add your handling code here:
        int a=jTableBorrows.getSelectedRow();
        System.out.println(loggedMember.getBorrowedCopies().get(a).getBorrowedCopy().getBook().getTitle());
        checkReturnDate(a);
    }//GEN-LAST:event_returnBook
    // </editor-fold> 
    //**********************************************************************************************************************
    //**********************************************************************************************************************

    private void checkReturnDate(int SelectedRow){
    Date limit=loggedMember.getBorrowedCopies().get(SelectedRow).getLimitDate();
    Date today=new Date();
    if(today.compareTo(limit)>0)
    {
    //SE HA PASADO DE FECHA, HAY QUE APLICAR SANCIÓN
        double tax=0.0;
        if(Date.difDaysBetweenDates(limit,today)>180)
        {
        tax=5.0;
        }
        /*/////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        // AÑADIMOS LA MULTA A LA LISTA
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        BorrowDAL.addBorrow(newBorrow);
        MemberDAL.changeFineBorrow(loggedMember.getMemberID(), newBorrow.getBorrowID(), "borrows");
        CopyDAL.changeCopyState(borrowCopy.getBookCode(), "BORROWED");
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        // Recargamos desde la base de datos
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////////////////////////////
        loggedMember.setBorrowedCopies(newBorrow);
        genBorrows();
        genModelCopies();
        genMembers();
        fillMemberPanel();
        borrowCopy = new Copy();
        destination = SwitchPanel.PREFERENCES;
        CardLayout cl = (CardLayout) (searchAndBorrow.getLayout());
        cl.show(searchAndBorrow, "cardSearchPanel");
        mainJTabbedPanel.setTitleAt(1, "                             Búsqueda                             ");
        mainJTabbedPanel.setSelectedIndex(0);
        memberPanel.requestFocus();*/
    }
    else
    {}
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        //**********************************************************************************************************************
        //**********************************************************************************************************************
        // <editor-fold defaultstate="collapsed" desc="// SETS WINDOWS STYLE IN WINDOWS SYSTEMS AND AQUA STYLE IN MAC SYSTEMS. FOR OTHER SYSTEMS SETS NIMBUS">   
        if (System.getProperty("os.name").startsWith("Mac OS")) {
            try {
                UIManager.setLookAndFeel("com.apple.laf.AquaLookAndFeel");
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                Logger.getLogger(LibraryMain.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else if (System.getProperty("os.name").startsWith("Windows")) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                Logger.getLogger(LibraryMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(LibraryMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
        // </editor-fold> 
        //**********************************************************************************************************************
        //**********************************************************************************************************************
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LibraryMain().setVisible(true);
            }
        });

    }
//**********************************************************************************************************************
//**********************************************************************************************************************
// <editor-fold defaultstate="collapsed" desc="// VAR DECLARATION FROM NETBEANS">   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bGroupFilterState;
    private javax.swing.ButtonGroup bGroupFilterTAI;
    private javax.swing.JPanel borrowPanel;
    private javax.swing.JPanel coverPanel;
    private javax.swing.JButton jButtonBack;
    private javax.swing.JButton jButtonBackFromBorrow;
    private javax.swing.JButton jButtonBorrow;
    private javax.swing.JButton jButtonBorrowRequest;
    private javax.swing.JButton jButtonCloseMemberPanel;
    private javax.swing.JButton jButtonCompleteRegister;
    private javax.swing.JButton jButtonGoWelcome;
    private javax.swing.JButton jButtonLogin;
    private javax.swing.JButton jButtonOpenWelcome;
    private javax.swing.JButton jButtonRegister;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jListAuthors;
    private javax.swing.JList jListCopies;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelCopyInfo;
    private javax.swing.JPanel jPanelLogin;
    private javax.swing.JRadioButton jRadioAll;
    private javax.swing.JRadioButton jRadioByAuthor;
    private javax.swing.JRadioButton jRadioByISBN;
    private javax.swing.JRadioButton jRadioByTitle;
    private javax.swing.JRadioButton jRadioStored;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollTableBorrows;
    private javax.swing.JScrollPane jScrollTableFines;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPaneLogin;
    private javax.swing.JTable jTableBorrows;
    private javax.swing.JTable jTableFines;
    private javax.swing.JTextField jTextFieldMemberAddress;
    private javax.swing.JTextField jTextFieldMemberLastName;
    private javax.swing.JTextField jTextFieldMemberName;
    private javax.swing.JTextField jTextFieldMemberPWD;
    private javax.swing.JTextField jTextFieldMemberPhone;
    private javax.swing.JTextField jTextFieldMemberPhone1;
    private javax.swing.JTextField jTextFieldPWD;
    private javax.swing.JTextField jTextFieldRegisterAddress;
    private javax.swing.JTextField jTextFieldRegisterLastName;
    private javax.swing.JTextField jTextFieldRegisterName;
    private javax.swing.JTextField jTextFieldRegisterPhone;
    private javax.swing.JTextField jTextFieldRegisterUserName;
    private javax.swing.JTextField jTextFieldSearch;
    private javax.swing.JTextField jTextFieldUser;
    private javax.swing.JTextField jTextRegisterPWD;
    private javax.swing.JLabel lblBookCopyCode;
    private javax.swing.JLabel lblBookEdition;
    private javax.swing.JLabel lblBookEditorial;
    private javax.swing.JLabel lblBookISBN;
    private javax.swing.JLabel lblBookImg;
    private javax.swing.JLabel lblBookName;
    private javax.swing.JLabel lblBookState;
    private javax.swing.JLabel lblBookTitle;
    private javax.swing.JLabel lblBookType;
    private javax.swing.JLabel lblBorrowEndDate;
    private javax.swing.JLabel lblBorrowFine;
    private javax.swing.JLabel lblBorrowMaxReached;
    private javax.swing.JLabel lblBorrowStartDate;
    private javax.swing.JLabel lblCopyCode;
    private javax.swing.JLabel lblDateOneDesc;
    private javax.swing.JLabel lblDateTwoDesc;
    private javax.swing.JLabel lblEdition1;
    private javax.swing.JLabel lblEditorial;
    private javax.swing.JLabel lblFailedAddress;
    private javax.swing.JLabel lblFailedName;
    private javax.swing.JLabel lblFailedPhone;
    private javax.swing.JLabel lblFailedUserName;
    private javax.swing.JLabel lblPWD;
    private javax.swing.JLabel lblState;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblType;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblWelcomeLibrary;
    private javax.swing.JLabel lblWelcomeLibraryName;
    private javax.swing.JLabel lblWelcomePhoto;
    private javax.swing.JLabel lblWelcomeUser;
    private javax.swing.JLabel lblWrongUPWD;
    private javax.swing.JPanel loginPanel;
    private javax.swing.JLabel lvlISBN;
    private javax.swing.JTabbedPane mainJTabbedPanel;
    private javax.swing.JPanel memberPanel;
    private javax.swing.JPanel registerPanel;
    private javax.swing.JPanel searchAndBorrow;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JPanel welcomePanel;
    // End of variables declaration//GEN-END:variables
// </editor-fold> 
//**********************************************************************************************************************
//**********************************************************************************************************************
//**********************************************************************************************************************
//**********************************************************************************************************************
// <editor-fold defaultstate="collapsed" desc="// PRIVATE VAR DECLARATIONS">  
    DefaultTableModel tableModelBorrow = new DefaultTableModel(new Object[]{"Copia prestada", "Fecha de registro", "Fecha límite de entrega"}, 3) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    DefaultTableModel tableModelFine = new DefaultTableModel(new Object[]{"Fecha de imposición", "Fecha de expiración", "Impuesto"}, 3) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    //private DefaultTableModel tableModelBorrow = new DefaultTableModel(new Object[]{"Copia prestada", "Fecha de registro", "Fecha límite"}, 3);

    enum SwitchPanel {

        BORROW, PREFERENCES, SEARCH;
    }
    ArrayList<Author> listAuthors;
    ArrayList<Copy> listCopies;
    ArrayList<Member> listMembers;
    ArrayList<Borrow> listBorrows;
    Member loggedMember = new Member();
    ArrayList<Copy> stateCopies = new ArrayList();
    SwitchPanel destination = SwitchPanel.SEARCH;
    Copy borrowCopy;
    javax.swing.DefaultListModel<Copy> modelCopies = new javax.swing.DefaultListModel<>();
    javax.swing.DefaultListModel<Author> modelAuthors = new javax.swing.DefaultListModel<>();
// </editor-fold> 
//**********************************************************************************************************************
//**********************************************************************************************************************
}
