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
import java.util.Objects;
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
        Coordinate street_start_point = starting_point;

        // Fill the route
        for (Street s : streets) {
            boolean backwards_coordinates = false;

            // We are going from end of the street to the beginning, so coordinates have to be reordered
            if (street_start_point.equals(s.endOfTheStreet())){
                // Set flag
                backwards_coordinates = true;
            }
            street_start_point = addStreetToRoute(s, backwards_coordinates, -1);
        }
    }

    /**
     * Creates the Route, if the parameters are correct
     * @param streets Which streets does the route contain, must be ordered
     * @param stops_to_visit Which stops does the route contain, must be ordered
     * @param starting_point Where does the route begins
     * @return Route object, or null in case that the streets are not connected, or starting_point is not the beginning
     * or end of the first street in the streets
     */
    public static Route defaultRoute(List<Street> streets, List<Stop> stops_to_visit, Coordinate starting_point){
        if (!(starting_point.equals(streets.get(0).beginOfTheStreet()) || starting_point.equals(streets.get(0).endOfTheStreet()))){
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
     * Add every Coordinate with Street to the route.
     * If the index parameter is given (not -1), it adds it to given index (used in detour_route func)
     * @param s Street to add
     * @param reorder_coordinates If the coordinates needs to be reordered
     * @param index Index, where you want to add the Street in route, fill -1 if you don't want to use index
     * @return Last coordinate added to the route
     */
    private Coordinate addStreetToRoute(Street s, boolean reorder_coordinates, int index){
        List<Coordinate> coordinates_in_street = s.getCoordinates();
        Coordinate last_coordinate = null;

        for (Coordinate current_c : coordinates_in_street) {
            SimpleImmutableEntry<Coordinate, Street> tmp_position = new SimpleImmutableEntry<Coordinate, Street>(current_c, s);

            if (reorder_coordinates){
                List<SimpleImmutableEntry<Coordinate, Street>> tmp_route = new ArrayList<SimpleImmutableEntry<Coordinate, Street>>();
                tmp_route.add(tmp_position);

                // Backwards iteration
                for (int i = tmp_route.size(); i-- > 0;){
                    if (index != -1){
                        this.route.add(index, tmp_route.get(i));
                    }
                    this.route.add(tmp_route.get(i));
                }
            }
            else {
                // Just add position to the route
                if (index != -1){
                    this.route.add(index, tmp_position);
                }
                this.route.add(tmp_position);
            }
            last_coordinate = current_c; 
        }
        // Return where is the next street starting point
        return last_coordinate;
    }

    /**
     * Getter for the starting position position
     * @return Where the route starts
     */
    public SimpleImmutableEntry<Coordinate, Street> getFirst(){
        return this.route.get(0);
    }

    /**
     * Getter for the next position of the route
     * @param current_position Current position, where the Line is
     * @return Next position for Line, or null if the current_position is not part of the route
     */
    public SimpleImmutableEntry<Coordinate, Street> getNext(SimpleImmutableEntry<Coordinate, Street> current_position){
        int index = this.route.indexOf(current_position);

        if (index != -1){
            return this.route.get(index + 1);
        }
        else{
            // The current position is not part of the route
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
     * @return true or false, according to the result
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
     * It replaces the closed_street coordinates within the route with the
     * new detour_streets coordinates.
     * @param closed_street The street that is closed, and have to be deleted from route
     * @param detour_streets The streets that will be connected to the route.
     * They have to be ordered and first one needs to be connected to street that was closed
     * @return true if everything went well, false otherwise
     */
    public boolean detourRoute(Street closed_street, Street... detour_streets){
        // Closed street is not part of the route
        if (!this.streets.contains(closed_street)){
            return false;
        }

        Coordinate first_connection_coordinate = null;
        // Index where the new coordinates will be added (closed street starting point)
        int index_where_add = null;

        for (SimpleImmutableEntry<Coordinate, Street> position_to_be_deleted : this.route) {
            // Find first occurrence of the closed street (connecting Coordinate)
            if (position_to_be_deleted.getValue() == closed_street){
                index_where_add = this.route.indexOf(position_to_be_deleted);
                first_connection_coordinate = position_to_be_deleted.getKey();
                break;
            }
        }

        List<Street> list_of_streets = Arrays.asList(detour_streets);
        Street new_first_street = list_of_streets.get(0);
        Street new_last_street = list_of_streets.get(list_of_streets.size() - 1);
        
        // Check if the first street is connected to the closed point
        if (!(first_connection_coordinate.equals(new_first_street.beginOfTheStreet()) ||
            first_connection_coordinate.equals(new_first_street.endOfTheStreet()))){
            // It's not correctly connected
            return false;
        } else {
            // It is connected, so delete blocked street coordinates
            // Using lambdas to delete any coordinate with closed street
            this.route.removeIf(n -> Objects.equals(n, closed_street));
        }

        // Now when the route has deleted the closed route, index points to beggining of the street
        // connected to the closed one (same coordinate as end of the closed street)
        if (!(this.route.get(index_where_add).getKey().equals(new_last_street.beginOfTheStreet()) || 
            this.route.get(index_where_add).getKey().equals(new_last_street.endOfTheStreet()))){
            // It's not correctly connected
            return false;
        }

        // Start filling the coordinates to the route
        for (Street street : list_of_streets) {
            boolean backwards_coordinates = false;

            // We are going from end of the street to the beginning, so coordinates have to be reordered
            if (first_connection_coordinate.equals(s.endOfTheStreet())){
                // Set flag
                backwards_coordinates = true;
            }
            // Increment the index, so the street goes in order
            first_connection_coordinate = addStreetToRoute(s, backwards_coordinates, index_where_add++);
        }

        // TODO delete Street and add new streets?
        // What to do with Stops

        return true;
    }
}