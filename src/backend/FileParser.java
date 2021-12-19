package backend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileParser {

    public static ObservableList<Map<String, String>> loadSupportedInstructionsTableData() {
        ObservableList<Map<String, String>> items = FXCollections.observableArrayList();

        try {
            BufferedReader csvReader = new BufferedReader(new FileReader("resources/dataFiles/supportedInstructions.csv"));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");

                Map<String, String> rawData = new HashMap<>();
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

    public static ObservableList<Map<String, String>> loadControlMemoryTableData() {
        ObservableList<Map<String, String>> items = FXCollections.observableArrayList();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("resources/dataFiles/controlMemory.txt"));
            String row;
            int i = 0;
            while ((row = reader.readLine()) != null) {
                String[] data = row.split(" ");
                if (data.length != 13) continue;

                Map<String, String> rawData = new HashMap<>();
                rawData.put("address", String.valueOf(i++));
                rawData.put("amux", data[0]);
                rawData.put("cond" , data[1]);
                rawData.put("alu", data[2]);
                rawData.put("sh" , data[3]);
                rawData.put("mbr", data[4]);
                rawData.put("mar", data[5]);
                rawData.put("rd", data[6]);
                rawData.put("wr", data[7]);
                rawData.put("enc", data[8]);
                rawData.put("c", data[9]);
                rawData.put("b", data[10]);
                rawData.put("a", data[11]);
                rawData.put("addr", data[12]);

                items.add(rawData);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Problems with reading the file!");
            e.printStackTrace();
        }

        return items;
    }

    public static String loadMicroCode() {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader micReader = new BufferedReader(new FileReader("resources/dataFiles/microprogram.txt"));
            String row;
            short i = 0;
            String padd = "  ";
            while ((row = micReader.readLine()) != null) {
                if (i >= 10 && i < 100)
                    padd = " ";
                else if (i >= 100)
                    padd = "";
                result.append(padd + i + "|  " + row + "\n");
                i++;
            }
            micReader.close();
        } catch (IOException e) {
            System.out.println("Problems with reading the file!");
            e.printStackTrace();
        }

        return result.toString();
    }

    public static Map<String, String> getSupportedInstructionsMap() {
        Map<String, String> result = new HashMap<>();

        try {
            BufferedReader csvReader = new BufferedReader(new FileReader("resources/dataFiles/supportedInstructions.csv"));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                String[] opcodeLine = data[3].split(" ");
                String opcode = opcodeLine[0]; // get the opcode
                if (opcode.equals("1111"))     // if the opcode starts with 1111 then the next 4 bits are also included
                    opcode += opcodeLine[1];
                result.put(data[0], opcode);
            }
            csvReader.close();
        } catch (IOException e) {
            System.out.println("Problems with reading the file!");
            e.printStackTrace();
        }

        return result;
    }

    public static int[] getControlMemory() {
        int[] result = new int[256];
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("resources/dataFiles/controlMemoryINT.txt"));
            String row;
            int i = 0;
            while ((row = bufferedReader.readLine()) != null) {
                result[i++] = Integer.parseInt(row);
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("Problems with reading the file!");
            e.printStackTrace();
        }
        return result;
    }
}
