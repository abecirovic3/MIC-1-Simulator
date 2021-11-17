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
    private byte clock;

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
        clock = 0;
        memory = new Memory();
    }

    public void runFirstSubCycle() {
        MIR.set(controlMemory[MPC.get()]);
        if (memory.isReadReady())
            MBR.set(memory.read());

        if (memory.isWriteReady())
            memory.write((short)MBR.get());

        clock = (byte)((clock + 1) % 4);
    }

    public void runSecondSubCycle() {
        aDec = (short)getBytesField(8, 0x0000000F);
        bDec = (short)getBytesField(12, 0x0000000F);
        ALatch = (short)registers.get(aDec).getValue();
        BLatch = (short)registers.get(bDec).getValue();
        incrementer = (byte)(MPC.get() + 1);
        clock = (byte)((clock + 1) % 4);
    }

    public void runThirdSubCycle() {
        aMux.decideOutput(getBitAt(31), ALatch, (short) MBR.get());
        alu.calculate((byte)getBytesField(27, 0x00000003), aMux.getOutput(), BLatch);
        shifter.shift((byte)getBytesField(25, 0x00000003), alu.getOutput());
        if (getBitAt(23))
            MAR.set(0x0FFF & BLatch);

        clock = (byte)((clock + 1) % 4);
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

        clock = (byte)((clock + 1) % 4);
    }

    public void runSubCycle() {
        System.out.println("RUN subcycle");
        if (clock == 0)
            runFirstSubCycle();
        else if (clock == 1)
            runSecondSubCycle();
        else if (clock == 2)
            runThirdSubCycle();
        else
            runFourthSubCycle();
    }

    public void runCycle() {
        byte del = clock;
        for (byte i = 0; i < 4 - del; i++) {
            System.out.println("Prosao");
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

//    public String getRegistersToolTipText() {
//        return "Registers";
//    }
//
//    public String getALatchToolTipText() {
//        return "ALatch";
//    }
//
//    public String getBLatchToolTipText() {
//        return "BLatch";
//    }
//
//    public String getMARToolTipText() {
//        return "MAR";
//    }
//
//    public String getMBRToolTipText() {
//        return "MBR";
//    }
//
//    public String getAMUXToolTipText() {
//        return "AMUX";
//    }
//
//    public String getALUToolTipText() {
//        return "A: " + ALatch + "\nB: " + BLatch + "\nOut: " + alu.toString();
//    }
//
//    public String getShifterToolTipText() {
//        return "Shifter";
//    }
//
//    public String getMSeqLogicToolTipText() {
//        return "MSeqLogic";
//    }
//
//    public String getMIRToolTipText() {
//        return "MIR";
//    }
//
//    public String getControlMemoryToolTipText() {
//        return "Control Memory";
//    }
//
//    public String getMPCToolTipText() {
//        return "MPC";
//    }
//
//    public String getIncrementerToolTipText() {
//        return "Incrementer";
//    }
//
//    public String getMMUXToolTipText() {
//        return "MMUX";
//    }
//
//    public String getClockToolTipText() {
//        return "Clock";
//    }
//
//    public String getADecToolTipText() {
//        return "ADec";
//    }
//
//    public String getBDecToolTipText() {
//        return "BDec";
//    }
//
//    public String getCDecToolTipText() {
//        return "CDec";
//    }

    public String getComponentToolTip(String component) {
        if (component.equals("registersImg"))
            return "Registers";

        if (component.equals("aluImg"))
            return "ALU";

        if (component.equals("amuxImg"))
            return "AMUX";

        if (component.equals("aLatchImg"))
            return "ALatch";

        if (component.equals("bLatchImg"))
            return "BLatch";

        if (component.equals("aDecImg"))
            return "aDec";

        if (component.equals("bDecImg"))
            return "bDec";

        if (component.equals("cDecImg"))
            return "cDec";

        if (component.equals("clockImg"))
            return "CLOCK";

        if (component.equals("shifterImg"))
            return "Shifter";

        if (component.equals("marImg"))
            return "MAR";

        if (component.equals("mbrImg"))
            return "MBR";

        if (component.equals("mMuxImg"))
            return "MMux";

        if (component.equals("mpcImg"))
            return "MPC";

        if (component.equals("incImg"))
            return "Incrementer";

        if (component.equals("controlImg"))
            return "Control";

        if (component.equals("mirImg"))
            return "MIR";

        if (component.equals("mSeqLogicImg"))
            return "MSeqLogic";

        return "Unknown";
    }
}
