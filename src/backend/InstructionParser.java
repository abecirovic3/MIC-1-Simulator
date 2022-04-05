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

    public String getInstructionString(short bytes) {
        short opcode, operand;
        if ((bytes & 0xF000) == 0xF000) {
            opcode = (short) ((bytes & 0xFF00) >> 8);
            operand = (short) (bytes & 0x00FF);
        }
        else {
            opcode = (short) ((bytes & 0xF000) >> 12);
            operand = (short) (bytes & 0x0FFF);
        }

        if (instructionRequiresArgument(getMnemonic(opcode)))
            return getMnemonic(opcode) + " " + String.valueOf(operand);
        return getMnemonic(opcode);
    }

    public String getMnemonic(short opcode) {
        String opcodeString = getOpcodeBytesString(opcode);
        for (String mnemonic : supportedInstructions.keySet()) {
            if (supportedInstructions.get(mnemonic).equals(opcodeString))
                return mnemonic;
        }
        return null;
    }

    public String getOpcodeBytesString(short opcode) {
        StringBuilder result = new StringBuilder();
        short delimiter = 4;
        if ((opcode & 0x00F0) == 0x00F0) {
            delimiter = 8;
        }
        for (short i = 0; i < delimiter; i++) {
            result.insert(0, opcode & 0x0001);
            opcode >>= 1;
        }
        return result.toString();
    }

    public String getOpcodeBytesString(String mnemonic) {
        return supportedInstructions.get(mnemonic.toUpperCase());
    }

    public boolean instructionRequiresArgument(String mnemonic) {
        return !(mnemonic.equalsIgnoreCase("PSHI") || mnemonic.equalsIgnoreCase("POPI")
                || mnemonic.equalsIgnoreCase("PUSH") || mnemonic.equalsIgnoreCase("POP")
                || mnemonic.equalsIgnoreCase("RETN") || mnemonic.equalsIgnoreCase("SWAP"));
    }

    public boolean instructionIsJump(String mnemonic) {
        return mnemonic.equalsIgnoreCase("JPOS") || mnemonic.equalsIgnoreCase("JZER")
                || mnemonic.equalsIgnoreCase("JUMP") || mnemonic.equalsIgnoreCase("JNEG")
                || mnemonic.equalsIgnoreCase("JNZE");
    }

    public boolean instructionIsSupported(String mnemonic) {
        return supportedInstructions.containsKey(mnemonic.toUpperCase());
    }
}
