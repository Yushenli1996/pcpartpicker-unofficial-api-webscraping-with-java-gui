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

public class GpuFilter {
    private JSlider slider1, slider2;; //price, speed
    private JCheckBox a1GBCheckBox;
    private JCheckBox a2GBCheckBox;
    private JCheckBox a4GBCheckBox;
    private JCheckBox a8GBCheckBox;
    private JCheckBox a8plus;
    private JCheckBox AllRAM;

    private JButton applyButton;
    private JLabel thumbValue, clockThumbValue;
    private JPanel GPUpanel;
    private ArrayList<JCheckBox> RamCB;
    private int savedPriceVal;

    public GpuFilter() {
        RamCB = new ArrayList<>();

        RamCB.add(a1GBCheckBox);
        RamCB.add(a2GBCheckBox);
        RamCB.add(a4GBCheckBox);
        RamCB.add(a8GBCheckBox);
        RamCB.add(a8plus);


        int slider1Max = (int) Math.ceil(MainFrame.getHighest()); //based on highest price for given part
        savedPriceVal = MainFrame.getSavedSliderPrices()[1];
        slider1.setMaximum(slider1Max);
        if (savedPriceVal == -1) {//first time through
            slider1.setValue(slider1Max); //set to max
            thumbValue.setText("Max Price: $ " + slider1Max + "      ");
        }
        else {
            slider1.setValue(savedPriceVal);//set to saved value
            thumbValue.setText("Max Price: $ " + savedPriceVal + "      ");
        }

        int slider2Max = (int) Math.ceil(MainFrame.getMaxClock() * 1000); //0.0-1.9
        slider2.setMaximum(slider2Max);
        int slider2Min = (int) Math.ceil(MainFrame.getMinClock() * 1000);
        slider2.setMinimum(slider2Min);
        clockThumbValue.setText(slider2Min / 1000.00 + " to " + slider2Max / 1000.00 + " GHz");
        slider2.setValue(slider2Max);

        AllRAM.setSelected(true);

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

        slider2.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                slider2.setPaintTicks(true);
                slider2.setSnapToTicks(false);
            }
        });

        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = slider2.getValue();
                clockThumbValue.setText(slider2Min / 1000.00 + " to " + String.valueOf(value/1000.00) + " GHz"); //set text real time as slider moves
            }
        });

        AllRAM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox CB : RamCB) {
                    if (AllRAM.isSelected())
                        CB.setSelected(true); //turn on all else
                    else
                        CB.setSelected(false);
                }
            }
        });

        //create action listeners for all checkboxes (except 'All')
        for (JCheckBox CB : RamCB) {
            CB.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AllRAM.setSelected(false);
                }
            });
        }

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.filteredList.clear();

                System.out.println("................................");
                //all checkbox
                if (AllRAM.isSelected()) {
                    for (JCheckBox CB : RamCB) {
                        CB.setSelected(true);
                    }
                }

                double minPrice = 0.0;
                double maxPrice = slider1.getValue() + 0.99;  //+0.99 so that it gets one above (ex: user enters 0..400 -> still include $400.99)
                double minSpeed = slider2.getMinimum() / 1000.00; //divide by 1000 to get correct value
                double maxSpeed = slider2.getValue() / 1000.00;
                MainFrame.setSavedSliderPrices(1, slider1.getValue());

                //superlist manip:
                ArrayList<String> tempPrice = new ArrayList<>(); //price
                tempPrice.add(String.valueOf(minPrice)); //minimum is zero
                tempPrice.add(String.valueOf(maxPrice)); //maximum

                ArrayList<String> tempSpeed = new ArrayList<>(); //speed = clock
                tempSpeed.add(String.valueOf(minSpeed)); //min
                tempSpeed.add(String.valueOf(maxSpeed)); //maximum

                ArrayList<String> tempRAM = new ArrayList<>(); //RAM
                for (JCheckBox CB : RamCB) {
                    if (CB.isSelected()) {
                        tempRAM.add(CB.getText());
                    }
                }

                ArrayList<ArrayList<String>> SuperList = new ArrayList<>(); //arraylist of arraylists of strings
                SuperList.add(tempPrice);
                SuperList.add(tempSpeed);
                SuperList.add(tempRAM);

                filter(SuperList); //call filter
                MainFrame mf = GUI.getMf();
                mf.populateItemsList(); //populate the itemsList
                mf.getGpuGUI().dispose(); //close filter gui
            }
        });

    }

    //filters out what is wanted, based on input from SuperList
    public static void filter(ArrayList<ArrayList<String>> SuperList) {
        //ArrayList<GPU> filteredStuff = new ArrayList<>();
        double minPrice = Double.parseDouble(SuperList.get(0).get(0));
        double maxPrice = Double.parseDouble(SuperList.get(0).get(1));
        double minSpeed = Double.parseDouble(SuperList.get(1).get(0));
        double maxSpeed = Double.parseDouble(SuperList.get(1).get(1));


        for (GPU gpu : GPU.gpuList) { //go through the list once
            int passedTests = 0;

            //filter price
            if (gpu.getPrice() >= minPrice && gpu.getPrice() <= maxPrice && gpu.getPrice() != 0.0) {  /// && case_.getPrice() != 0.0) {
                passedTests++; //passed the 'price' test
            }

            //filter core clock (speed)
            if (gpu.getClock() >= minSpeed && gpu.getClock() <= maxSpeed) {
                passedTests++; //passed the 'clock speed' test
            }

            //filter video ram
            gpu.setFlag(false);
            for (String s : SuperList.get(2)) {  //if one of the following match -> set flag
                if(s.equalsIgnoreCase("<1 GB"))
                {
                    if(gpu.getMemory() < 1.0) gpu.setFlag(true); //if given range matches given GPU video ram ....
                }
                else if (s.equalsIgnoreCase("<2 GB"))
                {
                    if(gpu.getMemory() >= 1.0 && gpu.getMemory() < 2.0) gpu.setFlag(true);
                }
                else if(s.equalsIgnoreCase("<4 GB"))
                {
                    if(gpu.getMemory() >= 2.0 && gpu.getMemory() < 4.0) gpu.setFlag(true);
                }
                else if(s.equalsIgnoreCase("<8 GB"))
                {
                    if(gpu.getMemory() >= 4.0 && gpu.getMemory() < 8.0) gpu.setFlag(true);
                }
                else
                {
                    if(gpu.getMemory() >= 8) gpu.setFlag(true);
                }
            }

            if (gpu.getFlag()) { //if a match was found
                passedTests++; //passed the 'video ram' test
            }

            if (passedTests == 3) { //passed all 3 tests
                //filteredStuff.add(gpu); //add  //?
                //System.out.println(gpu.getName() + ", " + gpu.getMemory() + ", " + gpu.getPrice() + ", " + gpu.getClock());
                MainFrame.filteredList.add(gpu.getName());
            }
        }
    }

    public JPanel getGPUpanel() {
        return GPUpanel;
    }
}
