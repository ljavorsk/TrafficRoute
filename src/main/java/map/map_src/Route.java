/*
 * Source code for the final IJA project
 * Route class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 * 
 */

package map.map_src;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.AbstractMap.SimpleImmutableEntry;

/**
 * Represents the Route that will Line follow
 * The route contains ordered streets and stops that defines the route
 */
public class Route implements Drawable{
    /// Route that the Line will take
    private final List<SimpleImmutableEntry<Coordinate, Street>> route = new ArrayList<>();
    /// List of the stops within the route
    private final List<Stop> stops;
    /// List of the streets within the route in order
    private final List<Street> streets;
    /// Starting point of the route
    private final Coordinate starting_point;
    /// Route GUI shape
    private final List<Shape> route_shape = new ArrayList<>();

    /**
     * Constructor for the Route
     */
    private Route(List<Street> streets_to_visit, List<Stop> stops_to_visit, Coordinate starting_p){
        this.stops = stops_to_visit;
        this.streets = streets_to_visit;
        this.starting_point = starting_p;
        Coordinate new_street_start_point = starting_point;

        // Fill the route
        for (Street s : streets) {
            boolean backwards_coordinates = false;

            // We are going from end of the street to the beginning, so coordinates have to be reordered
            if (new_street_start_point.equals(s.endOfTheStreet())){
                // Set flag
                backwards_coordinates = true;
            }
            new_street_start_point = addStreetToRoute(s, backwards_coordinates);
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
        // Route have to start in the first stop in stops_to_visit
        if (!(starting_point.equals(stops_to_visit.get(0).getCoordinate()))){
            return null;
        }

        Street previous_street = null;
        for (Street s : streets) {

            // Are the given streets connected?
            if (previous_street != null){
                if (!s.follows(previous_street)){
                    return null;
                }
            }
            previous_street = s;
        }

        // Check if the stops are located in the streets
        int stop_count = stops_to_visit.size();
        for (Stop stop : stops_to_visit){
            for (Street street: streets) {
                if (stop.getStreet().equals(street))
                    stop_count--;
            }
        }
        if (stop_count != 0)
            return null;

        return new Route(streets, stops_to_visit, starting_point);
    }

    /**
     * Add every Coordinate with Street to the route.
     * If the index parameter is given (not -1), it adds it to given index (used in detour_route func)
     * @param s Street to add
     * @param reorder_coordinates If the coordinates needs to be reordered
     * @return Last coordinate added to the route
     */
    private Coordinate addStreetToRoute(Street s, boolean reorder_coordinates){
        List<Coordinate> coordinates_in_street = s.getCoordinates();
        Coordinate last_coordinate = null;

        List<SimpleImmutableEntry<Coordinate, Street>> tmp_route = new ArrayList<SimpleImmutableEntry<Coordinate, Street>>();
        for (Coordinate current_c : coordinates_in_street) {
            SimpleImmutableEntry<Coordinate, Street> tmp_position = new SimpleImmutableEntry<Coordinate, Street>(current_c, s);

            if (reorder_coordinates){
                tmp_route.add(tmp_position);
            }
            else {
                // Just add position to the route
                this.route.add(tmp_position);
            }
            last_coordinate = current_c; 
        }
        if (reorder_coordinates){
            // Backwards iteration
            for (int i = tmp_route.size(); i-- > 0;){
                this.route.add(tmp_route.get(i));
            }
            return this.route.get(this.route.size() -1).getKey();
        }
        // Return where is the next street starting point
        return last_coordinate;
    }

    /**
     * Getter for the starting point of the route
     * @return Starting point of the route
     */
    public Coordinate getStartingPoint(){
        return this.stops.get(0).getCoordinate();
    }

    /**
     * Getter for the ending point of the route
     * @return Ending point of the route
     */
    public Coordinate getEndingPoint(){
        return this.stops.get(this.stops.size()-1).getCoordinate();
    }

    /**
     * Getter for the starting position of the bus
     * @return Where the route starts
     */
    public SimpleImmutableEntry<Coordinate, Street> getFirst(){
        return new SimpleImmutableEntry<Coordinate, Street>(this.stops.get(0).getCoordinate(), this.stops.get(0).getStreet());
    }

    /**
     * Getter for the next position of the route
     * @param current_position Current position, where the Line is
     * @return Next position for Line, or null if the current_position is not part of the route
     */
    public SimpleImmutableEntry<Coordinate, Street> getNext(SimpleImmutableEntry<Coordinate, Street> current_position){
        int index = this.route.indexOf(current_position);

        if (index != -1){
            // Is there something next?
            if (index < this.route.size()){
                return this.route.get(index + 1);
            }
            return null;
        }
        else{
            // The current position is not part of the route
            return null;
        }
    }

    /**
     * Getter for the previous position of the route
     * Can be used, when the line reaches the end of the route and wants to go back
     * @param current_position Current position in route, where the Line is
     * @return Previous position for Line, or null if the current_position is not part of the route, or there is nothing previous
     */
    public SimpleImmutableEntry<Coordinate, Street> getPrevious(SimpleImmutableEntry<Coordinate, Street> current_position){
        int index = this.route.indexOf(current_position);

        if (index != -1){
            // Is there something previous?
            if (index > 0){
                return this.route.get(index - 1);
            }
            return null;
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
            if (c.equals(stop.getCoordinate())){
                return true;
            }
        }
        return false;
    }

    /**
     * Recalculate the route with given closed_street and given detour_streets
     * It creates new Route with new detoure_streets that replaced closed_street in previous route
     * @param closed_street The street that is closed, and have to be deleted from route
     * @param list_of_streets The streets that will be connected to the route.
     * They have to be ordered, first one needs to be connected to street that was closed
     * and the last one needs to be connected to the other end of the closed_street
     * @return New Route object, or null if anything went wrong
     */
    public Route detourRoute(Street closed_street, List<Street> list_of_streets){
        // Closed street is not part of the route
        if (!this.streets.contains(closed_street)){
            return null;
        }

        Coordinate first_connection_coordinate = null;
        // Index where the new coordinates will be added (closed street starting point)
        int index_where_add = -1;

        for (SimpleImmutableEntry<Coordinate, Street> position_to_be_deleted : this.route) {
            // Find first occurrence of the closed street (connecting Coordinate)
            if (position_to_be_deleted.getValue() == closed_street){
                index_where_add = this.route.indexOf(position_to_be_deleted);
                if (index_where_add == -1)
                    return null;
                first_connection_coordinate = position_to_be_deleted.getKey();
                break;
            }
        }

        // The coordinate is not there
        if (first_connection_coordinate == null){
            return null;
        }

        Street new_first_street = list_of_streets.get(0);
        Street new_last_street = list_of_streets.get(list_of_streets.size() - 1);
        
        // Check if the first street is connected to the closed point
        if (!(first_connection_coordinate.equals(new_first_street.beginOfTheStreet()) ||
            first_connection_coordinate.equals(new_first_street.endOfTheStreet()))){
            // It's not correctly connected
            return null;
        }

        // Now when the route has deleted the closed route, index points to beggining of the street
        // connected to the closed one (same coordinate as end of the closed street)
        if (!(this.route.get(index_where_add).getKey().equals(new_last_street.beginOfTheStreet()) || 
            this.route.get(index_where_add).getKey().equals(new_last_street.endOfTheStreet()))){
            // It's not correctly connected
            return null;
        }

        // Add new streets in route
        int street_index = this.streets.indexOf(closed_street);
        for (Street street : list_of_streets) {
            this.streets.add(street_index++, street);
        }

        // Delete the closed street
        this.streets.remove(closed_street);

        // Delete stops on the closed street
        stops.removeIf(stop -> stop.getStreet().equals(closed_street));

        // Create new Route with edited streets
        return new Route(this.streets, this.stops, this.starting_point);
    }

    @Override
    public List<Shape> getShapes() {
        boolean starting_stop_found = false;
        for (int i = 0; i < route.size(); i++) {
            Coordinate first = route.get(i).getKey();

            // Route starts at the starting bus stop so we need to skip the coordinates until the stop's coordinate
            if (!first.equals(getStartingPoint()) && !starting_stop_found)
                continue;
            else {
                starting_stop_found = true;
            }

            // Ending point of the route we can stop adding route's shape
            if(first.equals(getEndingPoint()))
                break;
            Coordinate second = null;
            if (route.size() > i + 1) {
                second = route.get(i + 1).getKey();
                Line line = new Line(first.getX(), first.getY(), second.getX(), second.getY());
                line.setStroke(Color.PURPLE);
                line.setOpacity(0);
                line.setStrokeWidth(5);
                route_shape.add(line);
            }
        }
        return route_shape;
    }

    /**
     * Selects the route
     * Increases it's opacity
     * Also selects every stop on the route
     */
    public void selectRoute(){
        for (Shape line : route_shape) {
            line.setOpacity(0.5);
        }
        for (Stop stop : stops) {
            stop.selectStop(Color.PURPLE);
        }
    }

    /**
     * Deselect the route
     * Decreases it's opacity to 0 so it's invisible
     * Also deselects every stop on the route
     */
    public void deselectRoute(){
        for (Shape line : route_shape) {
            line.setOpacity(0);
        }
        for (Stop stop : stops) {
            stop.deselectStop();
        }
    }

}