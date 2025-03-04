module hesse.example.mapofdenmark {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens hesse.example.mapofdenmark to javafx.fxml;
    exports hesse.example.mapofdenmark;
}