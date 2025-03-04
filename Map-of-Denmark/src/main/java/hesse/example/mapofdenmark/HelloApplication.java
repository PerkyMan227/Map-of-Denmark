package hesse.example.mapofdenmark;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.xml.stream.XMLStreamException;
import javafx.scene.*;
import java.io.IOException;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, XMLStreamException, ClassNotFoundException {


        String filename = "data/small.osm";
        var model = Model.load(filename);
        var view = new View(model, stage);

        UIComponents UI = new UIComponents(view, model);
        UI.CreatedAndAddOverlayComponents();


        new Controller(model, view);



        /*Implementation af knapper:

        //Setup af controller for knapperne
        ButtonController buttonController = new ButtonController(model, view);

        //Linker testbutton til ButtonController methods
        testButton.setOnAction(event -> buttonController.handleButtonAction());
         */




    }

    public static void main(String[] args) {
        launch();
    }
}