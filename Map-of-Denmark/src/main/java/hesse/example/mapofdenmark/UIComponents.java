package hesse.example.mapofdenmark;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.beans.property.SimpleObjectProperty;

public class UIComponents {
    private View view;
    private Model model;
    private ButtonController buttonController;

    private final SimpleObjectProperty<Color> boxColor = new SimpleObjectProperty<>(Color.WHITE);
    private final SimpleObjectProperty<Color> buttonColor = new SimpleObjectProperty<>(Color.BLACK);

    public UIComponents(View view, Model model) {
        this.view = view;
        this.model = model;
    }
    public void CreatedAndAddOverlayComponents() {
        buttonController = new ButtonController(model, view, this);

        Rectangle bottomBox = CreateBottomBox();

        Rectangle box = CreateUIBox();

        Button settingsButton = settingsButton();

        Text zoomLevel = CreateZoomLevel();
        
        Rectangle adressHoverBox = CreateAdressHoverBox();


        view.addOverlayControl(bottomBox, box, settingsButton, zoomLevel, adressHoverBox);

    }

    private Rectangle CreateBottomBox() {
        Rectangle bottomBox = new Rectangle(1360,26.5);
        AnchorPane.setBottomAnchor(bottomBox, -1.0);
        AnchorPane.setLeftAnchor(bottomBox, 0.0);
        AnchorPane.setRightAnchor(bottomBox, 0.0);
        bottomBox.fillProperty().bind(boxColor);
        bottomBox.setStroke(Color.DARKGRAY);
        return bottomBox;

    }


    private Rectangle CreateUIBox() {
        Rectangle box = new Rectangle(250, 600);
        box.fillProperty().bind(boxColor);
        box.setStroke(Color.DARKGRAY);
        AnchorPane.setLeftAnchor(box, 0d);
        AnchorPane.setTopAnchor(box, 0d);
        return box;
    }
    private Button settingsButton() {
        String svgPathData = "M12,22 C17.5228475,22 22,17.5228475 22,12 C22,6.4771525 17.5228475,2 12,2 C6.4771525,2 2,6.4771525 2,12 C2,17.5228475 6.4771525,22 12,22 Z M12,20.5 L12,3.5 C16.6944204,3.5 20.5,7.30557963 20.5,12 C20.5,16.6944204 16.6944204,20.5 12,20.5 Z";
        SVGPath svgPath = new SVGPath();
        svgPath.setContent(svgPathData);

        svgPath.fillProperty().bind(buttonColor);
        svgPath.setScaleX(1.0);
        svgPath.setScaleY(1.0);

        Button darkmodeButton = new Button();
        darkmodeButton.setStyle("-fx-background-color: transparent; -fx-padding: 5;");
        darkmodeButton.setGraphic(svgPath);

        AnchorPane.setBottomAnchor(darkmodeButton, 0.0);
        AnchorPane.setLeftAnchor(darkmodeButton, 5.0);

        darkmodeButton.setOnAction(event -> buttonController.DarkmodeFunction());

        return darkmodeButton;
    }

    private Text CreateZoomLevel() {
        Text zoomLevel = new Text();
        view.zoomLevelProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(()-> {
                double zoomPercentage = (newValue.doubleValue() / view.getInitialZoom()) * 10.0;
                zoomLevel.setText(String.format("Current Zoom: %.2f%%", zoomPercentage));
            });
        });

        zoomLevel.setText(String.format("Current Zoom: %.2f%%", view.getZoomlevel()));
        zoomLevel.setStyle("-fx-font-size: 14px; -fx-fill: black;");

        AnchorPane.setRightAnchor(zoomLevel, 50.0);
        AnchorPane.setBottomAnchor(zoomLevel, 5.0);

        return zoomLevel;

    }

    private Rectangle CreateAdressHoverBox() {
        Rectangle adressHoverBox = new Rectangle(400, 30);
        adressHoverBox.setFill(Color.WHITE);
        AnchorPane.setTopAnchor(adressHoverBox, 0.0);
        AnchorPane.setRightAnchor(adressHoverBox, 0.0);
        adressHoverBox.setFill(Color.WHITE);
        adressHoverBox.setStroke(Color.DARKGRAY);



        return adressHoverBox;

    }
    public void setUIBackgroundColor(Color newBoxColor, Color newButtonColor) {
        boxColor.set(newBoxColor);
        buttonColor.set(newButtonColor);
    }
}
