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
import map.map_src.Bus;
import map.map_src.Coordinate;
import map.map_src.Drawable;
import map.map_src.Street;

import java.util.ArrayList;
import java.util.Arrays;
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
        System.out.println(result);
        List<Street> streets = map.getStreets();

        MainController controller = loader.getController();
        Coordinate c1,c2;
        c1 = Coordinate.create(100, 100);
        c2 = Coordinate.create(100, 150);


        Bus b1 = new Bus("SD102", c1, 2);
        Bus b2 = new Bus("SD102", c2, 2);


        List<Drawable> elements = new ArrayList<>();
        elements.add(b1);
        elements.add(b2);
        elements.addAll(streets);

        controller.setDrawings(elements);
    }
}