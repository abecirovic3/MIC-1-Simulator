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
        registers[2] = 4095; // SP
        registers[6] = 1;    // +1
        registers[7] = -1;   // -1
        registers[8] = 0x0fff; // AMASK
        registers[9] = 0x00ff; // SMASK
        MPC = 0;
        controlMemory = FileParser.getControlMemory();
        clock = 0;
    }
}
