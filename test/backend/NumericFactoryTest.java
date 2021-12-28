package backend;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumericFactoryTest {

    @Test
    void getStringValue16Test1() {
        NumericFactory.setRadix(10);
        assertEquals("10", NumericFactory.getStringValue16((short) 10));
        NumericFactory.setRadix(2);
        assertEquals("0000000000001010", NumericFactory.getStringValue16((short) 10));
    }

    @Test
    void getStringValue16Test2() {
        NumericFactory.setRadix(10);
        assertEquals("-10", NumericFactory.getStringValue16((short) -10));
        NumericFactory.setRadix(2);
        assertEquals("1111111111110110", NumericFactory.getStringValue16((short) -10));
    }

    @Test
    void getStringValue16Test3() {
        NumericFactory.setRadix(10);
        assertEquals("-32767", NumericFactory.getStringValue16((short) -32767));
        NumericFactory.setRadix(2);
        assertEquals("1000000000000001", NumericFactory.getStringValue16((short) -32767));
    }

    @Test
    void getStringValue16Test4() {
        NumericFactory.setRadix(10);
        assertEquals("-1", NumericFactory.getStringValue16((short) -1));
        NumericFactory.setRadix(2);
        assertEquals("1111111111111111", NumericFactory.getStringValue16((short) -1));
    }

    @Test
    void getStringValue32Test1() {
        NumericFactory.setRadix(10);
        assertEquals("-1", NumericFactory.getStringValue32(-1));
        NumericFactory.setRadix(2);
        assertEquals("11111111111111111111111111111111",
                NumericFactory.getStringValue32(-1));
    }

    @Test
    void getStringValue32Test2() {
        NumericFactory.setRadix(10);
        assertEquals("-1340932068", NumericFactory.getStringValue32(-1340932068));
        NumericFactory.setRadix(2);
        assertEquals("10110000000100110000000000011100",
                NumericFactory.getStringValue32(-1340932068));
    }

    @Test
    void getStringValue32Test3() {
        NumericFactory.setRadix(10);
        assertEquals("1612709888", NumericFactory.getStringValue32(1612709888));
        NumericFactory.setRadix(2);
        assertEquals("01100000001000000000000000000000",
                NumericFactory.getStringValue32(1612709888));
    }

    @Test
    void getStringValue8Test1() {
        NumericFactory.setRadix(10);
        assertEquals("-1", NumericFactory.getStringValue8((short) -1));
        NumericFactory.setRadix(2);
        assertEquals("11111111",
                NumericFactory.getStringValue8((short) -1));
    }

    @Test
    void getStringValue8Test2() {
        NumericFactory.setRadix(10);
        assertEquals("255", NumericFactory.getStringValue8((short) 255));
        NumericFactory.setRadix(2);
        assertEquals("11111111",
                NumericFactory.getStringValue8((short) 255));
    }

    @Test
    void getStringValue8Test3() {
        NumericFactory.setRadix(10);
        assertEquals("72", NumericFactory.getStringValue8((short) 72));
        NumericFactory.setRadix(2);
        assertEquals("01001000",
                NumericFactory.getStringValue8((short) 72));
    }

    @Test
    void getStringValue8Test4() {
        NumericFactory.setRadix(10);
        assertEquals("128", NumericFactory.getStringValue8((short) 128));
        NumericFactory.setRadix(2);
        assertEquals("10000000",
                NumericFactory.getStringValue8((short) 128));
    }

    @Test
    void getShortValueTest1() {
        assertEquals(10, NumericFactory.getShortValue("10", 10));
        assertEquals(10, NumericFactory.getShortValue("0000000000001010", 2));
    }

    @Test
    void getShortValueTest2() {
        assertEquals(-32768, NumericFactory.getShortValue("1000000000000000", 2));
        assertEquals(-32768, NumericFactory.getShortValue("-32768", 10));
    }

    @Test
    void getShortValueTest3() {
        assertEquals(24576, NumericFactory.getShortValue("0110000000000000", 2));
        assertEquals(24576, NumericFactory.getShortValue("24576", 10));
    }

    @Test
    void getShortValueTest4() {
        assertEquals(-1, NumericFactory.getShortValue("1111111111111111", 2));
        assertEquals(-1, NumericFactory.getShortValue("-1", 10));
    }
}