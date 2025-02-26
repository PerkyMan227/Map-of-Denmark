module hesse.example.mapofdenmark {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;


    opens hesse.example.mapofdenmark to javafx.fxml;
    exports hesse.example.mapofdenmark;
}