package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Class;
import models.Exercise;
import models.Exercises;
import models.Test;

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
	@FXML private ListView listView;
    @FXML private TabPane tabPane;

	private List<Exercise> exerciseList;

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
        try {
            FileChooser catalog = new FileChooser();
            catalog.setTitle("Choose Catalog-File");
		            /*catalog.getExtensionFilters().add(
				    new FileChooser.ExtensionFilter("XML files (.xml)", ".xml")
		             );*/ //Something here does not work with Windows
            File file = catalog.showOpenDialog(new Stage());
		    System.out.println(file.getAbsoluteFile());

            Exercises exercises = new Exercises();
            exerciseList = exercises.getExercises(file);
            ObservableList<String> items = FXCollections.observableArrayList();
		    for(int i = 0; i < exerciseList.size(); i++){
			    items.add(exerciseList.get(i).getName());
		    }
		    listView.setItems(items);
        } catch(NullPointerException e){}
	}

	@FXML
	protected void handleLoad(){
		String selectedExercise = (String)listView.getSelectionModel().getSelectedItem();
        System.out.println(selectedExercise);
        tabPane.getTabs().clear();

        for(Exercise e: exerciseList)
            if(e.getName().equals(selectedExercise)){
                for(Class class1: e.getClasses()){
                    Tab tab = new Tab();
                    tab.setText(class1.getName());
                    TextArea textArea = new TextArea(class1.getCode());
                    tab.setContent(textArea);
                    tabPane.getTabs().add(tab);
                }
                for(Test test: e.getTests()){
                    Tab tab = new Tab();
                    tab.setText(test.getName());;
                    TextArea textArea = new TextArea(test.getTest());
                    tab.setContent(textArea);
                    tabPane.getTabs().add(tab);
                }
            }
	}
}
