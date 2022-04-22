package sample;

import backend.ObservableResourceFactory;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class HelpController {

    public TextArea textArea;

    private final ObservableResourceFactory resourceFactory = ObservableResourceFactory.getInstance();

    public void setTextAreaAction(ActionEvent actionEvent) {
        System.out.println("OK");
    }

    public void closeAction(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage currStage = (Stage) node.getScene().getWindow();
        currStage.close();
    }

    public void setIconsCreditsTextAction() {
        textArea.setText(resourceFactory.getResources().getString("icon-credits"));
    }

    public void setReportBugsTextAction() {
        textArea.setText(resourceFactory.getResources().getString("report-bugs-text"));
    }

    public void setHelpDevelopTextAction() {
        textArea.setText(resourceFactory.getResources().getString("help-development-text"));
    }

    public void setShortcutsTextAction(ActionEvent actionEvent) {
        textArea.setText(resourceFactory.getResources().getString("shortcuts-text"));
    }
}
