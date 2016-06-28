package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Exercise;
import models.Exercises;

import java.io.*;
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
            List<Exercise> exerciseList = exercises.getExercises(file);
	    	ObservableList<String> items = FXCollections.observableArrayList();
		    for(int i = 0; i < exerciseList.size(); i++){
			    items.add(exerciseList.get(i).getName());
		    }
		    listView.setItems(items);//Add Exercise "Start"-Button, for better time management and listView current state obtaining

            if (!exerciseList.isEmpty()){ //Just for now, can be replaced/ deleted after development
	    		Exercise e = exerciseList.get(0);
		    	System.out.println(e.getName());
			    System.out.println(e.getDescription());
			    for(String s : e.getClasses()){
			    	textCode.setText(s);
		    	}
		    	for(String s : e.getTests()){
		    		    textTest.setText(s);
		    	    }
             }
        } catch(NullPointerException e){}
	}

	public void saveToFile(String className, String code, String identifier, boolean isTest) {
		try {
			File sf = new File("saves/"+identifier+"/"+ (isTest?"tests/":"src/") + className + ".java");
			sf.getParentFile().mkdirs();
			sf.createNewFile();
			BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sf)));
			fw.write(code);
			fw.flush();
			fw.close();
		}catch(IOException e) {}
	}

	public String loadFromFile(String className, String identifier, boolean isTest) {
		String code = "";
		try{
			File sf = new File("saves/"+identifier+"/"+ (isTest?"tests/":"src/") + className + ".java");
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(sf)));
			for(Object t: br.lines().toArray()) {
				code += (String)t + "\n";
			}
		}catch(IOException e) {}
		return code;
	}
}
