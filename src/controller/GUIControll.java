package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
		//File file = catalog.showOpenDialog(new Stage());
		File file = new File("res/catalogs/test.xml");

		System.out.println(file.getAbsoluteFile());
		String s = "";
		try{
			System.out.println(file.length());
			InputStream is = new FileInputStream(file);
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLStreamReader reader = factory.createXMLStreamReader(is);

			while(reader.hasNext()){
				if(reader.hasText()){
					s += reader.getText();
				}
				reader.next();
			}

		} catch(FileNotFoundException fnfe){
			System.out.println("This file does not exist.");
		} catch(XMLStreamException xmlse){
			System.out.println();
		}
		System.out.println(s);
	}


}
