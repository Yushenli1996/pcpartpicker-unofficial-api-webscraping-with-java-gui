import java.io.Serializable;
import java.util.ArrayList;

/**
 * API Created by Aleksandr on 5/15/2017.
 * API Modified by Yushen, Matt, and Nathan
 *
 *
 * GPU clock is in MHz
 * series of GPU can be blank but not null.
 */
public class GPU implements Serializable, ComputerPart{
    private String name;
    private String series;
    private String chipSet;
    private double memory;
    private double clock;
    private double price;
    private boolean flag;
    private String ProductID;

    public static ArrayList<GPU> gpuList = new ArrayList<>();

    public GPU(String[] gpuData){
        name = gpuData[1];
        series = gpuData[2];
        chipSet = gpuData[3];
        if(gpuData[4].contains(" GB")) memory = Double.parseDouble(gpuData[4].replace(" GB", ""));
        else if(gpuData[4].contains( "MB")) memory = Double.parseDouble(gpuData[4].replace(" MB", "")) / 1024.0;
        if(gpuData[5].contains("GHz")) clock = Double.parseDouble(gpuData[5].replace("GHz", ""));
        else if (gpuData[5].contains("MHz")) clock = Double.parseDouble(gpuData[5].replace("MHz", "")) / 1000.0;

        try{
            price = Double.parseDouble(gpuData[6].replace("$", ""));
        } catch (NumberFormatException e) {
            //no price
            price = 0.0;
        }
        ProductID = gpuData[7];
        gpuList.add(this);
    }

    @Override
    public String toString() {
        return series + "\n" + chipSet + "\n" + memory + "\n" + clock;
    }

    public String getSeries() {
        return series;
    }

    public String getChipSet() {
        return chipSet;
    }

    public double getMemory() {
        return memory;
    }

    public double getClock() {
        return clock;
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
