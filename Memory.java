import java.io.Serializable;
import java.util.ArrayList;

/**
 * API Created by Aleksandr on 5/15/2017.
 * API Modified by Yushen, Matt, and Nathan
 */

public class Memory implements Serializable, ComputerPart{
    private String name;
    private String speed;
    private String type;
    private int cas;
    private String modules;
    private double size;
    private double pricePerGB;
    private double price;
    private boolean flag;
    private String ProductID;

    public static ArrayList<Memory> memList = new ArrayList<>();

    public Memory(String[] memData){
        name = memData[1];
        speed = memData[2];
        type = memData[3];
        if(memData[4].contains("N/A")) cas = 0;
        else cas = Integer.parseInt(memData[4]);
        modules = memData[5];
        if(memData[6].contains(" GB")) size = Double.parseDouble(memData[6].replace(" GB", ""));
        else if(memData[6].contains( "MB")) size = Double.parseDouble(memData[6].replace(" MB", "")) / 1024.0;
        try {
            pricePerGB = Double.parseDouble(memData[7].replace("$", ""));
            price = Double.parseDouble(memData[8].replace("$", ""));
        }catch (NumberFormatException e){
            //there can be no price, and therefore sometimes no price per gb
            pricePerGB = 0.0;
            price = 0.0;
        }
        ProductID = memData[9];
        memList.add(this);
    }

    @Override
    public String toString() {
        return name + "\n" + speed + "\n" + type + "\n" + cas + "\n" + modules + "\n" + size + "\n" + pricePerGB + "\n" + price;
    }

    public String getSpeed() {
        return speed;
    }

    public String getType() {
        return type;
    }

    public int getCas() {
        return cas;
    }

    public String getModules() {
        return modules;
    }

    public double getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public double getPricePerGB() {
        return pricePerGB;
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
