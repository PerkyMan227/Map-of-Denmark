package hesse.example.mapofdenmark;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Model implements Serializable{
    List<Line> list = new ArrayList<Line>();
    List<Way> ways = new ArrayList<Way>();

    List<Way> wayResidential = new ArrayList<Way>();
    List<Way> wayFootway = new ArrayList<Way>();
    List<Way> wayCycleway = new ArrayList<Way>();
    List<Way> wayCoast = new ArrayList<Way>();
    List<Way> wayWater = new ArrayList<Way>();

    List<Long> coastlineNodes = new ArrayList<>();
    ArrayList<Long> coastlineNodesTemp = new ArrayList<>();
    List<ArrayList<Long>> coastlineNodesAll = new ArrayList<>();

    static HashMap<Long, Node> id2node = new HashMap<Long, Node>();

    static Canvas canvas;
    Paint color;
    public static String savedName;


    double minlat, maxlat, minlon, maxlon;
    static Model load(String filename) throws FileNotFoundException, IOException, ClassNotFoundException, XMLStreamException, FactoryConfigurationError, ParserConfigurationException, SAXException {


        if (filename.endsWith(".obj")) {
            try (var in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
                return (Model) in.readObject();
            }
        }
        return new Model(filename);
    }


    public Model(String filename) throws XMLStreamException, FactoryConfigurationError, IOException, ParserConfigurationException, SAXException {
        if (filename.endsWith(".osm.zip")) {
            parseZIP(filename);
        } else if (filename.endsWith(".osm")) {
            parseOSM(filename);
        } else {
            parseTXT(filename);
        }
        save(filename+".obj");
    }

    void save(String filename) throws FileNotFoundException, IOException {
        try (var out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        }
    }

    private void parseZIP(String filename) throws IOException, XMLStreamException, FactoryConfigurationError, ParserConfigurationException, SAXException {
        var input = new ZipInputStream(new FileInputStream(filename));
        input.getNextEntry();
        parseOSM(input);
    }

    private void parseOSM(String filename) throws IOException, XMLStreamException, FactoryConfigurationError, ParserConfigurationException, SAXException {
        parseOSM(new FileInputStream(filename));
    }

    private void parseOSM(InputStream inputStream) throws IOException, XMLStreamException, FactoryConfigurationError, ParserConfigurationException, SAXException {
        var input = XMLInputFactory.newInstance().createXMLStreamReader(new InputStreamReader(inputStream));
        var way = new ArrayList<Node>();
        var coast = false;
        var highway = false;
        var footway = false;
        var cycleway = false;
        var water = false;
        var residential = false;

        // Remove DOM parsing (DocumentBuilder) and XML document parsing
        // DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        // DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        // Document doc = dBuilder.parse((InputStream) input);  <-- This should be removed

        while (input.hasNext()) {
            var tagKind = input.next();

            if (tagKind == XMLStreamConstants.START_ELEMENT) {
                var name = input.getLocalName();
                if (name.equals("bounds")) {
                    minlat = Double.parseDouble(input.getAttributeValue(null, "minlat"));
                    maxlat = Double.parseDouble(input.getAttributeValue(null, "maxlat"));
                    minlon = Double.parseDouble(input.getAttributeValue(null, "minlon"));
                    maxlon = Double.parseDouble(input.getAttributeValue(null, "maxlon"));
                } else if (name.equals("node")) {
                    var id = Long.parseLong(input.getAttributeValue(null, "id"));
                    var lat = Double.parseDouble(input.getAttributeValue(null, "lat"));
                    var lon = Double.parseDouble(input.getAttributeValue(null, "lon"));
                    id2node.put(id, new Node(lat, lon));
                } else if (name.equals("way")) {
                    way.clear();
                    coast = false;
                    water = false;
                    cycleway = false;
                    footway = false;
                    residential = false;
                    coastlineNodesTemp.clear();

                } else if (name.equals("tag")) {
                    var v = input.getAttributeValue(null, "v");
                    var k = input.getAttributeValue(null, "k");
                    if (k.equals("natural") && (v.equals("coastline"))) {
                        coast = true;
                        System.out.println("coastlineReached");
                        coastlineNodes.addAll(coastlineNodesTemp);
                        coastlineNodesAll.add(new ArrayList<>(coastlineNodesTemp));
                        System.out.println("coastlineNodesTemp " + coastlineNodesTemp.size());
                        System.out.println("coastlineNodes " + coastlineNodes.size());
                        coastlineNodesTemp.clear();

                    }
                    else if(v.equals("water") || k.equals("water")){
                        water = true;
                    }
                    else if (k.equals("highway")) {
                        highway = true;
                        if (v.equals("cycleway")) {
                            cycleway = true;
                            System.out.println("reachedC");
                        } else if (v.equals("footway")) {
                            footway = true;
                        } else if (v.equals("residential")) {
                            residential = true;
                        }
                    }
                }
                if (name.equals("nd")) {
                    System.out.println("ndReached");
                    var ref = Long.parseLong(input.getAttributeValue(null, "ref"));
                    coastlineNodesTemp.add(ref);

                    if (coast) {
                        System.out.println("ndReached2");
                        coastlineNodes.add(ref);
                    }
                    var node = id2node.get(ref);
                    if (node != null) {
                        way.add(node);
                    }

                }
            } else if (tagKind == XMLStreamConstants.END_ELEMENT) {
                var name = input.getLocalName();
                if (name.equals("way")) {
                    if (footway) {
                        wayFootway.add(new Way(way));
                    } else if (cycleway) {
                        wayCycleway.add(new Way(way));
                        System.out.println("Added");
                    } else if (residential) {
                        wayResidential.add(new Way(way));
                    } else if (coast) {
                        wayCoast.add(new Way(way));
                    }
                    else if(water){
                        wayWater.add(new Way(way));
                    }
                }
            }
        }
    }

    private void parseTXT(String filename) throws FileNotFoundException {
        File f = new File(filename);
        try (Scanner s = new Scanner(f)) {
            while (s.hasNext()) {
                Line l = new Line(s.nextLine());

                list.add(l);


            }
        }
    }

    public void add(Point2D p1, Point2D p2) {
        list.add(new Line(p1, p2));
    }
}
