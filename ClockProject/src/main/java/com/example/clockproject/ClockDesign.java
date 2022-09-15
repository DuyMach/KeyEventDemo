package com.example.clockproject;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ClockDesign extends Application {
    private static final int TOLERANCE_THRESHOLD = 0xCF;
    @Override
    public void start(Stage stage) throws IOException {
        ClockPane clock = new ClockPane();
        Calendar calendar = GregorianCalendar.getInstance();

        StackPane stackPane = new StackPane();

        InputStream stream = new FileInputStream("Images/clockHandFull.png");
        Image image = new Image(stream);
        Image secondHand = makeTransparent(image);

        ImageView secondHandView = new ImageView(secondHand);
        stackPane.getChildren().addAll(clock,secondHandView);

        Label point = new Label("");
        point.setAlignment(Pos.CENTER);

        BorderPane bp = new BorderPane();
        bp.setCenter(stackPane);
        bp.setBottom(point);

        final double seedSecondDegrees  = calendar.get(Calendar.SECOND) * (60) + 180;
        secondHandView.setRotate(seedSecondDegrees);

        RotateTransition rt = new RotateTransition(Duration.seconds(60),secondHandView);
        rt.setByAngle(360);
        rt.setCycleCount(Timeline.INDEFINITE);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.play();

        EventHandler<ActionEvent> updateClock = e -> {
            clock.setCurrentTime();
        };

        Timeline animation = new Timeline(new KeyFrame(Duration.millis(1000), updateClock));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();

        clock.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                point.setText(mouseEvent.getX() + " " + mouseEvent.getY());
            }
        });


        stage.setScene(new Scene(bp,300,300));
        stage.show();
    }

    private Image makeTransparent(Image inputImage) {
        int W = (int) inputImage.getWidth();
        int H = (int) inputImage.getHeight();
        WritableImage outputImage = new WritableImage(W, H);
        PixelReader reader = inputImage.getPixelReader();
        PixelWriter writer = outputImage.getPixelWriter();
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                int argb = reader.getArgb(x, y);

                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = argb & 0xFF;

                if (r >= TOLERANCE_THRESHOLD
                        && g >= TOLERANCE_THRESHOLD
                        && b >= TOLERANCE_THRESHOLD) {
                    argb &= 0x00FFFFFF;
                }

                writer.setArgb(x, y, argb);
            }
        }

        return outputImage;
    }

    public static void main(String[] args) {
        launch();
    }
}