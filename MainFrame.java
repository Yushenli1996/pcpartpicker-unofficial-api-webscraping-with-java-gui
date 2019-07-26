/**
 * Java Swing Manipulation by Yushen, Matt, and Nathan
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Vector;

//import org.controlsfx.control.RangeSlider; //for the rangeslider

public class MainFrame extends JFrame {

    private String CPUlink = "https://pcpartpicker.com/products/cpu/fetch/?sort=&page=&mode=list&xslug=&search=";
    private String GPUlink = "https://pcpartpicker.com/products/video-card/fetch/?sort=&page=&mode=list&xslug=&search=";
    private String CPUCoolerlink = "https://pcpartpicker.com/products/cpu-cooler/fetch/?sort=&page=&mode=list&xslug=&search=";
    private String Motherboardlink = "https://pcpartpicker.com/products/motherboard/fetch/?sort=&page=&mode=list&xslug=&search=";
    private String Memorylink = "https://pcpartpicker.com/products/memory/fetch/?sort=&page=&mode=list&xslug=&search=";
    private String Storagelink = "https://pcpartpicker.com/products/internal-hard-drive/fetch/?sort=&page=&mode=list&xslug=&search=";
    private String Caselink = "https://pcpartpicker.com/products/case/fetch/?sort=&page=&mode=list&xslug=&search=";
    private String powerSupply = "https://pcpartpicker.com/products/power-supply/fetch/?sort=&page=&mode=list&xslug=&search=";
    private String Monitorlink = "https://pcpartpicker.com/products/monitor/fetch/?sort=&page=&mode=list&xslug=&search=";

    private javax.swing.JPanel JPanel;
    private JFrame beta;
    private static JFrame popup, cpuFilter, gpuFilter, memoryFilter, moboFilter, storageFilter, psuFilter, caseFilter, monitorFilter, coolerFilter;
    private JButton CPUbutton, GPUButton, memoryButton, MotherboardButton, powerSupplyButton, hardDriveButton, caseButton;
    private JButton CPUCoolerButton, monitorButton, saveButton, AddButton, Details;
    private JLabel Item, Cost, ProductPic;
    private JList itemlist;
    private JTextArea PriceList, GPUProductname, GPUPrice, CPUProductname, CPUPrice, RAMProductname, RAMPrice, MoboProductname;
    private JTextArea MoboPrice, HDProductname, HDPrice, PSUProductname, PSUPrice, CaseProductname, CasePrice;
    private JTextArea MonitorProductname, MonitorPrice, CoolerProductname, CoolerPrice;
    private JButton clearAllButton, filtersButton;
    private JLabel TDP, label;
    private JButton cpuRemove, gpuRemove, ramRemove, moboRemove, hdRemove, psuRemove, caseRemove, moniRemove, coolerRemove;
    private Vector<JTextArea> priceTAList, productTAList;
    private Vector<String> product_name_copy;
    private Vector<Double> product_price; //for filtering out prices
    private ArrayList<String> specsData, priceData, moboMemType, moboType, socketType;
    private static JFileChooser chooser;
    private String currentPart, newURL, cpuSocket, motherboardSocket, DDRSpeed, caseInfo;
    private double value;
    private int pageNum, CPUPowerDraw, GPUPowerDraw, psuOutputWattage;
    protected static BufferedImage image = null;
    protected static ArrayList<String> imageLink;
    private static double highest;
    private static double maxClock, minClock, max_MaxRam, min_MaxRam;
    protected static Vector<String> filteredList;
    private static int[] savedSliderPrices;
    private static boolean createdNewbuild = false;

    public MainFrame() {
        //initialize stuff
        specsData = new ArrayList<>();
        priceData = new ArrayList<>();
        currentPart = ""; //set blank in the beginning
        filteredList = new Vector<>(); //for storing filtered output
        priceTAList = new Vector<>();
        productTAList = new Vector<>();
        imageLink = new ArrayList<>();
        moboMemType = new ArrayList<>();
        moboType = new ArrayList<>();
        socketType = new ArrayList<>();
        product_name_copy = new Vector<>();
        product_price = new Vector<>();
        savedSliderPrices = new int[9];

        for (int i = 0; i < 9; i++) {
            savedSliderPrices[i] = -1; //init
        }
        System.setProperty("http.agent", "Chrome");
        if(!createdNewbuild) {
//            WebScrape.loadCPU(CPUlink);  //0
//            WebScrape.loadGPU(GPUlink);  //1
//            WebScrape.loadMem(Memorylink); //2
//            WebScrape.loadMobo(Motherboardlink); //3
//            WebScrape.loadHD(Storagelink); //4
//            WebScrape.loadPSU(powerSupply); //5
//            WebScrape.loadCase(Caselink); //6
//            WebScrape.loadMon(Monitorlink);  //7
//            WebScrape.loadCooler(CPUCoolerlink); //8
        }
        //System.out.println(CPU.cpulist.size()+GPU.gpuList.size()+Memory.memList.size()+Motherboard.mobolist.size()+Storage.storagelist.size()+PowerSupply.psulist.size()+Case.caseList.size()+Monitor.monitorlist.size()+CPUCooler.coolerList.size());

        priceTAList.add(CPUPrice); //add to price textBox list
        priceTAList.add(GPUPrice);
        priceTAList.add(RAMPrice);
        priceTAList.add(MoboPrice);
        priceTAList.add(HDPrice);
        priceTAList.add(PSUPrice);
        priceTAList.add(CasePrice);
        priceTAList.add(MonitorPrice);
        priceTAList.add(CoolerPrice);
        productTAList.add(CPUProductname); //add to product textBox list
        productTAList.add(GPUProductname);
        productTAList.add(RAMProductname);
        productTAList.add(MoboProductname);
        productTAList.add(HDProductname);
        productTAList.add(PSUProductname);
        productTAList.add(CaseProductname);
        productTAList.add(MonitorProductname);
        productTAList.add(CoolerProductname);
        //
        Cost.setText("Est. Cost: $ 0.00");
        TDP.setText("Est. TDP: 0 W");
        highest = 100.0; //initial placeholder for filters
        maxClock = 0.0;
        max_MaxRam = 0.0;

        cpuFilter = new JFrame("CPU Filter");
        gpuFilter = new JFrame("GPU Filter");
        memoryFilter = new JFrame("Memory Filter");
        moboFilter = new JFrame("Motherboard Filter");
        storageFilter = new JFrame("Storage Filter");
        psuFilter = new JFrame("Power Supply Filter");
        caseFilter = new JFrame("Case Filter");
        monitorFilter = new JFrame("Monitor Filter");
        coolerFilter = new JFrame("Cooler Filter");

        cpuRemove.setIcon(new ImageIcon("remove.png"));
        gpuRemove.setIcon(new ImageIcon("remove.png"));
        ramRemove.setIcon(new ImageIcon("remove.png"));
        caseRemove.setIcon(new ImageIcon("remove.png"));
        hdRemove.setIcon(new ImageIcon("remove.png"));
        psuRemove.setIcon(new ImageIcon("remove.png"));
        moniRemove.setIcon(new ImageIcon("remove.png"));
        coolerRemove.setIcon(new ImageIcon("remove.png"));
        moboRemove.setIcon(new ImageIcon("remove.png"));
        motherboardSocket = "";
        cpuSocket = "";
        DDRSpeed = "";
        caseInfo = "";

        if (GUI.isRetrievalMode()) { //retrieval mode
            //parse the raw file
            try {
                File chosenFile = GUI.getChosenFile();
                Scanner sc = new Scanner(chosenFile); //scan the file
                sc.nextLine(); //skip 2 lines
                sc.nextLine();
                double wholePrice = 0.00;

                for (int i = 0; i < 9; i++) { //populate the text areas based on raw file
                    String productName = sc.nextLine();
                    String productPrice = sc.nextLine();
                    productTAList.get(i).setText(productName);
                    priceTAList.get(i).setText(productPrice);

                    if (productPrice.equals("")) { //if empty means $0.00
                        productPrice = "0.00";
                    }
                    wholePrice = wholePrice + Double.parseDouble(productPrice); //add to total
                    Cost.setText("Est. Cost: $ " + Double.toString(wholePrice)); //set
                }
                String totalTDP = sc.nextLine();
                TDP.setText(totalTDP);
            } catch (FileNotFoundException | NullPointerException nx) {
            }
        }

        beta = GUI.getBetaFrame(); //beta windows
        beta.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                Main.getAlphaFrame().setVisible(true);
            }
        });

        String myURL = "https://pcpartpicker.com/product/Kp3H99";
        WebScrape.webEngine(myURL, 1);

        //button listeners
        CPUbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                product_price.clear();
                product_name_copy.clear();
                specsData.clear();
                currentPart = "CPU"; //set the part we are dealing with to CPU
                itemlist.setListData(new Object[0]);
                System.setProperty("http.agent", "Chrome");
                try {
                    for (CPU c : CPU.cpulist) {
                        product_name_copy.add(c.getName());
                        product_price.add(c.getPrice());
                    }
                    //filter manipulation
                    Collections.sort(product_price);
                    highest = product_price.get(product_price.size() - 1);
                    //
                    Collections.sort(product_name_copy);
                    itemlist.setListData(product_name_copy);
                    beta.pack();
                } catch (NullPointerException ex) {
                }
            }
        });

        GPUButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                specsData.clear();
                product_price.clear();
                product_name_copy.clear();
                currentPart = "GPU";
                itemlist.setListData(new Object[0]);

                try {
                    maxClock = GPU.gpuList.get(0).getClock();
                    minClock = GPU.gpuList.get(0).getClock();
                    for (GPU g : GPU.gpuList) {
                        if (maxClock < g.getClock())
                            maxClock = g.getClock();
                        if (minClock > g.getClock() && g.getClock() != 0.0) //!= 0.0  => excludes items without a clock value)
                            minClock = g.getClock();
                        product_name_copy.add(g.getName());
                        product_price.add(g.getPrice());
                    }
                    //filter manipulation
                    Collections.sort(product_price);
                    highest = product_price.get(product_price.size() - 1);
                    //
                    Collections.sort(product_name_copy);
                    itemlist.setListData(product_name_copy);
                    beta.pack();
                } catch (NullPointerException ex) {
                }
            }
        });

        memoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                specsData.clear();
                product_price.clear();
                product_name_copy.clear();
                currentPart = "memory";
                itemlist.setListData(new Object[0]);
                try {
                    for (Memory m : Memory.memList) {
                        product_name_copy.add(m.getName() + "/" + m.getSize() + "GB/" + m.getSpeed());
                        product_price.add(m.getPrice());
                    }
                    //filter manipulation
                    Collections.sort(product_price);
                    highest = product_price.get(product_price.size() - 1);
                    //
                    Collections.sort(product_name_copy);
                    itemlist.setListData(product_name_copy);
                    beta.pack();
                } catch (NullPointerException ex) {
                }
            }
        });

        MotherboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                specsData.clear();
                product_name_copy.clear();
                product_price.clear();
                currentPart = "mobo";
                itemlist.setListData(new Object[0]);
                try {
                    for (int j = 0; j < Motherboard.mobolist.size(); j++) {
                        //get Largest Max Ram
                        String m[] = Motherboard.mobolist.get(j).getMaxRam().split(" ");
                        if (Double.valueOf(m[0]) > max_MaxRam) {
                            max_MaxRam = Double.valueOf(m[0]);
                        }
                    }
                    //get smallest Max Ram
                    min_MaxRam = Double.valueOf(Motherboard.mobolist.get(0).getMaxRam().split(" ")[0]);  //drop GB and save as double
                    for (Motherboard mobo : Motherboard.mobolist) {
                        double doubleVal = Double.valueOf(mobo.getMaxRam().split(" ")[0]);
                        if (doubleVal < min_MaxRam && doubleVal != 0.0) //!= 0.0  => excludes items without a clock value
                            min_MaxRam = doubleVal;
                    }
                    for (Motherboard m : Motherboard.mobolist) {
                        product_name_copy.add(m.getName());
                        product_price.add(m.getPrice());
                    }
                    //filter manipulation
                    Collections.sort(product_price);
                    highest = product_price.get(product_price.size() - 1);
                    //
                    Collections.sort(product_name_copy);
                    itemlist.setListData(product_name_copy);
                    beta.pack();
                } catch (NullPointerException ex) {

                }
            }
        });

        powerSupplyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                specsData.clear();
                product_name_copy.clear();
                product_price.clear();
                currentPart = "PSU";
                itemlist.setListData(new Object[0]);
                try {
                    for (PowerSupply p : PowerSupply.psulist) {
                        product_name_copy.add(p.getName());
                        product_price.add(p.getPrice());
                    }
                    //filter manipulation
                    Collections.sort(product_price);
                    highest = product_price.get(product_price.size() - 1);
                    //
                    Collections.sort(product_name_copy);
                    itemlist.setListData(product_name_copy);
                    beta.pack();
                } catch (NullPointerException ex) {

                }
            }
        });

        hardDriveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                specsData.clear();
                product_name_copy.clear();
                product_price.clear();
                currentPart = "HD";
                itemlist.setListData(new Object[0]);
                try {
                    for (Storage s : Storage.storagelist) {
                        product_name_copy.add(s.getName());
                        product_price.add(s.getPrice());
                    }
                    //filter manipulation
                    Collections.sort(product_price);
                    highest = product_price.get(product_price.size() - 1);
                    //
                    Collections.sort(product_name_copy);
                    itemlist.setListData(product_name_copy);
                    beta.pack();
                } catch (NullPointerException ex) {

                }
            }
        });

        monitorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                specsData.clear();
                product_name_copy.clear();
                product_price.clear();
                currentPart = "monitor";
                itemlist.setListData(new Object[0]);
                try {
                    for (Monitor m : Monitor.monitorlist) {
                        product_name_copy.add(m.getName());
                        product_price.add(m.getPrice());
                    }
                    //filter manipulation
                    Collections.sort(product_price);
                    highest = product_price.get(product_price.size() - 1);
                    //
                    Collections.sort(product_name_copy);
                    itemlist.setListData(product_name_copy);
                    beta.pack();

                } catch (NullPointerException ex) {

                }
            }
        });

        caseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                specsData.clear();
                product_name_copy.clear();
                product_price.clear();
                currentPart = "case";
                itemlist.setListData(new Object[0]);
                try {
                    for (Case c : Case.caseList) {
                        product_name_copy.add(c.getName());
                        product_price.add(c.getPrice());
                    }
                    //filter manipulation
                    Collections.sort(product_price);
                    highest = product_price.get(product_price.size() - 1);
                    //
                    Collections.sort(product_name_copy);
                    itemlist.setListData(product_name_copy);
                    beta.pack();

                } catch (NullPointerException ex) {

                }
            }
        });


        CPUCoolerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                specsData.clear();
                product_price.clear();
                product_name_copy.clear();
                currentPart = "cooler";
                itemlist.setListData(new Object[0]);
                try {

                    for (CPUCooler c : CPUCooler.coolerList) {
                        product_name_copy.add(c.getName());
                        product_price.add(c.getPrice());
                    }
                    //filter manipulation
                    Collections.sort(product_price);
                    highest = product_price.get(product_price.size() - 1);
                    //
                    Collections.sort(product_name_copy);
                    itemlist.setListData(product_name_copy);
                    beta.pack();
                } catch (NullPointerException ex) {

                }
            }
        });

        //save button
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //grab from wish list and save as file
                //to God be the glory.
                Boolean flag = false;
                for (int i = 0; i < productTAList.size(); i++) {
                    if (!productTAList.get(i).getText().equalsIgnoreCase("")) //if there is existed one product in any field
                    {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    try {
                        String filename = JOptionPane.showInputDialog(popup, "Okay, enter a file name for your build :)",
                                "Save", JOptionPane.PLAIN_MESSAGE);
                        //String filenameArr[] = filename.split("\\."); //is just the name (no extension)
                        //build saved to the same file
                        PrintWriter output = new PrintWriter("build.txt"); //always save to build.txt
                        Timestamp timestamp = new Timestamp(System.currentTimeMillis()); //add build timestamp
                        String strTimestamp = timestamp.toString();
                        String strTimestampArr[] = strTimestamp.split(" "); //discard milliseconds
                        output.println("Build \"" + filename + "\" " + strTimestampArr[0]); //output to file
                        output.println("--------------------------------------");
                        output.println("CPU    | " + CPUProductname.getText());
                        output.println("GPU    | " + GPUProductname.getText());
                        output.println("RAM    | " + RAMProductname.getText());
                        output.println("Mobo   | " + MoboProductname.getText());
                        output.println("HD     | " + HDProductname.getText());
                        output.println("PSU    | " + PSUProductname.getText());
                        output.println("Case   | " + CaseProductname.getText());
                        output.println("Monitor| " + MonitorProductname.getText());
                        output.println("Cooler | " + CoolerProductname.getText());
                        output.println("--------------------------------------");
                        output.println("Total " + Cost.getText());
                        output.print("Total " + TDP.getText());
                        output.close();
                        //raw build - saved in individual files
                        output = new PrintWriter(filename + ".rpc"); //use given filename
                        output.println("Raw Build " + strTimestampArr[0]); //output to file
                        output.println("--------------------------------------");
                        for (int i = 0; i < 9; i++) {  //prints names and prices in file
                            output.println(productTAList.get(i).getText());
                            output.println(priceTAList.get(i).getText());
                        }
                        output.println(TDP.getText());
                        output.close();
                        JOptionPane.showMessageDialog(popup, "Build is saved",
                                "Success", JOptionPane.INFORMATION_MESSAGE);

                    } catch (FileNotFoundException | NullPointerException Ex) {
                    }
                } else {
                    JOptionPane.showMessageDialog(popup, "Must have at least one product added to save as a file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //action listener for selecting element in JList
        itemlist.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {//This line prevents double events
                    try {
                        String S = itemlist.getSelectedValue().toString();
                        Container A = Functional.doSearchProductList(S, currentPart);
                        doAction(1, A);

                        if (image != null) {
                            BufferedImage resize = Functional.resizeImage(image, 175, 150);
                            ProductPic.setIcon(new ImageIcon(resize));
                        } else
                            ProductPic.setIcon(new ImageIcon("notavailable.jpg"));
                        beta.pack();
                    } catch (NullPointerException Ex) //prevent null selection error
                    {
                    }
                }
            }
        });

        AddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    try {
                        String S = (String) itemlist.getSelectedValue(); //grab string jlist
                        Container A = Functional.doSearchProductList(S, currentPart); //do search in data
                        doAction(2, A); //
                        TDP.setText("Est. TDP: " + Integer.toString(CPUPowerDraw + GPUPowerDraw) + " W");
                    } catch (NullPointerException Ex) {
                        JOptionPane.showMessageDialog(popup, "Please select a product first",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    calculateCost();
                } catch (NullPointerException Ex) {
                }
            }
        });

        clearAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filteredList.clear(); //clear filter vector
                specsData.clear();
                itemlist.setListData(new Object[0]);
                PriceList.setText("");
                currentPart = "";
                cpuSocket = "";
                motherboardSocket = "";
                moboType.clear();
                moboMemType.clear();
                socketType.clear();
                DDRSpeed = "";
                caseInfo = "";
                psuOutputWattage = 0;
                CPUPowerDraw = 0;
                GPUPowerDraw = 0;
                ProductPic.setIcon(null); //clear picture

                for (JTextArea productTA : productTAList) { //clear left column
                    productTA.setText("");
                }
                for (JTextArea priceTA : priceTAList) { //clear right column
                    priceTA.setText("");
                }
                Cost.setText("Est. Cost: $ 0.00");
                TDP.setText("Est. TDP: 0 W");
            }
        });

        //Product Details Button
        Details.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tempString = "";
                if (specsData.size() < 1)
                    JOptionPane.showMessageDialog(popup, "Please select a product first",
                            "Error", JOptionPane.ERROR_MESSAGE);
                else {
                    for (String s : specsData) {
                        tempString += s + "\n";
                    }
                    JOptionPane.showMessageDialog(popup, tempString, "Product Details", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        //Filters Button  //Thanks be to God
        filtersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Functional f = new Functional();
                f.filterAction(currentPart);
            }
        });
        cpuRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cpuSocket = "";
                CPUProductname.setText("");
                CPUPrice.setText("");
                CPUPowerDraw = 0;
                calculateCost();
                calculateTDP();
            }
        });
        gpuRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GPUProductname.setText("");
                GPUPrice.setText("");
                GPUPowerDraw = 0;
                calculateCost();
                calculateTDP();
            }
        });
        ramRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RAMProductname.setText("");
                RAMPrice.setText("");
                calculateCost();
            }
        });
        moboRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                motherboardSocket = "";
                moboMemType.clear();
                MoboProductname.setText("");
                MoboPrice.setText("");
                caseInfo = "";
                calculateCost();
            }
        });
        hdRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HDProductname.setText("");
                HDPrice.setText("");
                calculateCost();
            }
        });
        psuRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PSUProductname.setText("");
                PSUPrice.setText("");
                psuOutputWattage = 0;
                calculateCost();
            }
        });
        caseRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moboType.clear();
                CasePrice.setText("");
                CaseProductname.setText("");
                calculateCost();
            }
        });
        moniRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MonitorPrice.setText("");
                MonitorProductname.setText("");
                calculateCost();
            }
        });

        coolerRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CoolerProductname.setText("");
                CoolerPrice.setText("");
                socketType.clear();
                calculateCost();
            }
        });

        createdNewbuild = true;
    }//end MainFrame constructor

    public void doAction(int actionNum, Container A) {
        //CPU
        if (A.getType().equalsIgnoreCase("CPU")) {

            String IDtag = A.getCPUV().get(0).getProductID();
            String myURL = "https://pcpartpicker.com/product/" + IDtag;
            System.out.println(myURL);

            specsData = WebScrape.webEngine(myURL, 1); //specs

            if (actionNum == 1) { //populate the price list (retailers)
                PriceList.setText("");
                priceData = WebScrape.webEngine(myURL, 2); //price info

                Functional.createImageLink(imageLink); //Create Image
                if (priceData.size() == 0)
                    PriceList.append("Price is not available");
                else {
                    for (String s : priceData) {
                        PriceList.append(s + "\n");
                    }
                }

            } else if (actionNum == 2) { //populate textAreas (right hand side stuff)
                int tempTDP = 0;
                String tempSocket = "";
                if (priceData.size() == 0) {
                    JOptionPane.showMessageDialog(popup, "This product cannot be added.\nProduct is either out of stock or discontinued.", "Friendly Message", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    boolean flag = false;
                    boolean flag1 =false;
                    boolean flag2 = false;
                    for (String s : specsData) {
                        if (s.contains("Socket")) {
                            String[] socket = s.split(" ");
                            //System.out.println(socket[1]);
                            tempSocket = socket[1];
                        }
                        if (s.contains("Thermal")) {
                            String[] power = s.split(" ");
                            tempTDP = Integer.parseInt(power[3]);
                        }
                    }
                    if(caseCheck(tempSocket, socketType))
                        flag1 = true;
                    if (motherboardSocket.equalsIgnoreCase(tempSocket) || motherboardSocket.equalsIgnoreCase("")
                            && ((GPUPowerDraw + tempTDP) < psuOutputWattage || psuOutputWattage == 0)
                            && (flag1 || socketType.size() < 1)) {
                        flag = true;
                    }

                    if (flag) {
                        CPUPowerDraw = tempTDP;
                        cpuSocket = tempSocket;
                        CPUProductname.setText(""); //clear name area
                        CPUPrice.setText("");    //clear price area
                        CPUProductname.setText(A.getCPUV().get(0).getName());
                        String[] price = priceData.get(0).split("\\$");
                        CPUPrice.setText(Double.toString(Double.parseDouble(price[1].trim())));
                    }
                    if (!flag) {
                            JOptionPane.showMessageDialog(popup, "CPU is incompatible with some components", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }

        //GPU
        else if (A.getType().equalsIgnoreCase("GPU")) {
            String IDtag = A.getGpuV().get(0).getProductID();
            String myURL = "https://pcpartpicker.com/product/" + IDtag;
            System.out.println(myURL);
            specsData = WebScrape.webEngine(myURL, 1); //specs

            if (actionNum == 1) {
                PriceList.setText("");
                priceData = WebScrape.webEngine(myURL, 2); //price info
                Functional.createImageLink(imageLink);

                if (priceData.size() == 0)
                    PriceList.append("Price is not available");
                else {
                    for (String s : priceData) {
                        PriceList.append(s + "\n");
                    }
                }

            } else if (actionNum == 2) {
                int tempTDP = 0;
                boolean flag = false;
                if (priceData.size() == 0) {
                    JOptionPane.showMessageDialog(popup, "Current product cannot be added.\nProduct is either out-of stock or discontinued.", "Friendly Reminder", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    for (String s : specsData) {
                        if (s.contains("TDP")) {
                            String[] tdpValue = s.split(" ");
                            tempTDP = Integer.parseInt(tdpValue[1]);
                            break;
                        }
                    }
                    if (tempTDP + CPUPowerDraw < psuOutputWattage || psuOutputWattage == 0) flag = true;
                    if (flag) {
                        GPUPowerDraw = tempTDP;
                        GPUProductname.setText("");
                        GPUPrice.setText("");
                        GPUProductname.setText(A.getGpuV().get(0).getName());
                        String[] price = priceData.get(0).split("\\$");
                        GPUPrice.setText(Double.toString(Double.parseDouble(price[1].trim())));
                    }
                    if (!flag)
                        JOptionPane.showMessageDialog(popup, "Power supply output wattage is under the TDP requirement", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        //Motherboard
        else if (A.getType().equalsIgnoreCase("mobo")) {
            String IDtag = A.getMoboV().get(0).getProductID();
            String myURL = "https://pcpartpicker.com/product/" + IDtag;
            System.out.println(myURL);

            specsData = WebScrape.webEngine(myURL, 1); //specs
            //System.out.println(specsData);
            if (actionNum == 1) {
                PriceList.setText("");
                priceData = WebScrape.webEngine(myURL, 2); //price info
                Functional.createImageLink(imageLink);

                if (priceData.size() == 0)
                    PriceList.append("Price is not available");
                else {
                    for (String s : priceData) {
                        PriceList.append(s + "\n");
                    }
                }

            } else if (actionNum == 2) {
                if (priceData.size() == 0) {
                    JOptionPane.showMessageDialog(popup, "Current product cannot be added.\nProduct is either out-of stock or discontinued.", "Friendly Reminder", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    boolean flag = false;
                    boolean memflag = false;
                    boolean caseflag = false;
                    String tempSocket = "";
                    String caseInfoCopy = "";
                    for (String s : specsData) {
                        if (s.contains("CPU Socket")) {
                            String[] socket = s.split(" ");
                            tempSocket = socket[2];
                        }
                        if (s.contains("Memory Type")) {
                            if (s.contains("DDR4")) {
                                s = s.replaceAll("/ ", "DDR4-");
                            } else if (s.contains("DDR3"))
                                s = s.replaceAll("/ ", "DDR3-");
                            else if (s.contains("DDR2"))
                                s = s.replaceAll("/ ", "DDR2-");
                            else if (s.contains("DDR"))
                                s = s.replaceAll("/ ", "DDR-");

                            String[] type = s.split(" ");
                            memflag = memTypeCheck(DDRSpeed, type);
                            for (String x : type) {
                                moboMemType.add(x);
                            }
                        }
                        if (s.contains("Form Factor")) {
                            String[] caseInfos = s.split(":");
                            caseInfoCopy = caseInfos[1].trim();
                            //System.out.println(caseInfoCopy);
                            if (moboType.size() < 1) caseflag = true;
                            else caseflag = caseCheck(caseInfoCopy, moboType);
                        }
                    }
                    if ((cpuSocket.equalsIgnoreCase(tempSocket) || cpuSocket.equalsIgnoreCase(""))
                            && (memflag || DDRSpeed.equalsIgnoreCase(""))
                            && ((caseflag) || (socketType.size() < 1))) {
                        motherboardSocket = tempSocket;
                        caseInfo = caseInfoCopy;
                        flag = true;
                    }
                    if (flag) {
                        MoboProductname.setText("");
                        MoboPrice.setText("");
                        MoboProductname.setText(A.getMoboV().get(0).getName());
                        String[] price = priceData.get(0).split("\\$");
                        MoboPrice.setText(Double.toString(Double.parseDouble(price[1].trim())));
                    }
                    if (!flag)
                        JOptionPane.showMessageDialog(popup, "Current part is not compatible with one of the components", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        //Memory (RAM)
        else if (A.getType().equalsIgnoreCase("RAM")) {
            if (actionNum == 1) {
                PriceList.setText("");
                String IDtag = A.getRAMV().get(0).getProductID();
                String myURL = "https://pcpartpicker.com/product/" + IDtag;
                System.out.println(myURL);

                specsData = WebScrape.webEngine(myURL, 1); //specs
                priceData = WebScrape.webEngine(myURL, 2); //price info
                Functional.createImageLink(imageLink);

                if (priceData.size() == 0)
                    PriceList.append("Price is not available");
                else {
                    for (String s : priceData) {
                        PriceList.append(s + "\n");
                    }
                }

            } else if (actionNum == 2) {
                boolean flag = false;
                if (priceData.size() == 0) {
                    JOptionPane.showMessageDialog(popup, "Current product cannot be added.\nProduct is either out-of stock or discontinued.", "Friendly Reminder", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    for (String s : specsData) {
                        if (s.contains("Speed")) {
                            String[] memspeed = s.split(" ");
                            //System.out.println(socket[1]);
                            if (memTypeCheck(memspeed[1], moboMemType) || moboMemType.size() < 1) {
                                DDRSpeed = memspeed[1];
                                flag = true;
                                break;
                            }
                        }
                    }
                    if (flag) {
                        RAMProductname.setText("");
                        RAMPrice.setText("");
                        RAMProductname.setText(A.getRAMV().get(0).getName());
                        String[] price = priceData.get(0).split("\\$");
                        RAMPrice.setText(Double.toString(Double.parseDouble(price[1].trim())));
                    }
                    if (!flag)
                        JOptionPane.showMessageDialog(popup, "Current Part is not compatible with one of the components", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        //Storage (Hard Drive)
        else if (A.getType().equalsIgnoreCase("Storage")) {
            if (actionNum == 1) {
                PriceList.setText("");
                String IDtag = A.getStorageV().get(0).getProductID();
                String myURL = "https://pcpartpicker.com/product/" + IDtag;
                System.out.println(myURL);

                specsData = WebScrape.webEngine(myURL, 1); //specs
                priceData = WebScrape.webEngine(myURL, 2); //price info
                Functional.createImageLink(imageLink);

                if (priceData.size() == 0)
                    PriceList.append("Price is not available");
                else {
                    for (String s : priceData) {
                        PriceList.append(s + "\n");
                    }
                }

            } else if (actionNum == 2) {
                if (priceData.size() == 0) {
                    JOptionPane.showMessageDialog(popup, "Current product cannot be added.\nProduct is either out-of stock or discontinued.", "Friendly Reminder", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    HDProductname.setText("");
                    HDPrice.setText("");
                    HDProductname.setText(A.getStorageV().get(0).getName());
                    String[] price = priceData.get(0).split("\\$");
                    HDPrice.setText(Double.toString(Double.parseDouble(price[1].trim())));
                }
            }
        }
        //Monitor
        else if (A.getType().equalsIgnoreCase("Monitor")) {
            if (actionNum == 1) {
                PriceList.setText("");
                String IDtag = A.getMonitorV().get(0).getProductID();
                String myURL = "https://pcpartpicker.com/product/" + IDtag;
                System.out.println(myURL);

                specsData = WebScrape.webEngine(myURL, 1); //specs
                priceData = WebScrape.webEngine(myURL, 2); //price info
                Functional.createImageLink(imageLink);

                if (priceData.size() == 0)
                    PriceList.append("Price is not available");
                else {
                    for (String s : priceData) {
                        PriceList.append(s + "\n");
                    }
                }
            } else if (actionNum == 2) {
                if (priceData.size() == 0) {
                    JOptionPane.showMessageDialog(popup, "Current product cannot be added.\nProduct is either out-of stock or discontinued.", "Friendly Reminder", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    MonitorProductname.setText("");
                    MonitorPrice.setText("");
                    MonitorProductname.setText(A.getMonitorV().get(0).getName());
                    String[] price = priceData.get(0).split("\\$");
                    MonitorPrice.setText(Double.toString(Double.parseDouble(price[1].trim())));
                }
            }
        }
        //Case
        else if (A.getType().equalsIgnoreCase("Case")) {
            boolean flag = false;
            String tempBoard = "";
            if (actionNum == 1) {
                PriceList.setText("");
                String IDtag = A.getCaseV().get(0).getProductID();
                String myURL = "https://pcpartpicker.com/product/" + IDtag;
                System.out.println(myURL);

                specsData = WebScrape.webEngine(myURL, 1); //specs
                priceData = WebScrape.webEngine(myURL, 2); //price info
                Functional.createImageLink(imageLink);

                if (priceData.size() == 0)
                    PriceList.append("Price is not available");
                else {
                    for (String s : priceData) {
                        PriceList.append(s + "\n");
                    }
                }
            } else if (actionNum == 2) {
                if (priceData.size() == 0) {
                    JOptionPane.showMessageDialog(popup, "Current product cannot be added.\nProduct is either out-of stock or discontinued.", "Friendly Reminder", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    moboType.clear();
                    for (String s : specsData) {
                        if (s.contains("Motherboard Compatibility")) {
                            String[] boardInfo = s.split(",");
                            for (String temp : boardInfo) {
                                if (temp.contains("Motherboard")) {
                                    String[] boardInfoException = temp.split(" ");
                                    moboType.add(boardInfoException[2].trim());
                                } else
                                    moboType.add(temp.trim());
                            }
                        }
                    }
                    for (String check : moboType) {
                        if (caseInfo.equalsIgnoreCase(check) || caseInfo.equalsIgnoreCase("")) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        CaseProductname.setText("");
                        CasePrice.setText("");
                        CaseProductname.setText(A.getCaseV().get(0).getName());
                        String[] price = priceData.get(0).split("\\$");
                        CasePrice.setText(Double.toString(Double.parseDouble(price[1].trim())));
                    }
                    if (!flag)
                        JOptionPane.showMessageDialog(popup, "Current Part is not compatible with one of the components", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        //Cooler
        else if (A.getType().equalsIgnoreCase("Cooler")) {
            if (actionNum == 1) {
                PriceList.setText("");
                String IDtag = A.getCoolerV().get(0).getProductID();
                String myURL = "https://pcpartpicker.com/product/" + IDtag;
                System.out.println(myURL);

                specsData = WebScrape.webEngine(myURL, 1); //specs
                priceData = WebScrape.webEngine(myURL, 2); //price info
                Functional.createImageLink(imageLink);
                if (priceData.size() == 0)
                    PriceList.append("Price is not available");
                else {
                    for (String s : priceData) {
                        PriceList.append(s + "\n");
                    }
                }
            } else if (actionNum == 2) {
                if (priceData.size() == 0) {
                    JOptionPane.showMessageDialog(popup, "Current product cannot be added.\nProduct is either out-of stock or discontinued.", "Friendly Reminder", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    boolean flag = false;
                    boolean flag1 = false;
                    boolean flag2 = false;
                    for(String s :specsData)
                    {
                        if(s.contains("Supported Sockets"))
                        {
                            String[] supportSocket = s.split(",");
                            for (String temp : supportSocket) {
                                if (temp.contains(":")) {
                                    String[] SocketInfoException = temp.split(" ");
                                    socketType.add(SocketInfoException[2].trim());
                                } else
                                    socketType.add(temp.trim());
                            }
                        }
                    }
                    for (String check : socketType) {
                        if (motherboardSocket.equalsIgnoreCase(check) || motherboardSocket.equalsIgnoreCase(""))
                            flag1 = true; //If select motherboard without cpu, check if the cooler works on this board
                        if(cpuSocket.equalsIgnoreCase(check) || cpuSocket.equalsIgnoreCase(""))
                            flag2 = true; //If select cpu first, then check if the cooler works on this cpu
                    }
                    if(flag1 && flag2)
                        flag = true;
                    if(flag) {
                        CoolerProductname.setText("");
                        CoolerPrice.setText("");
                        CoolerProductname.setText(A.getCoolerV().get(0).getName());
                        String[] price = priceData.get(0).split("\\$");
                        CoolerPrice.setText(Double.toString(Double.parseDouble(price[1].trim())));
                    }
                    if(!flag){
                        if(!flag1 && !flag2)
                            JOptionPane.showMessageDialog(popup, "Current Part is not compatible with one of the components", "Error", JOptionPane.ERROR_MESSAGE);
                        else if(flag1 && !flag2)
                            JOptionPane.showMessageDialog(popup, "Current Part is not compatible with this cpu", "Error", JOptionPane.ERROR_MESSAGE);
                        else
                            JOptionPane.showMessageDialog(popup, "Current Part is not compatible with this motherboard", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        //PowerSupply
        else {
            if (actionNum == 1) {
                PriceList.setText("");
                String IDtag = A.getPsuV().get(0).getProductID();
                String myURL = "https://pcpartpicker.com/product/" + IDtag;
                System.out.println(myURL);

                specsData = WebScrape.webEngine(myURL, 1); //specs
                priceData = WebScrape.webEngine(myURL, 2); //price info
                Functional.createImageLink(imageLink);

                if (priceData.size() == 0)
                    PriceList.append("Price is not available");
                else {
                    for (String s : priceData) {
                        PriceList.append(s + "\n");
                    }
                }
            } else if (actionNum == 2) {
                if (priceData.size() == 0) {
                    JOptionPane.showMessageDialog(popup, "Current product cannot be added.\nProduct is either out-of stock or discontinued.", "Friendly Reminder", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    boolean flag = false;
                    String[] tdpvalue = TDP.getText().split(" ");
                    try {
                        if (A.getPsuV().get(0).getWatts() > Integer.parseInt(tdpvalue[2].trim())) {
                            flag = true;
                        }
                    } catch (Exception ex) {
                        tdpvalue[2] = "0";
                    }
                    if (flag) {
                        PSUProductname.setText("");
                        PSUPrice.setText("");
                        PSUProductname.setText(A.getPsuV().get(0).getName());
                        String[] price = priceData.get(0).split("\\$");
                        PSUPrice.setText(Double.toString(Double.parseDouble(price[1].trim())));
                        psuOutputWattage = A.getPsuV().get(0).getWatts();
                    }
                    if (!flag)
                        JOptionPane.showMessageDialog(popup, "Power supply output wattage is under TDP requirement", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public void populateItemsList() {
        Collections.sort(filteredList);  //sort alphabetically
        itemlist.setListData(filteredList); //display in JList
    }

    private void createUIComponents() {
        ProductPic = new JLabel(new ImageIcon());
    }

    //getters/setters:
    public static JFrame getCpuGUI() {
        return cpuFilter;
    }

    public static JFrame getGpuGUI() {
        return gpuFilter;
    }

    public static JFrame getMemoryGUI() {
        return memoryFilter;
    }

    public static JFrame getMoboGUI() {
        return moboFilter;
    }

    public static JFrame getStorageGUI() {
        return storageFilter;
    }

    public static JFrame getPsuGUI() {
        return psuFilter;
    }

    public static JFrame getCaseGUI() {
        return caseFilter;
    }

    public static JFrame getMonitorGUI() {
        return monitorFilter;
    }

    public static JFrame getCoolerGUI() {
        return coolerFilter;
    }

    public static JFrame getPopup() {
        return popup;
    } //?

    public JPanel getMainPanel() {
        return JPanel;
    }

    public static double getHighest() {
        return highest;
    }

    public static double getMaxClock() {
        return maxClock;
    }

    public static double getMinClock() {
        return minClock;
    }

    public static double getMax_MaxRam() {
        return max_MaxRam;
    }

    public static double getMin_MaxRam() {
        return min_MaxRam;
    }

    public static int[] getSavedSliderPrices() {
        return savedSliderPrices;
    }

    public static void setSavedSliderPrices(int index, int setTo) {
        savedSliderPrices[index] = setTo;
    }

    public boolean memTypeCheck(String memType, String[] type) {
        for (int i = 2; i < type.length; i++) {
            if (memType.equalsIgnoreCase(type[i]))
                return true;
        }
        return false;
    }

    public boolean memTypeCheck(String memType, ArrayList<String> type) {
        for (int i = 2; i < type.size(); i++) {
            if (memType.equalsIgnoreCase(type.get(i)))
                return true;
        }
        return false;
    }

    public boolean caseCheck(String Type, ArrayList<String> caseType) {
        for (String s : caseType) {
            if (s.equalsIgnoreCase(Type))
                return true;
        }
        return false;
    }

    public void calculateCost(){
        Cost.setText("");
        value = 0.0;
        //Calculate total Estimate cost
        try {
            for (int i = 0; i < priceTAList.size(); i++) {
                if (!(priceTAList.get(i).getText().equalsIgnoreCase(""))) {
                    value += Double.parseDouble(priceTAList.get(i).getText());
                } else
                    value += 0.0;
            }
            Cost.setText("Est. Cost: $ " + String.format("%.2f", value)); //Thanks be to God
        }catch (Exception ex)
        {
            value = 0;
        }
    }
    public void calculateTDP()
    {
        int TDPs = 0;
        try {
            TDPs = CPUPowerDraw + GPUPowerDraw;
            String watt = "Est. TDP: " + TDPs + " W";
            TDP.setText(watt);
        }
        catch (Exception ex)
        {}
    }
}
