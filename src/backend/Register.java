package backend;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Register {
    private SimpleStringProperty name;
    private SimpleIntegerProperty value;

    public Register(SimpleStringProperty name, SimpleIntegerProperty value) {
        this.name = name;
        this.value = value;
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
        return value.get();
    }

    public SimpleIntegerProperty valueProperty() {
        return value;
    }

    public void setValue(int value) {
        this.value.set(value);
    }
}
