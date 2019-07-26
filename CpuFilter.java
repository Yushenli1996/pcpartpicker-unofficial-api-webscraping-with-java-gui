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

public class CpuFilter {
    private JPanel CPUpanel;
    private JSlider slider1;
    private JCheckBox c13, c4, c6, c8, c10, c12plus;
    private JLabel Cores;
    private JCheckBox AMD, Intel;
    private JLabel Manufacture;
    private JLabel thumbValue;

    private JButton applyButton;
    private ArrayList<JCheckBox> CoresCB;
    private ArrayList<JCheckBox> ManufactureCB;
    private JCheckBox AllMan;
    private JCheckBox AllCores;
    private int savedPriceVal;


    public CpuFilter()
    {
        CoresCB = new ArrayList<>();
        ManufactureCB = new ArrayList<>();

        CoresCB.add(c13);
        CoresCB.add(c4);
        CoresCB.add(c6);
        CoresCB.add(c8);
        CoresCB.add(c10);
        CoresCB.add(c12plus);

        ManufactureCB.add(AMD);
        ManufactureCB.add(Intel);

        int slider1Max = (int) Math.ceil(MainFrame.getHighest()); //based on highest price for given part
        savedPriceVal = MainFrame.getSavedSliderPrices()[0];
        slider1.setMaximum(slider1Max);
        if (savedPriceVal == -1) {//first time through
            slider1.setValue(slider1Max); //set to max
            thumbValue.setText("Max Price: $ " + slider1Max + "      ");
        }
        else {
            slider1.setValue(savedPriceVal);//set to saved value
            thumbValue.setText("Max Price: $ " + savedPriceVal + "      ");
        }
        AllMan.setSelected(true);
        AllCores.setSelected(true);

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


        AllMan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox CB : ManufactureCB) {
                    if (AllMan.isSelected())
                        CB.setSelected(true); //turn on all else
                    else
                        CB.setSelected(false);
                }
            }
        });

        AllCores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox CB : CoresCB) {
                    if (AllCores.isSelected())
                        CB.setSelected(true); //turn on all else
                    else
                        CB.setSelected(false);
                }
            }
        });

        //create action listeners for all checkboxes (except 'All')
        for (JCheckBox CB : ManufactureCB) {
            CB.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AllMan.setSelected(false);
                }
            });
        }

        //create action listeners for all checkboxes (except 'All')
        for (JCheckBox CB : CoresCB) {
            CB.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AllCores.setSelected(false);
                }
            });
        }

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.filteredList.clear();

                System.out.println("................................");
                //all checkbox
                if (AllMan.isSelected()) {
                    for (JCheckBox CB : ManufactureCB) {
                        CB.setSelected(true);
                    }
                }
                
                if (AllCores.isSelected()) {
                    for (JCheckBox CB : CoresCB) {
                        CB.setSelected(true);
                    }
                }

                double minPrice = 0.0;
                double maxPrice = slider1.getValue() + 0.99;  //+0.99 so that it gets one above (ex: user enters 0..400 -> still include $400.99)
                MainFrame.setSavedSliderPrices(0, slider1.getValue());

                //superlist manip:
                ArrayList<String> tempPrice = new ArrayList<>(); //price
                tempPrice.add(String.valueOf(minPrice)); //minimum is zero
                tempPrice.add(String.valueOf(maxPrice)); //maximum

                ArrayList<String> tempManufacturer = new ArrayList<>(); //man
                for (JCheckBox CB : ManufactureCB) {
                    if (CB.isSelected()) {
                        tempManufacturer.add(CB.getText());
                    }
                }

                ArrayList<String> tempCores = new ArrayList<>(); //cores
                for (JCheckBox CB : CoresCB) {
                    if (CB.isSelected()) {
                        tempCores.add(CB.getText());
                    }
                }

                ArrayList<ArrayList<String>> SuperList = new ArrayList<>(); //arraylist of arraylists of strings
                SuperList.add(tempPrice);
                SuperList.add(tempManufacturer);
                SuperList.add(tempCores);

                filter(SuperList); //call filter
                MainFrame mf = GUI.getMf();
                mf.populateItemsList(); //populate the itemsList
                mf.getCpuGUI().dispose(); //close filter gui
            }
        });
    }

    //filters out what is wanted, based on input from SuperList
    public static void filter(ArrayList<ArrayList<String>> SuperList) {
        ArrayList<CPU> filteredStuff = new ArrayList<>();
        double minPrice = Double.parseDouble(SuperList.get(0).get(0));
        double maxPrice = Double.parseDouble(SuperList.get(0).get(1));


        for (CPU cpu : CPU.cpulist) { //go through the list once
            int passedTests = 0;

            //filter price
            if (cpu.getPrice() >= minPrice && cpu.getPrice() <= maxPrice && cpu.getPrice() != 0.0){  /// && case_.getPrice() != 0.0) {
                passedTests++; //passed the 'price' test
            }

            //filter manufacturers
            cpu.setFlag(false); //flag is only for checkboxes!
            for(String s : SuperList.get(1))
            {
                if (cpu.getName().contains(s.trim())) { //if contains string "AMD" vs "Intel"
                    cpu.setFlag(true);
                }
            }
            if (cpu.getFlag()) { //if at least one checkmark checked for manufacturers
                passedTests++; //passed the 'man' test
            }

            //filter cores
            cpu.setFlag(false);
            for (String s : SuperList.get(2)) {
                if(s.equalsIgnoreCase("1-3"))
                {
                    if(cpu.getCores() <=3) cpu.setFlag(true);
                }
                else if (s.equalsIgnoreCase("12+"))
                {
                    if(cpu.getCores() >= 12) cpu.setFlag(true);
                }
                else
                {
                    if(cpu.getCores() == Integer.parseInt(s.trim())) //if matches
                        cpu.setFlag(true);
                }
            }

            if (cpu.getFlag()) { //if at least one checkmark checked for cores
                passedTests++; //passed the 'cores' test
            }

            if (passedTests == 3) { //passed all 3 tests
                filteredStuff.add(cpu); //add
                //System.out.println(cpu.getName() + ", " + cpu.getCores() + ", " + cpu.getPrice());
                MainFrame.filteredList.add(cpu.getName());
            }
        }
    }

    public JPanel getCPUpanel() {
        return CPUpanel;
    }

}
