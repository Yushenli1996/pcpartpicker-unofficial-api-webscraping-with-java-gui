import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class MonitorFilter {
    private JSlider slider1;
    private JCheckBox AHVA;
    private JCheckBox PVA;
    private JCheckBox IPS;
    private JCheckBox VA;
    private JCheckBox allType;
    private JCheckBox PLS;
    private JCheckBox TN;
    private JCheckBox allSize;
    private JCheckBox below21;
    private JCheckBox a21CheckBox;
    private JCheckBox a28CheckBox;
    private JCheckBox a34CheckBox;
    private JCheckBox a40CheckBox;
    private JCheckBox above50;
    private JLabel thumbValue;
    private JPanel MonitorPanel;
    private JButton applyButton;
    private ArrayList<JCheckBox> SizeCB;
    private ArrayList<JCheckBox> TypeCB;
    private int savedPriceVal;

    public MonitorFilter()
    {
        SizeCB = new ArrayList<>();
        TypeCB = new ArrayList<>();

        //add all CBs except "AllCheckBox"
        SizeCB.add(below21);
        SizeCB.add(a21CheckBox);
        SizeCB.add(a28CheckBox);
        SizeCB.add(a34CheckBox);
        SizeCB.add(a40CheckBox);
        SizeCB.add(above50);

        TypeCB.add(AHVA);
        TypeCB.add(PVA);
        TypeCB.add(IPS);
        TypeCB.add(VA);
        TypeCB.add(PLS);
        TypeCB.add(TN);

        int slider1Max = (int) Math.ceil(MainFrame.getHighest()); //based on highest price for given part
        savedPriceVal = MainFrame.getSavedSliderPrices()[7];
        slider1.setMaximum(slider1Max);
        if (savedPriceVal == -1) {//first time through
            slider1.setValue(slider1Max); //set to max
            thumbValue.setText("Max Price: $ " + slider1Max + "      ");
        }
        else {
            slider1.setValue(savedPriceVal);//set to saved value
            thumbValue.setText("Max Price: $ " + savedPriceVal + "      ");
        }

        allSize.setSelected(true);
        allType.setSelected(true);

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

        allSize.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox CB : SizeCB) {
                    if (allSize.isSelected())
                        CB.setSelected(true); //turn on all else
                    else
                        CB.setSelected(false);
                }
            }
        });

        allType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox CB : TypeCB) {
                    if (allType.isSelected())
                        CB.setSelected(true); //turn on all else
                    else
                        CB.setSelected(false);
                }
            }
        });

        for (JCheckBox CB : SizeCB) {
            CB.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    allSize.setSelected(false);
                }
            });
        }

        //create action listeners for all checkboxes (except 'All')
        for (JCheckBox CB : TypeCB) {
            CB.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    allType.setSelected(false);
                }
            });
        }

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.filteredList.clear();

                System.out.println("................................");
                //all checkbox
                if (allSize.isSelected()) {
                    for (JCheckBox CB : SizeCB) {
                        CB.setSelected(true);
                    }
                }

                if (allType.isSelected()) {
                    for (JCheckBox CB : TypeCB) {
                        CB.setSelected(true);
                    }
                }

                double minPrice = 0.0;
                double maxPrice = slider1.getValue() + 0.99;  //+0.99 so that it gets one above (ex: user enters 0..400 -> still include $400.99)
                MainFrame.setSavedSliderPrices(7, slider1.getValue());

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

                ArrayList<String> tempType = new ArrayList<>(); //Type
                for (JCheckBox CB : TypeCB) {
                    if (CB.isSelected()) { //if checkbox selected, add to superlist
                        tempType.add(CB.getText());
                    }
                }

                ArrayList<ArrayList<String>> SuperList = new ArrayList<>(); //arraylist of arraylists of strings
                SuperList.add(tempPrice);
                SuperList.add(tempSize);
                SuperList.add(tempType);

                filter(SuperList); //call filter
                MainFrame mf = GUI.getMf();
                mf.populateItemsList(); //populate the itemsList
                mf.getMonitorGUI().dispose(); //close filter gui
            }
        });

    } //end

    public static void filter(ArrayList<ArrayList<String>> SuperList) {
        ArrayList<Monitor> filteredStuff = new ArrayList<>();
        double minPrice = Double.parseDouble(SuperList.get(0).get(0));
        double maxPrice = Double.parseDouble(SuperList.get(0).get(1));

        for (Monitor mon : Monitor.monitorlist) { //go through the list once
            int passedTests = 0;

            //filter price
            if (mon.getPrice() >= minPrice && mon.getPrice() <= maxPrice && mon.getPrice() != 0.0) {  /// && case_.getPrice() != 0.0) {
                passedTests++; //passed the 'price' test
            }


            //filter size
            mon.setFlag(false); //flag is only for checkboxes!
            for (String s : SuperList.get(1)) {
                if (s.equalsIgnoreCase("Below 21\"")) {
                    if (mon.getSize() < 21.0) {
                        mon.setFlag(true);
                    }
                } else if (s.equalsIgnoreCase("21\"+")) {
                    if (mon.getSize() < 28.0 && mon.getSize() >= 21.0) {
                        mon.setFlag(true);
                    }
                } else if (s.equalsIgnoreCase("28\"+")) {
                    if (mon.getSize() < 34.0 && mon.getSize() >= 28.0) {
                        mon.setFlag(true);
                    }
                } else if (s.equalsIgnoreCase("34\"+")) {
                    if (mon.getSize() < 40.0 && mon.getSize() >= 34.0) {
                        mon.setFlag(true);
                    }
                } else if (s.equalsIgnoreCase("40\"+")) {
                    if (mon.getSize() < 50.0 && mon.getSize() >= 40.0) {
                        mon.setFlag(true);
                    }
                } else if(s.equalsIgnoreCase("Above 50\"")){
                    if (mon.getSize() >= 50.0) {
                        mon.setFlag(true);
                    }
                }
                else
                    System.out.println("Error4?");
            }

            if (mon.getFlag()) { //if at least one checkmark checked for capacity
                passedTests++; //passed the 'size' test
            }

            mon.setFlag(false); //reset
            for (int j = 0; j < SuperList.get(2).size(); j++) {
                if (mon.getPanel().equalsIgnoreCase(SuperList.get(2).get(j).trim())) { //if matches
                    mon.setFlag(true);
                }
            }

            if (mon.getFlag()) {
                passedTests++; //passed the 'type' test
            }

            if (passedTests == 3) { //passed all 3 tests
                filteredStuff.add(mon); //add
                //System.out.println(mon.getName() + ", " + mon.getSize() + ", " + mon.getPanel());
                MainFrame.filteredList.add(mon.getName());
            }
        }
    }

    public JPanel getMonitorPanel() {
        return MonitorPanel;
    }
}
