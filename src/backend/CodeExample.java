package backend;

public class CodeExample {
    public static final String SIMPLE_ADDER_EXAMPLE = "LOCO 10\nSTOD 2048\nloop:\n  ADDD 2048\nJUMP loop";

    public static final String NTH_FIB_NUMBER_COMMENTS_EN = "; n-th Fibonacci number\n" +
                    "; first in the memory tab set to n the value at address 25\n" +
                    "; run the code\n" +
                    "; at the end memory address 30 will hold the n-th Fibonacci number\n";

    public static final String NTH_FIB_NUMBER_COMMENTS_BS = "; n-ti Fibonacijev broj\n" +
            "; prvi korak, postavite vrijednost memorijske lokacije 25 na n\n" +
            "; pokrenite kod\n" +
            "; na kraju memorijska lokacija 30 ce sadrzavati vrijednost n-tog Fib. broja\n";

    public static final String NTH_FIB_NUMBER_EXAMPLE =
            "\nLOCO 0\nSTOD 26\nSTOD 30\nLOCO 1\nSTOD 27\nSTOD 29\nLODD 25\nloop:\n" +
            "  JZER end\n  LODD 26\n  ADDD 27\n  STOD 28\n  LODD 27\n  STOD 26\n" +
            "  LODD 28\n  STOD 27\n  LODD 25\n  SUBD 29\n  STOD 25\n  JUMP loop\n" +
            "end:\n LODD 26\n STOD 30";
}
