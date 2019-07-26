import java.io.Serializable;import java.util.ArrayList;

/**
 * API Created by Aleksandr on 5/15/2017.
 * API Modified by Yushen, Matt, and Nathan
 */


public class CPUCooler implements Serializable, ComputerPart{
    private String name;
    private String fanRpm = "";
    private String noiseLevel = "";
    private double price;
    private String ProductID;

    public static ArrayList<CPUCooler> coolerList = new ArrayList<>();

    public CPUCooler(String[] coolerData){
        if(coolerData.length < 5) return;
        name = coolerData[1];
        fanRpm = coolerData[2];
        noiseLevel = coolerData[3];
        try {
            price = Double.parseDouble(coolerData[4].replace("$", ""));
        }catch (NumberFormatException e){
            //no price
            price = 0.0;
        }
        ProductID = coolerData[5];
        coolerList.add(this);
    }

    @Override
    public String toString(){
        return "name = " +  this.name + "\nprice = " +this.getPrice() + "\nfan rpm = " + fanRpm + "\nnoise level = " + this.noiseLevel;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getFanRpm() {
        return fanRpm;
    }

    public String getNoiseLevel() {
        return noiseLevel;
    }

    public String getProductID()
    {
        return ProductID;
    }

    public static boolean checkExistance()
    {
        if(coolerList.size() > 0)
            return true;
        return false;
    }
}
