package hesse.example.mapofdenmark;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
public class Way implements Serializable{
    double[] coords;
    public Way(ArrayList<Node> way) {
        coords = new double[way.size() * 2];
        for (int i = 0 ; i < way.size() ; ++i) {
            var node = way.get(i);
            coords[2 * i] = 0.56 * node.lon;
            coords[2 * i + 1] = -node.lat;
        }
    }

    public void draw(GraphicsContext gc, javafx. scene. paint. Paint color) {
        gc.setStroke(color);
        gc.beginPath();
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2 ; i < coords.length ; i += 2) {
            gc.lineTo(coords[i], coords[i+1]);
        }
        gc.stroke();
    }
}
