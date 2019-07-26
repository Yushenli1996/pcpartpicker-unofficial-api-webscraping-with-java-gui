import java.io.Serializable;
import java.util.ArrayList;

/**
 * API Created by Aleksandr on 5/15/2017.
 * API Modified by Yushen, Matt, and Nathan
 */

public class Motherboard implements Serializable, ComputerPart{
    private String name;
    private String socket;
    private String formFactor;
    private int ramSlots;
    private String maxRam;
    private double price;
    private String ProductID;
    public static ArrayList<Motherboard> mobolist = new ArrayList<>();

    public Motherboard(String[] moboData){

        if(moboData.length < 6) return;
        name = moboData[1];
        socket = moboData[2];
        formFactor = moboData[3];
        ramSlots = Integer.parseInt(moboData[4]);
        maxRam = moboData[5];
        try {
            price = Double.parseDouble(moboData[6].replace("$", ""));
        }catch (NumberFormatException e){
            //no price
            price = 0.0;
        }
        ProductID = moboData[7];
        mobolist.add(this);

        //maxRam: convert into GB (any that are TB)
        String m[] = maxRam.split(" ");
        double doubleVal = Double.valueOf(m[0]);
        if (m[1].equalsIgnoreCase("TB")){
            m[0] = String.valueOf(1024 * doubleVal);
            m[1] = "GB";
        }
        maxRam = m[0] + " " + m[1]; //concat back together
    }

    @Override
    public String toString() {
        return name + "\n" + socket + "\n" + formFactor + "\n" + ramSlots + "\n" + maxRam + "\n" + price;
    }

    public String getSocket() {
        return socket;
    }

    public int getRamSlots() {
        return ramSlots;
    }

    public String getMaxRam() {
        return maxRam;
    }

    public String getName() {
        return name;
    }

    public String getFormFactor() {
        return formFactor;
    }

    public double getPrice() {
        return price;
    }

    public String getProductID()
    {
        return ProductID;
    }
}
