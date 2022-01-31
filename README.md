# MIC-1-Simulator

### Pre-release notes
To run the pre-release please follow these steps:
1. First install java version 11. Download JDK 11 [here](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).
2. Download JavaFX v17 [here](https://gluonhq.com/products/javafx/).
3. Go to [Releases](https://github.com/abecirovic3/MIC-1-Simulator/releases)
4. Click on Assets for the last pre-release and download the MIC-1-Simulator.jar file
5. Open up terminal and change directory to where you've downloaded the MIC-1-Simulator.jar file. For example:
   ```sh
   cd C:\Users\UserName\Downloads
   ```
6. Run the following command
   ```sh
   java --module-path %PATH-TO-JAVAFX-LIB% --add-modules javafx.controls,javafx.fxml -jar MIC-1-Simulator.jar
   ```
   Change `%PATH-TO-JAVAFX-LIB%` to the path of the lib directory in the javafx-sdk directory which you've downloaded earlier. For example:
   ```sh
   java --module-path C:\Users\UserName\Downloads\javafx-sdk-17.0.1\lib --add-modules javafx.controls,javafx.fxml -jar MIC-1-Simulator.jar
   ```

### Features to be implemented:
- Register and Memory table edit
- Input validation
- Other tooltips (partly done)
- Save file
- Load file
- Translation
- Code examples
- About etc

### Features to be edited:
- Datapath tooltip radix
- Table widths
- Tooltips delay