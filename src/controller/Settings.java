package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.*;

public class Settings {
    private boolean babysteps;
    private boolean acceptanceTest;
    private boolean deleteOnExit;

    private Stage main;

    public Settings() {
        main = new Stage();
        loadSettings();
    }

    public void loadSettings() {
        try {
            File settingsFile = new File("res/settings.xml");
            InputStream is = new FileInputStream(settingsFile);
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(is);

            while(reader.hasNext()){
                if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    if (reader.getLocalName().equals("babysteps"))
                        babysteps = Integer.parseInt(reader.getElementText()) == 1;
                    if (reader.getLocalName().equals("acceptanceTest"))
                        acceptanceTest = Integer.parseInt(reader.getElementText()) == 1;
                    if (reader.getLocalName().equals("deleteOnExit"))
                        deleteOnExit = Integer.parseInt(reader.getElementText()) == 1;
                }
                reader.next();
            }
        }catch(Exception e) {
        }
    }

    public void saveSettings() {
        try {
            File settingsFile = new File("res/settings.xml");
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder b = f.newDocumentBuilder();
            Document doc = b.parse(settingsFile);

            XPath xPath = XPathFactory.newInstance().newXPath();
            Node node = (Node) xPath.compile("/settings/babysteps").evaluate(doc, XPathConstants.NODE);
            node.setTextContent( babysteps?"1":"0");

            xPath = XPathFactory.newInstance().newXPath();
            node = (Node) xPath.compile("/settings/acceptanceTest").evaluate(doc, XPathConstants.NODE);
            node.setTextContent(acceptanceTest?"1":"0");

            xPath = XPathFactory.newInstance().newXPath();
            node = (Node) xPath.compile("/settings/deleteOnExit").evaluate(doc, XPathConstants.NODE);
            node.setTextContent(deleteOnExit?"1":"0");

            Transformer tf = TransformerFactory.newInstance().newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.setOutputProperty(OutputKeys.METHOD, "xml");
            tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource domSource = new DOMSource(doc);
            StreamResult result = new StreamResult(settingsFile);
            tf.transform(domSource, result);
        }catch(Exception e){
            System.out.println("[Settings] Exception: " + e);
        }
    }

    public boolean isBabysteps() {
        return babysteps;
    }

    public boolean isAcceptanceTest() {
        return acceptanceTest;
    }

    public boolean isDeleteOnExit() {
        return deleteOnExit;
    }

    public void start() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane root = fxmlLoader.load(new FileInputStream("res/fxml/Cookiejar-settings.fxml"));
            //controller = (GUIControll) fxmlLoader.getController();

            Scene scene = new Scene(root, 800, 600);
        } catch(Exception e) {}
    }
}
