package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
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

    private Timer timer;
    private List<TrackSave> saves;

    public Tracker(){
        saves = new ArrayList<>();
        timer = new Timer();
        timer.start(Integer.MAX_VALUE);
    }

    public void save(int state, CodeTab[] tabs){
        saves.add(new TrackSave(state, tabs, timer.getTime()));
        timer.reset();
    }

    public String averageTime(int state){
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
            GridPane root = fxmlLoader.load(new FileInputStream("res/fxml/Tracker.fxml"));

            Scene scene = new Scene(root, 200, 232);
            File file = new File("res/css/startstyle.css");
            scene.getStylesheets().add(file.toURI().toString());

            stage.setScene(scene);
            stage.setTitle("Tracker");
            stage.show();
        } catch (Exception e){
            System.out.println("[GUID] Unhandled exception: " + e);
        }
    }
}
