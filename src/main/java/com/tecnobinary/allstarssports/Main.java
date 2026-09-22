package main.java.com.tecnobinary.allstarssports;

import javafx.application.Application;
import javafx.stage.Stage;
import main.java.com.tecnobinary.allstarssports.util.SceneManager;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        SceneManager sceneManager = new SceneManager(stage);
        sceneManager.showSplashView();
    }

    public static void main(String[] args) {
        launch(args);
    }
}