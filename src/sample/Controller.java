package sample;

import backend.CPU;
import backend.CodeParser;
import backend.FileParser;
import backend.Register;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.util.HashMap;
import java.util.Map;

public class Controller {
    public TableColumn<Map, String> instrMnemonic;
    public TableColumn<Map, String> instrInstruction;
    public TableColumn<Map, String> instrMeaning;
    public TableColumn<Map, String> instrBinaryCode;
    public TableView<Map> supportedInstructionsTable;

    public TableView registersTable;
    public TableColumn regName;
    public TableColumn regValue;

    //public TextArea microcodeTextArea;
    public CodeArea codeArea;
    public CodeArea microcodeArea;

    private final FileParser fileParser = new FileParser();

    private CPU cpu = new CPU();

    @FXML
    public void initialize() {
        instrMnemonic.setCellValueFactory(new MapValueFactory<>("mnemonic"));
        // If we want the cells to be editable ...
        
//        instrMnemonic.setCellFactory(TextFieldTableCell.forTableColumn());
//        instrMnemonic.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Map, String>>() {
//            @Override
//            public void handle(TableColumn.CellEditEvent<Map, String> event) {
//                Map<String, Object> row = event.getRowValue();
//                row.put("mnemonic", event.getNewValue());
//            }
//        });
        instrInstruction.setCellValueFactory(new MapValueFactory<>("instruction"));
        instrMeaning.setCellValueFactory(new MapValueFactory<>("meaning"));
        instrBinaryCode.setCellValueFactory(new MapValueFactory<>("binaryCode"));

        supportedInstructionsTable.getItems().addAll(fileParser.loadSupportedInstructionsTableData());

        //microcodeArea.setParagraphGraphicFactory(LineNumberFactory.get(microcodeArea));
        microcodeArea.replaceText(fileParser.loadMicroCode());
        // Scroll the area to the top
        microcodeArea.moveTo(0); // this method works with characters...
        microcodeArea.requestFollowCaret();

        // To highlight a line
        // microcodeArea.moveTo(10,0);
        // microcodeArea.setLineHighlighterOn(true);

        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        //codeArea.setContextMenu( new DefaultContextMenu() );

        // Registers Table
        regName.setCellValueFactory(new PropertyValueFactory<Register, String>("name"));
        regValue.setCellValueFactory(new PropertyValueFactory<Register, String>("value"));
        registersTable.setItems(cpu.getRegisters());
    }

    public void runCodeAction(ActionEvent actionEvent) {
        CodeParser cp = new CodeParser();
        String res = cp.parseCode(codeArea.getText());
        System.out.println(res);
    }

    public void runClockCycleAction(ActionEvent actionEvent) {
        cpu.runCycle();
    }
}
