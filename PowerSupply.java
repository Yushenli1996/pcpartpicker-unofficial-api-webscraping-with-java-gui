import java.io.Serializable;
import java.util.ArrayList;

/**
 * API Created by Aleksandr on 5/15/2017.
 * API Modified by Yushen, Matt, and Nathan
 */

public class PowerSupply implements Serializable, ComputerPart{
    private String name;
    private String series;
    private String form;
    private String efficiency;
    private int watts;
    private String modular;
    private double price;
    private String ProductID;
    private boolean flag;
    public static ArrayList<PowerSupply> psulist = new ArrayList<>();

    public PowerSupply(String[] powData){
        name = powData[1];
        series = powData[2];
        form = powData[3];
        efficiency = powData[4];
        watts = Integer.parseInt(powData[5].replace(" W", ""));
        modular = powData[6];
        try{
            price = Double.parseDouble(powData[7].replace("$", ""));
        }catch (NumberFormatException e){
            //no price
            price = 0;
        }
        ProductID = powData[8];
        psulist.add(this);
    }

    @Override
    public String toString() {
        return name + "\n" + series + "\n" + form + "\n" + efficiency + "\n" + watts + "\n" + modular + "\n" + price;
    }

    public String getSeries() {
        return series;
    }

    public String getForm() {
        return form;
    }

    public String getEfficiency() {
        return efficiency;
    }

    public int getWatts() {
        return watts;
    }

    public String getModular() {
        return modular;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getProductID()
    {
        return ProductID;
    }

    public void setFlag(boolean inputFlag) {
        flag = inputFlag;
    }

    public boolean getFlag() {
        return flag;
    }
}
