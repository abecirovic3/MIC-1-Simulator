package sample;

import javafx.application.HostServices;

public class Host {
    private static Host instance;

    private HostServices hostServices;

    public static Host getInstance() {
        if (instance == null) instance = new Host();
        return instance;
    }

    private Host() {}

    public HostServices getHostServices() {
        return hostServices;
    }

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }
}
