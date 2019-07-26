import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class PsuFilter {
    private JPanel PsuPanel;
    private JSlider slider1;
    private JCheckBox ATX;
    private JCheckBox microATX;
    private JCheckBox miniITX;
    private JCheckBox BTX;
    private JCheckBox EPS;
    private JCheckBox SFX;
    private JCheckBox TFX;
    private JCheckBox flexATX;
    private JButton applyButton;
    private JCheckBox P400WMinus;
    private JCheckBox P400W;
    private JCheckBox P800W;
    private JCheckBox P1200W;
    private JCheckBox P1600W;
    private JCheckBox full;
    private JCheckBox semi;
    private JCheckBox no;
    private JCheckBox allMod, allWatts, allTypes;
    private JLabel thumbValue;
    private ArrayList<JCheckBox> types,wattages, modulars;
    private int savedPriceVal;

    public PsuFilter()
    {
        types = new ArrayList<>();
        wattages = new ArrayList<>();
        modulars = new ArrayList<>();

        types.add(ATX);
        types.add(microATX);
        types.add(miniITX);
        types.add(BTX);
        types.add(EPS);
        types.add(SFX);
        types.add(TFX);
        types.add(flexATX);

        wattages.add(P400WMinus);
        wattages.add(P400W);
        wattages.add(P800W);
        wattages.add(P1200W);
        wattages.add((P1600W));

        modulars.add(full);
        modulars.add(semi);
        modulars.add(no);


        int slider1Max = (int) Math.ceil(MainFrame.getHighest()); //based on highest price for given part
        savedPriceVal = MainFrame.getSavedSliderPrices()[5];
        slider1.setMaximum(slider1Max);
        if (savedPriceVal == -1) {//first time through
            slider1.setValue(slider1Max); //set to max
            thumbValue.setText("Max Price: $ " + slider1Max + "      ");
        }
        else {
            slider1.setValue(savedPriceVal);//set to saved value
            thumbValue.setText("Max Price: $ " + savedPriceVal + "      ");
        }

        allWatts.setSelected(true);
        allMod.setSelected(true);
        allTypes.setSelected(true);

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

        allTypes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox CB : types) {
                    if (allTypes.isSelected())
                        CB.setSelected(true); //turn on all else
                    else
                        CB.setSelected(false);
                }
            }
        });

        allWatts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox CB : wattages) {
                    if (allWatts.isSelected())
                        CB.setSelected(true); //turn on all else
                    else
                        CB.setSelected(false);
                }
            }
        });

        allMod.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox CB : modulars) {
                    if (allMod.isSelected())
                        CB.setSelected(true); //turn on all else
                    else
                        CB.setSelected(false);
                }
            }
        });

        //create action listeners for all checkboxes (except 'All')
        for (JCheckBox CB : types) {
            CB.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    allTypes.setSelected(false);
                }
            });
        }

        //create action listeners for all checkboxes (except 'All')
        for (JCheckBox CB : wattages) {
            CB.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    allWatts.setSelected(false);
                }
            });
        }

        //create action listeners for all checkboxes (except 'All')
        for (JCheckBox CB : modulars) {
            CB.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    allMod.setSelected(false);
                }
            });
        }

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.filteredList.clear();

                System.out.println("................................");
                //all checkbox
                if (allTypes.isSelected()) {
                    for (JCheckBox CB : types) {
                        CB.setSelected(true);
                    }
                }

                if (allWatts.isSelected()) {
                    for (JCheckBox CB : wattages) {
                        CB.setSelected(true);
                    }
                }

                if (allMod.isSelected()) {
                    for (JCheckBox CB : modulars) {
                        CB.setSelected(true);
                    }
                }

                double minPrice = 0.0;
                double maxPrice = slider1.getValue() + 0.99;  //+0.99 so that it gets one above (ex: user enters 0..400 -> still include $400.99)
                MainFrame.setSavedSliderPrices(5, slider1.getValue());

                //superlist manip:
                ArrayList<String> tempPrice = new ArrayList<>(); //price
                tempPrice.add(String.valueOf(minPrice)); //minimum is zero
                tempPrice.add(String.valueOf(maxPrice)); //maximum

                ArrayList<String> tempType = new ArrayList<>(); //Size
                for (JCheckBox CB : types) {
                    if (CB.isSelected()) { //if checkbox selected, add to superlist
                        tempType.add(CB.getText());
                    }
                }

                ArrayList<String> tempWattage = new ArrayList<>(); //Wattage
                for (JCheckBox CB : wattages) {
                    if (CB.isSelected()) { //if checkbox selected, add to superlist
                        tempWattage.add(CB.getText());
                    }
                }

                ArrayList<String> tempModular = new ArrayList<>(); //Type
                for (JCheckBox CB : modulars) {
                    if (CB.isSelected()) { //if checkbox selected, add to superlist
                        tempModular.add(CB.getText());
                    }
                }

                ArrayList<ArrayList<String>> SuperList = new ArrayList<>(); //arraylist of arraylists of strings
                SuperList.add(tempPrice);
                SuperList.add(tempType);
                SuperList.add(tempWattage);
                SuperList.add(tempModular);

                PsuFilter(SuperList); //call filter
                MainFrame mf = GUI.getMf();
                mf.populateItemsList(); //populate the itemsList
                mf.getPsuGUI().dispose(); //close filter gui
            }
        }); 
    }
    //filters out what is wanted, based on input from SuperList
    public static void PsuFilter(ArrayList<ArrayList<String>> SuperList) {
        ArrayList<PowerSupply> filteredStuff = new ArrayList<>();
        double minPrice = Double.parseDouble(SuperList.get(0).get(0));
        double maxPrice = Double.parseDouble(SuperList.get(0).get(1));

        for (PowerSupply psu : PowerSupply.psulist) { //go through the list once
            int passedTests = 0; //zero

            //filter price
            if (psu.getPrice() >= minPrice && psu.getPrice() <= maxPrice && psu.getPrice() != 0.0){  /// && case_.getPrice() != 0.0) {
                passedTests++; //passed the 'price' test
            }

            //filter type
            psu.setFlag(false); //flag is only for checkboxes!
            for (int i = 0; i < SuperList.get(1).size(); i++) {
                if (psu.getForm().equalsIgnoreCase(SuperList.get(1).get(i).trim())) { //if matches
                    psu.setFlag(true);
                }
            }
            if (psu.getFlag()) { //if at least one checkmark checked for type
                passedTests++; //passed the 'type' test
            }

            //filter wattage
            psu.setFlag(false); //reset
            for (String x : SuperList.get(2)) {
                if (x.equalsIgnoreCase("Below 400 W")) { //if this checkbox was selected
                    if (psu.getWatts() < 400) { //if hd
                        psu.setFlag(true);
                    }
                } else if (x.equalsIgnoreCase("400+ W")) { //if this checkbox was selected...
                    if (psu.getWatts() >= 400 && psu.getWatts() < 800) {
                        psu.setFlag(true);
                    }
                } else if (x.equalsIgnoreCase("800+ W")) {
                    if (psu.getWatts() >= 800 && psu.getWatts() < 1200) {
                        psu.setFlag(true);
                    }
                } else if (x.equalsIgnoreCase("1200+ W")) {
                    if (psu.getWatts() >= 1200 && psu.getWatts() < 1600) {
                        psu.setFlag(true);
                    }
                } else if (x.equalsIgnoreCase("1600+ W")) {
                    if (psu.getWatts() >= 1600) {
                        psu.setFlag(true);
                    }
                } else {
                    System.out.println("Error3?");
                }
            }

            if (psu.getFlag()) {
                passedTests++; //passed the 'wattage' test
            }

            //filter modular
            psu.setFlag(false);
            for (int i = 0; i < SuperList.get(3).size(); i++) {
                if (psu.getModular().equalsIgnoreCase(SuperList.get(3).get(i).trim())) { //if matches
                    psu.setFlag(true);
                }
            }

            if(psu.getFlag())
            {
                passedTests++;
            }

            if (passedTests == 4) { //passed  both tests
                filteredStuff.add(psu); //add
                //System.out.println(psu.getName() + ", " + psu.getWatts());
                MainFrame.filteredList.add(psu.getName());
            }
        }
    }

    public JPanel getPsuPanel()
    {
        return PsuPanel;
    }
}
