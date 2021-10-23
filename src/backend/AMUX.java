package backend;

public class AMUX {
    private short output;

    public short getOutput() {
        return output;
    }

    public void setOutput(short output) {
        this.output = output;
    }

    public void decideOutput(boolean control, short inputA, short inputB) {
        if (control)
            output = inputB;
        else
            output = inputA;
    }
}
