package com.example.clockproject;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TransformDemo extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    private static final int TOLERANCE_THRESHOLD = 0xCF;

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Circle circle = new Circle(100);
        circle.setCenterX(150);
        circle.setCenterY(150);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.WHITE);

        Label point = new Label("");

        InputStream stream = new FileInputStream("Images/clockHand.png");
        Image image = new Image(stream);
        Image secondHand = makeTransparent(image);
        ImageView imageView = new ImageView(secondHand);

        imageView.setX(144);
        imageView.setY(150);

        Calendar calendar = GregorianCalendar.getInstance();
        final double seedSecondDegrees  = calendar.get(Calendar.SECOND) * (360 / 60);
        final Rotate secondRotate    = new Rotate(seedSecondDegrees);
        secondRotate.setPivotX(144);
        secondRotate.setPivotY(150);

        Pane pane = new Pane();
        pane.getChildren().addAll(circle,imageView,point);

        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                point.setText(mouseEvent.getX() + " " + mouseEvent.getY());
            }
        });

        EventHandler<ActionEvent> changeSecond = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                imageView.getTransforms().add(secondRotate);

            }
        };

        Timeline secondTime = new Timeline(new KeyFrame(Duration.seconds(60), changeSecond));

        secondTime.setCycleCount(Timeline.INDEFINITE);
        secondTime.play();

        primaryStage.setScene(new Scene(pane, 300,300));
        primaryStage.show();

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
}
