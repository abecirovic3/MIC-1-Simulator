package sample;

import backend.CPU;
import backend.CodeExample;
import backend.CodeParser;
import backend.CodeParserException;
import backend.FileParser;
import backend.InstructionParser;
import backend.MemoryLine;
import backend.NumericFactory;
import backend.ObservableResourceFactory;
import backend.Register;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;

public class Controller {
    public TableView<Map<String, String>> supportedInstructionsTable;
    public TableColumn<Map, String> instrMnemonic;
    public TableColumn<Map, String> instrInstruction;
    public TableColumn<Map, String> instrMeaning;
    public TableColumn<Map, String> instrBinaryCode;

    public TableView<Map> controlMemoryTable;
    public TableColumn<Map, String> cmAddressCol;
    public TableColumn<Map, String> cmValueCol;
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

    public TabPane tabPane;
    public Tab codeTab;
    public Tab memoryTab;

    public MenuItem menuItemRun;
    public MenuItem menuItemNextSubClk;
    public MenuItem menuItemNextClk;

    public AnchorPane dataPathPane;
    public ImageView registersImg;
    private final Map<ImageView, Pair<Tooltip, Function<String, String>>> toolTips = new HashMap<>();

    private final Map<ImageView, Image[]> dataPathImages = new HashMap<>();

    private final CodeParser codeParser = CodeParser.getInstance();
    private final InstructionParser instructionParser = InstructionParser.getInstance();
    public Menu fileMenu;
    public MenuItem newMenuitem;
    public MenuItem openMenuItem;
    public MenuItem saveMenuItem;
    public Menu examplesMenu;
    public MenuItem simpleAdderExampleMenuItem;
    public MenuItem nthFibNumExampleMenuItem;
    public MenuItem exitMenuItem;
    public Menu executeMenu;
    public Menu helpMenu;
    public MenuItem aboutMenuItem;
    public Tooltip newFileTooltip;
    public Tooltip loadFileTooltip;
    public Tooltip saveFileTooltip;
    public Tooltip runCodeTooltip;
    public Tooltip nextSubClockTooltip;
    public Tooltip nextClockTooltip;
    public Tab controlTab;
    public Label controlMemoryLabel;
    public Label clockTitleLabel;
    public Label subClockTitleLabel;
    public Tab datapathTab;
    public Label languageLab;

    public MenuButton radixChoiceMenu;
    public MenuItem decimalRadixItem;
    public MenuItem binaryRadixItem;

    private CPU cpu = new CPU();

    private final int[][] microCodeLinesLengths = new int[256][2];

    private final FileChooser fileChooser = new FileChooser();

    private final SimpleBooleanProperty activeExecutionState = new SimpleBooleanProperty(false);

    private Stage aboutStage = new Stage();

    private final ObservableResourceFactory resourceFactory = ObservableResourceFactory.getInstance();

    public Label supportedInstructionsLab;

    private String errorKey = null;
    private String errorLineNumber = null;

    ObservableList<Map<String, String>> supportedInstructionsList;

    @FXML
    public void initialize() {
        initializeInternationalizationBindings();
        initializeSupportedInstructionsTable();
        initializeControlMemoryTable();
        initializeMicrocodeArea();
        initializeRegistersTable();
        initializeMemoryTable();
        initializeMARAndMBRFields();
        initializeClockGrid();
        initializeMPCField();
        initializeMIRField();
        initializeConsole();
        initializeMemoryTab();
        bindTooltips();
        bindImageViews();
        initializeFileChooser();
        initializeExecutionState();
        initializeAboutStage();
    }

