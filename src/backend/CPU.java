package backend;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;

public class CPU {
//    private short[] registers = new short[16];
    private ObservableList<Register> registers;
    private short ALatch, BLatch;
    private Mux aMux, mMux;
    private ALU alu;
    private Shifter shifter;
//    private short MAR, MBR;
    private SimpleIntegerProperty MAR, MBR;
//    private byte MPC;
    private SimpleIntegerProperty MPC;
    private int[] controlMemory;
//    private int MIR;
    private SimpleIntegerProperty MIR;
    private byte incrementer;
    private short aDec, bDec, cDec;
    private MSeqLogic mSeqLogic;
    private byte clock; // ??

    private Memory memory;

    public CPU() {
//        Arrays.fill(registers, (short)0);
//        registers[2] = 4095; // SP
//        registers[6] = 1;    // +1
//        registers[7] = -1;   // -1
//        registers[8] = 0x0fff; // AMASK
//        registers[9] = 0x00ff; // SMASK
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
        alu = new ALU();
        shifter = new Shifter();
        mSeqLogic = new MSeqLogic();
        MAR = new SimpleIntegerProperty(0);
        MBR = new SimpleIntegerProperty(0);
//        MPC = 0;
        MPC = new SimpleIntegerProperty(0);
        MIR = new SimpleIntegerProperty(0);
        incrementer = 0;
        controlMemory = FileParser.getControlMemory();
        clock = 0;
        memory = new Memory();
    }

    public void runFirstSubCycle() {
//        MIR = controlMemory[MPC.get()];
        MIR.set(controlMemory[MPC.get()]);
        if (memory.isReadReady())
            MBR.set(memory.read());
//            MBR = memory.read();
        if (memory.isWriteReady())
            memory.write((short)MBR.get());
//            memory.write(MBR);
        clock = (byte)((clock + 1) % 4);
    }

    public void runSecondSubCycle() {
        aDec = (short)getBytesField(8, 0x0000000F);
        bDec = (short)getBytesField(12, 0x0000000F);
//        ALatch = registers[aDec];
//        BLatch = registers[bDec];
        ALatch = (short)registers.get(aDec).getValue();
        BLatch = (short)registers.get(bDec).getValue();
        incrementer = (byte)(MPC.get() + 1);
        clock = (byte)((clock + 1) % 4);
    }

    private int getBytesField(int shift, int mask) {
        return (MIR.get() >> shift) & mask;
    }

    private boolean getBitAt(int position) {
        return ((MIR.get() >> position) & 1) == 1;
    }

    public void runThirdSubCycle() {
//        aMux.decideOutput(getBitAt(31), ALatch, MBR);
        aMux.decideOutput(getBitAt(31), ALatch, (short) MBR.get());
        alu.calculate((byte)getBytesField(27, 0x00000003), aMux.getOutput(), BLatch);
        shifter.shift((byte)getBytesField(25, 0x00000003), alu.getOutput());
        if (getBitAt(23))
            MAR.set(0x0FFF & BLatch);
//            MAR = (short)(0x0FFF & BLatch);
        clock = (byte)((clock + 1) % 4);
    }

    public void runFourthSubCycle() {
        if (getBitAt(24))
            MBR.set(shifter.getOutput());
//            MBR = shifter.getOutput();
        if (getBitAt(20)) {
            cDec = (short)getBytesField(16, 0x0000000F);
//            registers[cDec] = shifter.getOutput();
            registers.get(cDec).setValue(shifter.getOutput());
        }

        mSeqLogic.generateOutput((byte)getBytesField(29, 0x00000003), alu.getNBit(), alu.getZBit());
        mMux.decideOutput(mSeqLogic.isOutput(), incrementer, (short)getBytesField(0, 0x000000FF));
//        MPC = (byte)mMux.getOutput();
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

    public void runCycle() {
        runFirstSubCycle();
        runSecondSubCycle();
        runThirdSubCycle();
        runFourthSubCycle();
        System.out.println(this);
    }

    public ObservableList<Register> getRegisters() {
        return registers;
    }

    //    public short[] getRegisters() {
//        return registers;
//    }

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
}
