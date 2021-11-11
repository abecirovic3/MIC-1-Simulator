package backend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CodeParserTest {

    private final CodeParser cp = CodeParser.getInstance();

    @Test
    @DisplayName("Correct Code Test")
    void parseCodeValid() {
        String code = "ADDD 12\nSUBD 10 ;komentar\nlabela:\nLOCO 2\nJUMP labela";
        assertDoesNotThrow(() -> {cp.parseCode(code);});
        try {
            short[] machineCode = cp.parseCode(code);
            short[] correct = new short[2048];
            Arrays.fill(correct, (short)0x7000);
            correct[0] = 0b0010000000001100;
            correct[1] = 0b0011000000001010;
            correct[2] = 0b0111000000000010;
            correct[3] = 0b0110000000000010;

            assertArrayEquals(correct, machineCode);
        } catch (CodeParserException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Recurring Label Test")
    void parseCodeRecurringLabel() {
        String code = ";Pocetak programa\nLOCO 100\nlabela: ADDD 12\nLODD 1025\nSTODD 1026\nlabela:\nADDD 100";
        CodeParserException exception = assertThrows(CodeParserException.class, ()->{cp.parseCode(code);});
        assertEquals("Error on line number 6, recurring label name", exception.getMessage());
    }

    @Test
    @DisplayName("Unknown Instruction Mnemonic Test")
    void parseCodeInvalidInstructionMnemonic() {
        String code = "LODD 1030\nADDD 100\nUNKNOWN 200";
        CodeParserException exception = assertThrows(CodeParserException.class, ()->{cp.parseCode(code);});
        assertEquals("Error on line number 3, unknown instruction mnemonic", exception.getMessage());
    }

    @Test
    @DisplayName("Missing Argument Test")
    void parseCodeMissingArgument() {
        String code = "LODD 1200\n;sad ce greska\nADDD ;greska";
        CodeParserException exception = assertThrows(CodeParserException.class, ()->{cp.parseCode(code);});
        assertEquals("Error on line number 3, missing argument", exception.getMessage());
    }

    @Test
    @DisplayName("Too Many Arguments Test 1")
    void parseCodeTooManyArguments1() {
        String code = "LODD 1200\nloco 100\nADDD 1 2"; // lowercase is ok
        CodeParserException exception = assertThrows(CodeParserException.class, ()->{cp.parseCode(code);});
        assertEquals("Error on line number 3, too many arguments", exception.getMessage());
    }

    @Test
    @DisplayName("Too Many Arguments Test 2")
    void parseCodeTooManyArguments2() {
        String code = "ADDD 123\nPUSH 100";
        CodeParserException exception = assertThrows(CodeParserException.class, ()->{cp.parseCode(code);});
        assertEquals("Error on line number 2, too many arguments", exception.getMessage());
    }

    @Test
    @DisplayName("Unknown Label Test")
    void parseCodeUnknownLabel() {
        String code = "lodd 1025\nloco 200\njneg labela\naddd 123\nnovaLabela:";
        CodeParserException exception = assertThrows(CodeParserException.class, ()->{cp.parseCode(code);});
        assertEquals("Error on line number 3, unknown label name", exception.getMessage());
    }

    @Test
    @DisplayName("Argument Out of Bounds Test")
    void parseCodeArgumentOutOfBounds() {
        String code = "lodd 5000\nloco 200\njneg labela\naddd 123\nlabela:";
        CodeParserException exception = assertThrows(CodeParserException.class, ()->{cp.parseCode(code);});
        assertEquals("Error on line number 1, argument out of bounds", exception.getMessage());
        String code1 = "INSP 100\nDESP 256";
        exception = assertThrows(CodeParserException.class, ()->{cp.parseCode(code1);});
        assertEquals("Error on line number 2, argument out of bounds", exception.getMessage());
    }

    @Test
    @DisplayName("Invalid Argument Test")
    void parseCodeInvalidArgument() {
        String code = "ADDD 21\n;0 <= x <= 4095 (12b)\nSUBD x";
        CodeParserException exception = assertThrows(CodeParserException.class, ()->{cp.parseCode(code);});
        assertEquals("Error on line number 3, invalid argument", exception.getMessage());
    }

    @Test
    @DisplayName("Blank Code Test")
    void parseCodeBlankCodeArea() {
        String code = ";Komentari su isto sto i blank\n\n\n;Takođe i labele\nlabela: labela2:";
        CodeParserException exception = assertThrows(CodeParserException.class, ()->{cp.parseCode(code);});
        assertEquals("Error blank code area", exception.getMessage());
    }

    @Test
    @DisplayName("Two Labels Test")
    void parseCode2Labels() {
        String code = ";We can have 2 labels pointing to the same mem address\nlab1: lab2:\nADDD 1\nJNEG lab1\nJZER lab2";

        assertDoesNotThrow(() -> {cp.parseCode(code);});
        try {
            short[] machineCode = cp.parseCode(code);
            short[] correct = new short[2048];
            Arrays.fill(correct, (short)0x7000);
            correct[0] = 0b0010000000000001;
            correct[1] = (short)0b1100000000000000;
            correct[2] = 0b0101000000000000;

            assertArrayEquals(correct, machineCode);
        } catch (CodeParserException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Wild Working Code Test")
    void parseCodeWildCodeArea() {
        String code = ";Ovo se ne treba protumaciti kao instr ADDD 123\n     " +
                "SUBD 1  \n\n LOCO    4000    ;linijski komentar\n  labela: ADDD 0\nJNEG labela";
        assertDoesNotThrow(() -> {cp.parseCode(code);});
        try {
            short[] machineCode = cp.parseCode(code);
            short[] correct = new short[2048];
            Arrays.fill(correct, (short)0x7000);
            correct[0] = 0b0011000000000001;
            correct[1] = 0b0111111110100000;
            correct[2] = 0b0010000000000000;
            correct[3] = (short)0b1100000000000010;
            assertArrayEquals(correct, machineCode);
        } catch (CodeParserException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Assembled Code Test 2")
    void checkAssembledCode1() {
        String code = "ADDD 10\nSTOD 1025\nJUMP 0";
        assertDoesNotThrow(()->{cp.parseCode(code);});
        try {
            short[] machineCode = cp.parseCode(code);
            short[] correct = new short[2048];
            Arrays.fill(correct, (short)0x7000);
            correct[0] = 8202;
            correct[1] = 5121;
            correct[2] = 24576;
            assertArrayEquals(correct, machineCode);
        } catch (CodeParserException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Assembled Code Test 1")
    void checkAssembledCode2() {
        String code = "PSHI\n;Cisto onako neki komentar\nPOPI\nINSP 100";
        assertDoesNotThrow(()->{cp.parseCode(code);});
        try {
            short[] machineCode = cp.parseCode(code);
            short[] correct = new short[2048];
            Arrays.fill(correct, (short)0x7000);
            correct[0] = -4096;
            correct[1] = -3584;
            correct[2] = -924;
            assertArrayEquals(correct, machineCode);
        } catch (CodeParserException e) {
            e.printStackTrace();
        }
    }
}