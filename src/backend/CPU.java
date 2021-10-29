package backend;

import java.util.Arrays;

public class CPU {
    private short[] registers = new short[16];
    private short ALatch, BLatch;
    private Mux aMux, mMux;
    private ALU alu;
    private Shifter shifter;
    private short MAR, MBR;
    private byte MPC;
    private int[] controlMemory;
    private int MIR;
    private byte incrementer;
    private short aDec, bDec, cDec;
    private MSeqLogic mSeqLogic;
    private byte clock; // ??

    private Memory memory;

    public CPU() {
        Arrays.fill(registers, (short)0);
        registers[2] = 4095; // SP
        registers[6] = 1;    // +1
        registers[7] = -1;   // -1
        registers[8] = 0x0fff; // AMASK
        registers[9] = 0x00ff; // SMASK
        aMux = new Mux();
        mMux = new Mux();
        alu = new ALU();
        shifter = new Shifter();
        mSeqLogic = new MSeqLogic();
        MPC = 0;
        incrementer = 0;
        controlMemory = FileParser.getControlMemory();
        clock = 0;
        memory = new Memory();
    }

    public void runFirstSubCycle() {
        MIR = controlMemory[MPC];
        if (memory.isReadReady())
            MBR = memory.read();
        if (memory.isWriteReady())
            memory.write(MBR);
        clock = (byte)((clock + 1) % 4);
    }

    public void runSecondSubCycle() {
        aDec = (short)getBytesField(8, 0x0000000F);
        bDec = (short)getBytesField(12, 0x0000000F);
        ALatch = registers[aDec];
        BLatch = registers[bDec];
        incrementer = (byte)(MPC + 1);
        clock = (byte)((clock + 1) % 4);
    }

    private int getBytesField(int shift, int mask) {
        return (MIR >> shift) & mask;
    }

    private boolean getBitAt(int position) {
        return ((MIR >> position) & 1) == 1;
    }

    public void runThirdSubCycle() {
        aMux.decideOutput(getBitAt(31), ALatch, MBR);
        alu.calculate((byte)getBytesField(27, 0x00000003), aMux.getOutput(), BLatch);
        shifter.shift((byte)getBytesField(25, 0x00000003), alu.getOutput());
        if (getBitAt(23))
            MAR = (short)(0x0FFF & BLatch);
        clock = (byte)((clock + 1) % 4);
    }

    public void runFourthSubCycle() {
        if (getBitAt(24))
            MBR = shifter.getOutput();
        if (getBitAt(20)) {
            cDec = (short)getBytesField(16, 0x0000000F);
            registers[cDec] = shifter.getOutput();
        }

        mSeqLogic.generateOutput((byte)getBytesField(29, 0x00000003), alu.getNBit(), alu.getZBit());
        mMux.decideOutput(mSeqLogic.isOutput(), incrementer, (short)getBytesField(0, 0x000000FF));
        MPC = (byte)mMux.getOutput();

        // Read Write Memory
        if (getBitAt(22)) {
            memory.setAddress(MAR);
            memory.incrementReadCounter();
        }

        if (getBitAt(21)) {
            memory.setAddress(MAR);
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

    public short[] getRegisters() {
        return registers;
    }

    public Memory getMemory() {
        return memory;
    }

    @Override
    public String toString() {
        return "CPU{" +
                "registers=" + Arrays.toString(registers) +
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