    private void initializeInternationalizationBindings() {
        fileMenu.textProperty().bind(resourceFactory.getStringBinding("file"));
        newMenuitem.textProperty().bind(resourceFactory.getStringBinding("new"));
        openMenuItem.textProperty().bind(resourceFactory.getStringBinding("load"));
        saveMenuItem.textProperty().bind(resourceFactory.getStringBinding("save"));
        examplesMenu.textProperty().bind(resourceFactory.getStringBinding("examples"));
        simpleAdderExampleMenuItem.textProperty().bind(resourceFactory.getStringBinding("simpleAdder"));
        nthFibNumExampleMenuItem.textProperty().bind(resourceFactory.getStringBinding("nthFibNum"));
        exitMenuItem.textProperty().bind(resourceFactory.getStringBinding("exit"));
        executeMenu.textProperty().bind(resourceFactory.getStringBinding("execute"));
        menuItemRun.textProperty().bind(resourceFactory.getStringBinding("run"));
        menuItemNextClk.textProperty().bind(resourceFactory.getStringBinding("nextClk"));
        menuItemNextSubClk.textProperty().bind(resourceFactory.getStringBinding("nextSubClk"));
        helpMenu.textProperty().bind(resourceFactory.getStringBinding("help"));
        aboutMenuItem.textProperty().bind(resourceFactory.getStringBinding("about"));
        newFileTooltip.textProperty().bind(resourceFactory.getStringBinding("newFile"));
        loadFileTooltip.textProperty().bind(resourceFactory.getStringBinding("loadFile"));
        saveFileTooltip.textProperty().bind(resourceFactory.getStringBinding("saveFile"));
        runCodeTooltip.textProperty().bind(resourceFactory.getStringBinding("runCode"));
        nextClockTooltip.textProperty().bind(resourceFactory.getStringBinding("nextClk"));
        nextSubClockTooltip.textProperty().bind(resourceFactory.getStringBinding("nextSubClk"));
        codeTab.textProperty().bind(resourceFactory.getStringBinding("code"));
        controlTab.textProperty().bind(resourceFactory.getStringBinding("control"));
        memoryTab.textProperty().bind(resourceFactory.getStringBinding("memory"));
        datapathTab.textProperty().bind(resourceFactory.getStringBinding("datapath"));
        instrMnemonic.textProperty().bind(resourceFactory.getStringBinding("mnemonic"));
        instrInstruction.textProperty().bind(resourceFactory.getStringBinding("instruction"));
        instrMeaning.textProperty().bind(resourceFactory.getStringBinding("meaning"));
        instrBinaryCode.textProperty().bind(resourceFactory.getStringBinding("binaryCode"));
        console.promptTextProperty().bind(resourceFactory.getStringBinding("console"));
        supportedInstructionsLab.textProperty().bind(resourceFactory.getStringBinding("supportedInstructions"));
        controlMemoryLabel.textProperty().bind(resourceFactory.getStringBinding("controlMemory"));
        cmAddressCol.textProperty().bind(resourceFactory.getStringBinding("address"));
        cmValueCol.textProperty().bind(resourceFactory.getStringBinding("value"));
        memAddress.textProperty().bind(resourceFactory.getStringBinding("address"));
        memValue.textProperty().bind(resourceFactory.getStringBinding("value"));
        memorySearchField.promptTextProperty().bind(resourceFactory.getStringBinding("searchByAddress"));
        regName.textProperty().bind(resourceFactory.getStringBinding("register"));
        regValue.textProperty().bind(resourceFactory.getStringBinding("value"));
        clockTitleLabel.textProperty().bind(resourceFactory.getStringBinding("clock"));
        subClockTitleLabel.textProperty().bind(resourceFactory.getStringBinding("subClock"));
        languageLab.textProperty().bind(resourceFactory.getStringBinding("language"));
        decimalRadixItem.textProperty().bind(resourceFactory.getStringBinding("decimal"));
        binaryRadixItem.textProperty().bind(resourceFactory.getStringBinding("binary"));
    }

    private void initializeAboutStage() {
        aboutStage.setTitle("About MIC-1 Simulator");
        aboutStage.setResizable(false);
    }

    private void initializeExecutionState() {
        activeExecutionState.addListener((o, oldVal, newVal) -> {
            if (newVal) {
                menuItemRun.setDisable(true);
                menuItemNextSubClk.setDisable(false);
                menuItemNextClk.setDisable(false);
                btnRun.setDisable(true);
                btnNextSubClock.setDisable(false);
                btnNextClock.setDisable(false);
            } else {
                menuItemRun.setDisable(false);
                menuItemNextSubClk.setDisable(true);
                menuItemNextClk.setDisable(true);
                btnRun.setDisable(false);
                btnNextSubClock.setDisable(true);
                btnNextClock.setDisable(true);
            }
        });
    }

