package backend;

import java.util.Map;

public class InstructionParser {
    private final Map<String, String> supportedInstructions;
    private static InstructionParser instance;

    public static InstructionParser getInstance() {
        if (instance == null) instance = new InstructionParser();
        return instance;
    }

    private InstructionParser() {
        supportedInstructions = FileParser.getSupportedInstructionsMap();
    }

    public Map<String, String> getSupportedInstructions() {
        return supportedInstructions;
    }

    // TODO finish method
    public String getInstructionString(short bytes) {
        short opcode, operand;
        if ((bytes & 0xF000) == 0xF000) {
            opcode = (short) ((bytes & 0xFF00) >> 8);
            // TODO operand
        }
        return null;
    }

    // TODO more methods
}
