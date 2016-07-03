import controller.ExerciseHandling;
import controller.FileHandling;
import models.CodeTab;
import models.Console;
import models.Exercise;
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
    private ExerciseHandling exerciseHanler;

    public GUIDisplay() {
        main = new Stage();
        start(main);
        exerciseHanler = new ExerciseHandling();
        controller.showExerciseList(exerciseHanler.getExerciseList());
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

            //add stylesheets for phase recognition
            scene.getStylesheets().add(0, styles.get(0));

            f = new File("res/css/scenestyle.css");

            //add stylesheet for scene style
            scene.getStylesheets().add(f.toURI().toString());

            main.setTitle("TDDT");
            main.setScene(scene);
            main.show();

            initializeEventHandlers();

            //redirect standard output to "console" TextArea
            TextArea console = controller.getElementById("textConsole");
            Console outStr = new Console(console);
            PrintStream ps = new PrintStream(outStr, true);
            System.setOut(ps);
            System.setErr(ps);
            console.setEditable(false);

        } catch (Exception e) {
            System.out.println("[GUID] Unhandled exception: " + e);
        }
    }

    public void setState(int pState, TestResult tres) {

        TextArea console = controller.getElementById("textConsole");
        TextArea description = controller.getElementById("description");
        description.setEditable(false);

        if (pState == 0) {    //enable writing tests
            try{
                if (tres == null || tres.getNumberOfFailedTests() == 0) {  //add check if all tests pass

                    for (CodeTab t : controller.getCodeTabs())
                        t.setEditable(t.isTest());

                    scene.getStylesheets().set(0, styles.get(0));
                    state = pState;
                    System.out.println("You are now in test-writing mode\n");
                }
            } catch(Exception e) {}
        }
        if (pState == 1 && tres != null) {    //enable writing code
            try {
                if (tres.getNumberOfFailedTests() == 1) {    //check if EXACTLY one test fails

                    for (CodeTab t : controller.getCodeTabs())
                        t.setEditable(!t.isTest());


                    scene.getStylesheets().set(0, styles.get(1));

                    state = pState;
                    System.out.println("You are now in code-writing mode\n");
                } else
                    System.out.println(tres.getNumberOfFailedTests() + " tests failed. Needs to be exactly 1");
            } catch(Exception e) {}
        }
        if (pState == 2 && tres != null) {  //enable writing code and tests
            try {
                if (tres.getNumberOfFailedTests() == 0) {  //add save and load for files; also if tests fail after refactoring, go back to test writing

                    for (CodeTab t : controller.getCodeTabs())
                        t.setEditable(!t.isTest());

                    scene.getStylesheets().set(0, styles.get(2));

                    state = 2;
                    System.out.println("You are now in refactoring mode\n");
                } else
                    System.out.println(tres.getNumberOfFailedTests() + " tests failed. Needs to be exactly 0");
            }catch(Exception e) {}
        }
    }

    public TestResult getTestResult() {
        try {
			ArrayList<CompilationUnit> cu = new ArrayList();
			for (CodeTab tab : controller.getCodeTabs()) {
				cu.add(new CompilationUnit(tab.getText(), tab.getCode(), tab.isTest()));
			}
			CompilationUnit[] compilationUnitArray = cu.toArray(new CompilationUnit[0]);
            JavaStringCompiler cmp = CompilerFactory.getCompiler(compilationUnitArray);

            cmp.compileAndRunTests();
            CompilerResult compilerResult = cmp.getCompilerResult();
            if (!compilerResult.hasCompileErrors()) {
                return cmp.getTestResult();
            } else {
                System.out.println("An error occurred compiling your tests!");
                for(CompilationUnit unit: compilationUnitArray)
                    compilerResult.getCompilerErrorsForCompilationUnit(unit).forEach((CompileError err)-> {
                        System.out.println(err.getMessage());
                    });
                return null;
            }
        }
        catch (NullPointerException nullPtr) {
            System.out.println("An error occurred compiling your tests!");
        }
        catch(Exception e){
            System.out.println("An error occurred compiling your tests! [GUID] Exception: " + e);
        }
        return null;
    }

    private void initializeEventHandlers() {
        //Add EventHandler for Cycle-button
        Button cycle = controller.getElementById("buttonCycle");
        cycle.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            TestResult tr = getTestResult();
            if(tr != null) {
                System.out.println("Number of failed tests: " + tr.getNumberOfFailedTests());
                System.out.println("Number of successful tests: " + tr.getNumberOfSuccessfulTests());
            }
            setState(state+1<3?state+1:0, tr);
        });

        Button test = controller.getElementById("buttonTest");
        test.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            TestResult tr = getTestResult();
            if(tr != null) {
                System.out.println("Number of failed tests: " + tr.getNumberOfFailedTests());
                System.out.println("Number of successful tests: " + tr.getNumberOfSuccessfulTests());
            }
        });

        Button save = controller.getElementById("buttonSave");
        save.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            try {
                for (CodeTab tab : controller.getCodeTabs()) {
                    Exercise current = exerciseHanler.getCurrentExercise();
                    String identifier = current==null?"temp":current.getName();
                    FileHandling.saveToFile(tab.getText(), tab.getCode(), current.getIdentifier(), tab.isTest());
                }
                System.out.println("Code saved.");

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
            } catch(Exception e) {
                System.out.println("[GUID] Exception: " + e);
            }
        });

        Button file = controller.getElementById("buttonFile");
        file.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            exerciseHanler = new ExerciseHandling();
            controller.showExerciseList(exerciseHanler.getExerciseList());
        });

        Button load = controller.getElementById("buttonLoad");
        load.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            Exercise selected = controller.getSelectedExercise(exerciseHanler.getExerciseList());
            exerciseHanler.setCurrentExercise(selected);
            controller.loadExercise(selected);
            setState(0, null);
        });
    }
}
