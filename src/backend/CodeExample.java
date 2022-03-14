package backend;

public class CodeExample {
    private static final String simpleAdderExample = "LOCO 10\nSTOD 2048\nloop:\n  ADDD 2048\nJUMP loop";

    private static final String nthFibNumberExample = "; n-th Fibonacci number\n" +
            "; first in the memory tab set to n the value at address 25\n" +
            "; run the code\n" +
            "; at the end memory address 30 will hold the n-th Fibonacci number\n" +
            "\nLOCO 0\nSTOD 26\nSTOD 30\nLOCO 1\nSTOD 27\nSTOD 29\nLODD 25\nloop:\n" +
            "  JZER end\n  LODD 26\n  ADDD 27\n  STOD 28\n  LODD 27\n  STOD 26\n" +
            "  LODD 28\n  STOD 27\n  LODD 25\n  SUBD 29\n  STOD 25\n  JUMP loop\n" +
            "end:\n LODD 26\n STOD 30";

    public static String getSimpleAdderExample() {
        return simpleAdderExample;
    }

    public static String getNthFibNumberExample() {
        return nthFibNumberExample;
    }
}
