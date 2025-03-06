package hesse.example.mapofdenmark;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;




public class Way implements Serializable{
    double[] coords;
    public Way(ArrayList<Node> way) {
        coords = new double[((way.size())) * 2];
        for (int i = 0 ; i < way.size() ; ++i) {
            var node = way.get(i);
           coords[2 * i] = 0.56 * node.lon;
           coords[2 * i + 1] = -node.lat;
        }


    }

    public Way(){

    }

    public void draw(GraphicsContext gc, Paint color) {
        gc.setStroke(color);
        gc.beginPath();
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2 ; i < coords.length ; i += 2) {
            gc.lineTo(coords[i], coords[i+1]);
        }
        gc.stroke();
        gc.closePath();

    }

    public void drawFill(GraphicsContext gc, Paint color, List<ArrayList<Long>> list) {
        ArrayList<Double> xPoints = new ArrayList<>();
        ArrayList<Double> yPoints = new ArrayList<>();


/*
        for (int i = 0; i < coords.length / 2; ++i) {
            double lon = coords[2 * i];
            double lat = coords[2 * i + 1];

            double x = (lon - 10.0) * 1000;
            double y = (50.0 - lat) * 1000;

            xPoints.add(x);
            yPoints.add(y);
        }




        gc.setFill(Color.GREEN);
        gc.setStroke(Color.PURPLE);
        for(ArrayList<Long> l : list){
            xPoints.clear();
            yPoints.clear();
            for (Long nodeId : l) {
                Node node = Model.id2node.get(nodeId);

                //System.out.println(node.toString());
                //double x = (node.lon - 10.0) * 1000;
                //double y = (50.0 - node.lat) * 1000;

                double x = node.lon + 5.4 - 11;
                double y = (50.0 - node.lat) * 10 + 1;
                xPoints.add(x);
                yPoints.add(y);

                //System.out.println("1size:" + list.size());
                //System.out.println("2size:" + l.size());
            }
            xPoints.add(xPoints.get(0));
            yPoints.add(yPoints.get(0));

            double[] xArray = new double[xPoints.size()];
            for (int i = 0; i < xPoints.size(); i++) {
                xArray[i] = xPoints.get(i);  // Unbox Long to long
            }

            double[] yArray = new double[yPoints.size()];
            for (int i = 0; i < yPoints.size(); i++) {
                yArray[i] = yPoints.get(i);  // Unbox Long to long
            }



            //double[] xArray = xPoints.stream().mapToDouble(d -> d).toArray();
            //double[] yArray = yPoints.stream().mapToDouble(d -> d).toArray();
            gc.beginPath();
            gc.fillPolygon(xArray, yArray, xArray.length);
            gc.strokePolygon(xArray, yArray, xArray.length);


            System.out.println("xArray:" + Arrays.toString(xArray));
            System.out.println("yArray:" + Arrays.toString(yArray));
            System.out.println("xArrayLength:" + xArray.length);
            System.out.println("yArrayLength:" + yArray.length);
            System.out.println();
            System.out.println();



        }



 */
        //coords[coords.length-2] = coords[0];
       // coords[coords.length-1] = coords[1];

        gc.setFill(Color.BLUE);
        gc.setStroke(Color.PURPLE);
        gc.setStroke(color);
        gc.beginPath();
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2 ; i < coords.length ; i += 2) {
            gc.lineTo(coords[i], coords[i+1]);
            System.out.println(coords[i] + " " + coords[i+1]);
        }
        gc.lineTo(coords[0], coords[1]);
        gc.fill();

        gc.stroke();
        gc.closePath();


        double sum = 0;
        for (int i = 0; i < coords.length - 2; i += 2) {
            sum += (coords[i] * coords[i + 3]) - (coords[i + 2] * coords[i + 1]);
        }
        System.out.println("sum" + sum);



