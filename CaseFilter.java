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

public class CaseFilter {
    private javax.swing.JPanel JPanel;
    private JSlider slider1;
    private JPanel CasePanel;
    private JLabel thumbValue;
    //
    private JCheckBox allCheckBox;
    private JCheckBox ATXDesktop;
    private JCheckBox ATXMini;
    private JCheckBox ATXTestBench;
    private JCheckBox HTPC;
    private JCheckBox microATXDesktop;
    private JCheckBox microATXMiniTower;
    private JCheckBox microATXSlim;
    private JCheckBox miniITXDesktop;
    private JCheckBox miniITXTestBench;
    private JCheckBox miniITXTower;
    private JCheckBox ATXFull;
    private JCheckBox ATXMid;
    private JCheckBox microATXMidTower;
    private JButton applyButton;
    private int sliderMax;
    private ArrayList<JCheckBox> types;
    private int savedPriceVal;


    public CaseFilter() {
        types = new ArrayList<>();
        types.add(ATXDesktop);
        types.add(ATXMini);
        types.add(ATXTestBench);
        types.add(HTPC);
        types.add(microATXDesktop);
        types.add(microATXMiniTower);
        types.add(microATXSlim);
        types.add(miniITXDesktop);
        types.add(miniITXTestBench);
        types.add(miniITXTower);
        types.add(ATXFull);
        types.add(ATXMid);
        types.add(microATXMidTower);

        sliderMax = (int) Math.ceil(MainFrame.getHighest()); //based on highest price for given part
        savedPriceVal = MainFrame.getSavedSliderPrices()[6];
        slider1.setMaximum(sliderMax);
        if (savedPriceVal == -1) {//first time through
            slider1.setValue(sliderMax); //set to max
            thumbValue.setText("Max Price: $ " + sliderMax + "      ");
        }
        else {
            slider1.setValue(savedPriceVal);//set to saved value
            thumbValue.setText("Max Price: $ " + savedPriceVal + "      ");
        }

        allCheckBox.setSelected(true);


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

        allCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox t : types) {
                    if (allCheckBox.isSelected())
                        t.setSelected(true); //turn on all else
                    else
                        t.setSelected(false);
                }
            }
        });

        //create action listeners for all checkboxes (except 'All')
        for (JCheckBox t : types) {
            t.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    allCheckBox.setSelected(false);
                }
            });
        }

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                MainFrame.filteredList.clear();

                System.out.println("................................");
                //all checkbox
                if (allCheckBox.isSelected()) {
                    for (JCheckBox t : types) {
                        t.setSelected(true);
                    }
                }

                double min = 0.0;
                double max = slider1.getValue() + 0.99;  //+0.99 so that it gets one above (ex: user enters 0..400 -> still include $400.99)
                MainFrame.setSavedSliderPrices(6, slider1.getValue());

                //superlist manip:
                ArrayList<String> tempPrice = new ArrayList<>();
                tempPrice.add(String.valueOf(min)); //minimum is zero
                tempPrice.add(String.valueOf(max)); //maximum

                ArrayList<String> tempType = new ArrayList<>();
                for (JCheckBox t : types) {
                    if (t.isSelected()) {
                        tempType.add(t.getText());
                    }
                }

                ArrayList<ArrayList<String>> SuperList = new ArrayList<>(); //arraylist of arraylists of strings
                SuperList.add(tempPrice);
                SuperList.add(tempType);

                caseFilter(SuperList); //call filter
                MainFrame mf = GUI.getMf();
                mf.populateItemsList(); //populate the itemsList
                mf.getCaseGUI().dispose(); //close filter gui
            }
        });

    }

    //filters out what is wanted, based on input from SuperList
    public static void caseFilter(ArrayList<ArrayList<String>> SuperList) {
        ArrayList<Case> filteredStuff = new ArrayList<>();
        double minPrice = Double.parseDouble(SuperList.get(0).get(0));
        double maxPrice = Double.parseDouble(SuperList.get(0).get(1));


        for (Case case_ : Case.caseList) { //go through the list once
            int passedTests = 0; //zero

            //filter price
            if (case_.getPrice() >= minPrice && case_.getPrice() <= maxPrice && case_.getPrice() != 0.0){  /// && case_.getPrice() != 0.0) {
                passedTests++; //passed the 'price' test
            }

            //filter type
            //ALL = <ATX, mid, full>
            case_.setFlag(false); //flag is only for checkboxes!
            for (int j = 0; j < SuperList.get(1).size(); j++) { // ... ATX Desktop, ATX Tower)
                if (case_.getType().equalsIgnoreCase(SuperList.get(1).get(j).trim())) { //if matches
                    case_.setFlag(true);
                }
            }

            if (case_.getFlag()) { //if at least one checkmark checked for type
                passedTests++; //passed the 'type' test
            }

            if (passedTests == 2) { //passed  both tests
                filteredStuff.add(case_); //add
                //System.out.println(case_.getName() + ", " + case_.getType() + ", " + case_.getPrice());
                MainFrame.filteredList.add(case_.getName());
            }
        }
    }

    public JPanel getCasePanel() {
        return CasePanel;
    }
}
