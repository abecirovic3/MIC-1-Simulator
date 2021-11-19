package sample;

import backend.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javafx.util.Pair;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Controller {
    public TableColumn<Map, String> instrMnemonic;
    public TableColumn<Map, String> instrInstruction;
    public TableColumn<Map, String> instrMeaning;
    public TableColumn<Map, String> instrBinaryCode;
    public TableView<Map> supportedInstructionsTable;

    // Registers Table
    public TableView registersTable;
    public TableColumn regName;
    public TableColumn regValue;

    // Memory Table
    public TableView memoryTable;
    public TableColumn memAddress;
    public TableColumn memValue;

    //public TextArea microcodeTextArea;
    public CodeArea codeArea;
    public CodeArea microcodeArea;
    public TextArea console;

    public TextField MARField;
    public TextField MBRField;
    public TextField MPCField;
    public TextField MIRField;

    public TextField memorySearchField;
    public TextField searchedAddressValueField;

    public Label clockLab;
    public Label subcycleLab;

    // tooltips
    public AnchorPane dataPathPane;
    public ImageView registersImg;
    private Map<ImageView, Pair<Tooltip, Function<String, String>>> toolTips = new HashMap<>();

    private CodeParser codeParser = CodeParser.getInstance();
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

        supportedInstructionsTable.getItems().addAll(FileParser.loadSupportedInstructionsTableData());

        //microcodeArea.setParagraphGraphicFactory(LineNumberFactory.get(microcodeArea));
        microcodeArea.replaceText(FileParser.loadMicroCode());
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

        // Memory Table
        memAddress.setCellValueFactory(new PropertyValueFactory<MemoryLine, String>("address"));
        memValue.setCellValueFactory(new PropertyValueFactory<MemoryLine, String>("value"));
        memoryTable.setItems(cpu.getMemory().getMemory());

        // MAR and MBR
        MARField.setText(cpu.MARProperty().getValue().toString());
        MBRField.setText(cpu.MBRProperty().getValue().toString());
        cpu.MARProperty().addListener((ChangeListener) (o, oldVal, newVal) -> MARField.setText(newVal.toString()));
        cpu.MBRProperty().addListener((ChangeListener) (o, oldVal, newVal) -> MBRField.setText(newVal.toString()));

        // clock grid
        clockLab.setText(cpu.clockCounterProperty().getValue().toString());
        subcycleLab.setText(cpu.clockProperty().getValue().toString());
        cpu.clockCounterProperty().addListener((ChangeListener) (o, oldVal, newVal) -> clockLab.setText(newVal.toString()));
        cpu.clockProperty().addListener((ChangeListener) (o, oldVal, newVal) -> subcycleLab.setText(newVal.toString()));

        // MPC
        MPCField.setText(cpu.MPCProperty().getValue().toString());
        cpu.MPCProperty().addListener((ChangeListener) (o, oldVal, newVal) -> MPCField.setText(newVal.toString()));

        // MIR
        MIRField.setText(cpu.MIRProperty().getValue().toString());
        cpu.MIRProperty().addListener((ChangeListener) (o, oldVal, newVal) -> MIRField.setText(newVal.toString()));

        // tooltips
        bindTooltips();
    }

    private void bindTooltips() {
        for (Node img : dataPathPane.getChildren()) {
            if (img.getId().equals("placeHolderImg") || img.getId().equals("clockGrid")) continue;
            Tooltip tooltip = new Tooltip();
            Tooltip.install(img, tooltip);
            tooltip.setShowDuration(new Duration(60000));
            tooltip.setShowDelay(new Duration(250));
            toolTips.put((ImageView) img, new Pair<>(tooltip, cpu::getComponentToolTip));
            tooltip.setText(cpu.getComponentToolTip(img.getId()));
        }
    }

    public void runCodeAction(ActionEvent actionEvent) {
        try {
            short[] machineCode = codeParser.parseCode(codeArea.getText());

            console.setText("Code assembled successfully");
            cpu.getMemory().write(machineCode);
        } catch (CodeParserException e) {
            console.setText(e.getMessage());
        }
    }

    public void runClockCycleAction(ActionEvent actionEvent) {
        cpu.runCycle();
        updateToolTips();
    }

    public void searchMemoryAction(ActionEvent actionEvent) {
        try {
            String addressString = memorySearchField.getText();
            int address = Integer.parseInt(addressString);
            if (address >= 0 && address <= 4095) {
                searchedAddressValueField.setText(String.valueOf(cpu.getMemory().read((short) address)));
            }
        } catch (NumberFormatException e) {
            // TODO
            // style for bad search input
        }
    }

    public void runSubClockCycleAction(ActionEvent actionEvent) {
        cpu.runSubCycle();
        updateToolTips();
    }

    private void updateToolTips() {
        for (ImageView img : toolTips.keySet()) {
            toolTips.get(img).getKey().setText(toolTips.get(img).getValue().apply(img.getId()));
        }
    }
}
