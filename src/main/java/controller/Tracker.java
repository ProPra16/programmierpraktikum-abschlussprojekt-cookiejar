package controller;

import models.CodeTab;
import models.TrackSave;
import tasks.Timer;

import java.util.ArrayList;
import java.util.List;

public class Tracker {
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

        int minutes = (int)time/60000;
        int seconds = (int)(time/1000)%60;
        return String.format("%02d:%02d", minutes,seconds);
    }
}
