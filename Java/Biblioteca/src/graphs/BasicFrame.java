/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package graphs;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author berik
 */
public class BasicFrame extends JFrame {

    public static void createAndShowGUI() {
        JFrame myFrame = new javax.swing.JFrame();
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = new Dimension((int) (screenSize.width / 1.5), (int) (screenSize.height / 1.5));
        int x = (int) (100);
        int y = (int) (10);
        myFrame.setBounds(x, y, frameSize.width, frameSize.height);
        myFrame.setVisible(true);
    }

    public static void createAndShowGUI(String title) {
        JFrame myFrame = new javax.swing.JFrame(title);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = new Dimension((int) (screenSize.width / 1.5), (int) (screenSize.height / 1.5));
        int x = (int) (100);
        int y = (int) (10);
        myFrame.setBounds(x, y, frameSize.width, frameSize.height);
        myFrame.setVisible(true);
    }
}
