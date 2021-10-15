package backend;
import java.util.Map;

public class CodeParser {
    private final Map<String, String> supportedInstructions;

    public CodeParser() {
        supportedInstructions = FileParser.getSupportedInstructionsMap();
    }

    public String parseCode(String code) {
        String[] codeLines = code.split("\n");
        int lineNumber = 1;
        int blankLinesCounter = 0;
        String errMessage = "Error on line number ";
        for (String line : codeLines) {
            line = line.replaceAll(";.*", ""); // remove comments
            line = line.trim(); // get rid of all leading and trailing blanks
            if (line.equals("")) {
                blankLinesCounter++;
                lineNumber++;
                continue; // blank rows are allowed
            }

            line = line.replaceAll(" +", " "); // replace multiple blanks with one

            String[] instructionElements = line.split(" ");
            if (!supportedInstructions.containsKey(instructionElements[0].toUpperCase()))
                return errMessage + lineNumber + ", unknown instruction mnemonic";

            if (instructionRequiresArgument(instructionElements[0])) {
                if (instructionElements.length < 2)
                    return errMessage + lineNumber + ", missing argument";;
                try {
                    int paramValue = Integer.parseInt(instructionElements[1]);
                    int boundary = 4095;
                    if (instructionElements[0].equalsIgnoreCase("INSP")
                            || instructionElements[0].equalsIgnoreCase("DESP"))
                        boundary = 255;
                    if (paramValue < 0 || paramValue > boundary)
                        return errMessage + lineNumber + ", argument out of bounds";

                } catch (NumberFormatException ex) {
                    return errMessage + lineNumber + ", invalid argument";
                }
                if (instructionElements.length > 2)
                    return errMessage + lineNumber + ", too many arguments";
            } else if (instructionElements.length > 1) {
                return errMessage + lineNumber + ", too many arguments";
            }

            lineNumber++;
        }
        if (blankLinesCounter == codeLines.length)
            return "Error blank code area";
        return "OK";
    }

    private boolean instructionRequiresArgument(String mnemonic) {
        return !(mnemonic.equals("PSHI") || mnemonic.equals("POPI") || mnemonic.equals("PUSH")
                || mnemonic.equals("POP") || mnemonic.equals("RETN") || mnemonic.equals("SWAP"));
    }
}
