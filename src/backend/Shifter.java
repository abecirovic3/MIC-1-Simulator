package backend;

public class Shifter {
    private short output;

    public short getOutput() {
        return output;
    }

    public void setOutput(short output) {
        this.output = output;
    }

    public void shift(byte control, short input) {
        output = input;
        if (control == 1)
            output = (short) (output >> 1);
        else if (control == 2)
            output = (short) (output << 1);
    }
}
