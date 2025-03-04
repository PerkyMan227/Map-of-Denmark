package hesse.example.mapofdenmark;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

public class View {
    Canvas canvas = new Canvas(1360, 720);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    double x1 = 100;
    double y1 = 100;
    double x2 = 200;
    double y2 = 800;

    Affine trans = new Affine();

    private Color currentBackgroundColor = Color.WHITE;

    //Box hvor du kan sætte noget på
    protected AnchorPane overlayPane;

    //Zoom niveau
    private double zoomlevel = 1.0;
    private DoubleProperty zoomLevelProperty = new SimpleDoubleProperty(zoomlevel);

    Model model;
    public View(Model model, Stage stage) {
        this.model = model;
        this.zoomLevelProperty = new SimpleDoubleProperty(this.zoomlevel);
        stage.setTitle("Draw Lines");


        Pane canvasPane = new Pane(canvas);

        overlayPane = new AnchorPane();
        overlayPane.setPickOnBounds(false);

        StackPane root = new StackPane(canvasPane, overlayPane);

        Scene scene = new Scene(root);


        stage.setScene(scene);
        stage.show();
        redraw();
        pan(-0.56*model.minlon, model.maxlat);
        zoom(0, 0, canvas.getHeight() / (model.maxlat - model.minlat));

    }


    //Metode til at sætte vores knapper ind i views pane/layout
    public void addOverlayControl(Node... control) {
        overlayPane.getChildren().addAll(control);
    }
    void redraw() {
        gc.setTransform(new Affine());
        gc.setFill(currentBackgroundColor);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);
        gc.setLineWidth(1/Math.sqrt(trans.determinant()));
        for (Line line : model.list) {
            line.draw(gc);
        }
        for (Way way : model.ways) {
            way.draw(gc);
        }
    }

    public void setBackgroundColor(Color color) {
        this.currentBackgroundColor = color;
        redraw();
        System.out.println("Background changed");

    }

    void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        redraw();

    }

    void zoom(double dx, double dy, double factor) {
        pan(-dx, -dy);
        trans.prependScale(factor, factor);

        setZoomlevel(zoomlevel * factor);

        pan(dx, dy);
        redraw();

    }

    public Point2D mousetoModel(double lastX, double lastY) {
        try {
            return trans.inverseTransform(lastX, lastY);
        } catch (NonInvertibleTransformException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }

    }
    public double getZoomlevel() {
        return this.zoomlevel;
    }

    public void setZoomlevel(double zoom) {
        this.zoomlevel = zoom;
        this.zoomLevelProperty.setValue(zoom);


        // Ensure the property is updated
        Platform.runLater(() -> {
            this.zoomLevelProperty.setValue(zoom);

        });
    }
    public DoubleProperty zoomLevelProperty() {
        return this.zoomLevelProperty;
    }
}

