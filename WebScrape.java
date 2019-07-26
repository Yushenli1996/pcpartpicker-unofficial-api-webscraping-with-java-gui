import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * API Created by Aleksandr on 5/15/2017.
 * API Modified by Yushen, Matt, and Nathan
 */

public class WebScrape {

    //This function grab get's all the productID for all the products
    public static ArrayList<String> getUrlsFromDoc(Document doc) {
        ArrayList<String> productID = new ArrayList<>();
        Elements links = doc.getElementsByTag("a");
//        Pattern pattern = Pattern.compile("&quot;#(.*?)\\&quot;");
        Pattern pattern = Pattern.compile("&quot;#(.*?)\\\\");//should grab it without the annoying "\" at the end
        //the original is still there just in case ;)
        for (Element curr : links) {
            //System.out.println(curr.toString());
            if (curr.text().equals("Add")) {
                Matcher matcher = pattern.matcher(curr.toString());
                if (matcher.find()) {
                    productID.add(matcher.group(1));
                }
            }
        }
        return productID;
    }

    //This function scrape all the data info for product on the product list page
    public static String[][] getRawData(Document doc) {
        String[][] rawData = new String[doc.getElementsByTag("tr").size()][];
        int j = 0;
        ArrayList<String> temp = getUrlsFromDoc(doc);
        for (Element curr : doc.getElementsByTag("tr")) {
            int i = 0;
            rawData[j] = new String[curr.getElementsByTag("td").size() - 1];//one of the 2 base cases
            for (Element info : curr.getElementsByTag("td")) {
                if (info.text().equals("Add"))
                    continue;
                if (info.text().matches("\\(\\d+\\)")) continue;//number between parenthesis, the ratings
                rawData[j][i] = info.text().trim();
                //System.out.println(rawData[j][i]);
                i++;
            }
            j++;
        }

        int index = 0;
        for (j = 0; j < rawData.length; j++) {
            index = rawData[j].length - 1;
            rawData[j][index] = temp.get(j);
        }
        return rawData;
    }

    //This function scrape all the product information from the product's page
    public static ArrayList<String> getSpecData(Document doc) {
        //System.out.println(doc);
        ArrayList<String> rawData = new ArrayList<>();
        MainFrame.imageLink.clear();
        String s = "";
        Elements ele = doc.select("div.specs.block");

        for (Element info : ele.select("div.specs.block")) {
            for (Element children : info.children()) {
                if (children.text().trim().equalsIgnoreCase("")) {
                    s = rawData.get(rawData.size() - 1);
                    //System.out.println(children.nextSibling().toString().trim());
                    s = s + ", " + children.nextSibling().toString().trim();
//                    rawData.remove(rawData.size()-1);
//                    rawData.add(s);
                    rawData.set(rawData.size()-1, s);
                }
                else {
                    //System.out.println(children.text() + ": " + children.nextSibling().toString().trim());
                    //if(children.nextSibling().hasAttr("br /")) System.out.println("true");
                    rawData.add(children.text() + ": " + children.nextSibling().toString().trim());
                }
            }
        }

        Elements pic = doc.select("div.image-gallery");
        //System.out.println(pic);
        for (Element picInfo : pic.select("img")) {
            //System.out.println(picInfo.attr("src"));
            MainFrame.imageLink.add(picInfo.attr("src"));
        }
        return rawData;
    }

    //This function gets the product price information from the product's page
    public static ArrayList<String> getPriceData(Document doc) {
        ArrayList<String> data = new ArrayList<>();
        String[] price = new String[doc.select("td.total").size()];
        int j = 0;

        for (Element tr : doc.getElementsByTag("tr")) {
            if (ContainChildWithCSSClass(tr, "total")) {
                //System.out.println(tr.attr("class"));
                price[j] = tr.attr("class").trim();
                j++;
            }
        }
        j = 0;

        for (Element info : doc.select("td.total")) {
            price[j] += ": " + info.text();
            j++;
        }

        for (String s : price)
            data.add(s.replaceAll("\\+", "").trim());
        return data;
    }


