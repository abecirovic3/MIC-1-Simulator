package backend;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumericFactoryTest {
    @Test
    void getShortValueTest1() {
        NumericFactory.setRadix(10);
        assertEquals(-32767, NumericFactory.getShortValue("-32767"));
        NumericFactory.setRadix(2);
        assertEquals(-32767, NumericFactory.getShortValue("1000000000000001"));
    }

    @Test
    void getShortValueTest2() {
        NumericFactory.setRadix(10);
        assertEquals(-1, NumericFactory.getShortValue("-1"));
        NumericFactory.setRadix(2);
        assertEquals(-1, NumericFactory.getShortValue("1111111111111111"));
    }

    @Test
    void getShortValueTest3() {
        NumericFactory.setRadix(10);
        assertEquals(-32768, NumericFactory.getShortValue("-32768"));
        NumericFactory.setRadix(2);
        assertEquals(-32768, NumericFactory.getShortValue("1000000000000000"));
    }

    @Test
    void getStringValueTest1() {
        NumericFactory.setRadix(10);
        assertEquals("10", NumericFactory.getStringValue((short) 10));
        NumericFactory.setRadix(2);
        assertEquals("0000000000001010", NumericFactory.getStringValue((short) 10));
    }

    @Test
    void getStringValueTest2() {
        NumericFactory.setRadix(10);
        assertEquals("-10", NumericFactory.getStringValue((short) -10));
        NumericFactory.setRadix(2);
        assertEquals("1111111111110110", NumericFactory.getStringValue((short) -10));
    }

    @Test
    void getStringValueTest3() {
        NumericFactory.setRadix(10);
        assertEquals("-32767", NumericFactory.getStringValue((short) -32767));
        NumericFactory.setRadix(2);
        assertEquals("1000000000000001", NumericFactory.getStringValue((short) -32767));
    }

    @Test
    void getStringValueTest4() {
        NumericFactory.setRadix(10);
        assertEquals("-1", NumericFactory.getStringValue((short) -1));
        NumericFactory.setRadix(2);
        assertEquals("1111111111111111", NumericFactory.getStringValue((short) -1));
    }
}