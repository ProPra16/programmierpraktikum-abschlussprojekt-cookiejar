import controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.FileInputStream;


public class TDDT extends Application {
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage stage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader();
		Pane root = fxmlLoader.load(new FileInputStream("res/Cookiejar-main.fxml"));
		GUIControll gui = (GUIControll) fxmlLoader.getController();


		gui.test();

		
		Scene scene = new Scene(root, 1280, 800);
		
		stage.setTitle("TDDT");
		stage.setScene(scene);
		stage.show();
	}
}
