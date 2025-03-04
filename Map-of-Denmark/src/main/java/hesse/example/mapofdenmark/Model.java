package hesse.example.mapofdenmark;
import java.awt.*;
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
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import javafx.geometry.Point2D;
import org.xml.sax.SAXException;

public class Model implements Serializable{
    List<Line> list = new ArrayList<Line>();
    List<Way> ways = new ArrayList<Way>();

    List<Way> wayResidential = new ArrayList<Way>();
    List<Way> wayFootway = new ArrayList<Way>();
    List<Way> wayCycleway = new ArrayList<Way>();
    List<Way> wayCoast = new ArrayList<Way>();
    List<Way> wayMotorway = new ArrayList<Way>();
    List<Way> wayTrunk = new ArrayList<>();
    List<Way> waySecondary = new ArrayList<>();

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
        ZipFile zipFile = new ZipFile(filename);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            try (InputStream input = zipFile.getInputStream(entry)) {
                parseOSM(input);
            }

        }

        zipFile.close(); // Ensure ZIP file is closed after processing


        /*for(int i = 0; i < 10; i++){
           var input = new ZipInputStream(new FileInputStream(filename));
           //System.out.println(input.available());
           //input.getNextEntry();
           //input.skip(i);
           for (int j = 0; j <= i; j++){
               input.getNextEntry();

           }
           parseOSM(input);
        }
        var input = new ZipInputStream(new FileInputStream(filename));
        input.getNextEntry();
        parseOSM(input);*/
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
        var residential = false;
        var motorway = false;
        var trunk = false;
        var secondary = false;

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
                    cycleway = false;
                    footway = false;
                    residential = false;
                    motorway = false;
                    trunk = false;
                    secondary = false;
                    coastlineNodesTemp.clear();

                } else if (name.equals("tag")) {
                    var v = input.getAttributeValue(null, "v");
                    var k = input.getAttributeValue(null, "k");
                    if (k.equals("natural") && v.equals("coastline")) {
                        coast = true;
                        //System.out.println("coastlineReached");
                        coastlineNodes.addAll(coastlineNodesTemp);
                        coastlineNodesAll.add(coastlineNodesTemp);
                        //System.out.println("coastlineNodesTemp " + coastlineNodesTemp.size());
                        //System.out.println("coastlineNodes " + coastlineNodes.size());
                        coastlineNodesTemp.clear();


                    } else if (k.equals("highway")) {
                        highway = true;
                        if (v.equals("cycleway")) {
                            cycleway = true;
                            //System.out.println("reachedC");
                        } else if (v.equals("footway")) {
                            footway = true;
                        } else if (v.equals("residential")) {
                            residential = true;
                        } else if (v.equals("motorway")) {
                            motorway = true;
                        } else if (v.equals("trunk")) {
                            trunk = true;
                        } else if (v.equals("secondary")) {
                            secondary = true;
                        }
                    }
                }
                if (name.equals("nd")) {
                    //System.out.println("ndReached");
                    var ref = Long.parseLong(input.getAttributeValue(null, "ref"));
                    coastlineNodesTemp.add(ref);

                    if (coast) {
                        //System.out.println("ndReached2");
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
                        //System.out.println("Added");
                    } else if (residential) {
                        wayResidential.add(new Way(way));
                    } else if (coast) {
                        wayCoast.add(new Way(way));
                    } else if (motorway) {
                        wayMotorway.add(new Way(way));
                    } else if (trunk) {
                        wayTrunk.add(new Way(way));
                    } else if (secondary) {
                        waySecondary.add(new Way(way));
                    }
                }
            }
        }
    }
        private void parseTXT(String filename) throws FileNotFoundException {
            File f = new File(filename);
            try (Scanner s = new Scanner(f)) {
                while (s.hasNext()) {
                    list.add(new Line(s.nextLine()));
                }
            }
        }

        public void add(Point2D p1, Point2D p2) {
            list.add(new Line(p1, p2));
        }
}
