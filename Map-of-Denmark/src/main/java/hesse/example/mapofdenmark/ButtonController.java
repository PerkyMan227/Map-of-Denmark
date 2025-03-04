package hesse.example.mapofdenmark;

import javafx.fxml.FXML;
import javafx.scene.paint.Color;

import java.sql.SQLOutput;


public class ButtonController {
    private final Model model;
    private final View view;
    private final UIComponents uiComponents;
    private boolean isDarkMode = false;


    public ButtonController(Model model, View view, UIComponents uiComponents) {
        this.model = model;
        this.view = view;
        this.uiComponents = uiComponents;
    }

    //Metode som kan kaldes på en knap

    @FXML
    public void DarkmodeFunction(){

        if (isDarkMode) {
            view.setBackgroundColor(Color.WHITE);
            uiComponents.setUIBackgroundColor(Color.WHITE, Color.BLACK);
            isDarkMode = false;
        }
        else {
            view.setBackgroundColor(Color.LIGHTGRAY);
            uiComponents.setUIBackgroundColor(Color.LIGHTGRAY, Color.WHITE);
            isDarkMode = true;
        }

        System.out.println("Darkmode function called" + isDarkMode);



    }
}
