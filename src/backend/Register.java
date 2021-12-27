package backend;

import javafx.beans.property.SimpleStringProperty;

public class Register {
    private SimpleStringProperty name;
    private SimpleStringProperty value;

    public Register(String name, short value) {
        this.name = new SimpleStringProperty(name);
        this.value = new SimpleStringProperty(NumericFactory.getStringValue(value));
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

    public int getValue() {
        return NumericFactory.getShortValue(value.getValue());
    }

    public SimpleStringProperty valueProperty() {
        return value;
    }

    public void setValue(int value) {
        this.value.set(NumericFactory.getStringValue((short) value));
    }

    @Override
    public String toString() {
        return "Register{" +
                "name=" + name +
                ", value=" + value +
                '}';
    }
}