    //Using the collected cpu information to create a cpu object
    public static ArrayList<CPU> getCPUsFromDoc(Document doc) {
        String[][] rawData = getRawData(doc);
        ArrayList<CPU> cpus = new ArrayList<>(rawData.length);

        for (int i = 0; i < rawData.length; i++) {
            cpus.add(i, new CPU(rawData[i]));
        }
        return cpus;
    }


    //Using the collected cooler information to create a cooler object
    public static ArrayList<CPUCooler> getCPUCoolersFromDoc(Document doc) {
        String[][] rawData = getRawData(doc);
        ArrayList<CPUCooler> coolers = new ArrayList<>(rawData.length);

        for (int i = 0; i < rawData.length; i++) {
            coolers.add(i, new CPUCooler(rawData[i]));
        }

        return coolers;
    }

    //Using the collected motherboard information to create a motherboard object
    public static ArrayList<Motherboard> getMobosFromDoc(Document doc) {
        String[][] rawData = getRawData(doc);
        ArrayList<Motherboard> motherboards = new ArrayList<>(rawData.length);

        for (int i = 0; i < rawData.length; i++) {
            motherboards.add(new Motherboard(rawData[i]));
        }
        return motherboards;
    }

    //Using the collected RAM information to create a RAM object
    public static ArrayList<Memory> getMemoryFromDoc(Document doc) {
        String[][] rawData = getRawData(doc);
        ArrayList<Memory> rams = new ArrayList<>(rawData.length);
        for (int i = 0; i < rawData.length; i++) {
            rams.add(new Memory(rawData[i]));
        }
        return rams;
    }

    //Using the collected Hard drive information to create a Hard drive object
    public static ArrayList<Storage> getStorageFromDoc(Document doc) {
        String[][] rawData = getRawData(doc);
        ArrayList<Storage> drives = new ArrayList<>(rawData.length);
        for (int i = 0; i < rawData.length; i++) {
            drives.add(new Storage(rawData[i]));
        }
        return drives;
    }

    //Using the collected GPU information to create a GPU object
    public static ArrayList<GPU> getGPUsFromDoc(Document doc) {
        String[][] rawData = getRawData(doc);
        ArrayList<GPU> gpus = new ArrayList<>(rawData.length);
        for (int i = 0; i < rawData.length; i++) {
            gpus.add(new GPU(rawData[i]));
        }

        return gpus;
    }

    //Using the collected case information to create a case object
    public static ArrayList<Case> getCasesFromDoc(Document doc) {
        String[][] rawData = getRawData(doc);
        ArrayList<Case> cases = new ArrayList<>(rawData.length);
        for (int i = 0; i < rawData.length; i++) {
            cases.add(new Case(rawData[i]));
        }
        return cases;
    }


    //Using the collected PSU information to create a PSU object
    public static ArrayList<PowerSupply> getPowerSuppliesFromDoc(Document doc) {
        String[][] rawData = getRawData(doc);
        ArrayList<PowerSupply> powerSupplies = new ArrayList<>(rawData.length);
        for (int i = 0; i < rawData.length; i++) {
            powerSupplies.add(new PowerSupply(rawData[i]));
        }
        return powerSupplies;
    }

    //Using the collected Monitor information to create a Monitor object
    public static ArrayList<Monitor> getMonitorsFromDoc(Document doc) {
        String[][] rawData = getRawData(doc);
        ArrayList<Monitor> monitors = new ArrayList<>(rawData.length);
        for (int i = 0; i < rawData.length; i++) {
            monitors.add(new Monitor(rawData[i]));
        }
        return monitors;
    }


    //A special function that gets the child from the collected element collection for html parsing
    public static boolean ContainChildWithCSSClass(Element element, String cssClass) {
        for (Element ele : element.children()) {
            if (ele.hasClass(cssClass))
                return true;
        }
        return false;
    }


    //Get's the total webpage number for looping use
    public static int getProductPageNum(Document doc) {
        int pageNum = 0;
        Elements links = doc.getElementsByTag("a");
        Pattern pattern = Pattern.compile("&quot;#page=(.*?)\\\\");//should grab it without the annoying "\" at the end
        //the original is still there just in case ;)
        for (Element curr : links) {
            if (!curr.text().equalsIgnoreCase("Add")) {
                Matcher matcher = pattern.matcher(curr.toString());
                if (matcher.find()) {
                    pageNum = Integer.parseInt(matcher.group(1));
                }
            }
        }
        return pageNum;
    }

