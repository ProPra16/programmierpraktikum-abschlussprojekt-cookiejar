package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.*;
import tasks.Timer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class GUIControll{

	@FXML private Button buttonFile;
	@FXML private Button buttonLoad;
	@FXML private Button buttonBack;
	@FXML private Button buttonTest;
	@FXML private Button buttonHelp;
	@FXML private TextArea textConsole;
	@FXML private Button buttonCycle;
	@FXML private ListView listView;
    @FXML private TabPane tabPane;
	@FXML private TextArea description;
	@FXML private Timer timer;
	@FXML private Label timerLabel;
	@FXML private Label maxTimer;

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

	public void showExerciseList(List<Exercise> exerciseList){ //loads File with catalog
        try {
		    listView.setItems(ExerciseHandling.toObservableList(exerciseList));
        } catch(NullPointerException e){}
	}

	public void loadExercise(Exercise e) {
		try {
			System.out.println(e.getName());
			tabPane.getTabs().clear();
			tabPane.getTabs().addAll(ExerciseHandling.createTabView(e));
		} catch(NullPointerException npe){
			System.out.print("Please load a catalog first."); //NULL printed before ERROR
		}
	}

	public CodeTab[] getCodeTabs() {
		ArrayList<CodeTab> ct = new ArrayList();
		for(Tab t: tabPane.getTabs()) {
			if(t instanceof CodeTab)
				ct.add((CodeTab)t);
		}
		return ct.toArray(new CodeTab[0]);
	}

	public Exercise getSelectedExercise(List<Exercise> exerciseList) {
		String selectedExercise = (String) listView.getSelectionModel().getSelectedItem();
		for(Exercise ex: exerciseList)
			if(ex.getName().equals(selectedExercise))
				return ex;
		return null;
	}
}
