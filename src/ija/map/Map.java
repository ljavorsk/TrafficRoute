/*
 * Source code for the final IJA project
 * Map class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 * 
 */

package ija.map;

import java.util.ArrayList;
import java.util.List;

import ija.map.map_src.Coordinate;
import ija.map.map_src.Line;
import ija.map.map_src.Stop;
import ija.map.map_src.Street;
import library.org.json.simple.JSONArray; 
import library.org.json.simple.JSONObject; 
import library.org.json.simple.parser.*; 
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

    public Map(String name){
        this.town_name = name;
    }

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

    private boolean load_streets(String streets_file){
        String street_name;
        List<Coordinate> street_coordinates = new ArrayList<>();
        List<Stop> street_stops = new ArrayList<>();
        try {
            Object street_obj = new JSONParser().parse(new FileReader(streets_file));         
            JSONArray streets = (JSONArray) street_obj;
            for(Object object : streets){
                JSONObject street = (JSONObject) object;
                street_coordinates.clear();
                street_stops.clear();
                street_name = (String) street.get("name");
                for(Object coordinate_obj : street.get("coordinates")){
                    street_coordinates.add(create_coordinate((JSONObject) coordinate_obj));
                    if(street_coordinates.get(street_coordinates.size()-1) == null){
                        return false;
                    }
                }
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

    private Coordinate create_coordinate(JSONObject coordinate_json){
        float x = (float) coordinate_json.get("x");
        float y = (float) coordinate_json.get("y");
        return Coordinate.create(x, y);
    }

    private Stop create_stop(JSONObject stop_json){
        Coordinate coordinate = create_coordinate((JSONObject) stop_json.get("coordinates"));
        if(coordinate == null){
            return null;
        }
        return new Stop((String) stop_json.get("name"), coordinate);
    }

    private boolean load_lines(String lines_file){
        long line_id;
        List<String> street_list = new ArrayList<>();
        List<String> stop_list = new ArrayList<>();
        try {
            Object line_obj = new JSONParser().parse(new FileReader(lines_file));         
            JSONArray lines = (JSONArray) line_obj;
            for(Object object : lines){
                JSONObject line = (JSONObject) object;
                line_id = (long) line.get("id");
                this.lines.add(new Line((int) line_id));
                street_list.clear();
                JSONArray streets = (JSONArray) line.get("streets");
                for(Object street_obj : streets){
                    street_list.add((String) street_obj);
                }            
                stop_list.clear();
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

    private boolean prepare_line(Line line, List<String> streets, List<String> stops){
        List<Street> street_list = new ArrayList<>();
        List<Stop> stop_list = new ArrayList<>();
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