    private void initializeFileChooser() {
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
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
                instructionStatusLabel.textProperty().bind(resourceFactory.getStringBinding("instr-fetch"));
            }
            if (newVal.intValue() == 3) {
                instructionStatusLabel.textProperty().unbind();
                instructionStatusLabel.
                        setText(resourceFactory.getResources().getString("instr-exec") + ": " +
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
        alert.setTitle(resourceFactory.getResources().getString("error"));
        alert.setHeaderText(resourceFactory.getResources().getString("invalid-new-val"));
        alert.setContentText((identifier.equals("memory") ? "mem" : identifier) + getBoundaryMsg(identifier));
        alert.showAndWait();
    }

    private String getBoundaryMsg(String identifier) {
        if (identifier.equalsIgnoreCase("memory"))
            return ": " + resourceFactory.getResources().getString("valid-range") + " [" + Short.MIN_VALUE + ", " + Short.MAX_VALUE +"].";
        if (isImmutableRegister(identifier))
            return " " + resourceFactory.getResources().getString("immutable-reg") + ".";
        return ": " + resourceFactory.getResources().getString("valid-range") + " [" + getRegisterLowerBound(identifier)
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
            if (activeExecutionState.get())
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
        supportedInstructionsList = FileParser.loadSupportedInstructionsTableData();
        supportedInstructionsTable.setItems(supportedInstructionsList);
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
            console.textProperty().bind(resourceFactory.getStringBinding("successful-assemble"));
            cpu.setCPUInitial();
            cpu.getMemory().write(machineCode);
            codeArea.setEditable(false);
            activeExecutionState.set(true);
            microcodeArea.setScrollTop(0);
            microcodeArea.setStyle("-fx-highlight-fill: #ADFF2F; -fx-highlight-text-fill: #000000");
            microcodeArea.selectRange(microCodeLinesLengths[0][0], microCodeLinesLengths[0][1]);
            updateToolTips();
            updateImgColors();
        } catch (CodeParserException e) {
            String[] errorInfo = e.getMessage().split("=");
            if (errorInfo.length > 1) {
                errorLineNumber = errorInfo[0];
                errorKey = errorInfo[1];
            } else {
                errorLineNumber = null;
                errorKey = errorInfo[0];
            }
            setConsoleErrorText();
        }
    }

    private void setConsoleErrorText() {
        console.textProperty().unbind();
        if (errorLineNumber != null) {
            console.setText(resourceFactory.getResources().getString("line-num-err-message")
                    + errorLineNumber + resourceFactory.getResources().getString(errorKey));
        } else {
            console.setText(resourceFactory.getResources().getString(errorKey));
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
            clearConsole();
            return;
        }

        Optional<ButtonType> selectedOption = confirmationAlertShowAndWait();
        if (!activeExecutionState.get()) {
            if (selectedOption.isPresent() && selectedOption.get() == ButtonType.OK) {
                codeArea.clear();
                clearConsole();
                tabPane.getSelectionModel().select(codeTab);
            }
        } else {
            if (selectedOption.isPresent() && selectedOption.get() == ButtonType.OK)
                reinitialiseAppState();
        }
    }

