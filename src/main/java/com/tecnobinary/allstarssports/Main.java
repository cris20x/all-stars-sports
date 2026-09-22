package main.java.com.tecnobinary.allstarssports;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import main.java.com.tecnobinary.allstarssports.util.SceneManager;

public class Main extends Application {

    private static final String APP_TITLE =
            "All-Stars Sports League";

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle(APP_TITLE);

        Image appIcon =
                new Image(
                        getClass()
                                .getResourceAsStream(
                                        "/main/resources/images/Logo-AllSS.png"
                                )
                );

        stage.getIcons().add(appIcon);

        SceneManager sceneManager =
                new SceneManager(stage);

        sceneManager.showSplashView();
    }

    public static void main(String[] args) {
        launch(args);
    }
}