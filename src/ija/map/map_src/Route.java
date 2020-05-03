/*
 * Source code for the final IJA project
 * Line class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 * 
 */

package ija.map.map_src;

import java.util.List;
import java.util.AbstractMap.SimpleImmutableEntry;


public class Route{

    
    private List<SimpleImmutableEntry<Coordinate, Street>> route;
    private List<Stop> stops;

    /**
     * Constructor
     */
    private Route(List<Street> streets, List<Stop> stops_to_visit){
        for (Street s : streets) {
            List<Coordinate> c = s.getCoordinates();
            for (Coordinate current_c : c) {
                SimpleImmutableEntry<Coordinate, Street> tmp_position = new SimpleImmutableEntry<Coordinate, Street>(c, s);
                
                // Add possition to the route
                this.route.add(tmp_position);
            }
        }
    }

    public Route defaultRoute(List<Street> streets, List<Stop> stops_to_visit){
        Street previous_street = null;
        for (Street s : streets) {

            // Are the given streets connected?
            if (previous_street != null){
                if (s.follows(previous_street)){
                    return null;
                }
            }
            previous_street = s;
        }
        return new Route(streets, stops_to_visit);
    }

    /**
     * 
     * @return
     */
    public SimpleImmutableEntry<Coordinate, Street> get_first(){

    }

    public SimpleImmutableEntry<Coordinate, Street> get_next(){

    }

}