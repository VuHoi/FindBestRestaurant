package lan_chat;

import java.awt.*;
import javax.swing.*;

/**
 *construct a frame, and wait for user to choose a function
 * @author Gaunthan
 *
 */
public class LANtrans {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new ChooserFrame();

                frame.setTitle("LANtrans");
                frame.setResizable(false);
                frame.setIconImage(new ImageIcon("chat.png").getImage());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}