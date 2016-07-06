package tasks;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Timer extends Label{
    Timeline timer;
    private long start;
    private long time;

    public Timer() {
        super("00:00");
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> update()));
    }

    public void start(int stop) {
        reset();
        start = System.currentTimeMillis();
        timer.setCycleCount(stop);
        timer.play();
    }

    public void update() {
        time = System.currentTimeMillis() - start;
        setText(getTime());
    }

    public void reset() {
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
        timer.stop();
    }
}
