/*
 * Source code for the final IJA project
 * Main class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 *
 */

package ija;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import ija.map.Map;
import ija.map.map_src.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Main application class
 * Displays everything to the user
 */
public class Main extends Application {
    /// Controller that controls the application
    private MainController controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout.fxml"));
        BorderPane window = loader.load();
        Scene scene = new Scene(window);
        primaryStage.setScene(scene);

        primaryStage.show();

        Map map = new Map("Brno");
        boolean result = map.loadData("./data/street.json", "./data/line.json");
        if (!result){
            Alert json_error = new Alert(Alert.AlertType.ERROR, "Incorrect JSON file");
            json_error.showAndWait();
            primaryStage.close();
        }
        primaryStage.setTitle("Mapa mesta: " + map.getTownName());
        List<Street> streets = map.getStreets();
        List<Stop> stops = map.getStops();
        List<Line> lines = map.getLines();

        List<Drawable> elements = new ArrayList<>();

        elements.addAll(streets);
        elements.addAll(stops);

        for (Line line : lines){
            elements.addAll(line.getBuses());
            elements.add(line.getRoute());
        }

        this.controller = loader.getController();
        controller.setDrawings(elements);
        controller.startTime(map, 1);
        controller.setRightScreen();
    }

    @Override
    public void stop() throws Exception {
        if (controller != null)
            controller.stopSimulation();
        super.stop();
    }
}