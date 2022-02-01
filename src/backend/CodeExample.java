package backend;

public class CodeExample {
    private static String simpleAdderExample = "LOCO 10\nSTOD 2048\nloop:\n  ADDD 2048\nJUMP loop";

    public static String getSimpleAdderExample() {
        return simpleAdderExample;
    }

    public void setSimpleAdder(String simpleAdderExample) {
        this.simpleAdderExample = simpleAdderExample;
    }
}
