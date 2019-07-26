import java.io.Serializable;
import java.util.ArrayList;

/**
 * API Created by Aleksandr on 5/15/2017.
 * API Modified by Yushen, Matt, and Nathan
 */

//for (case: caselist)
//if case.variable == xxx


public class Case implements Serializable, ComputerPart {
    private String name;
    private String type;
    private int fiveInchSlots;
    private int threeInchSlots;
    private int powerSupply;
    private double price;
    private String ProductID;
    private boolean flag;
    public static ArrayList<Case> caseList = new ArrayList<>();


    public Case(String[] caseData) {
        name = caseData[1];
        type = caseData[2];
        fiveInchSlots = Integer.parseInt(caseData[3]);
        threeInchSlots = Integer.parseInt(caseData[4]);
        if (caseData[5].contains(" W")) powerSupply = Integer.parseInt(caseData[5].replace(" W", ""));
        try {
            price = Double.parseDouble(caseData[6].replace("$", ""));
        } catch (NumberFormatException e) {
            //no price
            price = 0;
        }
        ProductID = caseData[7];
        caseList.add(this);
    }

    @Override
    public String toString() {
        return name + "\n" + type + "\n" + fiveInchSlots + "\n" + threeInchSlots + "\n" + powerSupply + "\n" + price;
    }

    public String getType() {
        return type;
    }

    public int getFiveInchSlots() {
        return fiveInchSlots;
    }

    public int getThreeInchSlots() {
        return threeInchSlots;
    }

    public int isPowerSupply() {
        return powerSupply;
    }

    public String getName() {
        return name;
    }

    public int getPowerSupply() {
        return powerSupply;
    }

    public double getPrice() {
        return price;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setFlag(boolean inputFlag){
        flag = inputFlag;
    }

    public boolean getFlag(){
        return flag;
    }
}

