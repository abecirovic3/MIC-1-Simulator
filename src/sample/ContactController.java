package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class ContactController {
    private final Host host = Host.getInstance();

    public void openFacebookLinkAction(ActionEvent actionEvent) {
        openLinkAction("https://www.facebook.com/ajdin.becirovic.1/");
    }

    public void openGitHubLinkAction(ActionEvent actionEvent) {
        openLinkAction("https://github.com/abecirovic3");
    }

    public void openLinkedInLinkAction(ActionEvent actionEvent) {
        openLinkAction("https://www.linkedin.com/in/ajdin-be%C4%8Dirovi%C4%87-bb83b6208/");
    }

    public void openLinkAction(String link) {
        try {
            host.getHostServices().showDocument(link);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ooops");
            alert.setHeaderText("Something went wrong, could not open link");
            alert.showAndWait();
        }
    }
}