    //To get price and spec information
    public static ArrayList<String> webEngine(String myURL, int Action) {

        ArrayList<String> rawData = new ArrayList<>();
        myURL = myURL.trim();
        //System.out.println(myURL);
        try {
            Document doc = Jsoup.parse(new URL(myURL).openStream(), "UTF-8", "", Parser.xmlParser());
            //System.out.println(doc);
            if (Action == 1)
                rawData = getSpecData(doc);
            if (Action == 2)
                rawData = getPriceData(doc);
        } catch (IOException ex) {
        }
        return rawData;
    }


    /*------------------------------------------------------------------------------------------------------------------------------*/
    /*------------------------------------------------------------------------------------------------------------------------------*/
    public static void loadCPU(String CPUlink) {
        int pageNum = 0;
        String newURL = "";
        try {
            Document doc = Jsoup.parse(new URL(CPUlink).openStream(), "UTF-8", "", Parser.xmlParser());
            pageNum = getProductPageNum(doc);
            CPU.cpulist.clear();

            for (int i = 1; i <= pageNum; i++) {
                newURL = "https://pcpartpicker.com/products/cpu/fetch/?sort=&page=" + i + "&mode=list&xslug=&search=";
                doc = Jsoup.parse(new URL(newURL).openStream(), "UTF-8", "", Parser.xmlParser());
                WebScrape.getCPUsFromDoc(doc);
            }
        } catch (IOException ex) {

        }
    }

    public static void loadGPU(String GPUlink) {
        int pageNum = 0;
        String newURL = "";
        try {
            double maxClock = 0.0;
            double minClock = 0.0;
            Document doc = Jsoup.parse(new URL(GPUlink).openStream(), "UTF-8", "", Parser.xmlParser());
            pageNum = WebScrape.getProductPageNum(doc);
            GPU.gpuList.clear();
            ArrayList<GPU> gpus = new ArrayList<>();

            for (int i = 1; i <= pageNum; i++) { //add to name and price vectors
                newURL = "https://pcpartpicker.com/products/video-card/fetch/?sort=&page=" + i + "&mode=list&xslug=&search=";
                doc = Jsoup.parse(new URL(newURL).openStream(), "UTF-8", "", Parser.xmlParser());
                gpus = WebScrape.getGPUsFromDoc(doc);
                for (int j = 0; j < gpus.size(); j++) {
                    if (gpus.get(j).getClock() > maxClock) {
                        maxClock = gpus.get(j).getClock();
                    }
                }
            }
            minClock = GPU.gpuList.get(0).getClock();
            for (GPU g : GPU.gpuList) {
                if (g.getClock() < minClock && g.getClock() != 0.0) //!= 0.0  => excludes items without a clock value
                    minClock = g.getClock();
            }
        } catch (IOException ex) {

        }
    }

    public static void loadMem(String Memorylink) {
        int pageNum = 0;
        String newURL = "";
        try {
            Document doc = Jsoup.parse(new URL(Memorylink).openStream(), "UTF-8", "", Parser.xmlParser());
            pageNum = WebScrape.getProductPageNum(doc);
            Memory.memList.clear();

            for (int i = 1; i <= pageNum; i++) { //add to name and price vectors
                newURL = "https://pcpartpicker.com/products/memory/fetch/?sort=&page=" + i + "&mode=list&xslug=&search=";
                doc = Jsoup.parse(new URL(newURL).openStream(), "UTF-8", "", Parser.xmlParser());
                WebScrape.getMemoryFromDoc(doc);
            }
        } catch (IOException ex) {
        }
    }

    public static void loadMobo(String Motherboardlink) {
        int pageNum = 0;
        String newURL = "";
        try {
            Document doc = Jsoup.parse(new URL(Motherboardlink).openStream(), "UTF-8", "", Parser.xmlParser());
            pageNum = WebScrape.getProductPageNum(doc);
            Motherboard.mobolist.clear();
            ArrayList<Motherboard> mobos = new ArrayList<>();

            for (int i = 1; i <= pageNum; i++) { //add to name and price vectors
                newURL = "https://pcpartpicker.com/products/motherboard/fetch/?sort=&page=" + i + "&mode=list&xslug=&search=";
                doc = Jsoup.parse(new URL(newURL).openStream(), "UTF-8", "", Parser.xmlParser());
                WebScrape.getMobosFromDoc(doc);
            }
        } catch (IOException ex) {
        }
    }

