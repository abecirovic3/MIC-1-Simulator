package backend;

public class MSeqLogic {
    private boolean output;

    public boolean isOutput() {
        return output;
    }

    public void setOutput(boolean output) {
        this.output = output;
    }

    public void generateOutput(byte control, boolean n, boolean z) {
        output = control == 3 || control == 1 && n || control == 2 && z;
    }

    public boolean generateTempOutput(byte control, boolean n, boolean z) {
        return control == 3 || control == 1 && n || control == 2 && z;
    }

    @Override
    public String toString() {
        return "MSeqLogic{" +
                "output=" + output +
                '}';
    }
}
