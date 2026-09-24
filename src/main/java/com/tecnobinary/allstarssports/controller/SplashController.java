package main.java.com.tecnobinary.allstarssports.controller;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Random;

public class SplashController {

    @FXML
    private ImageView imgLogo;

    @FXML
    private Label lblAutor;

    @FXML
    private Pane confettiPane;

    private final Random random = new Random();

    @FXML
    public void initialize() {

        // Cargar el logo
        Image logo = new Image(
                getClass().getResourceAsStream(
                        "/main/resources/images/Logo-AllSS.png"
                )
        );

        imgLogo.setImage(logo);

        // El texto 
        lblAutor.setOpacity(1.0);

        // Confeti
        crearConfeti();
    }

    private void crearConfeti() {

        // Centro de la pantalla
        double centroX = 300;
        double centroY = 180;

        // cantidad de confeti
        int cantidad = 360;

        Color[] colores = {
            Color.RED,
            Color.YELLOW,
            Color.CYAN,
            Color.LIME,
            Color.ORANGE,
            Color.HOTPINK,
            Color.WHITE,
            Color.DEEPSKYBLUE
        };

        for (int i = 0; i < cantidad; i++) {


            Rectangle confeti = new Rectangle(
                    4 + random.nextDouble() * 3,
                    9 + random.nextDouble() * 7
            );

            confeti.setFill(
                    colores[random.nextInt(colores.length)]
            );

            confeti.setArcWidth(1);
            confeti.setArcHeight(1);

            confeti.setLayoutX(centroX);
            confeti.setLayoutY(centroY);

            confettiPane.getChildren().add(confeti);


            double angulo = Math.toRadians(
                    (360.0 / cantidad) * i
            );


            double radioX = 180 + random.nextDouble() * 220;
            double radioY = 130 + random.nextDouble() * 150;

            double circuloX =
                    centroX + Math.cos(angulo) * radioX;

            double circuloY =
                    centroY + Math.sin(angulo) * radioY;


            double movimientoLateral =
                    (random.nextDouble() - 0.5) * 120;

            TranslateTransition explosion =
                    new TranslateTransition(
                            Duration.millis(500),
                            confeti
                    );

            explosion.setFromX(0);
            explosion.setFromY(0);

            explosion.setToX(
                    circuloX - centroX
            );

            explosion.setToY(
                    circuloY - centroY
            );


            TranslateTransition caida =
                    new TranslateTransition(
                            Duration.millis(1500),
                            confeti
                    );

            caida.setFromX(
                    circuloX - centroX
            );

            caida.setFromY(
                    circuloY - centroY
            );

            caida.setToY(
                    circuloY - centroY + 230
            );


            caida.setToX(
                    circuloX - centroX + movimientoLateral
            );

            caida.setInterpolator(
                    Interpolator.EASE_OUT
            );


            RotateTransition giro =
                    new RotateTransition(
                            Duration.millis(1500),
                            confeti
                    );

            giro.setFromAngle(
                    random.nextInt(360)
            );

            giro.setByAngle(
                    random.nextBoolean()
                            ? 180
                            : -180
            );

            giro.setInterpolator(
                    Interpolator.LINEAR
            );


            FadeTransition aparecer =
                    new FadeTransition(
                            Duration.millis(200),
                            confeti
                    );

            aparecer.setFromValue(0);
            aparecer.setToValue(1);


            FadeTransition desaparecer =
                    new FadeTransition(
                            Duration.millis(900),
                            confeti
                    );

            desaparecer.setFromValue(1);
            desaparecer.setToValue(0);

            desaparecer.setDelay(
                    Duration.millis(1100)
            );


            ParallelTransition explosionCompleta =
                    new ParallelTransition(
                            explosion,
                            aparecer
                    );


            ParallelTransition caidaCompleta =
                    new ParallelTransition(
                            caida,
                            giro,
                            desaparecer
                    );


            SequentialTransition animacion =
                    new SequentialTransition(
                            explosionCompleta,
                            caidaCompleta
                    );


            animacion.setDelay(
                    Duration.millis(
                            random.nextDouble() * 100
                    )
            );

            animacion.play();
        }
    }
}