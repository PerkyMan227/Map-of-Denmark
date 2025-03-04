package hesse.example.mapofdenmark;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javafx.scene.*;
import java.io.IOException;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import org.xml.sax.SAXException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, XMLStreamException, ClassNotFoundException, ParserConfigurationException, SAXException {


        String filename = "data/small.osm";
        var model = Model.load(filename);
        var view = new View(model, stage);

        //Ting der skal være i vores stage


        //Box
        Rectangle box = new Rectangle(250, 600);
        box.setFill(Color.WHITE);
        box.setStroke(Color.DARKGRAY);
        AnchorPane.setLeftAnchor(box, 0d);
        AnchorPane.setTopAnchor(box, 0d);

        //Bund box
        Rectangle bottomBox = new Rectangle(1360,20);
        AnchorPane.setBottomAnchor(bottomBox, 0.0);
        AnchorPane.setLeftAnchor(bottomBox, 0.0);
        AnchorPane.setRightAnchor(bottomBox, 0.0);
        bottomBox.setFill(Color.WHITE);
        bottomBox.setStroke(Color.DARKGRAY);




        /*Directions knappen
        Button directionsButton = new Button("Directions");
        directionsButton.setMinSize(110, 20);
        AnchorPane.setTopAnchor(directionsButton, 0.0);
        AnchorPane.setLeftAnchor(directionsButton, 0.0);

        //Find knappen
        Button findButton = new Button("Find");
        findButton.setMinSize(110, 20);
        AnchorPane.setTopAnchor(findButton, 0.0);
        AnchorPane.setLeftAnchor(findButton, 0.0);

         */


        view.addOverlayControl(box, bottomBox);


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