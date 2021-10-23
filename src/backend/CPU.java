package backend;

public class CPU {
    private short[] registers = new short[16];
    private short ALatch, BLatch;
    private AMUX amux;
    private ALU alu;
    private Shifter shifter;
    private short MAR, MBR;

}
