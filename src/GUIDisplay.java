import controller.GUIControll;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.PrintStream;

public class GUIDisplay extends Application{
    public GUIControll controller;
    private Stage main;
    private int state;

    public GUIDisplay() {
        main = new Stage();
        start(main);
        state = 0;
    }

    public void start(Stage stage) {
        try {
            main = stage;
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane root = fxmlLoader.load(new FileInputStream("res/Cookiejar-main.fxml"));
            controller = (GUIControll) fxmlLoader.getController();

            Scene scene = new Scene(root, 1280, 800);

            main.setTitle("TDDT");
            main.setScene(scene);
            main.show();

            //Add EventHandler for Cycle-button
            Button cycle = controller.getElementById("buttonCycle");
            cycle.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                setState(state^1);
            });

            //Redirect standart output to "console" TextArea
            TextArea console = controller.getElementById("textConsole");
            Console outStr = new Console(console);
            PrintStream ps = new PrintStream(outStr, true);
            System.setOut(ps);
            System.setErr(ps);

        }
        catch (Exception e) {
            System.out.println("[GUID] Unhandled exception: " + e);
        }
    }

    public void setState(int pState) {

        TextArea console = controller.getElementById("textConsole");
        TextArea code = controller.getElementById("textCode");
        TextArea tests = controller.getElementById("textTest");

        if(pState == 0) {    //enable writing tests

            if(true) {  //add check if all tests pass

                code.setEditable(false);
                tests.setEditable(true);
                console.setEditable(false);

                state = pState;
                console.appendText("You are now in test-writing mode\n");
            }
        }
        if(pState == 1) {    //enable writing code

            if(true) {  //add check if EXACTLY one test fails

                code.setEditable(true);
                tests.setEditable(false);
                console.setEditable(false);

                state = pState;

                console.appendText("You are now in code-writing mode\n");
            }
        }
    }
}
