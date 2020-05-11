/*
 * Source code for the final IJA project
 * Map class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 * 
 *//*


package map;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import map.map_src.Stop;
import map.map_src.Line;
import map.map_src.Street;
import map.map_src.Json_handler;

*/
/**
 * Representing the Map. Map contains everything about the town (lines, streets,
 * stops)
 *//*

public class Map extends Thread {
    /// Handler for json files with streets, stops and lines.
    private Json_handler json_handler;
    /// Name of the town, that this map represents
    private String town_name;
    /// All of the lines in town
    private List<Line> lines = new ArrayList<Line>();
    /// All of the streets in town
    private List<Street> streets = new ArrayList<Street>();
    /// All of the stops in town
    private List<Stop> stops = new ArrayList<Stop>();
    /// Time between next move with all lines in milliseconds.
    private int sleep_time = 5;
    /// Pause move operation.
    private boolean pause;

    */
/**
     * Constructor
     * 
     * @param name
     *//*

    public Map(String name) {
        this.town_name = name;
        this.json_handler = new Json_handler(this);
    }

    */
/**
     * Getter for lines.
     * 
     * @return the lines
     *//*

    public List<Line> getLines() {
        return this.lines;
    }

    */
/**
     * Getter for stops.
     * 
     * @return the stops
     *//*

    public List<Stop> getStops() {
        return this.stops;
    }

    */
/**
     * Getter for streets.
     * 
     * @return the streets
     *//*

    public List<Street> getStreets() {
        return this.streets;
    }

    */
/**
     * Getter for town name.
     * 
     * @return the town_name
     *//*

    public String getTown_name() {
        return this.town_name;
    }

    */
/**
     * Setter for sleep time between next move with lines in milliseconds.
     * @param sleep_time the sleep_time to set
     *//*

    public void setSleep_time(int sleep_time) {
        this.sleep_time = sleep_time;
    }

    */
/**
     * Set pause to true.
     *//*

    public void map_pause(){
        this.pause = true;
    }

    */
/**
     * Set pause to false.
     *//*

    public void map_resume(){
        this.pause = false;
    }

    */
/**
     * Stop running lines on map, this method can by used only one time.
     * If it`s used, the map cannot run anymore.
     *//*

    public void map_stop(){
        this.interrupt();
    }

    */
/**
     * Simulate moves on map with lines.
     *//*

    @Override
    public void run() {
        while (true) {
            try {
                one_move();
                TimeUnit.MILLISECONDS.sleep(sleep_time);
                while(pause){
                    TimeUnit.MILLISECONDS.sleep(200);
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }
    
    */
/**
     * Simulate one move with lines on map
     *//*

    private void one_move(){
        for(Line line : this.lines){
            if(!line.wait_on_stop()){
                line.move();
            }
        }
    }

    */
/**
     * Load data from json files. If map has some data, there will be delete first.
     * @param streets_file with data about streets and stops
     * @param lines_file with data about lines
     * @return true if loading data was successful, otherwise false
     *//*

    public boolean load_data(String streets_file, String lines_file){
        this.lines.clear();
        this.streets.clear();
        this.stops.clear();
        if(json_handler.load_streets(streets_file)){
            if(json_handler.load_lines(lines_file)){
                return true;
            }
        }
        return false;
    }
}
*/
