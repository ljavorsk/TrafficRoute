/*
 * Source code for the final IJA project
 * Line class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 * 
 */

package ija.map.map_src;

import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleImmutableEntry;

/**
 * Represents the Route that will Line follow
 * The route contains ordered streets and stops that defines the route
 */
public class Route{
    /// Route that the Line will take
    private List<SimpleImmutableEntry<Coordinate, Street>> route;
    /// List of the stops within the route
    private List<Stop> stops;
    /// List of the streets within the route in order
    private List<Street> streets;

    /**
     * Constructor for the Route
     */
    private Route(List<Street> streets_to_visit, List<Stop> stops_to_visit, Coordinate starting_point){
        this.stops = stops_to_visit;
        this.streets = streets_to_visit;

        // Fill the route
        for (Street s : streets) {
            List<Coordinate> coordinates_in_street = s.getCoordinates();
            boolean backwards_coordinates = false;

            // We are going from end of the street to the beginning, so coordinates have to be reordered
            if (starting_point == s.endOfTheStreet() || coordinates_in_street.get(0) == s.endOfTheStreet()){
                // Set flag
                backwards_coordinates = true;
            }

            for (Coordinate current_c : coordinates_in_street) {
                SimpleImmutableEntry<Coordinate, Street> tmp_position = new SimpleImmutableEntry<Coordinate, Street>(current_c, s);

                if (backwards_coordinates){
                    List<SimpleImmutableEntry<Coordinate, Street>> tmp_route = new ArrayList<SimpleImmutableEntry<Coordinate, Street>>();
                    tmp_route.add(tmp_position);

                    // Backwards iteration
                    for (int i = tmp_route.size(); i-- > 0;){
                        this.route.add(tmp_route.get(i));
                    }
                }
                else {
                    // Just add possition to the route
                    this.route.add(tmp_position);
                }  
            }
        }
    }

    /**
     * Creates the Route, if the parameters are correct
     * @param streets Which streets does the route cointain, must be ordered
     * @param stops_to_visit Which stops does the route contain, must be ordered
     * @param starting_point Wehre does the route begins
     * @return Route object, or null in case that the streets are not connected, or starting_point is not the beginning
     * or end of the first street in the streets
     */
    public Route defaultRoute(List<Street> streets, List<Stop> stops_to_visit, Coordinate starting_point){
        if (!(starting_point == streets.get(0).beginOfTheStreet() || starting_point == streets.get(0).endOfTheStreet())){
            return null;
        }

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
        return new Route(streets, stops_to_visit, starting_point);
    }

    /**
     * Getter for the starting possition possition
     * @return Where the route starts
     */
    public SimpleImmutableEntry<Coordinate, Street> getFirst(){
        return this.route.get(0);
    }

    /**
     * Getter for the next possition of the route
     * @param current_possition Current possition, where the Line is
     * @return Next possition for Line, or null if the current_possition is not part of the route
     */
    public SimpleImmutableEntry<Coordinate, Street> getNext(SimpleImmutableEntry<Coordinate, Street> current_possition){
        int index = this.route.indexOf(current_possition);

        if (index != -1){
            return this.route.get(index + 1);
        }
        else{
            // The current possition is not part of the route
            return null;
        }
    }

    /**
     * Getter for the stops in route
     * They should be ordered
     * @return List of stops
     */
    public List<Stop> getStops(){
        return this.stops;
    }

    /**
     * Getter for the streets in route
     * They are ordered
     * @return List of streets
     */
    public List<Street> getStreets(){
        return this.streets;
    }

    /**
     * Returns if the Line should stop on given coordinate
     * @param c Coordinate that may be the stop in route
     * @return true or false, acording to the result
     */
    public boolean shouldStop(Coordinate c){
        for (Stop stop : this.stops) {
            if (c == stop.getCoordinate()){
                return true;
            }
        }
        return false;
    }

    /**
     * Recalculate the route with given closed_street and given detour_streets
     * @param closed_street The street that is closed, and have to be deleted from route
     * @param detour_streets The streets that will be connected to the route.
     * They have to be ordered and first one needs to be connected to street that was closed
     * @return Recalculated route, or null if parameters are wrong
     */
    public Route detourRoute(Street closed_street, Street... detour_streets){
        // Closed street is not part of the route
        if (!this.streets.contains(closed_street)){
            return null;
        }

        // TODO

        return null;

    }


}