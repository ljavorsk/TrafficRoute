/*
 * Source code for the final IJA project
 * MainController class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 *
 */

import gui.LineButton;
import gui.StreetButton;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import map.Map;

import map.map_src.Drawable;
import map.map_src.Line;
import map.map_src.Street;

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
    /// Items that will be drawn on Map
    private List<Drawable> drawings = new ArrayList<>();
    /// Timer in map
    private Timer clock;
    /// Number which represents how many times did we speed the time
    private int speed_times = 0;
    /// Map that is controlled in controller
    private Map map;
    /// Indicates if the simulation is running
    private boolean is_running = false;

    private List<LineButton> lineButtons = new ArrayList<>();
    private List<StreetButton> streetButtons = new ArrayList<>();
    private final ToggleGroup line_group = new ToggleGroup();
    private final ToggleGroup street_group = new ToggleGroup();


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

    @FXML
    private void showLines(){
        vbox_line_stop.getChildren().clear();
        for(Button button : this.lineButtons){
            vbox_line_stop.getChildren().add(button);
        }
    }

    @FXML
    private void showStreets(){
        vbox_line_stop.getChildren().clear();
        for(Button button : this.streetButtons){
            vbox_line_stop.getChildren().add(button);
        }
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
        this.drawings = drawings;
        for (Drawable element : drawings) {
            main_content.getChildren().addAll(element.getShapes());
        }

    }

    /**
     * Starts the simulation
     */
    public void startTime(Map m, double time_speed){
        this.is_running = true;
        this.map = m;
        this.time_speed_text.setText("Time speed: " + time_speed);
        clock = new Timer(false);
        clock.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    m.oneMove();
                } catch (Exception e){
                    Alert internal_err = new Alert(Alert.AlertType.ERROR, "Internal Error");
                    internal_err.showAndWait();
                }
            }
        }, 0, (long) (1000 / time_speed));
    }


    public void setRightScreen(){
        for(Line line : this.map.getLines()){
            LineButton lineButton = new LineButton(line);
            lineButtons.add(lineButton);
        }
        for(Street street : this.map.getStreets()){
            StreetButton streetButton = new StreetButton(street);
            this.streetButtons.add(streetButton);
        }
    }
}
