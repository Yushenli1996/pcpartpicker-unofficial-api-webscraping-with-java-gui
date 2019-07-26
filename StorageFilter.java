import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class StorageFilter {
    private JSlider slider1;
    private JCheckBox allCapacity;
    private JCheckBox gb512minus;
    private JCheckBox gb512;
    private JCheckBox tb1;
    private JCheckBox tb2;
    private JCheckBox tb4;
    private JCheckBox allTypes;
    private JCheckBox SSD;
    private JCheckBox hybrid;
    private JCheckBox rpm5400;
    private JCheckBox rpm7200;
    private JCheckBox other;


    private JButton applyButton;
    private ArrayList<JCheckBox> SizeCB;
    private ArrayList<JCheckBox> TypeCB;
    private JLabel thumbValue;
    private JPanel storagePanel;
    private int savedPriceVal;


    public StorageFilter() {
        SizeCB = new ArrayList<>();
        TypeCB = new ArrayList<>();

        //add all CBs except "AllCheckBox"
        SizeCB.add(gb512minus);
        SizeCB.add(gb512);
        SizeCB.add(tb1);
        SizeCB.add(tb2);
        SizeCB.add(tb4);

        TypeCB.add(SSD);
        TypeCB.add(hybrid);
        TypeCB.add(rpm5400);
        TypeCB.add(rpm7200);
        TypeCB.add(other);

        int slider1Max = (int) Math.ceil(MainFrame.getHighest()); //based on highest price for given part
        savedPriceVal = MainFrame.getSavedSliderPrices()[4];
        slider1.setMaximum(slider1Max);
        if (savedPriceVal == -1) {//first time through
            slider1.setValue(slider1Max); //set to max
            thumbValue.setText("Max Price: $ " + slider1Max + "      ");
        }
        else {
            slider1.setValue(savedPriceVal);//set to saved value
            thumbValue.setText("Max Price: $ " + savedPriceVal + "      ");
        }

        allCapacity.setSelected(true);
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

        allCapacity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox CB : SizeCB) {
                    if (allCapacity.isSelected())
                        CB.setSelected(true); //turn on all else
                    else
                        CB.setSelected(false);
                }
            }
        });

        allTypes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox CB : TypeCB) {
                    if (allTypes.isSelected())
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
                    allCapacity.setSelected(false);
                }
            });
        }

        //create action listeners for all checkboxes (except 'All')
        for (JCheckBox CB : TypeCB) {
            CB.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    allTypes.setSelected(false);
                }
            });
        }

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.filteredList.clear();

                System.out.println("................................");
                //all checkbox
                if (allCapacity.isSelected()) {
                    for (JCheckBox CB : SizeCB) {
                        CB.setSelected(true);
                    }
                }

                if (allTypes.isSelected()) {
                    for (JCheckBox CB : TypeCB) {
                        CB.setSelected(true);
                    }
                }

                double minPrice = 0.0;
                double maxPrice = slider1.getValue() + 0.99;  //+0.99 so that it gets one above (ex: user enters 0..400 -> still include $400.99)
                MainFrame.setSavedSliderPrices(4, slider1.getValue());

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
                mf.getStorageGUI().dispose(); //close filter gui
            }
        });

    } //end

    //filters out what is wanted, based on input from SuperList
    public static void filter(ArrayList<ArrayList<String>> SuperList) {
        ArrayList<Storage> filteredStuff = new ArrayList<>();
        double minPrice = Double.parseDouble(SuperList.get(0).get(0));
        double maxPrice = Double.parseDouble(SuperList.get(0).get(1));

        for (Storage hd : Storage.storagelist) { //go through the list once
            int passedTests = 0;

            //filter price
            if (hd.getPrice() >= minPrice && hd.getPrice() <= maxPrice && hd.getPrice() != 0.0) {  /// && case_.getPrice() != 0.0) {
                passedTests++; //passed the 'price' test
            }

            //filter size
            hd.setFlag(false); //flag is only for checkboxes!
            double hdCapacity = Double.valueOf(hd.getCapacity().replaceAll("GB", "").trim());
            for (String s : SuperList.get(1)) {
                if (s.equalsIgnoreCase("below 512 GB")) {
                    if (hdCapacity < 512.0) {
                        hd.setFlag(true);
                    }
                } else if (s.equalsIgnoreCase("512+ GB")) {
                    if (hdCapacity < 1024.0 && hdCapacity >= 512.0) {
                        hd.setFlag(true);
                    }
                } else if (s.equalsIgnoreCase("1+ TB")) {
                    if (hdCapacity < 2048.0 && hdCapacity >= 1024.0) {
                        hd.setFlag(true);
                    }
                } else if (s.equalsIgnoreCase("2+ TB")) {
                    if (hdCapacity < 4096.0 && hdCapacity >= 2048.0) {
                        hd.setFlag(true);
                    }
                } else if (s.equalsIgnoreCase("4+ TB")) {
                    if (hdCapacity >= 4096.0) {
                        hd.setFlag(true);
                    }
                } else {
                    System.out.println("Error1?");
                }
            }

            if (hd.getFlag()) { //if at least one checkmark checked for capacity
                passedTests++; //passed the 'size' test
            }

            hd.setFlag(false); //reset
            for (String x : SuperList.get(2)) {
                if (x.equalsIgnoreCase("SSD")) { //if this checkbox was selected
                    if (hd.getType().equalsIgnoreCase(x)) { //if hd
                        hd.setFlag(true);
                    }
                } else if (x.equalsIgnoreCase("Hybrid")) { //if this checkbox was selected...
                    if (hd.getType().equalsIgnoreCase(x)) {
                        hd.setFlag(true);
                    }
                } else if (x.equalsIgnoreCase("5400 RPM")) {
                    if (hd.getType().equalsIgnoreCase(x)) {
                        hd.setFlag(true);
                    }
                } else if (x.equalsIgnoreCase("7200 RPM")) {
                    if (hd.getType().equalsIgnoreCase(x)) {
                        hd.setFlag(true);
                    }
                } else if (x.equalsIgnoreCase("Other")) {
                    if (!hd.getType().equalsIgnoreCase("SSD")
                            && !hd.getType().equalsIgnoreCase("Hybrid")
                            && !hd.getType().equalsIgnoreCase("5400 RPM")
                            && !hd.getType().equalsIgnoreCase("7200 RPM")) {
                        hd.setFlag(true);
                    }
                } else {
                    System.out.println("Error2?");
                }
            }

            if (hd.getFlag()) {
                passedTests++; //passed the 'type' test
            }

            if (passedTests == 3) { //passed all 3 tests
                filteredStuff.add(hd); //add
                //System.out.println(hd.getName() + ", " + hd.getType() + ", " + hd.getPrice());
                MainFrame.filteredList.add(hd.getName());
            }
        }
    }

    //return panel
    public JPanel getStoragePanel() {
        return storagePanel;
    }
}
