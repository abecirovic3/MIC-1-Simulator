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

//        assertEquals(12, cpu.getRegisters()[1]);
        assertEquals(12, cpu.getRegisters().get(1).getValue());
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

//        assertEquals(12, cpu.getRegisters()[1]); // accumulator has value 12
        assertEquals(12, cpu.getRegisters().get(1).getValue()); // accumulator has value 12
        assertEquals(12, cpu.getMemory().read((short)1024)); // memory should be written to
    }

    @Test
    @DisplayName("LODD instruction")
    void loddInstruction() {
        CodeParser cp = new CodeParser();
        String code = "LODD 1024\n; manuelno cemo u memoriju pisati";
        assertEquals("OK", cp.parseCode(code));

        short[] machineCode = cp.getMachineCode();

        CPU cpu = new CPU();
        cpu.getMemory().write((short)0, machineCode[0]);
        cpu.getMemory().write((short)1024, (short)275);
        for (int i = 0; i < 9; i++)
            cpu.runCycle();

//        assertEquals(275, cpu.getRegisters()[1]); // accumulator has value 275 if LODD was successful
        assertEquals(275, cpu.getRegisters().get(1).getValue()); // accumulator has value 275 if LODD was successful
    }

    @Test
    @DisplayName("STOD instruction")
    void stodInstruction() {
        CodeParser cp = new CodeParser();
        String code = "LODD 1024\n; manuelno cemo u memoriju pisati\nSTOD 1025";
        assertEquals("OK", cp.parseCode(code));

        short[] machineCode = cp.getMachineCode();

        CPU cpu = new CPU();
        cpu.getMemory().write((short)0, machineCode[0]);
        cpu.getMemory().write((short)1, machineCode[1]);
        cpu.getMemory().write((short)1024, (short)72);
        for (int i = 0; i < 18; i++)
            cpu.runCycle();

        // if stod was successful then on mem address 1025 we have the value 72
        assertEquals(72, cpu.getMemory().read((short)1025));
    }

    @Test
    @DisplayName("ADDD instruction")
    void adddInstruction() {
        CodeParser cp = new CodeParser();
        String code = "LODD 1024\nADDD 1025";
        assertEquals("OK", cp.parseCode(code));

        short[] machineCode = cp.getMachineCode();

        CPU cpu = new CPU();
        cpu.getMemory().write((short)0, machineCode[0]);
        cpu.getMemory().write((short)1, machineCode[1]);
        cpu.getMemory().write((short)1024, (short)72);
        cpu.getMemory().write((short)1025, (short)18);
        for (int i = 0; i < 18; i++)
            cpu.runCycle();

        // first 72 is loaded in ac then 18 is added
//        assertEquals(90, cpu.getRegisters()[1]);
        assertEquals(90, cpu.getRegisters().get(1).getValue());
    }

    @Test
    @DisplayName("SUBD instruction")
    void subdInstruction() {
        CodeParser cp = new CodeParser();
        String code = "LODD 1024\nSUBD 1025";
        assertEquals("OK", cp.parseCode(code));

        short[] machineCode = cp.getMachineCode();

        CPU cpu = new CPU();
        cpu.getMemory().write((short)0, machineCode[0]);
        cpu.getMemory().write((short)1, machineCode[1]);
        cpu.getMemory().write((short)1024, (short)25);
        cpu.getMemory().write((short)1025, (short)5);
        for (int i = 0; i < 19; i++)
            cpu.runCycle();

        // first 72 is loaded in ac then 18 is added
//        assertEquals(20, cpu.getRegisters()[1]);
        assertEquals(20, cpu.getRegisters().get(1).getValue());
    }

    @Test
    @DisplayName("JNEG instruction")
    void jnegInstruction() {
        CodeParser cp = new CodeParser();
        String code = "LODD 1024\nJNEG 5";
        assertEquals("OK", cp.parseCode(code));

        short[] machineCode = cp.getMachineCode();

        CPU cpu = new CPU();
        cpu.getMemory().write((short)0, machineCode[0]);
        cpu.getMemory().write((short)1, machineCode[1]);
        cpu.getMemory().write((short)1024, (short)-10);
        for (int i = 0; i < 17; i++)
            cpu.runCycle();

        // first 72 is loaded in ac then 18 is added
//        assertEquals(5, cpu.getRegisters()[0]);
        assertEquals(5, cpu.getRegisters().get(0).getValue());
    }

    @Test
    @DisplayName("PUSH instruction")
    void pushInstruction() {
        CodeParser cp = new CodeParser();
        String code = "LODD 1024\nPUSH";
        assertEquals("OK", cp.parseCode(code));

        short[] machineCode = cp.getMachineCode();

        CPU cpu = new CPU();
        cpu.getMemory().write((short)0, machineCode[0]);
        cpu.getMemory().write((short)1, machineCode[1]);
        cpu.getMemory().write((short)1024, (short)-9);
        for (int i = 0; i < 22; i++)
            cpu.runCycle();

        // first 72 is loaded in ac then 18 is added
//        assertEquals(4094, cpu.getRegisters()[2]);
        assertEquals(4094, cpu.getRegisters().get(2).getValue());
        assertEquals(-9, cpu.getMemory().read((short)4094));
    }
}