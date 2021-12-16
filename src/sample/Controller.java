package sample;

import backend.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.util.Pair;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    private Map<ImageView, Pair<Image, Image>> dataPathImages = new HashMap<>();
    private Map<String, Pair<String, String>> dataPathImagePaths = new HashMap<>();

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
        bindImageViews();
    }

    private void bindImageViews() {
        String imgPath = "file:resources/img/datapath/";
        for (Node img : dataPathPane.getChildren()) {
            if (img.getId().equals("registersImg")) {
                dataPathImages.put((ImageView) img,
                        new Pair<>(new Image(imgPath+"reg.png"), new Image(imgPath+"reg_active.png")));
            }
            else if (img.getId().equals("aluImg")) {
                dataPathImages.put((ImageView) img,
                        new Pair<>(new Image(imgPath+"alu.png"), new Image(imgPath+"alu_active.png")));
            }
            else if (img.getId().equals("amuxImg")) {
                dataPathImages.put((ImageView) img,
                        new Pair<>(new Image(imgPath+"amux.png"), new Image(imgPath+"amux_active.png")));
            }
            else if (img.getId().equals("aLatchImg")) {
                dataPathImages.put((ImageView) img,
                        new Pair<>(new Image(imgPath+"a-latch.png"), new Image(imgPath+"a-latch_active.png")));
            }
            else if (img.getId().equals("bLatchImg")) {
                dataPathImages.put((ImageView) img,
                        new Pair<>(new Image(imgPath+"b-latch.png"), new Image(imgPath+"b-latch_active.png")));
            }
            else if (img.getId().equals("aDecImg")) {
                dataPathImages.put((ImageView) img,
                        new Pair<>(new Image(imgPath+"a-dec.png"), new Image(imgPath+"a-dec_active.png")));
            }
            else if (img.getId().equals("bDecImg")) {
                dataPathImages.put((ImageView) img,
                        new Pair<>(new Image(imgPath+"b-dec.png"), new Image(imgPath+"a-dec_active.png")));
            }
            else if (img.getId().equals("cDecImg")) {
                dataPathImages.put((ImageView) img,
                        new Pair<>(new Image(imgPath+"c-dec.png"), new Image(imgPath+"c-dec_active.png")));
            }
            else if (img.getId().equals("clockImg")) {
                dataPathImages.put((ImageView) img,
                        new Pair<>(new Image(imgPath+"clock.png"), new Image(imgPath+"clock_active.png")));
            }
            else if (img.getId().equals("shifterImg")) {
                dataPathImages.put((ImageView) img,
                        new Pair<>(new Image(imgPath+"shifter.png"), new Image(imgPath+"shifter_active.png")));
            }
            else if (img.getId().equals("marImg")) {
                dataPathImages.put((ImageView) img,
                        new Pair<>(new Image(imgPath+"mar.png"), new Image(imgPath+"mar_active.png")));
            }
            else if (img.getId().equals("mbrImg")) {
                dataPathImages.put((ImageView) img,
                        new Pair<>(new Image(imgPath+"mbr.png"), new Image(imgPath+"mbr_active.png")));
            }
            else if (img.getId().equals("mMuxImg")) {
                dataPathImages.put((ImageView) img,
                        new Pair<>(new Image(imgPath+"mmux.png"), new Image(imgPath+"mmux_active.png")));
            }
            else if (img.getId().equals("mpcImg")) {
                dataPathImages.put((ImageView) img,
                        new Pair<>(new Image(imgPath+"mpc.png"), new Image(imgPath+"mpc_active.png")));
            }
            else if (img.getId().equals("incImg")) {
                dataPathImages.put((ImageView) img,
                        new Pair<>(new Image(imgPath+"incr.png"), new Image(imgPath+"incr_active.png")));
            }
            else if (img.getId().equals("controlImg")) {
                dataPathImages.put((ImageView) img,
                        new Pair<>(new Image(imgPath+"control.png"), new Image(imgPath+"control_active.png")));
            }
            else if (img.getId().equals("mirImg")) {
                dataPathImages.put((ImageView) img,
                        new Pair<>(new Image(imgPath+"mir.png"), new Image(imgPath+"mir_active.png")));
            }
            else if (img.getId().equals("mSeqLogicImg")) {
                dataPathImages.put((ImageView) img,
                        new Pair<>(new Image(imgPath+"m-seq.png"), new Image(imgPath+"m-seq_active.png")));
            }
        }
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

    public void runCodeAction() {
        try {
            short[] machineCode = codeParser.parseCode(codeArea.getText());
            console.setText("Code assembled successfully");
            cpu.getMemory().write(machineCode);
        } catch (CodeParserException e) {
            console.setText(e.getMessage());
        }
    }

    public void runClockCycleAction() {
        cpu.runCycle();
        updateToolTips();
    }

    public void runSubClockCycleAction() {
        cpu.runSubCycle();
        updateToolTips();
        updateImgColors();
    }

    private void updateImgColors() {
        for (ImageView img : dataPathImages.keySet()) {
            if (cpu.getComponentImg(img.getId()))
                img.setImage(dataPathImages.get(img).getValue());
            else
                img.setImage(dataPathImages.get(img).getKey());
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
