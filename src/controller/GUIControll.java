package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class GUIControll{
	
	@FXML public Button buttonNew;
	@FXML public Button buttonSave;
	@FXML public Button buttonLoad;
	@FXML public  Button buttonRun;
	@FXML public Button buttonTest;
	@FXML public Button buttonSettings;
	@FXML public TextArea textCode;
	@FXML public TextArea textTest;
	@FXML public TextArea textConsole;
	
	
	public void test() {
		textCode.setText("Ich bin der Code!");
		textTest.setText("Ich bin der Test!");
		textConsole.setText("Ich bin die Konsole!");
	}

	
	
}
