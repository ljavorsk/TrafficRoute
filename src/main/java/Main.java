/*
 * Source code for the final IJA project
 * Main class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 *
 */


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import map.Map;
import map.map_src.*;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Main application class
 * Displays everything to the user
 */
public class Main extends Application {

    
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layout.fxml"));
        BorderPane window = loader.load();
        Scene scene = new Scene(window);
        primaryStage.setScene(scene);

        primaryStage.show();

        Map map = new Map("Brno");
        boolean result = map.loadData("./data/street.json", "./data/line.json");
        primaryStage.setTitle("Mapa mesta: " + map.getTownName());
        List<Street> streets = map.getStreets();
        List<Stop> stops = map.getStops();

        Coordinate c1,c2;
        Street s1 = streets.get(0);
        c1 = s1.beginOfTheStreet();
        c2 = s1.endOfTheStreet();
        AbstractMap.SimpleImmutableEntry<Coordinate, Street> start_p1 =
                new AbstractMap.SimpleImmutableEntry<Coordinate, Street>(c1, s1);
        AbstractMap.SimpleImmutableEntry<Coordinate, Street> start_p2 =
                new AbstractMap.SimpleImmutableEntry<Coordinate, Street>(c2, s1);


        Bus b1 = new Bus("SD102", start_p1);
        Bus b2 = new Bus("SD102", start_p2);

        List<Drawable> elements = new ArrayList<>();
        elements.addAll(streets);
        elements.addAll(stops);
        elements.add(b1);
        elements.add(b2);

        MainController controller = loader.getController();
        controller.setDrawings(elements);
    }
}