import java.io.Serializable;
import java.util.ArrayList;

/**
 * API Created by Aleksandr on 5/15/2017.
 * API Modified by Yushen, Matt, and Nathan
 */

public class Storage implements Serializable, ComputerPart{
    private String name;
    private String series;
    private String form;
    private String type;
    private String capacity;
    private int cache;
    private double pricePerGb;
    private double price;
    private String ProductID;
    private boolean flag;
    public static ArrayList<Storage> storagelist = new ArrayList<>();

    public Storage(String[] storData){
        name = storData[1];
        series = storData[2];
        form = storData[3];
        type = storData[4];
        capacity = storData[5];
        try {
            cache = Integer.parseInt(storData[6].replace("MB", ""));
        }catch (NumberFormatException e){
            //its an ssd, it has no cache
            cache = 0;
        }
        try {
            pricePerGb = Double.parseDouble(storData[7].replace("$", ""));
            price = Double.parseDouble(storData[8].replace("$", ""));
        }catch (NumberFormatException e){
            pricePerGb = 0.0;
            price = 0.0;
        }
        ProductID = storData[9];
        storagelist.add(this);

        //capacity: convert into GB (any that are TB)
        String m[] = capacity.split(" "); //1 TB
        double doubleVal = Double.valueOf(m[0]);
        if (m[1].equalsIgnoreCase("TB")){
            m[0] = String.valueOf(1024 * doubleVal);
            m[1] = "GB";
        }
        capacity = m[0] + " " + m[1]; //concat back together
    }

    @Override
    public String toString() {
        return name + "\n" + series + "\n" + form + "\n" + type + "\n" + capacity + "\n" + cache + "\n" + pricePerGb
                + "\n" + price;
    }

    public String getSeries() {
        return series;
    }

    public String getType() {
        return type;
    }

    public int getCache() {
        return cache;
    }

    public double getPricePerGb() {
        return pricePerGb;
    }

    public String getName() {
        return name;
    }

    public String getForm() {
        return form;
    }

    public String getCapacity() {
        return capacity;
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
