/*
 * Source code for the final IJA project
 * Map class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 * 
 */

package src.ija.map;

import java.util.ArrayList;
import java.util.List;

import src.ija.map.map_src.Coordinate;
import src.ija.map.map_src.Line;
import src.ija.map.map_src.Stop;
import src.ija.map.map_src.Street;
import lib.org.json.simple.JSONArray; 
import lib.org.json.simple.JSONObject; 
import lib.org.json.simple.parser.*; 
import java.io.FileReader;

/**
 * Representing the Map.
 * Map contains everything about the town (lines, streets, stops)
 */
public class Map {
    /// Name of the town, that this map represents
    private String town_name;
    /// All of the lines in town
    private List<Line> lines = new ArrayList<Line>();
    /// All of the streets in town
    private List<Street> streets = new ArrayList<Street>();
    /// All of the stops in town
    private List<Stop> stops = new ArrayList<Stop>();

    /**
     * Constructor
     * @param name
     */
    public Map(String name){
        this.town_name = name;
    }

    /**
     * Load data from json files. If map has some data, there will be delete first.
     * @param streets_file with data about streets and stops
     * @param lines_file with data about lines
     * @return true if loading data was successful, otherwise false
     */
    public boolean load_data(String streets_file, String lines_file){
        this.lines.clear();
        this.streets.clear();
        this.stops.clear();
        if(load_streets(streets_file)){
            if(load_lines(lines_file)){
                return true;
            }
        }
        return false;
    }

    /**
     * Load data about streets and stops from json file. That data store into the list of streets and stops.
     * @param streets_file json file
     * @return true if loading data was successful, otherwise false
     */
    private boolean load_streets(String streets_file){
        String street_name;
        List<Coordinate> street_coordinates = new ArrayList<>();
        List<Stop> street_stops = new ArrayList<>();
        try {
            Object street_obj = new JSONParser().parse(new FileReader(streets_file));         
            JSONArray streets = (JSONArray) street_obj;
            //For loop via all streets in json file.
            for(Object object : streets){
                JSONObject street = (JSONObject) object;
                street_coordinates.clear();
                street_stops.clear();
                street_name = (String) street.get("name");
                //For loop via all coordinates for one street.
                for(Object coordinate_obj : street.get("coordinates")){
                    street_coordinates.add(create_coordinate((JSONObject) coordinate_obj));
                    if(street_coordinates.get(street_coordinates.size()-1) == null){
                        return false;
                    }
                }
                //For loop via all stops for one street.
                for(Object stop_obj : street.get("stops")){
                    Stop stop = create_stop((JSONObject) stop_obj);
                    if(stop == null){
                        return false;
                    }
                    this.stops.add(stop);
                    street_stops.add(stop);
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Create coordinate object with data from json file.
     * @param coordinate_json json object with x and y information
     * @return null if x or y isn`t in interval <0, inf), otherwise object of type Coordinate
     */
    private Coordinate create_coordinate(JSONObject coordinate_json){
        float x = (float) coordinate_json.get("x");
        float y = (float) coordinate_json.get("y");
        return Coordinate.create(x, y);
    }

    /**
     * Create stop object with data from json file.
     * @param stop_json json object with information about stop
     * @return null if x or y isn`t in interval <0, inf), otherwise object of type Stop
     */
    private Stop create_stop(JSONObject stop_json){
        Coordinate coordinate = create_coordinate((JSONObject) stop_json.get("coordinates"));
        if(coordinate == null){
            return null;
        }
        return new Stop((String) stop_json.get("name"), coordinate);
    }

    /**
     * Load data about lines from json file. That data store into the list of lines.
     * @param lines_file json file
     * @return true if loading data was successful, otherwise false
     */
    private boolean load_lines(String lines_file){
        long line_id;
        List<String> street_list = new ArrayList<>();
        List<String> stop_list = new ArrayList<>();
        try {
            Object line_obj = new JSONParser().parse(new FileReader(lines_file));         
            JSONArray lines = (JSONArray) line_obj;
            //For loop via all lines.
            for(Object object : lines){
                JSONObject line = (JSONObject) object;
                line_id = (long) line.get("id");
                //Create new Line object and add him into the list of lines.
                this.lines.add(new Line((int) line_id));
                street_list.clear();
                JSONArray streets = (JSONArray) line.get("streets");
                //For loop via all streets for one line.
                for(Object street_obj : streets){
                    street_list.add((String) street_obj);
                }            
                stop_list.clear();
                //For loop via all stops for one line.
                JSONArray stops = (JSONArray) line.get("stops");
                for(Object stop_obj : stops){
                    stop_list.add((String) stop_obj);
                }
                if(!prepare_line(this.lines.get(this.lines.size()-1), street_list, stop_list)){
                    return false;
                }
            }   
        } catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * Prepare line for her first move.
     * @param line for prepare
     * @param streets which line crossing
     * @param stops where line stopping
     * @return true if prepare was successful, otherwise false
     */
    private boolean prepare_line(Line line, List<String> streets, List<String> stops){
        List<Street> street_list = new ArrayList<>();
        List<Stop> stop_list = new ArrayList<>();
        //Find streets by they names.
        for(String street_name : streets){
            boolean street_found_flag = false;
            for(Street street : this.streets){
                if(street.getId().equals(street_name)){
                    street_list.add(street);
                    street_found_flag = true;
                    break;
                }
                if(!street_found_flag){
                    return false;
                }
            }
        }
        //Find stops by they names.
        for(String stop_name : stops){
            boolean stop_found_flag = false;
            for(Stop stop : this.stops){
                if(stop.getName().equals(stop_name)){
                    stop_list.add(stop);
                    stop_found_flag = true;
                    break;
                }
                if(!stop_found_flag){
                    return false;
                }
            }
        }
        return line.prepare(street_list, stop_list, stop_list.get(0).getCoordinate());
    }
}
