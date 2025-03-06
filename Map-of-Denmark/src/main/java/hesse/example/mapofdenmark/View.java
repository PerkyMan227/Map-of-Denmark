package hesse.example.mapofdenmark;

import javafx.stage.Stage;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class View {
    Canvas canvas = new Canvas(1920, 1080);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    double x1 = 100;
    double y1 = 100;
    double x2 = 200;
    double y2 = 800;

    Way simpleWay;

    Affine trans = new Affine();

    Model model;
    public View(Model model, Stage stage) {
        simpleWay = new Way();
        this.model = model;
        stage.setTitle("Draw Lines");
        BorderPane pane = new BorderPane(canvas);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        redraw();
        pan(-0.56*model.minlon, model.maxlat);
        zoom(0, 0, canvas.getHeight() / (model.maxlat - model.minlat));
    }
    void redraw() { //Kører denne flere gange??
        gc.setStroke(Color.BLACK);
        gc.setTransform(new Affine());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);
        gc.setLineWidth(1/Math.sqrt(trans.determinant()));
        for (Line line : model.list) {

            line.draw(gc);
        }
        //for (Way way : model.ways) {
          //  way.draw(gc);
        //}
        int i = 0;
        /*
        for (Way way : model.wayCoast) {
            List<ArrayList<Long>> list = model.coastlineNodesAll;
            way.drawFill(gc, Color.LIGHTBLUE, list);
            System.out.println("lightblue");
            i++;
            System.out.println("waycoast" + i);


        }
        */
        simpleWay.drawPolygon(gc, model.wayCoast);
        simpleWay.drawWater(gc, model.wayWater);
        for (Way way : model.wayCycleway) {
            //gc.setStroke(Color.BLUE);
            way.draw(gc, Color.BLUE);
            System.out.println("blue");
        }
        for (Way way : model.wayFootway) {
            // gc.setStroke(Color.RED);
            way.draw(gc, Color.RED);
            System.out.println("red");

        }
        for (Way way : model.wayResidential) {
            //gc.setStroke(Color.GREEN);
            way.draw(gc, Color.GREEN);
            System.out.println("green");
        }


    }

    void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        redraw();
    }

    void zoom(double dx, double dy, double factor) {
        pan(-dx, -dy);
        trans.prependScale(factor, factor);
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
}
