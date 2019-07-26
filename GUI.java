/**
 * Java Swing Manipulation by Yushen, Matt, and Nathan
 */

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class GUI extends JFrame {
    private JButton createANewBuildButton;
    private JButton retrieveAPreviousBuildButton;
    private JButton turtorialButton;
    private JPanel Panel1;
    private JButton About;
    private JLabel lab_label;
    private JFrame popup;
    private JFrame tutorialFrame;
    private static JFrame alphaFrame;
    private static JFrame beta;
    private static MainFrame mf;
    private static boolean retrievalMode;  //mode 0 is create build   mode 1 is retrieve build
    private static JFileChooser chooser;
    private static File chosenFile;

    public GUI() {

        retrievalMode = false; //default

        createANewBuildButton.setIcon(new ImageIcon("builder1.png"));

        createANewBuildButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                retrievalMode = false; //reset
                beta = new JFrame("Create a New Build");
                mf = new MainFrame();
                beta.setContentPane(mf.getMainPanel());
                beta.setVisible(true);
                beta.pack();
                beta.setLocationRelativeTo(null);
                beta.setResizable(false);

                alphaFrame = Main.getAlphaFrame(); ///
                alphaFrame.setVisible(false);//
            }
        });

        retrieveAPreviousBuildButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                retrievalMode = true; //set
                chooser = new JFileChooser();
                FileNameExtensionFilter rpcFilter = new FileNameExtensionFilter( //filter out only rpc files
                        "raw pc files", "rpc");
                chooser.setFileFilter(rpcFilter); //set the filter

                if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { //if approved
                    chosenFile = chooser.getSelectedFile(); //save chosen file

                    if (chosenFile.getName().toLowerCase().contains(".rpc")) { //allow only .rpc (in case user bypasses filter manually)
                        beta = new JFrame("Create a New Build"); //create beta window
                        mf = new MainFrame(); //Fixed
                        beta.setContentPane(mf.getMainPanel());  //Fixed
                       // beta.setContentPane(new MainFrame().getMainPanel()); //<- This was causing problem for filters in retrieval mode
                        beta.setVisible(true);
                        beta.pack();
                        beta.setLocationRelativeTo(null);
                        beta.setResizable(false);

                        alphaFrame = Main.getAlphaFrame();
                        alphaFrame.setVisible(false);
                    } else {
                        JOptionPane.showMessageDialog(tutorialFrame, "The file must be a raw pc type (.rpc)", "Error", JOptionPane.ERROR_MESSAGE);
                        actionPerformed(e); //try again recursively
                    }
                }
            }
        });
        turtorialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = "Create a New Build >> Start a build from scratch.\n" + "Retrieve a Previous Build >> Pull up a previously saved build.\n" +
                        "About >> Application information";
                JOptionPane.showMessageDialog(tutorialFrame, s, "Tutorial", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        About.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = "Thank you for choosing to the PC building Simulation app. \nThis app allows users to select " +
                        "parts and check the most recent prices across multiple online retailers. \n" +
                        "\nDesigned by Yushen Li, Nathan He, Matt Jankowski for CS 440 Spring 2019, Version 3.0";
                JOptionPane.showMessageDialog(tutorialFrame, s, "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public JPanel getPanel() {
        return Panel1;
    }

    public static JFrame getBetaFrame() {
        return beta;
    }

    public static MainFrame getMf() {
        return mf;
    }

    public static boolean isRetrievalMode() {
        return retrievalMode;
    }

    public static File getChosenFile() {
        return chosenFile;
    }

}
