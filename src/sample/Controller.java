package sample;

import backend.FileParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.util.Callback;


import java.util.HashMap;
import java.util.Map;

public class Controller {
    public TableColumn<Map, String> instrMnemonic;
    public TableColumn<Map, String> instrInstruction;
    public TableColumn<Map, String> instrMeaning;
    public TableColumn<Map, String> instrBinaryCode;
    public TableView<Map> supportedInstructionsTable;

    private FileParser fileParser = new FileParser();

    @FXML
    public void initialize() {
        instrMnemonic.setCellValueFactory(new MapValueFactory<>("mnemonic"));
        instrInstruction.setCellValueFactory(new MapValueFactory<>("instruction"));
        instrMeaning.setCellValueFactory(new MapValueFactory<>("meaning"));
        instrBinaryCode.setCellValueFactory(new MapValueFactory<>("binaryCode"));

//        supportedInstructionsTable.setFixedCellSize(Region.USE_COMPUTED_SIZE);

        // Enable multiline text in instrMeaning cells
//        Callback cellFactory = new Callback() {
//            @Override
//            public Object call(Object param) {
//                final TableCell cell = new TableCell() {
//                    private Text text;
//                    @Override
//                    public void updateItem(Object item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (!isEmpty()) {
//                            text= new Text(item.toString());
//                            text.setWrappingWidth(180);
//                            setGraphic(text);
//                        }
//                    }
//                };
//                return cell;
//            }
//        };
//
//        instrMeaning.setCellFactory(cellFactory);

        supportedInstructionsTable.getItems().addAll(fileParser.loadSupportedInstructionsTableData());
    }

//    private ObservableList<Map<String, Object>> loadSupportedInstructionsTableData() {
//        ObservableList<Map<String, Object>> items = FXCollections.<Map<String, Object>>observableArrayList();
//
//        Map<String, Object> rawData = new HashMap<>();
//
//        rawData.put("mnemonic", "LODD");
//        rawData.put("instruction" , "Load directly");
//        rawData.put("meaning", "ac:=m(x)");
//        rawData.put("binaryCode" , "0000 xxxx xxxx xxxx");
//
//        items.add(rawData);
//
//        return items;
//    }
}
