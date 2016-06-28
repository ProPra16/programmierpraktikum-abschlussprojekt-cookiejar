import controller.GUIControll;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class GUIDisplay extends Application {
    public GUIControll controller;
    private Stage main;
    private Scene scene;
    private int state;
    private List<String> styles;

    public GUIDisplay() {
        main = new Stage();
        start(main);
        setState(0);
    }

    public void start(Stage stage) {
        try {
            main = stage;
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane root = fxmlLoader.load(new FileInputStream("res/fxml/Cookiejar-main.fxml"));
            controller = (GUIControll) fxmlLoader.getController();

            scene = new Scene(root, 1280, 800);

            styles = new ArrayList<>();
            File f = new File("res/css/teststyle.css");
            styles.add(f.toURI().toString());
            f = new File("res/css/codestyle.css");
            styles.add(f.toURI().toString());
            f = new File("res/css/refactorstyle.css");
            styles.add(f.toURI().toString());
            scene.getStylesheets().add(0, styles.get(0)); //Add stylesheets for phase recognition

            f = new File("res/css/scenestyle.css");
            scene.getStylesheets().add(f.toURI().toString());//Add stylesheet for scene style

            main.setTitle("TDDT");
            main.setScene(scene);
            main.show();

            //Add EventHandler for Cycle-button
            Button cycle = controller.getElementById("buttonCycle");
            cycle.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                state++;
                setState(state);
            });
            Button run = controller.getElementById("buttonRun");
            run.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                controller.saveToFile("MyCode","public class MyCode{\npublic static int test1(){\nreturn 1;\n}\n}","test123", false);
                controller.saveToFile("MyTest","import org.junit.*;\npublic class MyTest{\n@Test public void test1(){\nassertEquals(1,MyCode.test());\n}\n}","test123", true);

                TextArea textCode = controller.getElementById("textCode");
                TextArea textTest = controller.getElementById("textTest");

                textCode.setText(controller.loadFromFile("MyCode","test123", false));
                textTest.setText(controller.loadFromFile("MyTest","test123", true));
            });

            //Redirect standart output to "console" TextArea
            TextArea console = controller.getElementById("textConsole");
            Console outStr = new Console(console);
            PrintStream ps = new PrintStream(outStr, true);
            System.setOut(ps);
            System.setErr(ps);

        } catch (Exception e) {
            System.out.println("[GUID] Unhandled exception: " + e);
        }
    }

    public void setState(int pState) {

        TextArea console = controller.getElementById("textConsole");
        TextArea code = controller.getElementById("textCode");
        TextArea tests = controller.getElementById("textTest");

        if (pState == 0) {    //enable writing tests

            if (true) {  //add check if all tests pass

                code.setEditable(false);
                tests.setEditable(true);
                console.setEditable(false);

                scene.getStylesheets().set(0 ,styles.get(0));

                state = pState;
                console.appendText("You are now in test-writing mode\n");
            }
        }
        if (pState == 1) {    //enable writing code

            if (true) {  //add check if EXACTLY one test fails

                code.setEditable(true);
                tests.setEditable(false);
                console.setEditable(false);

                scene.getStylesheets().set(0, styles.get(1));

                state = pState;
                console.appendText("You are now in code-writing mode\n");
            }
        }
        if (pState == 2) {    //enable writing code and tests

            if (true) {  //add save and load for files; also if tests fail after refactoring, go back to test writing

                code.setEditable(true);
                tests.setEditable(true);
                console.setEditable(false);

                scene.getStylesheets().set(0, styles.get(2));

                state = -1;
                console.appendText("You are now in refactoring mode\n");
            }
        }
    }
}
