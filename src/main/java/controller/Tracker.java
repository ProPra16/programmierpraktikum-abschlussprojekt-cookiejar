package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.CodeTab;
import models.TrackSave;
import tasks.Timer;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class Tracker {
    @FXML private Button buttonPrev;
    @FXML private Button buttonNext;
    @FXML private TabPane logTabs;
    @FXML private Label labelThisTime;
    @FXML private Label labelAverageTime;

    private Timer timer;
    private List<TrackSave> saves;
    private int currentLog = 0;
    private String[] labelTexts = {"test-writing: ", "code-writing: ", "refactor: ", "acceptance: "};

    public Tracker(){
        saves = new ArrayList<>();
        timer = new Timer();
        timer.start(Integer.MAX_VALUE);
    }

    public void save(int state, CodeTab[] tabs){
        saves.add(new TrackSave(state, tabs, timer.getTime()));
        timer.reset();
    }

    private String averageTime(int state){
        int time = 0;
        for(TrackSave trackSave: saves)
            if (trackSave.getState() == state){
                String trackSaveTime = trackSave.getTime();
                String trackSaveSeconds = trackSaveTime.substring(3, 4);
                String trackSaveMinutes = trackSaveTime.substring(0, 1);

                time+=Integer.parseInt(trackSaveSeconds);
                time+=(Integer.parseInt(trackSaveMinutes))*60;
            }

        int minutes = time/60000;
        int seconds = (time/1000)%60;
        return String.format("%02d:%02d", minutes,seconds);
    }

    public void trackerGUI(){
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();

            //system can't find this file
            GridPane root = fxmlLoader.load(new FileInputStream("res/fxml/Tracker.fxml"));

            Scene scene = new Scene(root, 200, 264);
            File file = new File("res/css/startstyle.css");
            scene.getStylesheets().add(file.toURI().toString());
            file = new File("res/css/scenestyle.css");
            scene.getStylesheets().add(file.toURI().toString());

            if(!saves.isEmpty())
                for(CodeTab saveTab: saves.get(0).getTabs()){
                    Tab tab = new Tab();
                    TextArea textArea = new TextArea();
                    textArea.setText(saveTab.getCode());
                    textArea.setEditable(false);
                    tab.setContent(textArea);
                    tab.setText(saveTab.getText());
                    tab.setClosable(false);
                    logTabs.getTabs().add(tab);
                }

            labelThisTime.setText("Time used for this refactor: "+saves.get(0).getTime());
            labelAverageTime.setText("Average time used to refactor: "+averageTime(2));

            stage.setScene(scene);
            stage.setTitle("Tracker");
            stage.show();

            addEventHandler();
        } catch (Exception e){
            System.out.println("[GUID] Unhandled exception: " + e);
        }
    }

    private void addEventHandler(){
        buttonPrev.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if (currentLog!=0) {
                currentLog--;
                loadSelectedTabs();
            }
        });

        buttonNext.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            if(currentLog!=saves.size()-1){
                currentLog++;
                loadSelectedTabs();
            }
        });
    }

    private void loadSelectedTabs(){
        logTabs.getTabs().clear();
        for (CodeTab saveTab : saves.get(currentLog).getTabs()) {
            Tab tab = new Tab();
            TextArea textArea = new TextArea();
            textArea.setText(saveTab.getCode());
            textArea.setEditable(false);
            tab.setContent(textArea);
            tab.setText(saveTab.getText());
            tab.setClosable(false);
            logTabs.getTabs().add(tab);
        }
        labelThisTime.setText("Time used for this"+labelTexts[saves.get(currentLog).getState()]+""+saves.get(currentLog).getTime());
        labelAverageTime.setText("Average time used for "+labelTexts[saves.get(currentLog).getState()]+""+averageTime(saves.get(currentLog).getState()));
    }
}
