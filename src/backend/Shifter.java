package backend;

public class Shifter {
    private short input, output;
    private byte controlBits;

    public short getInput() {
        return input;
    }

    public void setInput(short input) {
        this.input = input;
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
}
