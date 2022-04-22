package sample;

import backend.ObservableResourceFactory;
import com.sun.javafx.tk.Toolkit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Host h = Host.getInstance();
        h.setHostServices(getHostServices());

        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/img/app_icon.png")));
        ObservableResourceFactory resourceFactory = ObservableResourceFactory.getInstance();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"), resourceFactory.getResources());
        primaryStage.setTitle("MIC-1 Simulator");
        primaryStage.setScene(new Scene(root, 1280, 960));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
