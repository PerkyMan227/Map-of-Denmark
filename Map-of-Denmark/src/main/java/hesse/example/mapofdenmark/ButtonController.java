package hesse.example.mapofdenmark;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.SVGPath;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class ButtonController {
    private final Model model;
    private final View view;

    public ButtonController(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    //Metode som kan kaldes på en knap
    @FXML
    public void handleButtonAction() {
        System.out.println("testButton clicked");
    }
    /*
    public Button settingsButton() {
        Image svgDarkmode = new Image("resources/hesse/example/mapofdenmark/svgIcons/Dark-Theme-Icon.svg");
        ImageView imageView = new ImageView(svgDarkmode);
        imageView.setFitWidth(20); //Resize icon
        imageView.setFitHeight(20);

        // creates a button with svg image
        Button svgButton = new Button("", imageView);
        svgButton.setStyle("-fx-background-color: transparent; -fx-padding: 2;");
        //svgButton.setOnAction(e -> "noget action halløj"));
        return svgButton;
    }*/
}
