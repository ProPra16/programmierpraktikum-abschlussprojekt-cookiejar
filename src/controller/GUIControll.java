package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import models.Exercise;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GUIControll{

	@FXML private Button buttonFile;
	@FXML private Button buttonRun;
	@FXML private Button buttonTest;
	@FXML private Button buttonSettings;
	@FXML private TextArea textCode;
	@FXML private TextArea textTest;
	@FXML private TextArea textConsole;
	@FXML private Button buttonCycle;

	/*	Returns element from the field with the given name, as generic type.
	* 	Usage: 	Type t = getElementById("FieldName");
 	* 	E.g.: 	TextArea console = getElementById("textConsole");
	* 	Or: 	((Type)getElementById("FieldName")).someMethod();
	* */
	public <T> T getElementById(String id) {
		try {
			Field field = this.getClass().getDeclaredField(id);
			field.setAccessible(true);
			return (T)field.get(this);
		}
		catch (Exception e) {
			System.out.println("[GUIControll] Exception: " + e);
		}
		return null;
	}
	@FXML
	protected void handleFile(){ //loads File with catalog
		FileChooser catalog = new FileChooser();
		catalog.setTitle("Choose Catalog-File");
		/*catalog.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("XML files (.xml)", ".xml")
		);*/ //Something here does not work with Windows
		//File file = catalog.showOpenDialog(new Stage());
		File file = new File("res/catalogs/test.xml");

		List<Exercise> exercises = new ArrayList<>();
		System.out.println(file.getAbsoluteFile());

		String description;
		List<String> classes = new ArrayList<>();
		List<String> tests = new ArrayList<>();
		try {
            System.out.println(file.length());
            InputStream is = new FileInputStream(file);
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(is);
            Exercise exercise = new Exercise();
            while (reader.hasNext()) {
                if(reader.getEventType() == XMLStreamConstants.START_ELEMENT){
                    if(reader.getLocalName().equals("exercise")){
                        exercise = new Exercise();
                    }
                }
                if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    if (reader.getLocalName().equals("description")) {
                        description = reader.getElementText();
                        exercise.setDescription(description);
                    }
                }
                if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    if (reader.getLocalName().equals("class")) {
                        String temp = reader.getElementText();
                        classes.add(temp);
                    }
                }
                if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    if (reader.getLocalName().equals("test")) {
                        String temp = reader.getElementText();
                        tests.add(temp);
                    }
                }
                if(reader.getEventType() == XMLStreamConstants.END_ELEMENT){
                    if(reader.getLocalName().equals("exercise")){
                        exercise.setClasses(classes);
                        exercise.setTests(tests);
                        exercises.add(exercise);
                    }
                }
                reader.next();
            }

            if (!exercises.isEmpty()){
                for (Exercise e : exercises) {
                    System.out.println(e.getDescription());
                    for(String s : e.getClasses()){
                        System.out.println(s);
                    }
                    for(String s : e.getTests()){
                        System.out.println(s);
                    }
                }
            }
		} catch(FileNotFoundException fnfe){
			System.out.println("This file does not exist.");
		} catch(XMLStreamException xmlse){
			System.out.println();
		} catch(NullPointerException npe){
			System.out.println("The file is broken");
		}
	}


}
