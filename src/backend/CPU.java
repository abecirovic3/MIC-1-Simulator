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
    private short aDec, bDec, cDec;
    private byte clock; // ??

    public CPU() {
        Arrays.fill(registers, (short)0);
        MPC = 0;
        controlMemory = FileParser.getControlMemory();
        clock = 0;
    }
}
