package hesse.example.mapofdenmark;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, XMLStreamException, ClassNotFoundException {
        String filename = "data/map3.osm.zip";
        var model = Model.load(filename);
        var view = new View(model, stage);
        new Controller(model, view);
    }

    public static void main(String[] args) {
        launch();
    }
}