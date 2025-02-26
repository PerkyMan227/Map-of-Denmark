package hesse.example.mapofdenmark;
import java.io.Serializable;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
public class Line implements Serializable{
    double x1, y1, x2, y2;
    public Line(String line) {
        String[] coord = line.split(" ");
        x1 = Double.parseDouble(coord[1]);
        y1 = Double.parseDouble(coord[2]);
        x2 = Double.parseDouble(coord[3]);
        y2 = Double.parseDouble(coord[4]);
    }

    public Line(Point2D p1, Point2D p2) {
        x1 = p1.getX();
        y1 = p1.getY();
        x2 = p2.getX();
        y2 = p2.getY();
    }

    public void draw(GraphicsContext gc) {
        gc.beginPath();
        gc.moveTo(x1, y1);
        gc.lineTo(x2, y2);
        gc.stroke();
    }

}
