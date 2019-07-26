/**
 * Java Swing Manipulation by Yushen, Matt, and Nathan
 */

import javax.swing.*;
import java.io.IOException;

public class Main {
    private static JFrame frame;

    public static void main(String[] args) throws IOException {

        frame = new JFrame("Club PC Builder");
        frame.setContentPane(new GUI().getPanel()); //better: new JFrame, but..
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
    }

    //access alpha frame from whichever GUI
    public static JFrame getAlphaFrame(){
        return frame;
    }
}
