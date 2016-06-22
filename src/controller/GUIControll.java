package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

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

	public void prepareExample() {
		textCode.setText("public class MyCode{public static int test1(){return 1;}}");
		textTest.setText("import static org.junit.Assert.*;\nimport org.junit.Test;\n" +
				"public class MyTest{@Test public void testtest1(){assertEquals(1,MyCode.test1());}}");
	}
	
	
}
