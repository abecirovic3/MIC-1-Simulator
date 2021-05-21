package sample;

import backend.FileParser;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.util.Map;

public class Controller {
    public TableColumn<Map, String> instrMnemonic;
    public TableColumn<Map, String> instrInstruction;
    public TableColumn<Map, String> instrMeaning;
    public TableColumn<Map, String> instrBinaryCode;
    public TableView<Map> supportedInstructionsTable;
    //public TextArea microcodeTextArea;
    public CodeArea codeArea;
    public CodeArea microcodeArea;

    private FileParser fileParser = new FileParser();

    @FXML
    public void initialize() {
        instrMnemonic.setCellValueFactory(new MapValueFactory<>("mnemonic"));
        instrInstruction.setCellValueFactory(new MapValueFactory<>("instruction"));
        instrMeaning.setCellValueFactory(new MapValueFactory<>("meaning"));
        instrBinaryCode.setCellValueFactory(new MapValueFactory<>("binaryCode"));

        supportedInstructionsTable.getItems().addAll(fileParser.loadSupportedInstructionsTableData());

        microcodeArea.setParagraphGraphicFactory(LineNumberFactory.get(microcodeArea));
        microcodeArea.replaceText(fileParser.loadMicroCode());

        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        //codeArea.setContextMenu( new DefaultContextMenu() );
    }
}
