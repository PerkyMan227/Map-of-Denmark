module hesse.example.mapofdenmark {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens hesse.example.mapofdenmark to javafx.fxml;
    exports hesse.example.mapofdenmark;
}