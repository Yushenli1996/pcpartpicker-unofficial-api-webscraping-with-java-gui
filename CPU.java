import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

/**
 * API Created by Aleksandr on 5/15/2017.
 * API Modified by Yushen, Matt, and Nathan
 */

public class CPU extends Part implements ComputerPart, Serializable {
    private String name;
    private double clockSpeed;
    private int cores;
    private int tdp;
    private double price;
    private boolean flag;
    private String ProductID;
    public static ArrayList<CPU> cpulist = new ArrayList<>();

    public CPU(String[] cpuData) {

        //super();

        name = cpuData[1];
        clockSpeed = Double.parseDouble(cpuData[2].replace("GHz", ""));
        cores = Integer.parseInt(cpuData[3]);
        tdp = Integer.parseInt(cpuData[3].replace(" W", ""));
        try {
            price = Double.parseDouble(cpuData[5].replace("$", ""));
        } catch (NumberFormatException e) {
            price = 0.0;
        }
        ProductID = cpuData[6];
        cpulist.add(this);
    }


    @Override
    public String toString() {
        return "name= " + this.name + "\nprice= " + this.getPrice() + "$\ncores= " + this.cores + "\ntdp= " + this.tdp
                + "W\nclockSpeed= " + this.clockSpeed + "GHz";
    }

    public double getPrice() {
        return price;
    }

    public int getCores() {
        return this.cores;
    }

    public double getClockSpeed() {
        return this.clockSpeed;
    }

    public double getTdp() {
        return this.tdp;
    }

    public String getName() {
        return name;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setFlag(boolean inputFlag) {
        flag = inputFlag;
    }

    public boolean getFlag() {
        return flag;
    }
}