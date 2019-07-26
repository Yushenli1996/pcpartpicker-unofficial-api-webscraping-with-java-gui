import java.io.Serializable;
import java.util.ArrayList;

/**
 * API Created by Aleksandr on 5/15/2017.
 * API Modified by Yushen, Matt, and Nathan
 */


public class Monitor implements Serializable, ComputerPart{
    private String name;
    private String resolution;
    private double size;
    private int responseTime;
    private String panel;
    private double price;
    private boolean flag;
    private String ProductID;
    public static ArrayList<Monitor> monitorlist = new ArrayList<>();

    public Monitor(String[] monData){
        name = monData[1];
        resolution = monData[2];
        size = Double.parseDouble(monData[3].replace("\\\"", ""));
        if(monData[4].equalsIgnoreCase(""))
            responseTime = 0;
        else
            responseTime = Integer.parseInt(monData[4].replace("ms", "").trim());
        panel = monData[5];

        try{
            price = Double.parseDouble(monData[6].replace("$", ""));
        }catch (NumberFormatException e){
            //NO PRICE
            price = 0;
        }
        ProductID = monData[7];
        monitorlist.add(this);
    }

    @Override
    public String toString() {
        return name + "\n" + resolution + "\n" + size + "\n" + responseTime + "\n" + panel + "\n" + price;
    }

    public String getResolution() {
        return resolution;
    }

    public double getSize() {
        return size;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public String getPanel() {
        return panel;
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
