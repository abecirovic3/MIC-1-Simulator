package backend;

public class ALU {
    private short output;
    private boolean NBit, ZBit;

    public short getOutput() {
        return output;
    }

    public void setOutput(short output) {
        this.output = output;
    }

    public boolean getNBit() {
        return NBit;
    }

    public void setNBit(boolean nBit) {
        this.NBit = nBit;
    }

    public boolean getZBit() {
        return ZBit;
    }

    public void setZBit(boolean zBit) {
        this.ZBit = zBit;
    }

    public void calculate(byte control, short inputA, short inputB) {
        output = (short) (inputA + inputB); // control is 0
        if (control == 1)
            output = (short) (inputA & inputB);
        else if (control == 2)
            output = inputA;
        else if (control == 3)
            output = (short)~inputA;

        ZBit = output == 0;
        NBit = output < 0;
    }
}
