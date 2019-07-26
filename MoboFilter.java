import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class MoboFilter {
    private JPanel moboPanel;
    private JSlider slider1;
    private JComboBox socketComboBox;
    private JLabel thumbValue;
    private JButton applyButton;
    private JSlider slider2;
    private JLabel maxRamThumbValue;
    private String socket;
    private static ArrayList<String> otherSockets;
    private int savedPriceVal;

    public MoboFilter() {

        //add items to jComboBox:
        socketComboBox.addItem("AM1");
        socketComboBox.addItem("AM2");
        socketComboBox.addItem("AM2+/AM2");
        socketComboBox.addItem("AM3");
        socketComboBox.addItem("AM3+");
        socketComboBox.addItem("AM3+/AM3");
        socketComboBox.addItem("AM3/AM2+");
        socketComboBox.addItem("AM3/AM2+/AM2");
        socketComboBox.addItem("AM4");
        socketComboBox.addItem("FM1");
        socketComboBox.addItem("FM2");
        socketComboBox.addItem("FM2+");
        socketComboBox.addItem("G34 x 2");
        socketComboBox.addItem("LGA1150");
        socketComboBox.addItem("LGA1151");
        socketComboBox.addItem("LGA1155");
        socketComboBox.addItem("LGA1156");
        socketComboBox.addItem("LGA1366");
        socketComboBox.addItem("LGA1366 x 2");
        socketComboBox.addItem("LGA2011");
        socketComboBox.addItem("LGA2011 x 2");
        socketComboBox.addItem("LGA2011-3");
        socketComboBox.addItem("LGA2011-3 Narrow");
        socketComboBox.addItem("LGA2011-3 Narrow x 2");
        socketComboBox.addItem("LGA2011-3 x 2");
        socketComboBox.addItem("LGA2066");
        socketComboBox.addItem("LGA775");
        socketComboBox.addItem("TR4");
        socketComboBox.addItem("Other Socket types (Integrated)"); //exact words as below
        
        otherSockets = new ArrayList<>(); //integrated (rare sockets)
        otherSockets.add("A4-5000");
        otherSockets.add("Athlon II X2 215");
        otherSockets.add("Atom 230");
        otherSockets.add("Atom 330");
        otherSockets.add("Atom C2358");
        otherSockets.add("Atom C2550");
        otherSockets.add("Atom C2750");
        otherSockets.add("Atom D2500");
        otherSockets.add("Atom D2550");
        otherSockets.add("Atom D2700");
        otherSockets.add("Atom D410");
        otherSockets.add("Atom D425");
        otherSockets.add("Atom D510");
        otherSockets.add("Atom D525");
        otherSockets.add("Atom N550");
        otherSockets.add("C-Series C-70");
        otherSockets.add("Celeron 1037U");
        otherSockets.add("Celeron 847");
        otherSockets.add("Celeron J1900");
        otherSockets.add("Celeron N3050");
        otherSockets.add("Celeron N3150");
        otherSockets.add("E-Series E-350");
        otherSockets.add("E-Series E-450");
        otherSockets.add("Pentium J3710");
        otherSockets.add("Pentium N3700");
        otherSockets.add("Xeon D-1520");
        otherSockets.add("Xeon D-1521");
        otherSockets.add("Xeon D-1537");
        otherSockets.add("Xeon D-1541");

        socket = "NULL"; //not set

        int slider1Max = (int) Math.ceil(MainFrame.getHighest()); //based on highest price for given part
        savedPriceVal = MainFrame.getSavedSliderPrices()[3];
        slider1.setMaximum(slider1Max);
        if (savedPriceVal == -1) {//first time through
            slider1.setValue(slider1Max); //set to max
            thumbValue.setText("Max Price: $ " + slider1Max + "      ");
        }
        else {
            slider1.setValue(savedPriceVal);//set to saved value
            thumbValue.setText("Max Price: $ " + savedPriceVal + "      ");
        }

        //int slider2Max = (int) Math.ceil(MainFrame.getMax_MaxRam() * 1000); //0.0-1.9
        int slider2Max = 128000; //128 will represent 128 or above
        slider2.setMaximum(slider2Max);
        int slider2Min = (int) Math.ceil(MainFrame.getMin_MaxRam() * 1000); //real scraped value
        slider2.setMinimum(slider2Min);
        maxRamThumbValue.setText(slider2Min / 1000 + " to " + slider2Max / 1000 + " GB");
        slider2.setValue(slider2Max);

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
                slider2.setPaintTicks(false);
                slider2.setSnapToTicks(false);
            }
        });

        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = slider2.getValue();
                maxRamThumbValue.setText(slider2Min / 1000 + " to " + String.valueOf(value/1000) + " GB"); //set text real time as slider moves
            }
        });

        socketComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //whenever an element from the JComboBox is pressed do this:
                socket = socketComboBox.getSelectedItem().toString();
            }
        });

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.filteredList.clear(); //filtered list is objects that go into itemsList

                System.out.println("................................");

                double minPrice = 0.0;
                double maxPrice = slider1.getValue() + 0.99;  //+0.99 so that it gets one above (ex: user enters 0..400 -> still include $400.99)
                double min_MaxRam = slider2.getMinimum() / 1000.00; //divide by 1000 to get correct value
                double max_MaxRam = slider2.getValue() / 1000.00;
                MainFrame.setSavedSliderPrices(3, slider1.getValue());

                //superlist manip:
                ArrayList<String> tempPrice = new ArrayList<>(); //price slider
                tempPrice.add(String.valueOf(minPrice)); //minimum is zero
                tempPrice.add(String.valueOf(maxPrice)); //maximum

                ArrayList<String> tempSocket = new ArrayList<>(); //socket combobox
                tempSocket.add(socket); //add the one socket value chosen

                ArrayList<String> tempMaxRam = new ArrayList<>(); //max Ram slider
                tempMaxRam.add(String.valueOf(min_MaxRam)); //min
                tempMaxRam.add(String.valueOf(max_MaxRam)); //maximum
                
                //create superlist and add created lists
                ArrayList<ArrayList<String>> SuperList = new ArrayList<>(); //arraylist of arraylists of strings
                SuperList.add(tempPrice);
                SuperList.add(tempSocket);
                SuperList.add(tempMaxRam);

                //filter and add
                filter(SuperList); //call filter
                MainFrame mf = GUI.getMf();
                mf.populateItemsList(); //populate the itemsList
                mf.getMoboGUI().dispose(); //close filter gui
            }
        });



    }

    //filters out what is wanted, based on input from SuperList
    public static void filter(ArrayList<ArrayList<String>> SuperList) {
        double minPrice = Double.parseDouble(SuperList.get(0).get(0));
        double maxPrice = Double.parseDouble(SuperList.get(0).get(1));
        double min_MaxRam = Double.parseDouble(SuperList.get(2).get(0));
        double max_MaxRam = Double.parseDouble(SuperList.get(2).get(1));

        for (Motherboard mobo : Motherboard.mobolist) { //go through the list once
            int passedTests = 0;

            //filter price
            if (mobo.getPrice() >= minPrice && mobo.getPrice() <= maxPrice && mobo.getPrice() != 0.0) {  /// && case_.getPrice() != 0.0) {
                passedTests++; //passed the 'price' test
            }

            //filter socket
            String chosenSock = SuperList.get(1).get(0);
            //case of other socket type
            if (chosenSock.equalsIgnoreCase("Other Socket types (Integrated)")){
                for (String o : otherSockets){
                    if (o.equalsIgnoreCase(mobo.getSocket())) passedTests++; //upgrade if a hit happens
                }
            }
            //regular case (popular sockets)                        //the 2nd one (below) means no item selected in filter (Shows all)
            else if (chosenSock.equalsIgnoreCase(mobo.getSocket()) || chosenSock.equalsIgnoreCase("NULL")){
                passedTests++;
            }

            //filter max Ram capacity
            double doubleVal = Double.valueOf(mobo.getMaxRam().split(" ")[0]); //drop GB and save as double
            if (doubleVal >= min_MaxRam && doubleVal <= max_MaxRam && doubleVal != 0.0) {  /// && case_.getPrice() != 0.0) {
                passedTests++; //passed the 'price' test
            }

            if (passedTests == 3) { //passed all 3 tests
                //System.out.println(mobo.getName() + ", " + mobo.getSocket() + ", " + mobo.getMaxRam());
                MainFrame.filteredList.add(mobo.getName());
            }
        }
    }
    
    //return panel
    public JPanel getMoboPanel() {
        return moboPanel;
    }
}