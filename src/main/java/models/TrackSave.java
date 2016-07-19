package models;


public class TrackSave {
    private int state;
    private CodeTab[] save;
    private String time;

    public TrackSave(int state, CodeTab[] save, String time){
        this.state = state;
        this.save = save;
        this.time = time;
    }

    public int getState(){
        return state;
    }

    public CodeTab[] getTabs(){
        return save;
    }

    public String getTime(){
        return time;
    }
}
