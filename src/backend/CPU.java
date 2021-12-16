package backend;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CPU {
    private ObservableList<Register> registers;
    private short ALatch, BLatch;
    private Mux aMux, mMux;
    private ALU alu;
    private Shifter shifter;
    private SimpleIntegerProperty MAR, MBR;
    private SimpleIntegerProperty MPC;
    private int[] controlMemory;
    private SimpleIntegerProperty MIR;
    private byte incrementer;
    private short aDec, bDec, cDec;
    private MSeqLogic mSeqLogic;
    private SimpleIntegerProperty clock;
    private SimpleIntegerProperty clockCounter;
    private boolean memoryReadDone;

    private Memory memory;

    public CPU() {
        registers = FXCollections.observableArrayList(
                new Register("PC", (short)0),
                new Register("AC", (short)0),
                new Register("SP", (short)4095),
                new Register("IR", (short)0),
                new Register("TIR", (short)0),
                new Register("0", (short)0),
                new Register("1", (short)1),
                new Register("-1", (short)-1),
                new Register("AMASK", (short)0x0fff),
                new Register("SMASK", (short)0x00ff),
                new Register("A", (short)0),
                new Register("B", (short)0),
                new Register("C", (short)0),
                new Register("D", (short)0),
                new Register("E", (short)0),
                new Register("F", (short)0)
        );
        aMux = new Mux();
        mMux = new Mux();
        ALatch = 0;
        BLatch = 0;
        alu = new ALU();
        shifter = new Shifter();
        mSeqLogic = new MSeqLogic();
        MAR = new SimpleIntegerProperty(0);
        MBR = new SimpleIntegerProperty(0);
        MPC = new SimpleIntegerProperty(0);
        MIR = new SimpleIntegerProperty(0);
        incrementer = 0;
        controlMemory = FileParser.getControlMemory();
        clock = new SimpleIntegerProperty(0);
        clockCounter = new SimpleIntegerProperty(0);
        memory = new Memory();
        memoryReadDone = false;
    }

    public void runFirstSubCycle() {
        MIR.set(controlMemory[MPC.get()]);
        if (memory.isReadReady()) {
            MBR.set(memory.read());
            memoryReadDone = true;
        }

        if (memory.isWriteReady())
            memory.write((short)MBR.get());

        clock.set((clock.get() + 1) % 4);
    }

    public void runSecondSubCycle() {
        aDec = (short)getBytesField(8, 0x0000000F);
        bDec = (short)getBytesField(12, 0x0000000F);
        ALatch = (short)registers.get(aDec).getValue();
        BLatch = (short)registers.get(bDec).getValue();
        incrementer = (byte)(MPC.get() + 1);
        clock.set((clock.get() + 1) % 4);
    }

    public void runThirdSubCycle() {
        aMux.decideOutput(getBitAt(31), ALatch, (short) MBR.get());
        alu.calculate((byte)getBytesField(27, 0x00000003), aMux.getOutput(), BLatch);
        shifter.shift((byte)getBytesField(25, 0x00000003), alu.getOutput());
        if (getBitAt(23))
            MAR.set(0x0FFF & BLatch);

        clock.set((clock.get() + 1) % 4);
    }

    public void runFourthSubCycle() {
        if (getBitAt(24))
            MBR.set(shifter.getOutput());

        if (getBitAt(20)) {
            cDec = (short)getBytesField(16, 0x0000000F);
            registers.get(cDec).setValue(shifter.getOutput());
        }

        mSeqLogic.generateOutput((byte)getBytesField(29, 0x00000003), alu.getNBit(), alu.getZBit());
        mMux.decideOutput(mSeqLogic.isOutput(), incrementer, (short)getBytesField(0, 0x000000FF));

        MPC.set(mMux.getOutput());

        // Read Write Memory
        if (getBitAt(22)) {
            memory.setAddress((short) MAR.get());
            memory.incrementReadCounter();
        }

        if (getBitAt(21)) {
            memory.setAddress((short) MAR.get());
            memory.incrementWriteCounter();
        }

        clock.set((clock.get() + 1) % 4);
        clockCounter.set(clockCounter.get() + 1);

        memoryReadDone = false;
    }

    public void runSubCycle() {
        System.out.println("RUN subcycle");
        if (clock.get() == 0)
            runFirstSubCycle();
        else if (clock.get() == 1)
            runSecondSubCycle();
        else if (clock.get() == 2)
            runThirdSubCycle();
        else
            runFourthSubCycle();
    }

    public void runCycle() {
        int del = clock.get();
        for (int i = 0; i < 4 - del; i++) {
            runSubCycle();
        }
        System.out.println(this);
    }

    private int getBytesField(int shift, int mask) {
        return (MIR.get() >> shift) & mask;
    }

    private boolean getBitAt(int position) {
        return ((MIR.get() >> position) & 1) == 1;
    }

    public ObservableList<Register> getRegisters() {
        return registers;
    }

    public Memory getMemory() {
        return memory;
    }

    public SimpleIntegerProperty MARProperty() {
        return MAR;
    }

    public SimpleIntegerProperty MBRProperty() {
        return MBR;
    }

    public SimpleIntegerProperty MPCProperty() {
        return MPC;
    }

    public SimpleIntegerProperty MIRProperty() {
        return MIR;
    }

    public SimpleIntegerProperty clockProperty() {
        return clock;
    }

    public SimpleIntegerProperty clockCounterProperty() {
        return clockCounter;
    }

    //    MIR bytes fields
    public int getAddressBytes() {
        return getBytesField(0, 0x000000FF);
    }

    public int getABytes() {
        return getBytesField(8, 0x0000000F);
    }

    public int getBBytes() {
        return getBytesField(12, 0x0000000F);
    }

    public int getCBytes() {
        return getBytesField(16, 0x0000000F);
    }

    public int getENCBytes() {
        return getBytesField(20, 0x00000001);
    }

    public int getWRBytes() {
        return getBytesField(21, 0x00000001);
    }

    public int getRDBytes() {
        return getBytesField(22, 0x00000001);
    }

    public int getMARBytes() {
        return getBytesField(23, 0x00000001);
    }

    public int getMBRBytes() {
        return getBytesField(24, 0x00000001);
    }

    public int getShifterBytes() {
        return getBytesField(25, 0x00000003);
    }

    public int getALUBytes() {
        return getBytesField(27, 0x00000003);
    }

    public int getCONDBytes() {
        return getBytesField(29, 0x00000003);
    }

    public int getAMuxBytes() {
        return getBytesField(31, 0x00000001);
    }

    private boolean isWholeCycleDone() {
        return clockCounter.get() > 0 && clock.get() == 0;
    }

    public String getRegistersToolTip() {
        String result = "A: /\nB: /\nC: /";
        if (isWholeCycleDone() || clock.get() >= 1) {
            result = "A: " + registers.get(getABytes()).getName() + "\n" +
                    "B: " + registers.get(getBBytes()).getName();
            if (isWholeCycleDone() || getENCBytes() == 1 && clock.get() >= 3)
                result +=  "\nC: " + registers.get(getCBytes()).getName();
            else
                result += "\nC: /";
        }
        return result;
    }

    public String getALatchToolTip() {
        String result = "data: /";
        if (isWholeCycleDone() || clock.get() >= 2)
            result = "data: " + ALatch;
        return result;
    }

    public String getBLatchToolTip() {
        String result = "data: /";
        if (isWholeCycleDone() || clock.get() >= 2)
            result = "data: " + BLatch;
        return result;
    }

    public String getAmuxToolTip() {
        String inp0 = "0: /";
        String inp1 = "1: /";
        String out = "out: /";
        if (isWholeCycleDone() || clock.get() >= 2) {
            inp0 = "0: " + ALatch;
            inp1 = "1: " + MBR.get();
        }
        if (isWholeCycleDone() || clock.get() >= 3)
            out = "out: " + aMux.getOutput();

        return inp0 + "\n" + inp1 + "\n" + out;
    }

    public String getAluToolTip() {
        String a = "A: /";
        String b = "B: /";
        if (isWholeCycleDone() || clock.get() >= 2)
            b = "B: " + BLatch;
        if (isWholeCycleDone() || clock.get() >= 3) {
            return  "A: " + aMux.getOutput() + "\n" + b + "\n" + alu.toString();
        }
        return a + "\n" + b + "\nout: /\nN: /\nZ: /";
    }

    public String getShifterToolTip() {
        String result = "in: /\nout: /";
        if (isWholeCycleDone() || clock.get() >= 3)
            result = "in: " + alu.getOutput() + "\nout: " + shifter.getOutput();
        return result;
    }

    public String getADecToolTip() {
        String in = "in: /";
        String out = "out: /";
        if (isWholeCycleDone() || clock.get() >= 1) {
            in = "in: " + getABytes();
            out = "out: " + getDecoderOutput(getABytes());
        }
        return in + "\n" + out;
    }

    public String getBDecToolTip() {
        String in = "in: /";
        String out = "out: /";
        if (isWholeCycleDone() || clock.get() >= 1) {
            in = "in: " + getBBytes();
            out = "out: " + getDecoderOutput(getBBytes());
        }
        return in + "\n" + out;
    }

    public String getCDecToolTip() {
        String in = "in: /";
        String out = "out: /";
        String en = "en: /";
        if (isWholeCycleDone() || clock.get() >= 1) {
            en = "en: " + getENCBytes();
            in = "in: " + getCBytes();
        }
        if (isWholeCycleDone() || clock.get() >= 3)
            if (getENCBytes() == 1)
                out = "out: " + getDecoderOutput(getCBytes());
            else
                out = "out: " + "0000000000000000";
        return in + "\n" + out + "\n" + en;
    }

    public String getIncrementerToolTip() {
        if (isWholeCycleDone() || clock.get() >= 2)
            return "value: " + incrementer;
        return "value: /";
    }

    public String getMMuxToolTip() {
        String inp0 = "0: /";
        String inp1 = "1: /";
        String out = "out: /";
        if (isWholeCycleDone())
            out = "out: " + mMux.getOutput();
        if (isWholeCycleDone() || clock.get() >= 1)
            inp1 = "1: " + getAddressBytes();
        if (isWholeCycleDone() || clock.get() >= 2)
            inp0 = "0: " + incrementer;
        return inp0 + "\n" + inp1 + "\n" + out;
    }

    public String getMSeqLogicToolTip() {
        String l = "L: /";
        String r = "R: /";
        String n = "N: /";
        String z = "Z: /";
        String out = "out: /";
        if (isWholeCycleDone() || clock.get() >= 1) {
            l = "L: " + getBitAt(30);
            r = "R: " + getBitAt(29);
        }

        if (isWholeCycleDone() || clock.get() >= 3) {
            n = "N: " + alu.getNBit();
            z = "Z: " + alu.getZBit();
        }

        if (isWholeCycleDone())
            out = "out: " + mSeqLogic.isOutput();

        return l + "\n" + r + "\n" + n + "\n" + z + "\n" + out;
    }

    public String getMIRToolTip() {

        String result =  "AMUX: /\nCOND: /\nALU: /\nShifter: /\nMBR: /\nMAR: /\nRD: /\nWR: /\nENC: /\nC: /\nB: /\nA: /\nAddress: /";
        if (isWholeCycleDone() || clock.get() >= 1)
            result =  "AMUX: " + getBytesString(1, getAMuxBytes()) +
                    "\nCOND: " + getBytesString(2, getCONDBytes()) +
                    "\nALU: " + getBytesString(2, getALUBytes()) +
                    "\nShifter: " + getBytesString(2, getShifterBytes()) +
                    "\nMBR: " + getBytesString(1, getMBRBytes()) +
                    "\nMAR: " + getBytesString(1, getMARBytes()) +
                    "\nRD: " + getBytesString(1, getRDBytes()) +
                    "\nWR: " + getBytesString(1, getWRBytes()) +
                    "\nENC: " + getBytesString(1, getENCBytes()) +
                    "\nC: " + getBytesString(4, getCBytes()) +
                    "\nB: " + getBytesString(4, getBBytes()) +
                    "\nA: " + getBytesString(4, getABytes()) +
                    "\nAddress: " + getBytesString(8, getAddressBytes());
        return result;
    }

    public String getComponentToolTip(String component) {
        if (component.equals("registersImg"))
            return getRegistersToolTip();

        if (component.equals("aluImg"))
            return getAluToolTip();

        if (component.equals("amuxImg"))
            return getAmuxToolTip();

        if (component.equals("aLatchImg"))
            return getALatchToolTip();

        if (component.equals("bLatchImg"))
            return getBLatchToolTip();

        if (component.equals("aDecImg"))
            return getADecToolTip();

        if (component.equals("bDecImg"))
            return getBDecToolTip();

        if (component.equals("cDecImg"))
            return getCDecToolTip();

        if (component.equals("clockImg"))
            return "clk: " + (clock.get() + 1);

        if (component.equals("shifterImg"))
            return getShifterToolTip();

        if (component.equals("marImg"))
            return "Value: " + MAR.get();

        if (component.equals("mbrImg"))
            return "Value: " + MBR.get();

        if (component.equals("mMuxImg"))
            return getMMuxToolTip();

        if (component.equals("mpcImg"))
            return "Value: " + MPC.get();

        if (component.equals("incImg"))
            return getIncrementerToolTip();

        if (component.equals("controlImg"))
            return "Address: " + MPC.get() + "\nValue: " + controlMemory[MPC.get()];

        if (component.equals("mirImg"))
            return getMIRToolTip();

        if (component.equals("mSeqLogicImg"))
            return getMSeqLogicToolTip();

        return "Unknown";
    }

    public int getComponentImg(String component) {
        if (component.equals("registersImg"))
            return getRegistersImg();

        if (component.equals("aluImg"))
            return clock.get() == 3 ? 1 : 0;

        if (component.equals("amuxImg"))
            return clock.get() == 3 ? 1 : 0;

        if (component.equals("aLatchImg"))
            return clock.get() == 2 ? 1 : 0;

        if (component.equals("bLatchImg"))
            return clock.get() == 2 ? 1 : 0;

        if (component.equals("aDecImg"))
            return clock.get() == 2 ? 1 : 0;

        if (component.equals("bDecImg"))
            return clock.get() == 2 ? 1 : 0;

        if (component.equals("cDecImg"))
            return getCDecImg();

        if (component.equals("clockImg"))
            return getClockImg();

        if (component.equals("shifterImg"))
            return clock.get() == 3 ? 1 : 0;

        if (component.equals("marImg"))
            return clock.get() == 3 && getMARBytes() == 1 ? 1 : 0;

        if (component.equals("mbrImg"))
            return clock.get() == 1 && memoryReadDone || isWholeCycleDone() && getMBRBytes() == 1 ? 1 : 0;

        if (component.equals("mMuxImg"))
            return isWholeCycleDone() ? 1 : 0;

        if (component.equals("mpcImg"))
            return isWholeCycleDone() ? 1 : 0;

        if (component.equals("incImg"))
            return clock.get() == 2 ? 1 : 0;

        if (component.equals("controlImg"))
            return clock.get() == 1 ? 1 : 0;

        if (component.equals("mirImg"))
            return clock.get() == 1 ? 1 : 0;

        if (component.equals("mSeqLogicImg"))
            return isWholeCycleDone() ? 1 : 0;

        return 0;
    }

    private int getClockImg() {
        if (isWholeCycleDone())
            return 4;

        return clock.get();
    }

    private int getCDecImg() {
        return isWholeCycleDone() && getENCBytes() == 1 ? 1 : 0;
    }

    public int getRegistersImg() {
        return clock.get() == 2 || getCDecImg() == 1 ? 1 : 0;
    }


    private String getBytesString(int length, int value) {
        StringBuilder result = new StringBuilder(Integer.toBinaryString(value));
        while (result.length() < length)
            result.insert(0, "0");
        return result.toString();
    }

    private String getDecoderOutput(int position) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            if (i == position)
                result.insert(0, "1");
            else
                result.insert(0, "0");
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return "CPU{" +
                "registers=" + registers +
                ", ALatch=" + ALatch +
                ", BLatch=" + BLatch +
                ", aMux=" + aMux +
                ", mMux=" + mMux +
                ", alu=" + alu +
                ", shifter=" + shifter +
                ", MAR=" + MAR +
                ", MBR=" + MBR +
                ", MPC=" + MPC +
                ", MIR=" + MIR +
                ", incrementer=" + incrementer +
                ", aDec=" + aDec +
                ", bDec=" + bDec +
                ", cDec=" + cDec +
                ", mSeqLogic=" + mSeqLogic +
                ", clock=" + clock +
                '}';
    }
}
