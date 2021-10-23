package backend;

public class ALU {
    private short inputA, inputB, output;
    private byte controlBits;
    private byte NBit, ZBit;

    public short getInputA() {
        return inputA;
    }

    public void setInputA(short inputA) {
        this.inputA = inputA;
    }

    public short getInputB() {
        return inputB;
    }

    public void setInputB(short inputB) {
        this.inputB = inputB;
    }

    public short getOutput() {
        return output;
    }

    public void setOutput(short output) {
        this.output = output;
    }

    public byte getControlBits() {
        return controlBits;
    }

    public void setControlBits(byte controlBits) {
        this.controlBits = controlBits;
    }

    public byte getNBit() {
        return NBit;
    }

    public void setNBit(byte nBit) {
        this.NBit = nBit;
    }

    public byte getZBit() {
        return ZBit;
    }

    public void setZBit(byte zBit) {
        this.ZBit = zBit;
    }
}
