/*
 * Source code for the final IJA project
 * MainController class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 *
 */

import javafx.fxml.FXML;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import map.Map;

import map.map_src.Drawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Controls the interaction with the user
 */
public class MainController {
    /// Main content pane
    @FXML
    private Pane main_content;
    /// Items that will be drawn on Map
    private List<Drawable> drawings = new ArrayList<>();
    /// Timer in map
    private Timer clock;

    /**
     * Handler for the scroll zooming
     * @param event Events in Scroll Pane
     */
    @FXML
    private void zoom(ScrollEvent event){
        event.consume();
        double zoom_by = 0.1;
        double zoom_value = event.getDeltaY() >= 0 ? (1 + zoom_by) : (1 - zoom_by);

        main_content.setScaleX(zoom_value * main_content.getScaleX());
        main_content.setScaleY(zoom_value * main_content.getScaleY());
    }

    /**
     * Setter for the drawings that will be drawn on GUI
     * @param drawings List of elements that will be drawn
     */
    public void setDrawings(List<Drawable> drawings) {
        this.drawings = drawings;
        for (Drawable element : drawings) {
            main_content.getChildren().addAll(element.getShapes());
        }
    }

    /**
     * Starts the simulation
     */
    public void startTime(Map m){
        clock = new Timer(false);
        clock.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                m.oneMove();
            }
        }, 0, 500);
    }
}
