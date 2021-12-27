package backend;

public class NumericFactory {
    private int radix = 10;

    public short getShortValue(String value) {
        if (radix == 10)
            return getShortValue10(value);
        return getShortValue2(value);
    }

    private short getShortValue2(String value) {
        return (short) Integer.parseUnsignedInt(value, 2);
    }

    private short getShortValue10(String value) {
        return (short) Integer.parseInt(value);
    }

    public String getStringValue(short value) {
        if (radix == 10)
            return getStringValue10(value);
        return getStringValue2(value);
    }

    private String getStringValue10(short value) {
        return String.valueOf(value);
    }

    private String getStringValue2(short value) {
        String res = Integer.toBinaryString(value);
        if (res.length() > 16)
            return res.substring(res.length()-16);
        return "0".repeat(16 - res.length()) + res;
    }



    public int getRadix() {
        return radix;
    }

    public void setRadix(int radix) {
        this.radix = radix;
    }
}
