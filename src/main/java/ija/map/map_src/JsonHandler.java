/*
 * Source code for the final IJA project
 * Json_handler class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 * 
 */


package ija.map.map_src;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import java.io.FileReader;

import ija.map.Map;

/**
 * This handler work with json files and load data from them.
 * Data store into map.
 */
public class JsonHandler {
    /// For store and get lists of streets, stops and lines.
    private Map map;


    /**
     * Constructor
     * @param map Object of type map where will be stored loaded data about streets, stops and lines.
     */
    public JsonHandler(Map map){
        this.map = map;
    }


    /**
     * Load data about streets and stops from json file. That data store into the list of streets and stops
     * from object map.
     * @param streets_file Streets_file is file, that content data about streets
     * @return true if loading data was successful, false otherwise
     */
    public boolean loadStreets(String streets_file){
        String street_name;
        List<Coordinate> street_coordinates = new ArrayList<>();
        Street new_street;
        try {
            Object street_obj = new JSONParser().parse(new FileReader(streets_file));         
            JSONArray streets = (JSONArray) street_obj;
            //For loop via all streets in json file.
            for(Object object : streets){
                JSONObject street = (JSONObject) object;
                street_coordinates.clear();
                street_name = (String) street.get("name");
                //For loop via all coordinates for one street.
                for(Object coordinate_obj : (JSONArray) street.get("coordinates")){
                    street_coordinates.add(createCoordinate((JSONObject) coordinate_obj));
                    if(street_coordinates.get(street_coordinates.size()-1) == null){
                        return false;
                    }
                }
                new_street = new Street(street_name, street_coordinates);
                //For loop via all stops for one street.
                for(Object stop_obj : (JSONArray) street.get("stops")){
                    Stop stop = createStop((JSONObject) stop_obj);
                    if(stop == null){
                        return false;
                    }
                    this.map.getStops().add(stop);
                    boolean result = new_street.addStop(stop);
                    if (!result)
                        return false;
                }
                this.map.getStreets().add(new_street);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * Create coordinate object with data from json file.
     * @param coordinate_json Coordinate_json is json object with x and y information
     * @return null if x or y isn`t in interval <0, inf), object of type Coordinate otherwise
     */
    private Coordinate createCoordinate(JSONObject coordinate_json){
        float x = Float.parseFloat(coordinate_json.get("x").toString());
        float y = Float.parseFloat(coordinate_json.get("y").toString());
        return Coordinate.create(x, y);
    }


    /**
     * Create stop object with data from json file.
     * @param stop_json Stop_json is json object with information about stop
     * @return null if x or y isn`t in interval <0, inf), object of type Stop otherwise
     */
    private Stop createStop(JSONObject stop_json){
        Coordinate coordinate = createCoordinate((JSONObject) stop_json.get("coordinates"));
        if(coordinate == null){
            return null;
        }
        return new Stop((String) stop_json.get("name"), coordinate);
    }


    /**
     * Load data about lines from json file. That data stores into the list of lines.
     * @param lines_file Lines_file is json, that content data about lines
     * @return true if loading data was successful, false otherwise
     */
    public boolean loadLines(String lines_file){
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
                this.map.getLines().add(new Line((int) line_id));
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
                if(!prepareLine(this.map.getLines().get(this.map.getLines().size()-1), street_list, stop_list)){
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
     * @param line Line is object which will be prepare for use
     * @param streets Streets is list of streets which line crossing
     * @param stops Stops is list of stops where line will stopping
     * @return true if prepare was successful, false otherwise
     */
    private boolean prepareLine(Line line, List<String> streets, List<String> stops){
        List<Street> street_list = new ArrayList<>();
        List<Stop> stop_list = new ArrayList<>();
        //Find streets by they names.
        for(String street_name : streets){
            boolean street_found_flag = false;
            for(Street street : this.map.getStreets()) {
                if (street.getId().equals(street_name)) {
                    street_list.add(street);
                    street_found_flag = true;
                    break;
                }
            }
            if(!street_found_flag) {
                return false;
            }
        }
        //Find stops by they names.
        for(String stop_name : stops){
            boolean stop_found_flag = false;
            for(Stop stop : this.map.getStops()) {
                if (stop.getName().equals(stop_name)) {
                    stop_list.add(stop);
                    stop_found_flag = true;
                    break;
                }
            }
            if(!stop_found_flag){
                return false;
            }
        }
        return line.prepare(street_list, stop_list, stop_list.get(0).getCoordinate());
    }
}
