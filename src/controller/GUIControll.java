package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.List;

public class GUIControll{

	@FXML private Button buttonFile;
	@FXML private Button buttonSave;
	@FXML private Button buttonTest;
	@FXML private Button buttonSettings;
	@FXML private TextArea textConsole;
	@FXML private Button buttonCycle;
	@FXML private ListView listView;
    @FXML private TabPane tabPane;
	private Exercise currentExercise = null;

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
            exerciseList = FileHandling.loadCatalog();
            ObservableList<String> items = FXCollections.observableArrayList();
		    for(int i = 0; i < exerciseList.size(); i++){
			    items.add(exerciseList.get(i).getName());
		    }
		    listView.setItems(items);
        } catch(NullPointerException e){}
	}

	@FXML
	protected void handleLoad() {
		try {
			String selectedExercise = (String) listView.getSelectionModel().getSelectedItem();
			System.out.println(selectedExercise);
			tabPane.getTabs().clear();

			for (Exercise e : exerciseList)
				if (e.getName().equals(selectedExercise)) {
					currentExercise = e;

					for (ClassStruct class1 : e.getClasses()) {
						tabPane.getTabs().add(createTab(class1));
					}
					for (ClassStruct test : e.getTests()) {
						tabPane.getTabs().add(createTab(test));
					}
				}
		} catch(NullPointerException npe){
			System.out.print("Please load a catalog first."); //NULL printed before ERROR
		}
	}

	public static Tab createTab(ClassStruct struct){
		Tab tab = new Tab();
		tab.setText(struct.getName());
		TextArea textArea = new TextArea(struct.getCode());
		tab.setContent(textArea);
		tab.setUserData(struct.isTest());
		return tab;
	}

}
