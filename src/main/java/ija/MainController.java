/*
 * Source code for the final IJA project
 * MainController class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 *
 */

package ija;
import ija.gui.LineButton;
import ija.gui.StreetButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ija.map.Map;

import ija.map.map_src.Drawable;
import ija.map.map_src.Line;
import ija.map.map_src.Street;

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
    /// Text field that shows the speed of the time
    @FXML
    private TextField time_speed_text;
    /// Vbox field for show streets and lines
    @FXML
    private VBox vbox_line_stop;
    /// Vbox field for streets or lines setting
    @FXML
    private VBox vbox_setting;
    /// Timer in map
    private Timer clock;
    /// Number which represents how many times did we speed the time
    private int speed_times = 0;
    /// Map that is controlled in controller
    private Map map;
    /// Indicates if the simulation is running
    private boolean is_running = false;
    /// List for storing buttons, that represent lines on map
    private final List<LineButton> lineButtons = new ArrayList<>();
    /// List for storing button, that represent streets on map
    private final List<StreetButton> streetButtons = new ArrayList<>();

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
     * Speeds the time in simulation
     */
    @FXML
    private void speedTime(){
        if (this.speed_times >= 5)
            return;
        this.speed_times++;
        clock.cancel();
        playSimulation();
    }

    /**
     * Slows the time in simulation
     */
    @FXML
    private void slowTime(){
        if (this.speed_times <= 0)
            return;
        this.speed_times--;
        clock.cancel();
        playSimulation();
    }

    /**
     * Starts the simulation
     */
    @FXML
    private void startSimulation(){
        if (!is_running){
            clock.cancel();
            playSimulation();
        }
    }

    /**
     * Stops the simulation
     */
    @FXML
    public void stopSimulation(){
        if (is_running){
            clock.cancel();
            is_running = false;
        }
    }

    /**
     * Show all lines on right side of the screen.
     */
    @FXML
    private void showLines(){
        vbox_line_stop.getChildren().clear();
        for(LineButton button : this.lineButtons){
            vbox_line_stop.getChildren().add(button);
            button.getLine().deselectLine();
        }
        for(StreetButton button : this.streetButtons){
            button.getStreet().unhighlightTheStreet();
            button.getStreet().deselectStreet();
        }
        vbox_setting.getChildren().clear();
    }

    /**
     * Show all streets on right side of the screen. And highlight on map.
     */
    @FXML
    private void showStreets(){
        vbox_line_stop.getChildren().clear();
        for(StreetButton button : this.streetButtons){
            vbox_line_stop.getChildren().add(button);
            button.getStreet().highlightTheStreet();
            button.getStreet().deselectStreet();
        }
        for(LineButton button : this.lineButtons){
            button.getLine().deselectLine();
        }
        vbox_setting.getChildren().clear();
    }

    /**
     * Resolves how to start the simulation with right time frame
     */
    private void playSimulation(){
        if (speed_times == 0){
            startTime(this.map, 1);
        }
        else if (speed_times == 1) {
            startTime(this.map, speed_times + 1);
        } else{
            startTime(this.map, 2*speed_times);
        }
    }

    /**
     * Setter for the drawings that will be drawn on GUI
     * @param drawings List of elements that will be drawn
     */
    public void setDrawings(List<Drawable> drawings) {
        for (Drawable element : drawings) {
            main_content.getChildren().addAll(element.getShapes());
        }
    }

    /**
     * Starts the simulation
     * @param m Map that will be simulated
     * @param time_speed Speed value which controls time
     * higher is faster
     */
    public void startTime(Map m, double time_speed){
        this.is_running = true;
        this.map = m;
        this.time_speed_text.setText("Time speed: " + time_speed);
        clock = new Timer(false);
        clock.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(m::oneMove);
                }
        }, 0, (long) (1000 / time_speed));
    }

    /**
     * Create buttons for streets and lines. This buttons will be later show on right side of the screen.
     */
    public void setRightScreen(){
        for(Line line : this.map.getLines()){
            LineButton lineButton = new LineButton(line, this.lineButtons, this.vbox_setting, this.main_content, this.vbox_line_stop, map.getStreets());
            lineButtons.add(lineButton);
        }
        for(Street street : this.map.getStreets()){
            StreetButton streetButton = new StreetButton(street, this.streetButtons, this.vbox_setting);
            this.streetButtons.add(streetButton);
        }
    }
}
