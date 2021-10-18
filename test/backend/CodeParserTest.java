package backend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CodeParserTest {

    private final CodeParser cp = new CodeParser();

    @Test
    @DisplayName("Correct Code Test")
    void parseCodeValid() {
        String code = "ADDD 12\nSUBD 100 ;komentar\nlabela:\nLOCO 101";
        assertEquals("OK", cp.parseCode(code));
        assertEquals(2, cp.getLabels().get("labela"));
    }

    @Test
    @DisplayName("Recurring Label Test")
    void parseCodeRecurringLabel() {
        String code = ";Pocetak programa\nLOCO 100\nlabela: ADDD 12\nLODD 1025\nSTODD 1026\nlabela:\nADDD 100";
        assertEquals("Error on line number 6, recurring label name", cp.parseCode(code));
    }

    @Test
    @DisplayName("Unknown Instruction Mnemonic Test")
    void parseCodeInvalidInstructionMnemonic() {
        String code = "LODD 1030\nADDD 100\nUNKNOWN 200";
        assertEquals("Error on line number 3, unknown instruction mnemonic", cp.parseCode(code));
    }

    @Test
    @DisplayName("Missing Argument Test")
    void parseCodeMissingArgument() {
        String code = "LODD 1200\n;sad ce greska\nADDD ;greska";
        assertEquals("Error on line number 3, missing argument", cp.parseCode(code));
    }

    @Test
    @DisplayName("Too Many Arguments Test")
    void parseCodeTooManyArguments() {
        String code = "LODD 1200\nloco 100\nADDD 1 2"; // lowercase is ok
        assertEquals("Error on line number 3, too many arguments", cp.parseCode(code));
        code = "ADDD 123\nPUSH 100";
        assertEquals("Error on line number 2, too many arguments", cp.parseCode(code));
    }

    @Test
    @DisplayName("Unknown Label Test")
    void parseCodeUnknownLabel() {
        String code = "lodd 1025\nloco 200\njneg labela\naddd 123\nnovaLabela:";
        assertEquals("Error on line number 3, unknown label name", cp.parseCode(code));
    }

    @Test
    @DisplayName("Argument Out of Bounds Test")
    void parseCodeArgumentOutOfBounds() {
        String code = "lodd 5000\nloco 200\njneg labela\naddd 123\nlabela:";
        assertEquals("Error on line number 1, argument out of bounds", cp.parseCode(code));
        code = "INSP 100\nDESP 256";
        assertEquals("Error on line number 2, argument out of bounds", cp.parseCode(code));
    }

    @Test
    @DisplayName("Invalid Argument Test")
    void parseCodeInvalidArgument() {
        String code = "ADDD 21\n;0 <= x <= 4095 (12b)\nSUBD x";
        assertEquals("Error on line number 3, invalid argument", cp.parseCode(code));
    }

    @Test
    @DisplayName("Blank Code Test")
    void parseCodeBlankCodeArea() {
        String code = ";Komentari su isto sto i blank\n\n\n;Takođe i labele\nlabela: labela2:";
        assertEquals("Error blank code area", cp.parseCode(code));
    }

    @Test
    @DisplayName("Two Labels Test")
    void parseCode2Labels() {
        String code = ";We can have 2 labels pointing to the same mem address\nlab1: lab2:\nADDD 123";
        assertEquals("OK", cp.parseCode(code));
        assertEquals(0, cp.getLabels().get("lab1"));
        assertEquals(0, cp.getLabels().get("lab2"));
    }

    @Test
    @DisplayName("Wild Workin Code Test")
    void parseCodeWildCodeArea() {
        String code = ";Komentar ADDD 123\n     SUBD 1  \n\n LOCO    100    ;linijski komentar\n  labela: ADDD 0\nJNEG labela";
        assertEquals("OK", cp.parseCode(code));
    }

    @Test
    @DisplayName("Assembled Code Test 2")
    void checkAssembledCode1() {
        String code = "ADDD 10\nSTOD 1025\nJUMP 0";
        cp.parseCode(code);
        short[] machineCode = cp.getMachineCode();
        assertEquals(8202, machineCode[0]);   // 0010 0000 0000 1010
        assertEquals(5121, machineCode[1]);   // 0001 0100 0000 0001
        assertEquals(24576, machineCode[2]);  // 0110 0000 0000 0000
    }

    @Test
    @DisplayName("Assembled Code Test 1")
    void checkAssembledCode2() {
        String code = "PSHI\n;Cisto onako neki komentar\nPOPI\nINSP 100";
        cp.parseCode(code);
        short[] machineCode = cp.getMachineCode();
        assertEquals(-4096, machineCode[0]);   // 1111 0000 0000 0000
        assertEquals(-3584, machineCode[1]);   // 1111 0010 0000 0000
        assertEquals(-924, machineCode[2]);    // 1111 1100 0110 0100
    }
}