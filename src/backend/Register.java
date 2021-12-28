package backend;

import javafx.beans.property.SimpleStringProperty;

public class Register {
    private short value;
    private SimpleStringProperty name;
    private SimpleStringProperty stringValue;

    public Register(String name, short value) {
        this.name = new SimpleStringProperty(name);
        this.value = value;
        this.stringValue = new SimpleStringProperty(NumericFactory.getStringValue16(value));
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
        stringValue.setValue(NumericFactory.getStringValue16(value));
    }

    public String getStringValue() {
        return stringValue.get();
    }

    public SimpleStringProperty stringValueProperty() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue.set(stringValue);
    }

    @Override
    public String toString() {
        return "Register{" +
                "name=" + name +
                ", value=" + value +
                '}';
    }
}
