package hesse.example.mapofdenmark;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, XMLStreamException, ClassNotFoundException {

        String filename = "data/small.osm";
        var model = Model.load(filename);
        var view = new View(model, stage);

        //Ting der skal være i vores stage

        //Opretter en Button som tildeles et navn og placering på vores plane
        Button testButton = new Button("Test knap");
        testButton.setLayoutX(20);
        testButton.setLayoutY(20);



        Button testButton2 = new Button("Test knap 2");
        testButton2.setLayoutX(100);
        testButton2.setLayoutY(20);

        //Sætter vores buttons ind i View
        view.addOverlayControl(testButton, testButton2);


        //Implementation af knapper:

        //Setup af controller for knapperne
        ButtonController buttonController = new ButtonController(model, view);

        //Linker testbutton til ButtonController methods
        testButton.setOnAction(event -> buttonController.handleButtonAction());

        new Controller(model, view);


    }

    public static void main(String[] args) {
        launch();
    }
}