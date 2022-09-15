module com.example.clockproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.clockproject to javafx.fxml;
    exports com.example.clockproject;
}