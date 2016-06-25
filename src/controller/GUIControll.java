package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Exercise;
import models.Exercises;

import java.io.File;
import java.lang.reflect.Field;
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
		File file = catalog.showOpenDialog(new Stage());
		//File file = new File("res/catalogs/test.xml"); //Only for testing, can be deleted after development

		System.out.println(file.getAbsoluteFile());

        Exercises exercises = new Exercises();
        List<Exercise> exerciseList = exercises.getExercises(file);
        if (!exerciseList.isEmpty()){
			Exercise e = exerciseList.get(0); //make into ListView
			System.out.println(e.getName());
			System.out.println(e.getDescription());
			for(String s : e.getClasses()){
				textCode.setText(s);
			}
			for(String s : e.getTests()){
				textTest.setText(s);
			}
        }
	}
}
