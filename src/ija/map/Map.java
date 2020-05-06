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
}