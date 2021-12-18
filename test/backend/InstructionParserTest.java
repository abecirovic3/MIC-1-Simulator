package backend;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InstructionParserTest {
    InstructionParser ip = InstructionParser.getInstance();

    @Test
    void getOpcodeBytesStringTest1() {
        assertEquals("1010", ip.getOpcodeBytesString((short) 10));
    }

    @Test
    void getOpcodeBytesStringTest2() {
        assertEquals("0000", ip.getOpcodeBytesString((short) 0));
    }

    @Test
    void getOpcodeBytesStringTest3() {
        assertEquals("1110", ip.getOpcodeBytesString((short) 14));
    }

    @Test
    void getOpcodeBytesStringTest4() {
        assertEquals("11110000", ip.getOpcodeBytesString((short) 240));
    }

    @Test
    void getOpcodeBytesStringTest5() {
        assertEquals("11110100", ip.getOpcodeBytesString((short) 244));
    }

    @Test
    void getOpcodeBytesStringTest6() {
        assertEquals("11111110", ip.getOpcodeBytesString((short) 254));
    }

    @Test
    void getMnemonicTest1() {
        assertEquals("LODD", ip.getMnemonic((short) 0));
    }

    @Test
    void getMnemonicTest2() {
        assertEquals("JUMP", ip.getMnemonic((short) 6));
    }

    @Test
    void getMnemonicTest3() {
        assertEquals("STOD", ip.getMnemonic((short) 1));
    }

    @Test
    void getMnemonicTest4() {
        assertEquals("CALL", ip.getMnemonic((short) 14));
    }

    @Test
    void getMnemonicTest5() {
        assertEquals("PSHI", ip.getMnemonic((short) 0x00F0));
    }

    @Test
    void getMnemonicTest6() {
        assertEquals("DESP", ip.getMnemonic((short) 0x00FE));
    }

    @Test
    void getInstructionStringTest1() {
        assertEquals("LOCO 12", ip.getInstructionString((short) 0b0111000000001100));
    }

    @Test
    void getInstructionStringTest2() {
        assertEquals("STOD 2100", ip.getInstructionString((short) 0b0001100000110100));
    }

    @Test
    void getInstructionStringTest3() {
        assertEquals("JUMP 4095", ip.getInstructionString((short) 0b0110111111111111));
    }

    @Test
    void getInstructionStringTest4() {
        assertEquals("JNZE 99", ip.getInstructionString((short) 0b1101000001100011));
    }

    @Test
    void getInstructionStringTest5() {
        assertEquals("PSHI", ip.getInstructionString((short) 0b1111000000000000));
    }

    @Test
    void getInstructionStringTest6() {
        assertEquals("POP", ip.getInstructionString((short) 0b1111011000000000));
    }

    @Test
    void instructionIsSupportedTest() {
        assertTrue(ip.instructionIsSupported("addD"));
        assertTrue(ip.instructionIsSupported("loco"));
        assertTrue(ip.instructionIsSupported("juMP"));
        assertFalse(ip.instructionIsSupported("saberi"));
        assertFalse(ip.instructionIsSupported("add"));
        assertFalse(ip.instructionIsSupported("SUB"));
        assertFalse(ip.instructionIsSupported("MuL"));
    }
}