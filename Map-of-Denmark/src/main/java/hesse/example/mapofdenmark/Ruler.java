package hesse.example.mapofdenmark;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Ruler extends Canvas {
    private double width;
    private double height;
    private double zoomLevel;
    private Canvas rulerCanvas;

    public Ruler(double width, double height, double zoomLevel) {
        this.width = width;
        this.height = height;
        this.zoomLevel = zoomLevel;
        rulerCanvas = new Canvas(width, height);
        drawRuler();
    }
    public void drawRuler() {
        GraphicsContext gc = rulerCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, rulerCanvas.getWidth(), rulerCanvas.getHeight());

        double rulerSegmentLength = 100 / zoomLevel;

        for (double x = 0; x <= width; x += rulerSegmentLength) {
            gc.strokeLine(x, 0, x, 10); // Draw the tick marks
            gc.fillText(String.format("%.1f", x * zoomLevel / 100), x, 20); // Draw the labels
        }

    }
    private Canvas createRuler() {
        drawRuler();
        return rulerCanvas;
    }
    public void setZoomLevel(double zoomLevel) {
        this.zoomLevel = zoomLevel;
        drawRuler();
    }
    public Canvas getRulerCanvas() {
        return createRuler();
    }
}
