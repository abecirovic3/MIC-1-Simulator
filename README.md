<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/abecirovic3/MIC-1-Simulator">
    <img src="https://i.imgur.com/6TMr36U.png" alt="Logo" width="100" height="100">
  </a>

  <h3 align="center"><b>MIC-1 Simulator</b></h3>

  <p align="center">
    MIC-1 is a simple simulator for the MIC-1 CPU, which was originally described in Andrew S. Tanenbaum’s textbook Structured Computer Organization
</p>

<hr>

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#run-the-app">Run the app</a></li>
        <li><a href="#run-the-light-jar">Run the cross-platform light .jar</a></li>
        <li><a href="#run-the-fat-jar">Run the Windows/Linux fat .jar</a></li>
      </ul>
    </li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#credits">Credits</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About The Project

<br />

<img src="https://i.imgur.com/Q9fczrv.png" alt="Screenshot" />

<br />

MIC-1 Simulator is an interactive simulator for the MIC-1 CPU. The application is primarily intended to be used by the students of the Faculty of Electrical Engineering in Sarajevo, but everyone is welcome to use it. The idea is that the user, while using the application, gains a better understanding of the principles behind the MIC-1 CPU. The user is able to write code using the supported instruction set, to run the code and to see it being executed through the CPU cycles and/or sub-cycles. While the instructions are being executed the user can keep track of the memory state, control memory state, CPU registers and the different CPU components.

### Built With

Technologies used:
* JavaFX
* CSS

<!-- GETTING STARTED -->
## Getting Started

To run the application follow the steps below.

### Prerequisites

1. First you'll need to install java version 11 or later. Download JDK 11 [here](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).
2. Download JavaFX 17 or later [here](https://gluonhq.com/products/javafx/). [optional, needed for cross-platform light .jar]

### Run the app

To run the application you will need to have Java installed, as mentioned in the <a href="#prerequisites">Prerequisites</a> section. The preferred version is 11 since the app was built using this version.

Windows and Linux users have the option to download fat .jar files which include the needed JavaFX libraries. To run the light .jar file you will need to download JavaFX as explained in the <a href="#prerequisites">Prerequisites</a> section.

### Run the light .jar

To run the light .jar (cross-platform) file follow these steps:

1. Go to [Releases](https://github.com/abecirovic3/MIC-1-Simulator/releases)
2. Choose the latest release and download the MIC-1-Simulator.jar file
3. Open up terminal and change directory to where you've downloaded the MIC-1-Simulator.jar file. For example:
   ```sh
   cd C:\Users\UserName\Downloads
   ```
4. Run the folowing command:
   ```sh
   java --module-path %PATH-TO-JAVAFX-LIB% --add-modules javafx.controls,javafx.fxml -jar MIC-1-Simulator.jar
   ```
   Change `%PATH-TO-JAVAFX-LIB%` to the path of the lib directory in the javafx-sdk directory which you've downloaded earlier. For example:
   ```sh
   java --module-path C:\Users\UserName\Downloads\openjfx-17.0.1_windows-x64_bin-sdk\javafx-sdk-17.0.2\lib --add-modules javafx.controls,javafx.fxml -jar MIC-1-Simulator.jar
   ```

### Run the fat .jar

On Windows and Linux you can choose to run the fat .jar file which comes with the needed JavaFX libraries included. To do so follow these steps:

1. Go to [Releases](https://github.com/abecirovic3/MIC-1-Simulator/releases)
2. Choose the latest release and download the MIC-1-Simulator_YOUR-OS.jar file
3. Open up terminal and change directory to where you've downloaded the MIC-1-Simulator_YOUR-OS.jar file. For example:
   ```sh
   cd C:\Users\UserName\Downloads
   ```
4. Run the folowing command:
   ```sh
   java -jar MIC-1-Simulator_YOUR-OS.jar
   ```
Note that `YOUR-OS` refers to your operating system, so for Linux download and run the MIC-1-Simulator_Linux.jar, and for Windows MIC-1-Simulator_Windows.jar.

On Windows you should also be able to run the fat .jar file as any other application with a double-click on the downloaded .jar file.

To be able to run the app with a double-click on Linux you will first need to make the downloaded .jar file executable. To do so, follow these steps:

1. Open up terminal and change directory to where you've downloaded the MIC-1-Simulator_Linux.jar file. For example:
   ```sh
   cd ~/Downloads
   ```
2. Make the .jar executable by running the command:
   ```sh
   chmod +x MIC-1-Simulator_Linux.jar
   ```
Now you should be able to run the app with a double-click.

These fat .jar files have been tested on Ubuntu 18.04.6 LTS, and on Windows 10 Version 21H2.

<!-- CONTRIBUTING -->
## Contributing

Feel free to contribute as much as you like. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Branch (`git checkout -b your-branch`)
3. Commit your Changes (`git commit -m 'Add some Amazing Changes'`)
4. Push your Changes (`git push`)
5. Open a Pull Request

Note that the branch naming convention should be as follows `feature-your-feature` for feature branches, and `fix-your-fix` for fix branches.

<!-- LICENSE -->
## License

Distributed under the GNU General Public License, version 3. See `LICENSE` for more information.

<!-- CONTACT -->
## Contact

Ajdin Becirovic - [@Ajdin Becirovic](https://www.facebook.com/ajdin.becirovic.1/) - abecirovic3@etf.unsa.ba

<!-- Credits -->
## Credits

Authors of icons used in the application:
* [Pixel perfect](https://www.flaticon.com/authors/pixel-perfect)
* [iconixar](https://www.flaticon.com/authors/iconixar)
* [Smashicons](https://www.flaticon.com/authors/smashicons)
* [Alfredo Hernandez](https://www.flaticon.com/authors/alfredo-hernandez)
* [Freepik](https://www.flaticon.com/authors/freepik)
* [Chattapat](https://www.flaticon.com/authors/chattapat)
* [riajulislam](https://www.flaticon.com/authors/riajulislam)


[Readme template](https://github.com/othneildrew/Best-README-Template)