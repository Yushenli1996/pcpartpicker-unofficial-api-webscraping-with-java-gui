import java.util.Vector;

public class Container {
    private Vector<CPU> cpuV = new Vector<>(1);
    private Vector<GPU> gpuV = new Vector<>(1);
    private Vector<Motherboard> moboV = new Vector<>(1);
    private Vector<Memory> RAMV = new Vector<>(1);
    private Vector<Storage> storageV = new Vector<>(1);
    private Vector<Monitor> monitorV = new Vector<>(1);
    private Vector<Case> caseV = new Vector<>(1);
    private Vector<CPUCooler> CoolerV = new Vector<>(1);
    private Vector<PowerSupply> psuV = new Vector<>(1);
    private String Type = "";

    public Container(CPU c) {
        cpuV.clear();
        cpuV.add(c);
        Type = "CPU";
    }

    public Container(GPU g) {
        gpuV.clear();
        gpuV.add(g);
        Type = "GPU";
    }

    public Container(Motherboard m)
    {
        moboV.clear();
        moboV.add(m);
        Type = "Mobo";
    }
    public Container(Memory m) {
        RAMV.clear();
        RAMV.add(m);
        Type = "RAM";
    }

    public Container(Storage s) {
        storageV.clear();
        storageV.add(s);
        Type = "Storage";
    }

    public Container(Monitor mo) {
        monitorV.clear();
        monitorV.add(mo);
        Type = "Monitor";
    }

    public Container(Case c) {
        caseV.clear();
        caseV.add(c);
        Type = "Case";
    }

    public Container(CPUCooler co) {
        CoolerV.clear();
        CoolerV.add(co);
        Type = "Cooler";
    }

    public Container(PowerSupply psu)
    {
        psuV.clear();
        psuV.add(psu);
        Type = "PSU";
    }


    public Container getContainer()
    {
        return this;
    }
    public String getType(){
        return Type;
    }
    public Vector<CPU> getCPUV() {
        return cpuV;
    }
    public Vector<GPU> getGpuV(){
        return gpuV;
    }
    public Vector<Memory> getRAMV(){
        return RAMV;
    }
    public Vector<Storage> getStorageV()
    {
        return storageV;
    }
    public Vector<Monitor> getMonitorV() { return monitorV; }
    public Vector<Motherboard> getMoboV(){ return moboV;}
    public Vector<Case> getCaseV()
    {
        return caseV;
    }
    public Vector<CPUCooler> getCoolerV()
    {
        return CoolerV;
    }
    public Vector<PowerSupply> getPsuV()
    {
        return psuV;
    }

}