    public static void loadPSU(String powerSupply) {
        int pageNum = 0;
        String newURL = "";
        try {
            Document doc = Jsoup.parse(new URL(powerSupply).openStream(), "UTF-8", "", Parser.xmlParser());
            pageNum = WebScrape.getProductPageNum(doc);
            PowerSupply.psulist.clear();
            for (int i = 1; i <= pageNum; i++) { //add to name and price vectors
                newURL = "https://pcpartpicker.com/products/power-supply/fetch/?sort=&page=" + i + "&mode=list&xslug=&search=";
                doc = Jsoup.parse(new URL(newURL).openStream(), "UTF-8", "", Parser.xmlParser());
                WebScrape.getPowerSuppliesFromDoc(doc);
            }
        } catch (IOException ex) {
        }
    }

    public static void loadHD(String Storagelink) {
        int pageNum = 0;
        String newURL = "";
        try {
            Document doc = Jsoup.parse(new URL(Storagelink).openStream(), "UTF-8", "", Parser.xmlParser());
            pageNum = WebScrape.getProductPageNum(doc);

            for (int i = 1; i <= pageNum; i++) { //add to name and price vectors
                newURL = "https://pcpartpicker.com/products/internal-hard-drive/fetch/?sort=&page=" + i + "&mode=list&xslug=&search=";
                doc = Jsoup.parse(new URL(newURL).openStream(), "UTF-8", "", Parser.xmlParser());
                WebScrape.getStorageFromDoc(doc);
            }
        } catch (IOException ex) {
        }
    }

    public static void loadMon(String Monitorlink) {
        int pageNum = 0;
        String newURL = "";
        try {
            Document doc = Jsoup.parse(new URL(Monitorlink).openStream(), "UTF-8", "", Parser.xmlParser());
            pageNum = WebScrape.getProductPageNum(doc);
            for (int i = 1; i <= pageNum; i++) { //add to name and price vectors
                newURL = "https://pcpartpicker.com/products/monitor/fetch/?sort=&page=" + i + "&mode=list&xslug=&search=";
                doc = Jsoup.parse(new URL(newURL).openStream(), "UTF-8", "", Parser.xmlParser());
                WebScrape.getMonitorsFromDoc(doc);
            }
        } catch (IOException ex) {
        }
    }

    public static void loadCase(String Caselink) {
        int pageNum = 0;
        String newURL = "";
        try {
            Document doc = Jsoup.parse(new URL(Caselink).openStream(), "UTF-8", "", Parser.xmlParser());
            pageNum = WebScrape.getProductPageNum(doc);
            for (int i = 1; i <= pageNum; i++) { //add to name and price vectors
                newURL = "https://pcpartpicker.com/products/case/fetch/?sort=&page=" + i + "&mode=list&xslug=&search=";
                doc = Jsoup.parse(new URL(newURL).openStream(), "UTF-8", "", Parser.xmlParser());
                WebScrape.getCasesFromDoc(doc);
            }
        } catch (IOException ex) {
        }
    }

    public static void loadCooler(String CPUCoolerlink) {
        int pageNum = 0;
        String newURL = "";
        try {
            Document doc = Jsoup.parse(new URL(CPUCoolerlink).openStream(), "UTF-8", "", Parser.xmlParser());
            pageNum = WebScrape.getProductPageNum(doc);

            for (int i = 1; i <= pageNum; i++) { //add to name and price vectors
                newURL = "https://pcpartpicker.com/products/cpu-cooler/fetch/?sort=&page=" + i + "&mode=list&xslug=&search=";
                doc = Jsoup.parse(new URL(newURL).openStream(), "UTF-8", "", Parser.xmlParser());
                WebScrape.getCPUCoolersFromDoc(doc);
            }
        } catch (IOException ex) {
        }
    }
}
