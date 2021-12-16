package sample;

import backend.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.util.Pair;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

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
    public TableView<Register> registersTable;
    public TableColumn<Register, String> regName;
    public TableColumn<Register, String> regValue;

    // Memory Table
    public TableView<MemoryLine> memoryTable;
    public TableColumn<MemoryLine, String> memAddress;
    public TableColumn<MemoryLine, String> memValue;

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

    public AnchorPane dataPathPane;
    public ImageView registersImg;
    private Map<ImageView, Pair<Tooltip, Function<String, String>>> toolTips = new HashMap<>();

    private CodeParser codeParser = CodeParser.getInstance();
    private CPU cpu = new CPU();

    @FXML
    public void initialize() {
        initializeSupportedInstructionsTable();
        initializeMicrocodeArea();
        initializeCodeArea();
        initializeRegistersTable();
        initializeMemoryTable();
        initializeMARAndMBRFields();
        initializeClockGrid();
        initializeMPCField();
        initializeMIRField();
        bindTooltips();
    }

    private void initializeCodeArea() {
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
    }

    private void initializeMIRField() {
        MIRField.setText(cpu.MIRProperty().getValue().toString());
        cpu.MIRProperty().addListener((o, oldVal, newVal) -> MIRField.setText(newVal.toString()));
    }

    private void initializeMPCField() {
        MPCField.setText(cpu.MPCProperty().getValue().toString());
        cpu.MPCProperty().addListener((o, oldVal, newVal) -> MPCField.setText(newVal.toString()));
    }

    private void initializeClockGrid() {
        clockLab.setText(String.valueOf(cpu.clockCounterProperty().get()));
        subcycleLab.setText(String.valueOf(cpu.clockProperty().get() + 1));
        cpu.clockCounterProperty().addListener((o, oldVal, newVal) -> clockLab.setText(newVal.toString()));
        cpu.clockProperty().addListener(
                (o, oldVal, newVal) -> subcycleLab.setText(String.valueOf((Integer) newVal + 1))
        );
    }

    private void initializeMARAndMBRFields() {
        MARField.setText(cpu.MARProperty().getValue().toString());
        MBRField.setText(cpu.MBRProperty().getValue().toString());
        cpu.MARProperty().addListener((o, oldVal, newVal) -> MARField.setText(newVal.toString()));
        cpu.MBRProperty().addListener((o, oldVal, newVal) -> MBRField.setText(newVal.toString()));
    }

    private void initializeMemoryTable() {
        memAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        memValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        memoryTable.setItems(cpu.getMemory().getMemory());
    }

    private void initializeRegistersTable() {
        regName.setCellValueFactory(new PropertyValueFactory<>("name"));
        regValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        registersTable.setItems(cpu.getRegisters());
    }

    private void initializeMicrocodeArea() {
        microcodeArea.replaceText(FileParser.loadMicroCode());
        // Scroll the area to the top
        microcodeArea.moveTo(0); // this method works with characters...
        microcodeArea.requestFollowCaret();
    }

    private void initializeSupportedInstructionsTable() {
        instrMnemonic.setCellValueFactory(new MapValueFactory<>("mnemonic"));
        instrInstruction.setCellValueFactory(new MapValueFactory<>("instruction"));
        instrMeaning.setCellValueFactory(new MapValueFactory<>("meaning"));
        instrBinaryCode.setCellValueFactory(new MapValueFactory<>("binaryCode"));
        supportedInstructionsTable.getItems().addAll(FileParser.loadSupportedInstructionsTableData());
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

    public void runSubClockCycleAction(ActionEvent actionEvent) {
        cpu.runSubCycle();
        updateToolTips();
    }

    private void updateToolTips() {
        for (ImageView img : toolTips.keySet()) {
            toolTips.get(img).getKey().setText(toolTips.get(img).getValue().apply(img.getId()));
        }
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
}
