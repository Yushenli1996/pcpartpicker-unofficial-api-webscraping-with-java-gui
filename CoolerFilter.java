import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class CoolerFilter {
    private JPanel coolerPanel;
    private JSlider slider1;
    private JLabel thumbValue;
    private JButton applyButton;
//    private JComboBox comboBox1;
//    private String selectedSocket;
    private int savedPriceVal;

    public CoolerFilter() {
/*
        //add items to jComboBox:
        comboBox1.addItem("AM1");
        comboBox1.addItem("AM2");
        comboBox1.addItem("AM2+");
        comboBox1.addItem("AM3");
        comboBox1.addItem("AM3+");
        comboBox1.addItem("AM4");
        comboBox1.addItem("C32");
        comboBox1.addItem("FM1");
        comboBox1.addItem("FM2");
        comboBox1.addItem("FM2+");
        comboBox1.addItem("G34");
        comboBox1.addItem("LGA1150");
        comboBox1.addItem("LGA1151");
        comboBox1.addItem("LGA1155");
        comboBox1.addItem("LGA1156");
        comboBox1.addItem("LGA1356");
        comboBox1.addItem("LGA1366");
        comboBox1.addItem("LGA2011");
        comboBox1.addItem("LGA2011-3");
        comboBox1.addItem("LGA2011-3 Narrow");
        comboBox1.addItem("LGA2066");
        comboBox1.addItem("LGA3647");
        comboBox1.addItem("LGA771");
        comboBox1.addItem("LGA775");
        comboBox1.addItem("TR4");

        selectedSocket = "NULL"; //not set

*/
        int slider1Max = (int) Math.ceil(MainFrame.getHighest()); //based on highest price for given part
        savedPriceVal = MainFrame.getSavedSliderPrices()[8];
        slider1.setMaximum(slider1Max);
        if (savedPriceVal == -1) {//first time through
            slider1.setValue(slider1Max); //set to max
            thumbValue.setText("Max Price: $ " + slider1Max + "      ");
        }
        else {
            slider1.setValue(savedPriceVal);//set to saved value
            thumbValue.setText("Max Price: $ " + savedPriceVal + "      ");
        }

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
/*
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //whenever an element from the JComboBox is pressed do this:
                selectedSocket = comboBox1.getSelectedItem().toString();
            }
        });
*/
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.filteredList.clear(); //filtered list is objects that go into itemsList

                System.out.println("................................");

                double minPrice = 0.0;
                double maxPrice = slider1.getValue() + 0.99;  //+0.99 so that it gets one above (ex: user enters 0..400 -> still include $400.99)
                MainFrame.setSavedSliderPrices(8, slider1.getValue());

                //superlist manip:
                ArrayList<String> tempPrice = new ArrayList<>(); //price slider
                tempPrice.add(String.valueOf(minPrice)); //minimum is zero
                tempPrice.add(String.valueOf(maxPrice)); //maximum

//                ArrayList<String> tempSocket = new ArrayList<>(); //socket combobox
//                tempSocket.add(selectedSocket); //add the one socket value chosen
                
                ArrayList<ArrayList<String>> SuperList = new ArrayList<>(); //arraylist of arraylists of strings
                SuperList.add(tempPrice);
//                SuperList.add(tempSocket);

                filter(SuperList); //call filter
                MainFrame mf = GUI.getMf();
                mf.populateItemsList(); //populate the itemsList
                mf.getCoolerGUI().dispose(); //close filter gui
            }
        });



    }

    //filters out what is wanted, based on input from SuperList
    public static void filter(ArrayList<ArrayList<String>> SuperList) {
        double minPrice = Double.parseDouble(SuperList.get(0).get(0));
        double maxPrice = Double.parseDouble(SuperList.get(0).get(1));
//        ArrayList <String> partSockets;

        for (CPUCooler cooler : CPUCooler.coolerList) { //go through the list once
            //partSockets = ....
            int passedTests = 0;

            //filter price
            if (cooler.getPrice() >= minPrice && cooler.getPrice() <= maxPrice && cooler.getPrice() != 0.0) {  /// && case_.getPrice() != 0.0) {
                passedTests++; //passed the 'price' test
            }
/*
            //filter socket
            String chosenSock = SuperList.get(1).get(0);
                                                                    //the 2nd one (below) means no item selected in filter (Shows all)
            if (chosenSock.equalsIgnoreCase(partSocket) || chosenSock.equalsIgnoreCase("NULL")){
                passedTests++;
            }
*/
            if (passedTests == 1) { //passed all 1 test
                //System.out.println(cooler.getName() + ", " + cooler.getProductID());
                MainFrame.filteredList.add(cooler.getName());
            }
        }
    }
    //return panel
    public JPanel getCoolerPanel() {
        return coolerPanel;
    }
}
