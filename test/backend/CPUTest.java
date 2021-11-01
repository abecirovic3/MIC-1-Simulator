package backend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CPUTest {

    @Test
    @DisplayName("First CPU Run")
    void runOneInstruction() {
        CodeParser cp = new CodeParser();
        String code = "LOCO 12";
        assertEquals("OK", cp.parseCode(code));

        short[] machineCode = cp.getMachineCode();

        CPU cpu = new CPU();
        cpu.getMemory().write((short)0, machineCode[0]);
        cpu.runCycle();
        cpu.runCycle();
        cpu.runCycle();
        cpu.runCycle();
        cpu.runCycle();
        cpu.runCycle();
        cpu.runCycle();

        assertEquals(12, cpu.getRegisters()[1]);
    }

    @Test
    @DisplayName("LOCO and ADDD")
    void storeConstant() {
        CodeParser cp = new CodeParser();
        String code = "LOCO 12\nSTOD 1024\n";
        assertEquals("OK", cp.parseCode(code));

        short[] machineCode = cp.getMachineCode();

        CPU cpu = new CPU();
        cpu.getMemory().write((short)0, machineCode[0]);
        cpu.getMemory().write((short)1, machineCode[1]);
        for (int i = 0; i < 16; i++)
            cpu.runCycle();

        assertEquals(12, cpu.getRegisters()[1]); // accumulator has value 12
        assertEquals(12, cpu.getMemory().read((short)1024)); // memory should be written to
    }

}