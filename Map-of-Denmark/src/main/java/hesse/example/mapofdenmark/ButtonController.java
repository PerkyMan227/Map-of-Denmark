package hesse.example.mapofdenmark;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

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
}
