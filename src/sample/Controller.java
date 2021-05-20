package sample;

import backend.FileParser;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.MapValueFactory;

import java.util.Map;

public class Controller {
    public TableColumn<Map, String> instrMnemonic;
    public TableColumn<Map, String> instrInstruction;
    public TableColumn<Map, String> instrMeaning;
    public TableColumn<Map, String> instrBinaryCode;
    public TableView<Map> supportedInstructionsTable;
    public TextArea microcodeTextArea;

    private FileParser fileParser = new FileParser();

    @FXML
    public void initialize() {
        instrMnemonic.setCellValueFactory(new MapValueFactory<>("mnemonic"));
        instrInstruction.setCellValueFactory(new MapValueFactory<>("instruction"));
        instrMeaning.setCellValueFactory(new MapValueFactory<>("meaning"));
        instrBinaryCode.setCellValueFactory(new MapValueFactory<>("binaryCode"));

        supportedInstructionsTable.getItems().addAll(fileParser.loadSupportedInstructionsTableData());

        microcodeTextArea.setText(fileParser.loadMicroCode());
    }
}
