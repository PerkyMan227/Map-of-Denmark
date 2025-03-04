package hesse.example.mapofdenmark;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class View {
    Canvas canvas = new Canvas(690, 420);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    double x1 = 100;
    double y1 = 100;
    double x2 = 200;
    double y2 = 800;

    Affine trans = new Affine();

    //Box hvor du kan sætte noget på
    protected AnchorPane overlayPane;

    //Zoom niveau
    private double zoomlevel = 1.0;

    Model model;
    public View(Model model, Stage stage) {
        this.model = model;
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
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);
        gc.setLineWidth(1/Math.sqrt(trans.determinant()));
        for (Line line : model.list) {
            line.draw(gc);
        }

        if (zoomlevel > 25000) {
            for (Way way : model.wayResidential) {
                //gc.setStroke(Color.GREEN);
                way.draw(gc, Color.BLACK);
                //System.out.println("green");
            }
            for (Way way : model.wayCycleway) {
                //gc.setStroke(Color.BLUE);
                way.draw(gc, Color.PINK);
                //System.out.println("blue");
            }
        }


       /* for (Way way : model.wayCycleway) {
            //gc.setStroke(Color.BLUE);
            way.draw(gc, Color.BLUE);
            //System.out.println("blue");
        }
        for (Way way : model.wayFootway) {
            // gc.setStroke(Color.RED);
            way.draw(gc, Color.RED);
            //System.out.println("red");

        }
        for (Way way : model.wayResidential) {
            //gc.setStroke(Color.GREEN);
            way.draw(gc, Color.GREEN);
            //System.out.println("green");
        }*/

        for (Way way : model.wayCoast) {
            List<ArrayList<Long>> list = model.coastlineNodesAll;
            //way.drawFill(gc, Color.LIGHTBLUE, list);
            way.draw(gc,Color.BLUE);
            //System.out.println("lightblue");
        }
        for (Way way : model.wayMotorway) {
            way.draw(gc,Color.RED);
        }
        for (Way way : model.wayTrunk){
            way.draw(gc,Color.YELLOW);
        }
        for (Way way : model.waySecondary){
            way.draw(gc,Color.GREEN);
        }
    }

    void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        redraw();

    }

    void zoom(double dx, double dy, double factor) {
        pan(-dx, -dy);
        trans.prependScale(factor, factor);
        zoomlevel *= factor;
        pan(dx, dy);
        System.out.println(zoomlevel);
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
        return zoomlevel;
    }

    public void setZoomlevel(double zoomlevel) {
        this.zoomlevel = zoomlevel;
    }
}
