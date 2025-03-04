package hesse.example.mapofdenmark;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class Ruler extends Canvas {
    private double width;
    private double height;
    private double zoomLevel;
    Canvas rulerCanvas;
    GraphicsContext gc = getGraphicsContext2D();
    Affine affine = new Affine();

    public Ruler(double width, double height, double zoomLevel) {
        super(width, height);
        this.zoomLevel = zoomLevel;
        rulerCanvas = new Canvas(width, height);
        setVisible(true);
        reDraw();

        System.out.println("Ruler Width: " + getWidth());
        System.out.println("Ruler Height: " + getHeight());
        System.out.println("Initial Zoom Level: " + zoomLevel);
    }
    public void reDraw() {
        GraphicsContext gc = getGraphicsContext2D();


        gc.clearRect(0, 0, getWidth(), getHeight());

        gc.setStroke(Color.BLUE);
        gc.setFill(Color.BLUE);
        gc.setLineWidth(1);

        int maxSegments = 20;
        double baseSegmentMeters = calculateBaseSegmentLength();
        double pixelsPerSegment = calculatePixelsPerSegment(baseSegmentMeters);

        for (double i = 0; i <= maxSegments; i += pixelsPerSegment) {
            double x = i * pixelsPerSegment;
            if (x > getWidth()) break;

            gc.strokeLine(x, 0, x, 10); // Draw the tick marks

            // Use integer meters for cleaner labels
            double distanceLabel = i * baseSegmentMeters;
            gc.fillText(String.format("%d m", (int)distanceLabel), x, 20);
        }
        System.out.println("Ruler redrawn - Width: " + getWidth() + ", Height: " + getHeight() +", Segment Length: " + pixelsPerSegment);

    }

    private double calculatePixelsPerSegment(double baseSegmentMeters) {
        return (baseSegmentMeters * getWidth()) / (getWidth() * zoomLevel / 100);
    }

    private double calculateBaseSegmentLength() {
        return 1000 / zoomLevel;
    }

    public void setZoomLevel(Double zoom) {
        this.zoomLevel = zoom;
    }
    private double calculateSegmentLength() {
        // Adjust this method to create meaningful segments based on zoom level
        // For example, create segments that are meaningful at different zoom levels
        double baseSegmentSize = 50; // Base segment size in pixels
        double zoomFactor = 100 / zoomLevel; // Adjust based on your zoom scale

        return Math.max(20, Math.min(100, baseSegmentSize * zoomFactor));
    }
}
