import models.ClassStruct;
import vk.core.api.*;

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
        controller.prepareExample();
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
                setState(state+1<3?state+1:0);
            });

            Button test = controller.getElementById("buttonTest");
            test.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                TestResult tr = getTestResult();
                if(tr != null) {
                    System.out.println("Successful tests: " + tr.getNumberOfSuccessfulTests());
                    System.out.println("Failed tests: " + tr.getNumberOfFailedTests());
                }
            });

            Button save = controller.getElementById("buttonSave");
            save.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                controller.prepareExample();
                TextArea textCode = controller.getElementById("textCode");
                TextArea textTest = controller.getElementById("textTest");

                controller.saveToFile("MyCode",textCode.getText(),"test123", false);
                controller.saveToFile("MyTest",textTest.getText(),"test123", true);

                /* load with:
                textCode.setText(controller.loadFromFile("MyCode","test123", false));
                textTest.setText(controller.loadFromFile("MyTest","test123", true));

                or:
                ClassStruct[] clstr = controller.loadAllFiles("test123");
                for(ClassStruct c: clstr)
                    if(c.isTest())
                        textTest.setText(c.getCode());
                    else
                        textCode.setText(c.getCode());
                */
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

        TestResult tres = null;
        if (pState == 0) {    //enable writing tests

            if (tres == null || tres.getNumberOfFailedTests() == 0) {  //add check if all tests pass

                code.setEditable(false);
                tests.setEditable(true);
                console.setEditable(false);

                scene.getStylesheets().set(0 ,styles.get(0));

                state = pState;
                console.appendText("You are now in test-writing mode\n");
            }
        }
        if (pState == 1 && tres != null) {    //enable writing code

            if (tres.getNumberOfFailedTests() == 1) {  //add check if EXACTLY one test fails

                code.setEditable(true);
                tests.setEditable(false);
                console.setEditable(false);

                scene.getStylesheets().set(0, styles.get(1));

                state = pState;
                console.appendText("You are now in code-writing mode\n");
            }
            else
                System.out.println(tres.getNumberOfFailedTests() + " tests failed. Needs to be exactly 1");
        }
        if (pState == 2 && tres != null) {    //enable writing code and tests

            if (tres.getNumberOfFailedTests() == 0) {  //add save and load for files; also if tests fail after refactoring, go back to test writing

                code.setEditable(true);
                tests.setEditable(true);
                console.setEditable(false);

                scene.getStylesheets().set(0, styles.get(2));

                state = 2;
                console.appendText("You are now in refactoring mode\n");
            }
            else
                System.out.println(tres.getNumberOfFailedTests() + " tests failed. Needs to be exactly 0");
        }
    }

    public TestResult getTestResult() {
        try {
            CompilationUnit t1 = new CompilationUnit("MyCode", ((TextArea) controller.getElementById("textCode")).getText(), false);
            CompilationUnit t2 = new CompilationUnit("MyTest", ((TextArea) controller.getElementById("textTest")).getText(), true);
            JavaStringCompiler cmp = CompilerFactory.getCompiler(t1,t2);
            cmp.compileAndRunTests();
            CompilerResult cmpres = cmp.getCompilerResult();
            if (!cmpres.hasCompileErrors()) {
                return cmp.getTestResult();
            } else {
                System.out.println("Could not compile!");
                cmpres.getCompilerErrorsForCompilationUnit(t1).forEach((CompileError err)-> {
                    System.out.println(err.getMessage());
                });
                cmpres.getCompilerErrorsForCompilationUnit(t2).forEach((CompileError err)-> {
                    System.out.println(err.getMessage());
                });
                return null;
            }
        }
        catch(Exception e){
            System.out.println("[GUID] Exception: " + e);
            return null;
        }
    }
}
