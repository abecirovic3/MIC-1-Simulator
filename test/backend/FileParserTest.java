package backend;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileParserTest {

    @Test
    void getControlMemory() {
        int[] memory = FileParser.getControlMemory();
        assertEquals(12582912, memory[0]);
        assertEquals(805307407, memory[11]);
        assertEquals(-1743126528, memory[17]);
        assertEquals(0, memory[255]);
    }
}