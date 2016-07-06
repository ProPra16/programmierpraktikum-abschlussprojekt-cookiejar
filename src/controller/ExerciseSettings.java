package controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ExerciseSettings {
    private boolean babysteps;
    private boolean acceptanceTest;
    private int babysteps_time;

    private BooleanProperty started;

    private ComboBox<String> bstime;
    private CheckBox bsteps;
    private CheckBox acceptance;
    int times[] = {1,2,3,5,10};

    private Stage main;

    public ExerciseSettings(Stage stage) {
        main = stage;
        started = new SimpleBooleanProperty(false);
    }

    public boolean isBabysteps() {
        return babysteps;
    }

    public boolean isAcceptanceTest() {
        return acceptanceTest;
    }

    public int babystepsDuration() {
        return babysteps_time;
    }

    public BooleanProperty isStarted() {
        return  started;
    }

    public void start() {
        try {
            BorderPane root = new BorderPane();
            GridPane center = new GridPane();
            center.setAlignment(Pos.CENTER);
            center.setHgap(5.0);
            center.setVgap(50.0);

            bsteps = new CheckBox("Babysteps");
            bsteps.setAlignment(Pos.CENTER);
            center.add(bsteps,0,0);

            bstime = new ComboBox();

            for(int t:times)
                bstime.getItems().add(0,t+" minute" + (t>1?"s":""));
            bstime.setEditable(false);
            bstime.setValue("2 minutes");
            center.add(bstime,1,0);

            acceptance = new CheckBox("Acceptance Test");
            acceptance.setAlignment(Pos.CENTER);
            center.add(acceptance,0,1);

            root.setCenter(center);

            Button start = new Button("Start");
            root.setBottom(start);
            start.setOnMouseClicked((MouseEvent event) -> {
                main.hide();
                babysteps = bsteps.isSelected();
                acceptanceTest = acceptance.isSelected();

                for(int t: times)
                    if(bstime.getValue().equals(t + " minutes") || bstime.getValue().equals(t + " minute")) {
                        babysteps_time = 60 * t;
                        break;
                    }
                if(babysteps)
                    System.out.println("Babysteps are enabled, " + babysteps_time/60 + " minute steps");
                if(acceptanceTest)
                    System.out.println("Acceptance test enabled.");
                started.setValue(true);
            });


            root.setAlignment(start, Pos.CENTER);
            root.setMargin(start, new Insets(0,0,10,0));

            Scene scene = new Scene(root, 300, 200);
            main.setTitle("TDDT - Settings");
            main.setScene(scene);
            main.setResizable(false);
            main.setAlwaysOnTop(true);

            main.show();
        } catch(Exception e) {
            System.out.println("[Settings] Exception: " + e);
        }
    }
}
