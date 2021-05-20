package backend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileParser {
    
    public FileParser() {}

    public ObservableList<Map<String, Object>> loadSupportedInstructionsTableData() {
        ObservableList<Map<String, Object>> items = FXCollections.<Map<String, Object>>observableArrayList();

        try {
            BufferedReader csvReader = new BufferedReader(new FileReader("resources/dataFiles/supportedInstructions.csv"));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");

                Map<String, Object> rawData = new HashMap<>();
                rawData.put("mnemonic", data[0]);
                rawData.put("instruction" , data[1]);
                rawData.put("meaning", data[2]);
                rawData.put("binaryCode" , data[3]);

                items.add(rawData);
            }
            csvReader.close();
        } catch (IOException e) {
            System.out.println("Problems with reading the file!");
            e.printStackTrace();
        }

        return items;
    }

    public String loadMicroCode() {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader micReader = new BufferedReader(new FileReader("resources/dataFiles/microprogram.txt"));
            String row;
            while ((row = micReader.readLine()) != null) {
                result.append(row + "\n");
            }
            micReader.close();
        } catch (IOException e) {
            System.out.println("Problems with reading the file!");
            e.printStackTrace();
        }

        return result.toString();
    }
}
