import controller.ExerciseHandling;
import controller.ExerciseSettings;
import controller.FileHandling;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.*;
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
    private CodeTab[] save;

    public GUIDisplay() {
        main = new Stage();
        start(main);
        exerciseHandler = new ExerciseHandling(false);
        TabPane tabPane = controller.getElementById("tabPane");
        List<String> help = FileHandling.getHelpFiles();
        for(String s : help){
            Tab tab = new Tab();
            TextArea textArea = new TextArea();
            textArea.setText(s);
            textArea.setEditable(false);
            tab.setContent(textArea);
            tab.setText("Help");
            tab.setClosable(true);
            tabPane.getTabs().add(tab);
        }
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
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
            f = new File("res/css/acceptancestyle.css");
            styles.add(f.toURI().toString());
            f = new File("res/css/startstyle.css");
            styles.add(f.toURI().toString());

            //add stylesheets for phase recognition
            scene.getStylesheets().add(0, styles.get(4));

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
        if (pState == 3 || pState == 5 || (pState == 4 && !acceptance)) pState = 0;
        if (pState == -1) pState = 2;
        if (pState == 0) {    //enable writing tests
            try {
                for (CodeTab t : controller.getCodeTabs())
                    t.setEditable(t.isTest() && !t.isAcceptance());
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

        if(pState == 4 && acceptance){
            try {
                for (CodeTab t : controller.getCodeTabs())
                    t.setEditable(t.isTest() && t.isAcceptance());

                scene.getStylesheets().set(0, styles.get(3));
                state = 4;
                System.out.println("You are now in acceptance test mode\n");
            } catch (Exception e) {
            }
        }
    }

    public CompilationUnit[] getCompilationUnits(boolean getAcceptanceTest) {
        ArrayList<CompilationUnit> cu = new ArrayList();
        for (CodeTab tab : controller.getCodeTabs()) {
            if(!tab.isTest() || (!getAcceptanceTest && !tab.isAcceptance()) || (getAcceptanceTest && tab.isAcceptance()))
                cu.add(new CompilationUnit(tab.getText(), tab.getCode(), tab.isTest()));
        }
        return cu.toArray(new CompilationUnit[0]);
    }

    public JavaStringCompiler getStringCompiler(CompilationUnit[] compilationUnitArray) throws Exception {
        JavaStringCompiler cmp = CompilerFactory.getCompiler(compilationUnitArray);
        cmp.compileAndRunTests();
        return cmp;
    }


    public TestResult getTestResult(boolean getAcceptanceTest) {
        try {
            CompilationUnit[] compilationUnitArray = getCompilationUnits(getAcceptanceTest);
            JavaStringCompiler cmp = getStringCompiler(compilationUnitArray);
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

    public void compile() {
        if(state == 4) {
            if (acceptenceTestSuccess())
                System.out.println("At least one acceptance test has to fail.");
            else
                cycle(false);
            return;
        }
        TestResult tr = getTestResult(false);

        if (tr != null) { //no compile errors
            System.out.println("Number of failed tests: " + tr.getNumberOfFailedTests());
            System.out.println("Number of successful tests: " + tr.getNumberOfSuccessfulTests());
            if (tr.getNumberOfFailedTests() == 1 && state == 0) {
                cycle(false);
            } else if (tr.getNumberOfFailedTests() != 1 && state == 0) {
                System.out.println("Number of failed tests must be EXACTLY one!");
            } else if (tr.getNumberOfFailedTests() == 0 && state == 1) {
                cycle(false);
            } else if (tr.getNumberOfFailedTests() == 0 && state == 2) {
                cycle(acceptenceTestSuccess());
            } else if (tr.getNumberOfFailedTests() != 0 && (state == 1 || state == 2)) {
                System.out.println("All tests need to pass!");
            }
        } else {
            if (state == 0 && !babysteps)
                setState(1);
        }
    }

    public boolean acceptenceTestSuccess() {
        if(acceptance) {
            TestResult tr = getTestResult(true);
            if (tr != null) {
                if (tr.getNumberOfFailedTests() == 0) {
                    System.out.println("Acceptance test succeeded.");
                    return true;
                }
                System.out.println("Acceptance test did not succeed. " + tr.getNumberOfFailedTests() + " tests failed.");
            }
        }
        return false;
    }

    private void cycle(boolean acceptanceState) {
        if(babysteps) {
            Timer timer = controller.getElementById("timer");
            System.out.println("Timer stopped by : " + timer.getTime());
            timer.reset();
            if (state != 1) {
                timer.start(babystepDuration);
            }
            setStopHandler();
            saveTabs();
        }
        if(!acceptanceState)
            setState(state + 1);
        else
            setState(4);
    }

    //initializing buttons and their functions
    private void initializeEventHandlers() {
        //Cycle-button
        TextArea console = controller.getElementById("textConsole");
        //Add EventHandler for Cycle-button
        Button cycle = controller.getElementById("buttonCycle");
        cycle.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if(exerciseHandler.getCurrentExercise() != null && exerciseHandler.getExerciseList() != null) {
                compile();
            } else {
                System.out.println("Please select an exercise or a catalog first.");
            }
        });

        //Test-button
        Button test = controller.getElementById("buttonTest");
        test.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if(exerciseHandler.getCurrentExercise() != null && exerciseHandler.getExerciseList() != null){
                TestResult tr = getTestResult(false);
                if (tr != null) {
                    System.out.println("Number of failed tests: " + tr.getNumberOfFailedTests());
                    System.out.println("Number of successful tests: " + tr.getNumberOfSuccessfulTests());
                    acceptenceTestSuccess();
                }
            } else {
                System.out.println("Please select an exercise or a catalog first.");
            }
        });

        Button file = controller.getElementById("buttonFile");
        file.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            ListView listView = controller.getElementById("listView");
            listView.setItems(null);
            closeTimer();
            exerciseHandler = new ExerciseHandling(true);
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
            TabPane tabPane = controller.getElementById("tabPane");
            List<String> help = FileHandling.getHelpFiles();
            for(String s : help){
                Tab tab = new Tab();
                TextArea textArea = new TextArea();
                textArea.setText(s);
                textArea.setEditable(false);
                tab.setContent(textArea);
                tab.setText("Help");
                tab.setClosable(true);
                tabPane.getTabs().add(tab);
            }
        });

        //Load-button
        Button load = controller.getElementById("buttonLoad");
        load.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            closeTimer();
            try {
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
                    TextArea description = controller.getElementById("description");
                    description.setText("Description:\n"+formatDescription(selected.getDescription()));
                } else {
                    System.out.println("Please select an exercise.");
                }
            } catch(NullPointerException e){
                System.out.println("Please select a catalog first.");
            }
        });
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

    public String formatDescription(String string){
        int cnt = 0;
        String formatted = "";
        for(int i = 0; i < string.length(); i++){
            if(cnt > 15 && string.charAt(i) == ' '){
                formatted += string.charAt(i)+"\n";
                cnt = 0;
            } else{
                formatted += string.charAt(i);
            }
            cnt ++;
        }
        return formatted;
    }

    public void handleSettings() {
        if (babysteps) {
            handleBabysteps();
        } else if (!acceptance) {
            closeTimer();
            saveTabs();
            setState(0);
        }
        if (acceptance) {
            TabPane codes = controller.getElementById("tabPane");
            ClassStruct acceptanceTest = new ClassStruct("AcceptanceTest", "import static org.junit.Assert.*;\n" +
                                                        "import org.junit.Test;\n" +
                                                        "public class AcceptanceTest {\n" +
                                                        "    @Test\n"+
                                                        "    public void testSomething() {" +
                                                        "    }\n" +
                                                        "}", true);
            CodeTab acceptanceTab = new CodeTab(acceptanceTest);
            acceptanceTab.setAcceptance(true);
            codes.getTabs().add(acceptanceTab);
            codes.getSelectionModel().select(acceptanceTab);
            saveTabs();
            setState(4);
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

    public void closeTimer(){
        try {
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
        } catch (Exception e){}
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
}