package backend;

public class CodeExample {
    public static final String SIMPLE_ADDER_EXAMPLE = "LOCO 10\nSTOD 2048\nloop:\n  ADDD 2048\nJUMP loop";

    public static final String NTH_FIB_NUMBER_COMMENTS_EN = "; n-th Fibonacci number\n" +
                    "; run the code\n" +
                    "; select memory tab and set to n the value at address 25\n" +
                    "; at the end memory address 30 will hold the n-th Fibonacci number\n";

    public static final String NTH_FIB_NUMBER_COMMENTS_BS = "; n-ti Fibonacijev broj\n" +
            "; pokrenite kod\n" +
            "; zatim otvorite memorijski tab i postavite vrijednost memorijske lokacije 25 na n\n" +
            "; na kraju memorijska lokacija 30 ce sadrzavati vrijednost n-tog Fib. broja\n";

    public static final String NTH_FIB_NUMBER_EXAMPLE =
            "\nLOCO 0\nSTOD 26\nSTOD 30\nLOCO 1\nSTOD 27\nSTOD 29\nLODD 25\nloop:\n" +
            "  JZER end\n  LODD 26\n  ADDD 27\n  STOD 28\n  LODD 27\n  STOD 26\n" +
            "  LODD 28\n  STOD 27\n  LODD 25\n  SUBD 29\n  STOD 25\n  JUMP loop\n" +
            "end:\n LODD 26\n STOD 30";

    public static final String FIRST_N_TO_STACK_BS =
            "; Prvih n brojeva na stek\n" +
                    "; pokrenite kod\n" +
                    "; zatim otvorite memorijski tab i postavite vrijednost memorijske lokacije 17 na n\n" +
                    "; na kraju prvih n brojeva se nalazi na steku pocevsi od lokacije 4094\n" +
                    "\n" +
                    "LOCO 1\n" +
                    "STOD 15\t\t; brojac na adresi 15\n" +
                    "STOD 16\t\t; 1 na adresi 16\n" +
                    "LODD 17\n" +
                    "pocetak: \n" +
                    "\tJZER kraj\n" +
                    "\tLODD 15\n" +
                    "\tPUSH\n" +
                    "\tADDD 16\n" +
                    "\tSTOD 15\n" +
                    "\tLODD 17\n" +
                    "\tSUBD 16\n" +
                    "\tSTOD 17\n" +
                    "\tJUMP pocetak\n" +
                    "kraj:";

    public static final String FIRST_N_TO_STACK_EN =
            "; First n numbers to stack\n" +
                    "; run the kod\n" +
                    "; select memory tab and set to n the value at address 25\n" +
                    "; at the end the first n numbers are saved to stack, starting at location 4094\n" +
                    "\n" +
                    "LOCO 1\n" +
                    "STOD 15\t\t; counter on address 15\n" +
                    "STOD 16\t\t; 1 on address 16\n" +
                    "LODD 17\n" +
                    "start: \n" +
                    "\tJZER end\n" +
                    "\tLODD 15\n" +
                    "\tPUSH\n" +
                    "\tADDD 16\n" +
                    "\tSTOD 15\n" +
                    "\tLODD 17\n" +
                    "\tSUBD 16\n" +
                    "\tSTOD 17\n" +
                    "\tJUMP start\n" +
                    "end:";
}
