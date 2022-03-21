package backend;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Locale;
import java.util.ResourceBundle;

public class ObservableResourceFactory {
    private static ObservableResourceFactory instance;
    private final ObjectProperty<ResourceBundle> resources;

    public static ObservableResourceFactory getInstance() {
        if (instance == null) instance = new ObservableResourceFactory();
        return instance;
    }

    private ObservableResourceFactory() {
        resources = new SimpleObjectProperty<>();
        setResources(ResourceBundle.getBundle("Translation", new Locale("bs", "BA")));
    }

    public ResourceBundle getResources() {
        return resources.get();
    }

    public ObjectProperty<ResourceBundle> resourcesProperty() {
        return resources;
    }

    public void setResources(ResourceBundle resources) {
        this.resources.set(resources);
    }

    public StringBinding getStringBinding(String key) {
        return new StringBinding() {
            { bind(resourcesProperty()); }
            @Override
            public String computeValue() {
                return getResources().getString(key);
            }
        };
    }
}