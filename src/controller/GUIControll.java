package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
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
		//File file = catalog.showOpenDialog(new Stage());
		File file = new File("res/catalogs/test.xml");

		System.out.println(file.getAbsoluteFile());

        Exercises exercises = new Exercises();
        List<Exercise> exerciseList = exercises.getExercises(file);
        if (!exerciseList.isEmpty()){
            for (Exercise e : exerciseList) {
                System.out.println(""+e.getName());
                System.out.println(e.getDescription());
                for(String s : e.getClasses()){
                    System.out.println("class");
                    System.out.println(s);
                }
                for(String s : e.getTests()){
                    System.out.println("test");
                    System.out.println(s);
                }
            }
        }
	}
}
