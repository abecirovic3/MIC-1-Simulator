package sample;

import backend.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class Controller {
    public TableView<Map> supportedInstructionsTable;
    public TableColumn<Map, String> instrMnemonic;
    public TableColumn<Map, String> instrInstruction;
    public TableColumn<Map, String> instrMeaning;
    public TableColumn<Map, String> instrBinaryCode;

    public TableView<Map> controlMemoryTable;
    public TableColumn<Map, String> cmAddressCol;
    public TableColumn<Map, String> cmAmuxCol;
    public TableColumn<Map, String> cmCondCol;
    public TableColumn<Map, String> cmAluCol;
    public TableColumn<Map, String> cmShCol;
    public TableColumn<Map, String> cmMbrCol;
    public TableColumn<Map, String> cmMarCol;
    public TableColumn<Map, String> cmRdCol;
    public TableColumn<Map, String>cmWrCol;
    public TableColumn<Map, String> cmEncCol;
    public TableColumn<Map, String> cmCCol;
    public TableColumn<Map, String> cmBCol;
    public TableColumn<Map, String> cmACol;
    public TableColumn<Map, String> cmAddrCol;

    // Registers Table
    public TableView<Register> registersTable;
    public TableColumn<Register, String> regName;
    public TableColumn<Register, String> regValue;

    // Memory Table
    public TableView<MemoryLine> memoryTable;
    public TableColumn<MemoryLine, String> memAddress;
    public TableColumn<MemoryLine, String> memValue;

    public TextArea codeArea;
    public TextArea microcodeArea;
    public TextArea console;

    public Label MARField;
    public Label MBRField;
    public Label MPCField;
    public Label MIRField;

    public TextField memorySearchField;
    public TextField searchedAddressValueField;

    public Label clockLab;
    public Label subcycleLab;
    public Label instructionStatusLabel;

    public Button btnRun;
    public Button btnNextSubClock;
    public Button btnNextClock;

    public ChoiceBox<String> radixChoiceBox;

    public TabPane tabPane;
    public Tab codeTab;
    public Tab memoryTab;

    public AnchorPane dataPathPane;
    public ImageView registersImg;
    private final Map<ImageView, Pair<Tooltip, Function<String, String>>> toolTips = new HashMap<>();

    private final Map<ImageView, Image[]> dataPathImages = new HashMap<>();

    private final CodeParser codeParser = CodeParser.getInstance();
    private final InstructionParser instructionParser = InstructionParser.getInstance();
    private CPU cpu = new CPU();

    private final int[][] microCodeLinesLengths = new int[256][2];

    @FXML
    public void initialize() {
        initializeSupportedInstructionsTable();
        initializeControlMemoryTable();
        initializeMicrocodeArea();
        initializeRegistersTable();
        initializeMemoryTable();
        initializeMARAndMBRFields();
        initializeClockGrid();
        initializeMPCField();
        initializeMIRField();
        initializeRadixChoiceBox();
        initializeConsole();
        initializeMemoryTab();
        bindTooltips();
        bindImageViews();
    }

    private void initializeMemoryTab() {
        memoryTab.setOnSelectionChanged(event -> {
            memorySearchField.setText("");
            searchedAddressValueField.setText("");
        });
    }

    private void initializeConsole() {
        console.addEventFilter(MouseEvent.ANY, Event::consume);
    }

    private void initializeRadixChoiceBox() {
        radixChoiceBox.getItems().add("Decimal");
        radixChoiceBox.getItems().add("Binary");
        radixChoiceBox.setValue("Decimal");
        radixChoiceBox.setOnAction((event) -> {
            String choice = radixChoiceBox.getValue();
            int radix = 10;
            if (choice.equals("Binary")) radix = 2;
            if (radix != NumericFactory.getRadix())
                changeRadix(radix);
        });
    }

    private void initializeMIRField() {
        MIRField.setText(NumericFactory.getStringValue32(cpu.MIRProperty().get()));
        cpu.MIRProperty().addListener(
                (o, oldVal, newVal) -> MIRField.setText(NumericFactory.getStringValue32(newVal.intValue())));
    }

    private void initializeMPCField() {
        MPCField.setText(NumericFactory.getStringValue8(cpu.MPCProperty().getValue().shortValue()));
        cpu.MPCProperty().addListener((o, oldVal, newVal) -> {
            MPCField.setText(NumericFactory.getStringValue8(newVal.shortValue()));
            microcodeArea.selectRange(
                    microCodeLinesLengths[newVal.intValue()][0], microCodeLinesLengths[newVal.intValue()][1]);
            if (newVal.intValue() <= 2) {
                instructionStatusLabel.setText("Fetching instruction...");
            }
            if (newVal.intValue() == 3) {
                instructionStatusLabel.
                        setText("Fetched instruction: " +
                                instructionParser.getInstructionString((short) cpu.MBRProperty().get()));
            }
        });
    }

    private void initializeClockGrid() {
        clockLab.setText(String.valueOf(cpu.clockCounterProperty().get() + 1));
        subcycleLab.setText(String.valueOf(cpu.clockProperty().get() + 1));
        cpu.clockCounterProperty().addListener(
                (o, oldVal, newVal) -> clockLab.setText(String.valueOf((Integer) newVal + 1)));
        cpu.clockProperty().addListener(
                (o, oldVal, newVal) -> subcycleLab.setText(String.valueOf((Integer) newVal + 1)));
    }

    private void initializeMARAndMBRFields() {
        MARField.setText(NumericFactory.getStringValue16(cpu.MARProperty().getValue().shortValue()));
        MBRField.setText(NumericFactory.getStringValue16(cpu.MBRProperty().getValue().shortValue()));
        cpu.MARProperty().addListener(
                (o, oldVal, newVal) -> MARField.setText(NumericFactory.getStringValue16(newVal.shortValue())));
        cpu.MBRProperty().addListener(
                (o, oldVal, newVal) -> MBRField.setText(NumericFactory.getStringValue16(newVal.shortValue())));
    }

    private void initializeMemoryTable() {
        memAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        memValue.setCellValueFactory(new PropertyValueFactory<>("stringValue"));
        memValue.setCellFactory(TextFieldTableCell.forTableColumn());
        memValue.setOnEditCommit(event -> {
            changeMemoryLineValue(event.getRowValue(), event.getNewValue());
        });
        memoryTable.setItems(cpu.getMemory().getMemory());
    }

    private void changeMemoryLineValue(MemoryLine memLine, String newValue) {
        try {
            int value = Integer.parseInt(newValue);
            if (value < Short.MIN_VALUE || value > Short.MAX_VALUE)
                throw new NumberFormatException();
            memLine.setValue((short) value);
            memoryTable.refresh();
        } catch (NumberFormatException e) {
            showErrorAlert("memory");
            memoryTable.refresh();
        }
    }

    private void initializeRegistersTable() {
        regName.setCellValueFactory(new PropertyValueFactory<>("name"));
        regValue.setCellValueFactory(new PropertyValueFactory<>("stringValue"));
        regValue.setCellFactory(TextFieldTableCell.forTableColumn());
        regValue.setOnEditCommit(event -> {
            changeRegisterValue(event.getRowValue(), event.getNewValue());
        });
        registersTable.setItems(cpu.getRegisters());
    }

    private void changeRegisterValue(Register register, String newValue) {
        try {
            int value = Integer.parseInt(newValue);
            validateValue(register.getName(), value);
            register.setValue((short) value);
            registersTable.refresh();
        } catch (NumberFormatException e) {
            showErrorAlert(register.getName());
            registersTable.refresh();
        }
    }

    private void showErrorAlert(String identifier) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid new value!");
        alert.setContentText(identifier + getBoundaryMsg(identifier));
        alert.showAndWait();
    }

    private String getBoundaryMsg(String identifier) {
        if (identifier.equalsIgnoreCase("memory"))
            return ": valid range is [" + Short.MIN_VALUE + ", " + Short.MAX_VALUE +"].";
        if (isImmutableRegister(identifier))
            return " register is immutable.";
        return ": valid range is [" + getRegisterLowerBound(identifier)
                + ", " + getRegisterUpperBound(identifier) + "].";
    }

    private void validateValue(String regName, int value) {
        if (isImmutableRegister(regName))
            throw new NumberFormatException();

        int lower = getRegisterLowerBound(regName);
        int upper = getRegisterUpperBound(regName);

        if (value < lower || value > upper)
            throw new NumberFormatException();
    }

    private int getRegisterUpperBound(String regName) {
        int res = Short.MAX_VALUE;
        if (regName.equalsIgnoreCase("PC") || regName.equalsIgnoreCase("SP"))
            res = 4095;
        return res;
    }

    private int getRegisterLowerBound(String regName) {
        int res = Short.MIN_VALUE;
        if (regName.equalsIgnoreCase("PC") || regName.equalsIgnoreCase("SP"))
            res = 0;
        return res;
    }

    private boolean isImmutableRegister(String regName) {
        return regName.equals("0") || regName.equals("+1") || regName.equals("-1")
                || regName.equalsIgnoreCase("AMASK")
                || regName.equalsIgnoreCase("SMASK");
    }

    private void initializeMicrocodeArea() {
        String code = FileParser.loadMicroCode();
        String[] codeLines = code.split("\n");
        setMicroCodeLinesLengths(codeLines);
        microcodeArea.setText(FileParser.loadMicroCode());
        microcodeArea.setStyle(null);
        microcodeArea.addEventFilter(MouseEvent.MOUSE_RELEASED, t -> {
            int line = cpu.MPCProperty().get();
            if (btnRun.isDisabled())
                microcodeArea.selectRange(microCodeLinesLengths[line][0], microCodeLinesLengths[line][1]);
        });
    }

    private void setMicroCodeLinesLengths(String[] codeLines) {
        for (int i = 0; i < codeLines.length; i++) {
            microCodeLinesLengths[i][0] = i > 0 ? microCodeLinesLengths[i-1][1] + 1 : 0;
            microCodeLinesLengths[i][1] = microCodeLinesLengths[i][0] + codeLines[i].length();
        }
    }


    private void initializeControlMemoryTable() {
        cmAddressCol.setCellValueFactory(new MapValueFactory<>("address"));
        cmAmuxCol.setCellValueFactory(new MapValueFactory<>("amux"));
        cmCondCol.setCellValueFactory(new MapValueFactory<>("cond"));
        cmAluCol.setCellValueFactory(new MapValueFactory<>("alu"));
        cmShCol.setCellValueFactory(new MapValueFactory<>("sh"));
        cmMbrCol.setCellValueFactory(new MapValueFactory<>("mbr"));
        cmMarCol.setCellValueFactory(new MapValueFactory<>("mar"));
        cmRdCol.setCellValueFactory(new MapValueFactory<>("rd"));
        cmWrCol.setCellValueFactory(new MapValueFactory<>("wr"));
        cmEncCol.setCellValueFactory(new MapValueFactory<>("enc"));
        cmCCol.setCellValueFactory(new MapValueFactory<>("c"));
        cmBCol.setCellValueFactory(new MapValueFactory<>("b"));
        cmACol.setCellValueFactory(new MapValueFactory<>("a"));
        cmAddrCol.setCellValueFactory(new MapValueFactory<>("addr"));
        controlMemoryTable.getItems().addAll(FileParser.loadControlMemoryTableData());
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
            if (img.getId().equals("placeHolderImg")) continue;
            Tooltip tooltip = new Tooltip();
            Tooltip.install(img, tooltip);
            tooltip.setShowDuration(new Duration(60000));
            tooltip.setShowDelay(new Duration(250));
            toolTips.put((ImageView) img, new Pair<>(tooltip, cpu::getComponentToolTip));
            tooltip.setText(cpu.getComponentToolTip(img.getId()));
        }
    }

    private void bindImageViews() {
        // This code shouldn't throw NPE bcs the resources are present
        // Warnings are ignored bcs the fix with Objects.requireNonNull method doesn't really do much
        // except that it throws NPE instead of Image constructor
        // I'll leave one warning unsuppressed for future reference
        try {
            for (Node img : dataPathPane.getChildren()) {
                if (img.getId().equals("registersImg")) {
                    dataPathImages.put((ImageView) img, getImgArray("reg.png", "reg_active.png"));
                } else if (img.getId().equals("aluImg")) {
                    dataPathImages.put((ImageView) img, getImgArray("alu.png", "alu_active.png"));
                } else if (img.getId().equals("amuxImg")) {
                    dataPathImages.put((ImageView) img, getImgArray("amux.png", "amux_active.png"));
                } else if (img.getId().equals("aLatchImg")) {
                    dataPathImages.put((ImageView) img, getImgArray("a-latch.png", "a-latch_active.png"));
                } else if (img.getId().equals("bLatchImg")) {
                    dataPathImages.put((ImageView) img, getImgArray("b-latch.png", "b-latch_active.png"));
                } else if (img.getId().equals("aDecImg")) {
                    dataPathImages.put((ImageView) img, getImgArray("a-dec.png", "a-dec_active.png"));
                } else if (img.getId().equals("bDecImg")) {
                    dataPathImages.put((ImageView) img, getImgArray("b-dec.png", "b-dec_active.png"));
                } else if (img.getId().equals("cDecImg")) {
                    dataPathImages.put((ImageView) img, getImgArray("c-dec.png", "c-dec_active.png"));
                } else if (img.getId().equals("clockImg")) {
                    dataPathImages.put((ImageView) img,
                            getImgArray("clock.png", "clock_active_1.png",
                                    "clock_active_2.png", "clock_active_3.png", "clock_active_4.png"));
                } else if (img.getId().equals("shifterImg")) {
                    dataPathImages.put((ImageView) img, getImgArray("shifter.png", "shifter_active.png"));
                } else if (img.getId().equals("marImg")) {
                    dataPathImages.put((ImageView) img, getImgArray("mar.png", "mar_active.png"));
                } else if (img.getId().equals("mbrImg")) {
                    dataPathImages.put((ImageView) img, getImgArray("mbr.png", "mbr_active.png"));
                } else if (img.getId().equals("mMuxImg")) {
                    dataPathImages.put((ImageView) img, getImgArray("mmux.png", "mmux_active.png"));
                } else if (img.getId().equals("mpcImg")) {
                    dataPathImages.put((ImageView) img, getImgArray("mpc.png", "mpc_active.png"));
                } else if (img.getId().equals("incImg")) {
                    dataPathImages.put((ImageView) img, getImgArray("incr.png", "incr_active.png"));
                } else if (img.getId().equals("controlImg")) {
                    dataPathImages.put((ImageView) img, getImgArray("control.png", "control_active.png"));
                } else if (img.getId().equals("mirImg")) {
                    dataPathImages.put((ImageView) img, getImgArray("mir.png", "mir_active.png"));
                } else if (img.getId().equals("mSeqLogicImg")) {
                    dataPathImages.put((ImageView) img, getImgArray("m-seq.png", "m-seq_active.png"));
                }
            }
        } catch (NullPointerException e) {
            System.out.println("NullPointerException in bindImageViews method");
            e.printStackTrace();
        }
    }

    private Image[] getImgArray(String ... args) {
        // This code shouldn't throw NPE bcs the resources are present
        // Warnings are ignored bcs the fix with Objects.requireNonNull method doesn't really do much
        // except that it throws NPE instead of Image constructor
        Image[] result = new Image[args.length];
        String imgPath = "/img/datapath/";
        for (int i = 0; i < args.length; i++) {
            result[i] = new Image(getClass().getResourceAsStream(imgPath + args[i]));
        }
        return result;
    }

    private void changeRadix(int radix) {
        int oldRadix = NumericFactory.getRadix();
        NumericFactory.setRadix(radix);
        changeRadixInTables();
        changeRadixInStaticFields(oldRadix);
        updateToolTips();
    }

    private void changeRadixInStaticFields(int oldRadix) {
        MPCField.setText(NumericFactory.getStringValue8(cpu.MPCProperty().getValue().shortValue()));
        MIRField.setText(NumericFactory.getStringValue32(cpu.MIRProperty().get()));
        MARField.setText(NumericFactory.getStringValue16(cpu.MARProperty().getValue().shortValue()));
        MBRField.setText(NumericFactory.getStringValue16(cpu.MBRProperty().getValue().shortValue()));
        String memValue = searchedAddressValueField.getText();
        if (!memValue.isEmpty())
            searchedAddressValueField.setText(
                    NumericFactory.getStringValue16(NumericFactory.getShortValue(memValue, oldRadix)));
    }

    private void changeRadixInTables() {
        for (Register r : cpu.getRegisters())
            r.setValue(r.getValue());
        for (MemoryLine m : cpu.getMemory().getMemory())
            m.setValue(m.getValue());
    }

    public void runCodeAction() {
        try {
            short[] machineCode = codeParser.parseCode(codeArea.getText());
            console.setText("Code assembled successfully");
            cpu.setCPUInitial();
            cpu.getMemory().write(machineCode);
            codeArea.setEditable(false);
            btnRun.setDisable(true);
            btnNextClock.setDisable(false);
            btnNextSubClock.setDisable(false);
            microcodeArea.setScrollTop(0);
            microcodeArea.setStyle("-fx-highlight-fill: #ADFF2F; -fx-highlight-text-fill: #000000");
            microcodeArea.selectRange(microCodeLinesLengths[0][0], microCodeLinesLengths[0][1]);
            updateToolTips();
            updateImgColors();
        } catch (CodeParserException e) {
            console.setText(e.getMessage());
        }
    }

    public void runClockCycleAction() {
        cpu.runCycle();
        updateToolTips();
        updateImgColors();
    }

    public void runSubClockCycleAction() {
        cpu.runSubCycle();
        updateToolTips();
        updateImgColors();
    }

    private void updateImgColors() {
        for (ImageView img : dataPathImages.keySet()) {
            img.setImage(dataPathImages.get(img)[cpu.getComponentImg(img.getId())]);
        }
    }

    private void updateToolTips() {
        for (ImageView img : toolTips.keySet()) {
            toolTips.get(img).getKey().setText(toolTips.get(img).getValue().apply(img.getId()));
        }
    }

    public void searchMemoryAction() {
        try {
            String addressString = memorySearchField.getText();
            if (addressString.isEmpty())
                return;
            int address = Integer.parseInt(addressString);
            if (address < 0 || address > 4095)
                throw new NumberFormatException("out of bounds");
            searchedAddressValueField.setText(NumericFactory.getStringValue16(cpu.getMemory().read((short) address)));
            memorySearchField.setStyle(null);
        } catch (NumberFormatException e) {
            memorySearchField.setStyle("-fx-border-color: red;");
        }
    }

    public void newFileAction() {
        if (codeArea.getText().isEmpty()) {
            console.setText("");
            return;
        }

        Optional<ButtonType> selectedOption = confirmationAlertShowAndWait();
        if (!btnRun.isDisabled()) {
            if (selectedOption.isPresent() && selectedOption.get() == ButtonType.OK) {
                codeArea.clear();
                console.setText("");
            }
        } else {
            if (selectedOption.isPresent() && selectedOption.get() == ButtonType.OK)
                reinitialiseAppState();
        }
    }

    private Optional<ButtonType> confirmationAlertShowAndWait() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure you want to create a new code file?");
        alert.setContentText("Current progress will be lost!");
        return alert.showAndWait();
    }

    private void reinitialiseAppState() {
        cpu.setCPUInitial();
        codeArea.setEditable(true);
        codeArea.clear();
        microcodeArea.setScrollTop(0);
        microcodeArea.selectRange(0, 0);
        microcodeArea.setStyle(null);
        console.setText("");
        btnRun.setDisable(false);
        btnNextClock.setDisable(true);
        btnNextSubClock.setDisable(true);
        instructionStatusLabel.setText("");
        updateToolTips();
        updateImgColors();
        tabPane.getSelectionModel().select(codeTab);
    }
}
