package backend;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumericFactoryTest {
    NumericFactory numericFactory = new NumericFactory();

    @Test
    void getShortValueTest1() {
        numericFactory.setRadix(10);
        assertEquals(-32767, numericFactory.getShortValue("-32767"));
        numericFactory.setRadix(2);
        assertEquals(-32767, numericFactory.getShortValue("1000000000000001"));
    }

    @Test
    void getShortValueTest2() {
        numericFactory.setRadix(10);
        assertEquals(-1, numericFactory.getShortValue("-1"));
        numericFactory.setRadix(2);
        assertEquals(-1, numericFactory.getShortValue("1111111111111111"));
    }

    @Test
    void getShortValueTest3() {
        numericFactory.setRadix(10);
        assertEquals(-32768, numericFactory.getShortValue("-32768"));
        numericFactory.setRadix(2);
        assertEquals(-32768, numericFactory.getShortValue("1000000000000000"));
    }

    @Test
    void getStringValueTest1() {
        numericFactory.setRadix(10);
        assertEquals("10", numericFactory.getStringValue((short) 10));
        numericFactory.setRadix(2);
        assertEquals("0000000000001010", numericFactory.getStringValue((short) 10));
    }

    @Test
    void getStringValueTest2() {
        numericFactory.setRadix(10);
        assertEquals("-10", numericFactory.getStringValue((short) -10));
        numericFactory.setRadix(2);
        assertEquals("1111111111110110", numericFactory.getStringValue((short) -10));
    }

    @Test
    void getStringValueTest3() {
        numericFactory.setRadix(10);
        assertEquals("-32767", numericFactory.getStringValue((short) -32767));
        numericFactory.setRadix(2);
        assertEquals("1000000000000001", numericFactory.getStringValue((short) -32767));
    }

    @Test
    void getStringValueTest4() {
        numericFactory.setRadix(10);
        assertEquals("-1", numericFactory.getStringValue((short) -1));
        numericFactory.setRadix(2);
        assertEquals("1111111111111111", numericFactory.getStringValue((short) -1));
    }
}