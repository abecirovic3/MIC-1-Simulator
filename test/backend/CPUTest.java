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

}