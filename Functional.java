import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Functional {

    protected static String imagelink;

    //Generate Image link and set image based on the link provide
    public static void createImageLink(ArrayList<String> imageLinks) {
        if (imageLinks.size() > 0) {
            for (String s : imageLinks) {
                if (s.contains("http") || s.contains("https")) {
                    imagelink = s;
                    System.out.println(imagelink);
                    break;
                } else {
                    imagelink = "http:" + s;
                    System.out.println(imagelink);
                    break;
                }
            }
            try {
                URL url = new URL(imagelink);
                MainFrame.image = ImageIO.read(url);
            } catch (IOException e) {

            }
        } else
            MainFrame.image = null;
    }


    //Take an image and resize it then return the resized image
    public static BufferedImage resizeImage(final Image image, int width, int height) {
        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.Src);
        //below three lines are for RenderingHints for better image quality at cost of higher processing time
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();
        return bufferedImage;
    }


    //Search where the object is at from the database and return the object for different uses

    public static Container doSearchProductList(String S, String currentPart) { //WHAT IT HAS TO DO WITH "PRICE" !?

        if (currentPart.equalsIgnoreCase("CPU")) {
            for (CPU cpu : CPU.cpulist) {
                if (S.equalsIgnoreCase(cpu.getName())) {
                    return new Container(cpu);
                }
            }
        } else if (currentPart.equalsIgnoreCase("GPU")) {
            for (GPU gpu : GPU.gpuList) {
                if (S.equalsIgnoreCase(gpu.getName())) {
                    return new Container(gpu);
                }
            }
        } else if (currentPart.equalsIgnoreCase("memory")) {
            for (Memory memory : Memory.memList) {
                String[] temp = S.split("/");
                String name = temp[0].trim();
                String size = temp[1].replaceAll("GB","").trim();
                String speed = temp[2];
                if (name.equalsIgnoreCase(memory.getName()) && speed.equalsIgnoreCase(memory.getSpeed()) && Double.parseDouble(size) == memory.getSize()) {
                    return new Container(memory);
                }
            }
        } else if (currentPart.equalsIgnoreCase("mobo")) {
            for (Motherboard mobo : Motherboard.mobolist) {
                if (S.equalsIgnoreCase(mobo.getName())) {
                    return new Container(mobo);
                }
            }
        } else if (currentPart.equalsIgnoreCase("Monitor")) {
            for (Monitor monitor : Monitor.monitorlist) {
                if (S.equalsIgnoreCase(monitor.getName())) {
                    return new Container(monitor);
                }
            }
        } else if (currentPart.equalsIgnoreCase("PSU")) {
            for (PowerSupply psu : PowerSupply.psulist) {
                if (S.equalsIgnoreCase(psu.getName())) {
                    return new Container(psu);
                }
            }
        } else if (currentPart.equalsIgnoreCase("HD")) {
            for (Storage storage : Storage.storagelist) {
                if (S.equalsIgnoreCase(storage.getName())) {
                    return new Container(storage);
                }
            }
        } else if (currentPart.equalsIgnoreCase("case")) {
            for (Case case_ : Case.caseList) {
                if (S.equalsIgnoreCase(case_.getName())) {
                    return new Container(case_);
                }
            }
        } else if (currentPart.equalsIgnoreCase("cooler")) {
            for (CPUCooler cooler : CPUCooler.coolerList) {
                if (S.equalsIgnoreCase(cooler.getName())) {
                    return new Container(cooler);
                }
            }
        }
        return null;
    }

    public void filterAction(String currentPart)
    {
        //MainFrame mf = new MainFrame();
        switch (currentPart) {
            case "CPU":
                //create CPU filter GUI
                JFrame cpuFilter = MainFrame.getCpuGUI();
                cpuFilter.setContentPane(new CpuFilter().getCPUpanel());
                cpuFilter.setVisible(true);
                cpuFilter.pack();
                cpuFilter.setLocationRelativeTo(null);
                cpuFilter.setResizable(false);
                break;
            case "GPU":
                //create GPU filter GUI
                JFrame gpuFilter = MainFrame.getGpuGUI();
                gpuFilter.setContentPane(new GpuFilter().getGPUpanel());
                gpuFilter.setVisible(true);
                gpuFilter.pack();
                gpuFilter.setLocationRelativeTo(null);
                gpuFilter.setResizable(false);
                break;
            case "memory":
                //create memory filter GUI
                JFrame memoryFilter = MainFrame.getMemoryGUI();
                memoryFilter.setContentPane(new MemoryFilter().getMemoryPanel());
                memoryFilter.setVisible(true);
                memoryFilter.pack();
                memoryFilter.setLocationRelativeTo(null);
                memoryFilter.setResizable(false);
                break;
            case "mobo":
                //create motherboard Filter GUI
                JFrame moboFilter = MainFrame.getMoboGUI();
                moboFilter.setContentPane(new MoboFilter().getMoboPanel());
                moboFilter.setVisible(true);
                moboFilter.pack();
                moboFilter.setLocationRelativeTo(null);
                moboFilter.setResizable(false);
                break;
            case "HD": //storage
                //create storage Filter GUI
                JFrame storageFilter = MainFrame.getStorageGUI();
                storageFilter.setContentPane(new StorageFilter().getStoragePanel());
                storageFilter.setVisible(true);
                storageFilter.pack();
                storageFilter.setLocationRelativeTo(null);
                storageFilter.setResizable(false);
                break;
            case "PSU":
                //create PSU Filter GUI
                JFrame psuFilter = MainFrame.getPsuGUI();
                psuFilter.setContentPane(new PsuFilter().getPsuPanel());
                psuFilter.setVisible(true);
                psuFilter.pack();
                psuFilter.setLocationRelativeTo(null);
                psuFilter.setResizable(false);
                break;
            case "monitor":
                //create Monitor Filter GUI
                JFrame monitorFilter = MainFrame.getMonitorGUI();
                monitorFilter.setContentPane(new MonitorFilter().getMonitorPanel());
                monitorFilter.setVisible(true);
                monitorFilter.pack();
                monitorFilter.setLocationRelativeTo(null);
                monitorFilter.setResizable(false);
                break;
            case "case":
                //create Case Filter GUI
                JFrame caseFilter = MainFrame.getCaseGUI();
                caseFilter.setContentPane(new CaseFilter().getCasePanel());
                caseFilter.setVisible(true);
                caseFilter.pack();
                caseFilter.setLocationRelativeTo(null);
                caseFilter.setResizable(false);
                break;
            case "cooler":
                //create Cooler Filter GUI
                JFrame coolerFilter = MainFrame.getCoolerGUI();
                coolerFilter.setContentPane(new CoolerFilter().getCoolerPanel());
                coolerFilter.setVisible(true);
                coolerFilter.pack();
                coolerFilter.setLocationRelativeTo(null);
                coolerFilter.setResizable(false);
                break;
            default: //else (no current part selected)
                JFrame popup = MainFrame.getPopup();
                JOptionPane.showMessageDialog(popup, "Please select a part.",
                        "Error", JOptionPane.ERROR_MESSAGE); //error checking
                break;
        }
    }
}
