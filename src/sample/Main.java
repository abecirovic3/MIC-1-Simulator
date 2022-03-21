package sample;

import backend.ObservableResourceFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
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
