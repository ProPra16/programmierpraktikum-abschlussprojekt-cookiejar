import controller.ExerciseHandling;
import controller.ExerciseSettings;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;
import models.ClassStruct;
import models.CodeTab;
import models.Console;
import models.Exercise;
import tasks.Timer;
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
    private ExerciseHandling exerciseHandler;
    private ExerciseSettings settings;
    private boolean babysteps, acceptance;
    private int babystepDuration;
    private String saveTest, saveCode;
    private int shallSave;
    private CodeTab[] save;

    public GUIDisplay() {
        main = new Stage();
        start(main);
        exerciseHandler = new ExerciseHandling();
        controller.showExerciseList(exerciseHandler.getExerciseList());
        Stage settingsStage = new Stage();
        settings = new ExerciseSettings(settingsStage);
        settingsStage.initOwner(main);
        settingsStage.initModality(Modality.WINDOW_MODAL);
        settingsStage.setOnCloseRequest((WindowEvent event) -> {
            event.consume();
        });
    }

    //GUI setup
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

            Timer timer = controller.getElementById("timer");
            timer.setVisible(false);
            Label timerLabel = controller.getElementById("timerLabel");
            timerLabel.setVisible(false);
            Label maxTimer = controller.getElementById("maxTimer");
            maxTimer.setVisible(false);

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


    public void setState(int pState) {
        if (pState == 3) pState = 0;
        if (pState == -1) pState = 2;
        if (pState == 0) {    //enable writing tests
            try {
                for (CodeTab t : controller.getCodeTabs())
                    t.setEditable(t.isTest());
                scene.getStylesheets().set(0, styles.get(0));
                state = 0;
                System.out.println("You are now in test-writing mode\n");
            } catch (Exception e) {
            }
        }
        if (pState == 1) {    //enable writing code
            try {
                for (CodeTab t : controller.getCodeTabs())
                    t.setEditable(!t.isTest());
                scene.getStylesheets().set(0, styles.get(1));
                state = 1;
                System.out.println("You are now in code-writing mode\n");
                saveTabs();
            } catch (Exception e) {
            }
        }
        if (pState == 2) {  //enable writing code
            try {
                for (CodeTab t : controller.getCodeTabs())
                    t.setEditable(!t.isTest());

                scene.getStylesheets().set(0, styles.get(2));
                state = 2;
                System.out.println("You are now in refactoring mode\n");
            } catch (Exception e) {
            }
        }
    }

    public void compile() {
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
                TestResult tr = cmp.getTestResult();
                System.out.println("Number of failed tests: " + tr.getNumberOfFailedTests());
                System.out.println("Number of successful tests: " + tr.getNumberOfSuccessfulTests());
                if (tr.getNumberOfFailedTests() == 1 && state == 0) {
                    setState(1);
                } else if (tr.getNumberOfFailedTests() != 1 && state == 0) {
                    System.out.println("Number of failed tests must be EXACTLY one!");
                } else if (tr.getNumberOfFailedTests() == 0 && (state == 1 || state == 2)) {
                    setState(state + 1);
                } else if (tr.getNumberOfFailedTests() != 0 && (state == 1 || state == 2)) {
                    System.out.println("All tests need to pass!");
                }
            } else {
                System.out.println("An error occurred compiling your tests!");
                for (CompilationUnit unit : compilationUnitArray)
                    compilerResult.getCompilerErrorsForCompilationUnit(unit).forEach((CompileError err) -> {
                        System.out.println(err.getMessage());
                    });
                if (state == 0)
                    setState(1);
            }
        } catch (NullPointerException nullPtr) {
            System.out.println("An error occurred compiling your tests!");
        } catch (Exception e) {
            System.out.println("An error occurred compiling your tests! [GUID] Exception: " + e);
        }
    }

    public void compileBabysteps() {
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
                TestResult tr = cmp.getTestResult();
                System.out.println("Number of failed tests: " + tr.getNumberOfFailedTests());
                System.out.println("Number of successful tests: " + tr.getNumberOfSuccessfulTests());
                if (tr.getNumberOfFailedTests() == 1 && state == 0) {
                    goOnBabysteps();
                } else if (tr.getNumberOfFailedTests() != 1 && state == 0) {
                    System.out.println("Number of failed tests must be EXACTLY one!");
                } else if (tr.getNumberOfFailedTests() == 0 && (state == 1 || state == 2)) {
                    goOnBabysteps();
                } else if (tr.getNumberOfFailedTests() != 0 && (state == 1 || state == 2)) {
                    System.out.println("All tests need to pass!");
                }
            } else {
                System.out.println("An error occurred compiling your tests!");
                for (CompilationUnit unit : compilationUnitArray)
                    compilerResult.getCompilerErrorsForCompilationUnit(unit).forEach((CompileError err) -> {
                        System.out.println(err.getMessage());
                    });
                if (state == 0) {
                    goOnBabysteps();
                }
            }
        } catch (NullPointerException nullPtr) {
            System.out.println("An error occurred compiling your tests!");
        } catch (Exception e) {
            System.out.println("An error occurred compiling your tests! [GUID] Exception: " + e);
        }
    }

    private void goOnBabysteps() {
        Timer timer = controller.getElementById("timer");
        System.out.println("Timer stopped by : " + timer.getTime());
        timer.reset();
        if (state != 1) {
            timer.start(babystepDuration);
        }
        setStopHandler();
        saveTabs();
        setState(state + 1);
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
                for (CompilationUnit unit : compilationUnitArray)
                    compilerResult.getCompilerErrorsForCompilationUnit(unit).forEach((CompileError err) -> {
                        System.out.println(err.getMessage());
                    });
            }
        } catch (NullPointerException nullPtr) {
            System.out.println("An error occurred compiling your tests!");
        } catch (Exception e) {
            System.out.println("An error occurred compiling your tests! [GUID] Exception: " + e);
        }
        return null;
    }

    //initializing buttons and their functions
    private void initializeEventHandlers() {
        //Cycle-button
        TextArea console = controller.getElementById("textConsole");
        //Add EventHandler for Cycle-button
        Button cycle = controller.getElementById("buttonCycle");
        cycle.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (!babysteps)
                compile();
            else
                compileBabysteps();
        });

        //Test-button
        Button test = controller.getElementById("buttonTest");
        test.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            TestResult tr = getTestResult();
            if (tr != null) {
                System.out.println("Number of failed tests: " + tr.getNumberOfFailedTests());
                System.out.println("Number of successful tests: " + tr.getNumberOfSuccessfulTests());
            }
        });

        Button file = controller.getElementById("buttonFile");
        file.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            exerciseHandler = new ExerciseHandling();
            controller.showExerciseList(exerciseHandler.getExerciseList());
        });

        Button back = controller.getElementById("buttonBack");
        back.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (state == 1) {
                resetTabs();
                setState(state - 1);
            }
        });

        Button helpButton = controller.getElementById("buttonHelp");
        helpButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            System.out.println("Help!");
        });

        //Load-button
        Button load = controller.getElementById("buttonLoad");
        load.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            Exercise selected = controller.getSelectedExercise(exerciseHandler.getExerciseList());
            if (selected != null) {
                exerciseHandler.setCurrentExercise(selected);
                controller.loadExercise(selected);
                settings.start();
                BooleanProperty isStarted = settings.isStarted();
                isStarted.addListener((value) -> {
                    babysteps = settings.isBabysteps();
                    acceptance = settings.isAcceptanceTest();
                    babystepDuration = settings.babystepsDuration();
                    handleSettings();
                });
            } else {
                System.out.println("Please select an exercise.");
            }
        });
    }

    public void handleSettings() {
        if (babysteps) {
            handleBabysteps();
        } else if (!acceptance) {
            Timer timer = controller.getElementById("timer");
            Label timerLabel = controller.getElementById("timerLabel");
            Label maxTimer = controller.getElementById("maxTimer");
            try {
                timer.reset();
            } catch (NullPointerException e) {
            }
            timer.setVisible(false);
            timerLabel.setVisible(false);
            maxTimer.setText("/" + String.format("%02d:%02d", babystepDuration / 60, 0));
            maxTimer.setVisible(false); //Shut down timer after using
            saveTabs();
            setState(0);
        }
        if (acceptance) {
            setState(0);
        }
    }

    public void setStopHandler() {
        Timer timer = controller.getElementById("timer");
        BooleanProperty isStopped = timer.isStopped();
        isStopped.addListener((value) -> {
            resetTabs();
            setState(state - 1);
            if(state != 2)
                timer.start(babystepDuration);
        });
    }

    public void resetTabs() {
        TabPane tabPane = controller.getElementById("tabPane");
        Exercise exercise = exerciseHandler.getCurrentExercise();
        for (ClassStruct classStruct : exercise.getClasses()) {
            for (CodeTab codeTab : save) {
                if (codeTab.isTest() == classStruct.isTest() && codeTab.getText().equals(classStruct.getName())) {
                    codeTab.setCode(classStruct.getCode());
                }
            }
        }
        for (ClassStruct classStruct : exercise.getTests()) {
            for (CodeTab codeTab : save) {
                if (codeTab.isTest() == classStruct.isTest() && codeTab.getText().equals(classStruct.getName())) {
                    codeTab.setCode(classStruct.getCode());
                }
            }
        }
        tabPane.getTabs().clear();
        tabPane.getTabs().addAll(exerciseHandler.createTabView(exercise));
    }

    public void saveTabs() {
        save = controller.getCodeTabs();
        Exercise exercise = exerciseHandler.getCurrentExercise();
        for (ClassStruct classStruct : exercise.getClasses()) {
            for (CodeTab codeTab : save) {
                if (codeTab.isTest() == classStruct.isTest() && codeTab.getText().equals(classStruct.getName())) {
                    classStruct.setCode(codeTab.getCode());
                }
            }
        }
        for (ClassStruct classStruct : exercise.getTests()) {
            for (CodeTab codeTab : save) {
                if (codeTab.isTest() == classStruct.isTest() && codeTab.getText().equals(classStruct.getName())) {
                    classStruct.setCode(codeTab.getCode());
                }
            }
        }
    }

    public void handleBabysteps() {
        Timer timer = controller.getElementById("timer");
        Label timerLabel = controller.getElementById("timerLabel");
        Label maxTimer = controller.getElementById("maxTimer");

        timer.start(babystepDuration);
        setStopHandler();
        timer.setVisible(true);
        timerLabel.setVisible(true);
        maxTimer.setText("/" + String.format("%02d:%02d", babystepDuration / 60, 0));
        maxTimer.setVisible(true); //Initialize timer and labels
        setState(0);
        saveTabs();
    }
}