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
    }

    public String averageTime(int state){
        String time;
        int seconds = 0;
        int minutes = 0;
        for(TrackSave trackSave: saves)
            if (trackSave.getState() == state){
                String trackSaveTime = trackSave.getTime();
                String trackSaveSeconds = trackSaveTime.substring(3, 4);
                String trackSaveMinutes = trackSaveTime.substring(0, 1);

                seconds+=Integer.parseInt(trackSaveSeconds);
                minutes+=Integer.parseInt(trackSaveMinutes);
            }
        if (seconds<10)
            time = "00:0"+seconds;
        else
            time = "00:"+seconds;

        if (minutes<10)
            time = "0"+minutes+":"+time.substring(3, 4);
        else
            time = minutes+":"+time.substring(3, 4);

        return time;
    }
}
