package tasks;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Timer extends Label{
    private Timeline timer;
    private long start;
    private long time;
    private int stop;

    private BooleanProperty stopped;

    public Timer() {
        super("00:00");
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> update()));
    }

    public void start(int stop) {
        this.stop = stop/60;
        reset();
        start = System.currentTimeMillis();
        timer.setCycleCount(stop);
        timer.play();
    }

    public BooleanProperty isStopped(){
        return stopped;
    }

    public void update() {
        time = System.currentTimeMillis() - start;
        setText(getTime());
        if(getTime().equals(String.format("%02d:%02d", stop ,00))) stop();
    }

    public void reset() {
        stopped = new SimpleBooleanProperty(false);
        timer.stop();
        this.setText("00:00");
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> update()));
        time = 0l;
    }

    public String getTime(){
        int minutes = (int)time/60000;
        int seconds = (int)(time/1000)%60;
        return String.format("%02d:%02d", minutes,seconds);
    }

    public void stop() {
        stopped.setValue(true);
        timer.stop();
    }
}
