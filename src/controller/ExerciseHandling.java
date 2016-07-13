package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import models.ClassStruct;
import models.CodeTab;
import models.Exercise;

import java.util.ArrayList;
import java.util.List;

public class ExerciseHandling {

    private List<Exercise> exerciseList;
    private Exercise currentExercise;

    public ExerciseHandling(boolean start) {
        if(start) {
            exerciseList = FileHandling.loadCatalog();
            currentExercise = null;
        }
    }

    public Exercise getCurrentExercise() {
        return currentExercise;
    }

    public void setCurrentExercise(Exercise e) {
        currentExercise = e;
    }

    public List<Exercise> getExerciseList() {
        return exerciseList;
    }

    public void setExerciseList(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    public static ObservableList<String> toObservableList(List<Exercise> exList) {
        ObservableList<String> items = FXCollections.observableArrayList();
        for(Exercise e: exList){
            items.add(e.getName());
        }
        return items;
    }

    public static CodeTab[] createTabView(Exercise e){
        ArrayList<Tab> tabs = new ArrayList();
        for (ClassStruct class1 : e.getClasses()) {
            tabs.add(new CodeTab(class1));
        }
        for (ClassStruct test : e.getTests()) {
            tabs.add(new CodeTab(test));
        }
        return tabs.toArray(new CodeTab[0]);
    }

}
