package sample;

import backend.ObservableResourceFactory;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class HelpController {
    private final String appUseText = "Code tab\\nIn the code tab you can see a table containing information about the cpu instruction set. You can make use of the table when writing your code (Comments are single line only and start with the character ;). To the left of the table there is a code area in which you can write code. Under the table there is a console field which will indicate whether the assembly process was successful or not. As said earlier you can write your own code, or you can make use of the examples provided by the application. If you want to load an example, go to the File menu, choose Examples, and then choose your desired example to load. Yet another way to get code loaded is to load a file which contains proper code. You can load your file either by pressing Ctrl+L, or you can again navigate to the file menu and choose Load file. Note that the supported file types are .txt and .mic1 files. .mic1 is just an extension which you can give your files to distinguish them from other textual files. After you have loaded your code, one way or the other, you can then run it by pressing F1, clicking on the green play button, or navigating to the Execute menu and choosing the Run option. If the code is correct it will be assembled, and you will see in the console a success message. If on the other side the code has issues you will see an error message in the console. Fix the error and run the code again. To execute the code you can choose to run a sub-cycle or one entire cycle. To run a sub-cycle press F2, click on the single right arrow button, or navigate to the Execute menu and choose the Run sub-cycle option. To run a cycle press F3, click on the double right arrow button, or navigate to the Execute menu and choose the Run cycle option. To stop program execution and return to the initial state press F4, click on the red square button, or navigate to the Execute menu and choose the Stop execution option. There you have it. In this section we covered how to write or load code, run it and execute it.";

    public TextArea textArea;

    private final ObservableResourceFactory resourceFactory = ObservableResourceFactory.getInstance();

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

    public void setHowToUseAppTextAction() {
        textArea.setText(resourceFactory.getResources().getString("use-app-text"));
    }
}