/*
        gc.setStroke(color);
        gc.beginPath();
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2 ; i < coords.length ; i += 2) {
            gc.lineTo(coords[i], coords[i+1]);
            System.out.println(coords[i] + " " + coords[i+1]);
        }
        gc.stroke();
        gc.closePath();
*/


        gc.setFill(Color.LIGHTBLUE);
        double[] xPpoints = {1, 100000, 150000, 100000, 1};
        double[] yPpoints = {1, 100000, 50000, 20000, 1};
        int numPoints = xPpoints.length;

        gc.fillPolygon(xPpoints, yPpoints, numPoints);







    }

    public void drawWater(GraphicsContext gc, List<Way> ways) {
        gc.setFill(Color.LIGHTBLUE);

        for (Way way : ways) {
            gc.beginPath();
            gc.moveTo(way.coords[0], way.coords[1]);

            for (int i = 2; i < way.coords.length; i += 2) {
                gc.lineTo(way.coords[i], way.coords[i + 1]);
            }

            gc.closePath();
            gc.fill();
        }
    }

    public void drawPolygon(GraphicsContext gc, List<Way> ways) {
        gc.setFill(Color.BLUE);
        gc.setStroke(Color.BLACK);
        gc.beginPath();

        for (Way way : ways) {
            System.out.println("LON AND LAT: " + way.coords[0] + " " + way.coords[1]);
            generateCoastlinePolygon(gc, way);

            /*
            boolean wasClockwise = way.isClockwise();

            if (wasClockwise) {
                way.reverseOrder();
            }

            System.out.println("Way was " + (wasClockwise ? "Clockwise (Reversed)" : "Counterclockwise (Kept)"));

            System.out.println("Now: " + (way.isClockwise() ? "Clockwise" : "Counterclockwise"));

             */


           // coords[coords.length-2] = coords[0];
           // coords[coords.length-1] = coords[1];

/*
            gc.moveTo(way.coords[0], way.coords[1]);
            for (int i = 2; i < way.coords.length; i += 2) {
                gc.lineTo(way.coords[i], way.coords[i + 1]);
            }
           // gc.lineTo(way.coords[0], way.coords[1]);

            */

        }
        gc.closePath();
        gc.fill();
        gc.stroke();

    }

    private void generateCoastlinePolygon(GraphicsContext gc, Way way) {
        //This is where the magic happens.
        //1. Add the coastline points to the polygon.
        gc.moveTo(way.coords[0], way.coords[1]);
        for (int i = 2; i < way.coords.length; i += 2) {
            gc.lineTo(way.coords[i], way.coords[i + 1]);
        }
        //2. Add points far from the map, to close the polygon.

        gc.lineTo(12.7, 60); //These values will need to be large.
        gc.lineTo(12.5, 60);
        gc.lineTo(12.5, 50);
        gc.lineTo(12.7, 50);
        gc.lineTo(way.coords[0], way.coords[1]);





    }

    public void drawPolygon2(GraphicsContext gc, List<Way> ways) {
        gc.setFill(Color.BLUE);
        gc.setStroke(Color.BLUE);

        // Draw each way as a separate path
        for (Way way : ways) {
            gc.beginPath();
            gc.moveTo(way.coords[0], way.coords[1]);

            for (int i = 2; i < way.coords.length; i += 2) {
                gc.lineTo(way.coords[i], way.coords[i + 1]);
            }

            // Only close if it's a closed way (water polygon)
            // Don't close for coastlines as they might be segments
            if (way.isClosedPolygon()) { // You'll need to implement this check
                gc.closePath();
                gc.fill(); // Fill only closed polygons
            }

            gc.stroke();
        }
    }

    public boolean isClosedPolygon() {
        // A way is closed if the first and last points are the same
        int lastIndex = coords.length - 2;
        return coords[0] == coords[lastIndex] && coords[1] == coords[lastIndex + 1];
    }

    private boolean isClockwise() {
        double sum = 0;
        for (int i = 0; i < coords.length - 2; i += 2) {
            sum += (coords[i] * coords[i + 3]) - (coords[i + 2] * coords[i + 1]);
        }
        return sum < 0; // Negative means clockwise
    }

    private void reverseOrder() {
        for (int i = 0, j = coords.length - 2; i < j; i += 2, j -= 2) {
            double tempX = coords[i], tempY = coords[i + 1];
            coords[i] = coords[j];
            coords[i + 1] = coords[j + 1];
            coords[j] = tempX;
            coords[j + 1] = tempY;
        }
    }


}
