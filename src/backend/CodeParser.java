package backend;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CodeParser {
    private static CodeParser instance;
    private final InstructionParser instructionParser;

    public static CodeParser getInstance() {
        if (instance == null) instance = new CodeParser();
        return instance;
    }

    private CodeParser() {
        instructionParser = InstructionParser.getInstance();
    }

    public short[] parseCode(String code) throws CodeParserException {
        Map<String, Integer> labels = new HashMap<>();

        String[] codeLines = code.split("\n");

        purifyCode(codeLines, labels); // can throw error for recurring label name

        short[] machineCode = new short[2048];
        Arrays.fill(machineCode, (short)0x7000);

        int lineNumber = 1;
        int blankLinesCounter = 0;
        int memAddress = 0;
        String opcodeBinaryString;
        String argumentBinaryString;

        for (String line : codeLines) {
            if (memAddress > 2047)
                return machineCode;

            if (line.equals("")) {
                blankLinesCounter++;
                lineNumber++;
                continue; // blank rows are allowed
            }

            String[] instructionElements = line.split(" ");

            if (!instructionParser.instructionIsSupported(instructionElements[0]))
                throw new CodeParserException(lineNumber + "=unknown-instr-mnemonic");

            argumentBinaryString = "00000000";

            if (instructionParser.instructionRequiresArgument(instructionElements[0])) {
                if (instructionElements.length < 2)
                    throw new CodeParserException(lineNumber + "=missing-arg");
                try {
                    if (instructionParser.instructionIsJump(instructionElements[0])) {
                        try {
                            Integer.parseInt(instructionElements[1]);
                        } catch (NumberFormatException ex) {
                            if (!labels.containsKey(instructionElements[1]))
                                throw new CodeParserException(lineNumber + "=unknown-label");
                            instructionElements[1] = String.valueOf(labels.get(instructionElements[1]));
                        }
                    }
                    int paramValue = Integer.parseInt(instructionElements[1]);
                    int boundary = 4095;
                    if (instructionElements[0].equalsIgnoreCase("INSP")
                            || instructionElements[0].equalsIgnoreCase("DESP"))
                        boundary = 255;
                    if (paramValue < 0 || paramValue > boundary)
                        throw new CodeParserException(lineNumber + "=arg-out-of-bounds");

                    argumentBinaryString = Integer.toBinaryString(paramValue);

                } catch (NumberFormatException ex) {
                    throw new CodeParserException(lineNumber + "=invalid-arg");
                }
                if (instructionElements.length > 2)
                    throw new CodeParserException(lineNumber + "=too-many-args");
            } else if (instructionElements.length > 1) {
                throw new CodeParserException(lineNumber + "=too-many-args");
            }

            opcodeBinaryString = instructionParser.getOpcodeBytesString(instructionElements[0]);

            addInstructionToAssembledInstructions(machineCode, memAddress, opcodeBinaryString, argumentBinaryString);

            lineNumber++;
            memAddress++;
        }
        if (blankLinesCounter == codeLines.length)
            throw new CodeParserException("no-code-err");
        return machineCode;
    }

    private void addInstructionToAssembledInstructions(short[] machineCode, int memAddress, String opcodeBinaryString, String argumentBinaryString) {
        String format = "%" + (16 - opcodeBinaryString.length()) + "s";
        argumentBinaryString = String.format(format, argumentBinaryString).replace(' ', '0');
        String binaryInstruction = opcodeBinaryString + argumentBinaryString;
        machineCode[memAddress] = (short) Integer.parseInt(binaryInstruction, 2);
    }

    // As I couldn't think of a better name
    // This method removes all labels and comments from code, and removes all unnecessary blanks
    private void purifyCode(String[] codeLines, Map<String, Integer> labels) throws CodeParserException {
        int memoryAddress = 0;
        for (int i = 0; i < codeLines.length; i++) {
            codeLines[i] = codeLines[i].replaceAll(";.*", ""); // remove comments
            codeLines[i] = codeLines[i].trim(); // remove all leading and trailing blanks
            codeLines[i] = codeLines[i].replaceAll(" +", " "); // replace multiple blanks with one

            if (codeLines[i].equals("")) continue;

            String[] elements = codeLines[i].split(" ");
            if (elements.length >= 1 && elements[0].endsWith(":")) {
                String newLabel = elements[0].substring(0, elements[0].length()-1);
                if (labels.containsKey(newLabel))   // recurring label
                    throw new CodeParserException((i + 1) + "=recurring-label");

                labels.put(newLabel, memoryAddress);

                // remove label
                // we don't increment the mem address, bcs this line is only a label
                if (elements.length > 1) {
                    codeLines[i] = codeLines[i].replace(newLabel + ": ", "");
                    i--;    // if there are multiple labels on one line
                }
                else {
                    codeLines[i] = codeLines[i].replace(newLabel + ":", "");
                }
                continue;
            }
            memoryAddress++;
        }
    }
}
