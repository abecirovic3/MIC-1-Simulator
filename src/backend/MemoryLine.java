package backend;

import javafx.beans.property.SimpleIntegerProperty;

public class MemoryLine {
    SimpleIntegerProperty address;
    SimpleIntegerProperty value;

    public MemoryLine(short address, short value) {
        this.address = new SimpleIntegerProperty(address);
        this.value = new SimpleIntegerProperty(value);
    }

    public int getAddress() {
        return address.get();
    }

    public SimpleIntegerProperty addressProperty() {
        return address;
    }

    public void setAddress(int address) {
        this.address.set(address);
    }

    public int getValue() {
        return value.get();
    }

    public SimpleIntegerProperty valueProperty() {
        return value;
    }

    public void setValue(int value) {
        this.value.set(value);
    }
}
