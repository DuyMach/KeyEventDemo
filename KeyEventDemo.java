package keyevent.section9;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class KeyEventDemo extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Pane pane = new Pane();
        Text text = new Text(20,20,"A");

        pane.getChildren().add(text);
        text.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case UP -> text.setY(text.getY() > 10 ? text.getY() - 5 : 10);
                case DOWN -> text.setY(text.getY() < pane.getHeight() ? text.getY() + 10 : pane.getHeight());
            }
        });

        Scene scene = new Scene(pane,300,300);
        stage.setScene(scene);
        stage.setTitle("Key Event Demo");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}