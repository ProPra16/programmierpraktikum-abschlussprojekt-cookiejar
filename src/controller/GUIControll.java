package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.*;
import models.Class;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

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
		File file = catalog.showOpenDialog(new Stage());
		System.out.println(file.getAbsoluteFile());
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Exercises.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Exercises exercises = (Exercises) jaxbUnmarshaller.unmarshal(file);
			test(exercises);
		} catch (JAXBException e){
			System.out.println("[GUIC] Unmarshalling went wrong. This could have different causes:");
			System.out.println("[GUIC] 1. Wrong file chosen.");
			System.out.println("[GUIC] 2. Something in your file is not right.");
			System.out.println("[GUIC] 3. You didn't chose a file, then ignore this.");
		}
	}

	public void test(Exercises exercises){
		for(Exercise e : exercises.getExercise()){
			for(Class c : e.classes.getClasses()){
				System.out.println(c.getClassEx());
			}
			for(Test t : e.tests.getTests()){
				System.out.println(t.getTestEx());
			}
		}
	}
}
