/**
 * Java Swing Manipulation by Yushen, Matt, and Nathan
 */

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class MemoryFilter {
    private JSlider slider1;
    private JCheckBox a512MBCheckBox, a1GBCheckBox, a2GBCheckBox, a3GBCheckBox;
    private JCheckBox a4GBCheckBox, a6GBCheckBox, a8GBCheckBox, a12GBCheckBox;
    private JCheckBox a16GBCheckBox, a24GBCheckBox, a32GBCheckBox, a64GBCheckBox;
    private JCheckBox AllCheckBox;

    private JButton applyButton;
    private JLabel thumbValue;
    private JPanel memoryPanel;
    private ArrayList<JCheckBox> SizeCB;
    private int savedPriceVal;

    public MemoryFilter() {
        SizeCB = new ArrayList<>();

        //add all CBs except "AllCheckBox"
        SizeCB.add(a512MBCheckBox);
        SizeCB.add(a1GBCheckBox);
        SizeCB.add(a2GBCheckBox);
        SizeCB.add(a3GBCheckBox);
        SizeCB.add(a4GBCheckBox);
        SizeCB.add(a6GBCheckBox);
        SizeCB.add(a8GBCheckBox);
        SizeCB.add(a12GBCheckBox);
        SizeCB.add(a16GBCheckBox);
        SizeCB.add(a24GBCheckBox);
        SizeCB.add(a32GBCheckBox);
        SizeCB.add(a64GBCheckBox);


        int slider1Max = (int) Math.ceil(MainFrame.getHighest()); //based on highest price for given part
        savedPriceVal = MainFrame.getSavedSliderPrices()[2];
        slider1.setMaximum(slider1Max);
        if (savedPriceVal == -1) {//first time through
            slider1.setValue(slider1Max); //set to max
            thumbValue.setText("Max Price: $ " + slider1Max + "      ");
        }
        else {
            slider1.setValue(savedPriceVal);//set to saved value
            thumbValue.setText("Max Price: $ " + savedPriceVal + "      ");
        }

        AllCheckBox.setSelected(true);

        slider1.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                slider1.setPaintTicks(true);
                slider1.setSnapToTicks(false);
            }
        });

        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = slider1.getValue();
                thumbValue.setText("Max Price: $ " + String.valueOf(value)); //set text real time as slider moves
            }
        });

        AllCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox CB : SizeCB) {
                    if (AllCheckBox.isSelected())
                        CB.setSelected(true); //turn on all else
                    else
                        CB.setSelected(false);
                }
            }
        });

        //create action listeners for all checkboxes (except 'All')
        for (JCheckBox CB : SizeCB) {
            CB.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AllCheckBox.setSelected(false);
                }
            });
        }

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.filteredList.clear();

                System.out.println("................................");
                //all checkbox
                if (AllCheckBox.isSelected()) {
                    for (JCheckBox CB : SizeCB) {
                        CB.setSelected(true);
                    }
                }

                double minPrice = 0.0;
                double maxPrice = slider1.getValue() + 0.99;  //+0.99 so that it gets one above (ex: user enters 0..400 -> still include $400.99)
                MainFrame.setSavedSliderPrices(2, slider1.getValue());

                //superlist manip:
                ArrayList<String> tempPrice = new ArrayList<>(); //price
                tempPrice.add(String.valueOf(minPrice)); //minimum is zero
                tempPrice.add(String.valueOf(maxPrice)); //maximum

                ArrayList<String> tempSize = new ArrayList<>(); //Size
                for (JCheckBox CB : SizeCB) {
                    if (CB.isSelected()) { //if checkbox selected, add to superlist
                        tempSize.add(CB.getText());
                    }
                }

                ArrayList<ArrayList<String>> SuperList = new ArrayList<>(); //arraylist of arraylists of strings
                SuperList.add(tempPrice);
                SuperList.add(tempSize);

                filter(SuperList); //call filter
                MainFrame mf = GUI.getMf();
                mf.populateItemsList(); //populate the itemsList
                mf.getMemoryGUI().dispose(); //close filter gui
            }
        });



    }

    //filters out what is wanted, based on input from SuperList
    public static void filter(ArrayList<ArrayList<String>> SuperList) {
        double minPrice = Double.parseDouble(SuperList.get(0).get(0));
        double maxPrice = Double.parseDouble(SuperList.get(0).get(1));

        for (Memory mem : Memory.memList) { //go through the list once
            int passedTests = 0;

            //filter price
            if (mem.getPrice() >= minPrice && mem.getPrice() <= maxPrice && mem.getPrice() != 0.0) {  /// && case_.getPrice() != 0.0) {
                passedTests++; //passed the 'price' test
            }

            //filter gb size
            mem.setFlag(false); //flag is only for checkboxes!
            for (String s : SuperList.get(1)) { //512 MB, 1 GB ...64+ GB
                //trim off + and spaces, GB
                String t[] = s.split(" "); //remove spaces
                String u[] = t[0].split("\\+"); //remove plus signs
                //System.out.println("u[0] is " + u[0]);

                if(s.equalsIgnoreCase("32+ GB")) //if checkmark "32+ GB" was checked
                {
                    if(mem.getSize() >= 32.0 && mem.getSize() < 64.0) mem.setFlag(true); //check valid range
                }
                else if(s.equalsIgnoreCase("64+ GB")) //if checkmark "64+ GB" was checked
                {
                    if(mem.getSize() >= 64.0) mem.setFlag(true); //check valid range
                }
                else if (mem.getSize() == Double.valueOf(u[0].trim())) { //if matches (for all other cases)
                    mem.setFlag(true);
                }
            }

            if (mem.getFlag()) { //if a match was found
                passedTests++; //passed the 'size' test
            }

            if (passedTests == 2) { //passed all 2 tests
                //System.out.println(mem.getName() + ", " + mem.getSize() + ", " + mem.getPrice());
                MainFrame.filteredList.add(mem.getName() + "/" + mem.getSize() + "GB/"
                        + mem.getSpeed()); //add special sequence (memory)
            }
        }
    }

    //return panel
    public JPanel getMemoryPanel() {
        return memoryPanel;
    }
}