    private Optional<ButtonType> confirmationAlertShowAndWait() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(resourceFactory.getResources().getString("confirmation"));
        alert.setHeaderText(resourceFactory.getResources().getString("new-code-file") + "?");
        alert.setContentText(resourceFactory.getResources().getString("curr-progress") + "!");
        return alert.showAndWait();
    }

    private void reinitialiseAppState() {
        cpu.setCPUInitial();
        codeArea.setEditable(true);
        codeArea.clear();
        microcodeArea.setScrollTop(0);
        microcodeArea.selectRange(0, 0);
        microcodeArea.setStyle(null);
        clearConsole();
        activeExecutionState.set(false);
        clearInstructionStatusLabel();
        updateToolTips();
        updateImgColors();
        tabPane.getSelectionModel().select(codeTab);
    }

    private void clearInstructionStatusLabel() {
        instructionStatusLabel.textProperty().unbind();
        instructionStatusLabel.setText("");
    }

    private void clearConsole() {
        console.textProperty().unbind();
        console.setText("");
    }

    public void loadFileAction() {
        File selectedFile = fileChooser.showOpenDialog(btnRun.getScene().getWindow());
        if (selectedFile != null) {
            Optional<ButtonType> selectedOption = Optional.of(ButtonType.OK);
            if (!codeArea.getText().isEmpty()) {
                selectedOption = confirmationAlertShowAndWait();
            }
            if (selectedOption.isPresent() && selectedOption.get() == ButtonType.OK) {
                reinitialiseAppState();
                String content = FileParser.readFile(selectedFile);
                codeArea.setText(content);
            }
        }
    }

    public void saveFileAction() {
        File selectedFile = fileChooser.showSaveDialog(btnRun.getScene().getWindow());
        if (selectedFile != null)
            FileParser.writeFile(selectedFile, codeArea.getText());
    }

    public void exitAction() {
        Stage currStage = (Stage) btnRun.getScene().getWindow();
        currStage.close();
    }

    public void loadSimpleAdderAction() {
        loadExample(CodeExample.SIMPLE_ADDER_EXAMPLE);
    }

    public void loadNthFibonacciNum() {
        String comments = CodeExample.NTH_FIB_NUMBER_COMMENTS_EN;
        if (resourceFactory.getResources().getLocale().getLanguage().equals("bs")) {
            comments = CodeExample.NTH_FIB_NUMBER_COMMENTS_BS;
        }
        loadExample(comments + CodeExample.NTH_FIB_NUMBER_EXAMPLE);
    }

    private void loadExample(String example) {
        Optional<ButtonType> selectedOption = Optional.of(ButtonType.OK);
        if (!codeArea.getText().isEmpty())
            selectedOption = confirmationAlertShowAndWait();

        if (selectedOption.isPresent() && selectedOption.get() == ButtonType.OK) {
            reinitialiseAppState();
            codeArea.setText(example);
        }
    }

    public void openAboutAction() {
        AboutController ctrl = new AboutController();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/about.fxml"), resourceFactory.getResources());
        loader.setController(ctrl);

        Parent root;
        try {
            root = loader.load();
            aboutStage.setScene(new Scene(root, 500, 500));
        } catch (IOException e) {
            e.printStackTrace();
        }
        aboutStage.show();
    }

    public void setEnglishLanguageAction() {
        resourceFactory.setResources(ResourceBundle.getBundle("Translation", new Locale("en", "US")));
        applyLanguageChange();
    }

    public void setBosnianLanguageAction() {
        resourceFactory.setResources(ResourceBundle.getBundle("Translation", new Locale("bs", "BA")));
        applyLanguageChange();
    }

    private void applyLanguageChange() {
        if (!console.getText().isEmpty() && !activeExecutionState.get()) {
            setConsoleErrorText();
        }
        if (!instructionStatusLabel.getText().isEmpty()) {
            setInstructionStatusLabelTranslatedText();
        }
        if (NumericFactory.getRadix() == 2) {
            radixChoiceMenu.setText(resourceFactory.getResources().getString("binary"));
        } else {
            radixChoiceMenu.setText(resourceFactory.getResources().getString("decimal"));
        }
        updateSupportedInstructionsTable();
    }

    private void updateSupportedInstructionsTable() {
        for (int i = 0; i < supportedInstructionsList.size(); i++) {
            Map<String, String> updatedMap = new HashMap<>(supportedInstructionsList.get(i));
            updatedMap.put("instruction", resourceFactory.getResources().getString(updatedMap.get("mnemonic")));
            supportedInstructionsList.set(i, updatedMap);
        }
    }

    private void setInstructionStatusLabelTranslatedText() {
        String[] instructionStatusElements = instructionStatusLabel.getText().split(":");
        if (instructionStatusElements.length > 1) {
            instructionStatusLabel.textProperty().unbind();
            instructionStatusLabel.setText(
                    resourceFactory.getResources().getString("instr-exec") +
                            ": " + instructionStatusElements[1]
            );
        }
    }

    public void setDecimalRadix() {
        if (NumericFactory.getRadix() != 10) {
            changeRadix(10);
            radixChoiceMenu.setText(resourceFactory.getResources().getString("decimal"));
        }
    }

    public void setBinaryRadix() {
        if (NumericFactory.getRadix() != 2) {
            changeRadix(2);
            radixChoiceMenu.setText(resourceFactory.getResources().getString("binary"));
        }
    }
}
