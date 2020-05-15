/*
 * Source code for the final IJA project
 * Street class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 * 
 */

package map.map_src;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;


/**
 * Represents the Street in the Map.
 * The Street have it's unique ID, representing Coordinates, and may have Stops
 */
public class Street implements Drawable{
    /// Unique street's name
    private final String id_str;
    /// Ordered coordinates that represents the street
    private final List<Coordinate> list_of_coordinates;
    /// Stops that are located on the street
    private final List<Stop> list_of_stops = new ArrayList<Stop>();
    /// How much traffic is at the road. Values are 1,2,3,4. Default value is 1 (minimal). 
    private short traffic_overload = 1;
    /// Value if street is closed or opened.
    private boolean closed = false;
    /// Street's GUI shape
    private final List<Shape> street_shape = new ArrayList<>();

    /**
     * Constructor for the Street
     * @param id ID of the street
     * @param coordinates List of coordinates that represents the street,
     * there have to be stops coordinates and coordinates where new streets are connected.
     */
    public Street(String id, List<Coordinate> coordinates){
        this.id_str = id;
        this.list_of_coordinates = new ArrayList<>(coordinates);
    }

    /**
     * Getter for the Street's ID
     * @return ID
     */
    public String getId() {
        return this.id_str;
    }

    /**
     * Getter for the list of Coordinates of the street. First one is the beginning and the last one is the end of the street
     * @return List if Coordinates
     */
    public List<Coordinate> getCoordinates() {
        return this.list_of_coordinates;
    }

    /**
     * Getter for the list of Stops within the Street
     * @return List of Stops
     *         if the Street doesn't have a stop, List is empty
     */
    public List<Stop> getStops() {
        return this.list_of_stops;
    }

    /**
     * Getter for information if street is closed.
     * @return true if it`s closed, otherwise false
     */
    public boolean isClosed(){
        return this.closed;
    }

    /**
     * Open street. Set closed to false.
     */
    public void open_street(){
        this.closed = false;
    }

    /**
     * Close street. Set closed to true.
     */
    public void close_street(){
        this.closed = true;
    }

    /**
     * Adds the Stop in the Street's list of Stops
     * @param stop Stop that's going to be added
     * @return If the operation was successful
     */
    public boolean addStop(Stop stop) {
        Coordinate stop_c = stop.getCoordinate();

        for (Coordinate streets_c: list_of_coordinates) {
            if (stop_c.equals(streets_c)){
                this.list_of_stops.add(stop);
                stop.setStreet(this);
                return true;
            }
        }
        return false;
    }

    /**
     * Return the end of the street
     * @return ending Coordinate
     */
    public Coordinate endOfTheStreet() {
        List<Coordinate> street_coords = this.getCoordinates();
        return street_coords.get(street_coords.size() -1);
    }

    /**
     * Return the begin of the street
     * @return beginning Coordinate
     */
    public Coordinate beginOfTheStreet() {
        List<Coordinate> street_coords = this.getCoordinates();
        return street_coords.get(0);
    }

    /**
     * Return if the given Street follows the current Street
     * @param s Street to compare
     * @return true if the given street follows the current one, false if not
     */
    public boolean follows(Street s) {
        if (s == null){
            return false;
        }
        Coordinate beginning_first = this.beginOfTheStreet();
        Coordinate end_first = this.endOfTheStreet();

        Coordinate beginning_second = s.getCoordinates().get(0);
        Coordinate end_second = s.getCoordinates().get(s.getCoordinates().size() -1);

        if (beginning_first.equals(end_second) || beginning_second.equals(end_first) ||
        beginning_first.equals(beginning_second) || end_first.equals(end_second)){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Sets the value of traffic overload on the street
     * @param value One of the values - 1 (minimal), 2, 3, 4 (maximal)
     */
    public void setTrafficOverload(short value){
        this.traffic_overload = value;
    }

    /**
     * Getter for the traffic_overload
     * @return One of the values - 1 (minimal), 2, 3, 4 (maximal)
     */
    public short getTrafficOverload(){
        return this.traffic_overload;
    }

    @Override
    public List<Shape> getShapes() {
        street_shape.add(new Text(Math.abs((beginOfTheStreet().getX() + endOfTheStreet().getX()))/2,
                Math.abs((beginOfTheStreet().getY() + endOfTheStreet().getY()))/2, id_str));

        for (int i = 0; i < list_of_coordinates.size(); i++) {
            Coordinate first = list_of_coordinates.get(i);
            Coordinate second = null;
            if (list_of_coordinates.size() > i + 1) {
                second = list_of_coordinates.get(i + 1);
                Line line = new Line(first.getX(), first.getY(), second.getX(), second.getY());
                Line highlight_line = new Line(first.getX(), first.getY(), second.getX(), second.getY());
                highlight_line.setStrokeWidth(4);
                highlight_line.setOpacity(0);
                street_shape.add(line);
                street_shape.add(highlight_line);
            }
        }
        return street_shape;
    }

    /**
     * Highlight the street by it's traffic overload
     */
    public void highlightTheStreet(){
        for (int i = 2; i < street_shape.size(); i+=2) {
            Shape line = street_shape.get(i);
            line.setOpacity(0.6);
            switch (traffic_overload){
                case 1:
                    line.setStroke(Color.GREEN);
                    break;
                case 2:
                    line.setStroke(Color.YELLOW);
                    break;
                case 3:
                    line.setStroke(Color.ORANGE);
                    break;
                case 4:
                    line.setStroke(Color.RED);
                    break;
                default:
                    line.setStroke(Color.BLACK);
                    break;
            }
            if(isClosed()){
                line.setStroke(Color.BLACK);
            }
        }
    }

    /**
     * Unhighlight the street
     * Changes it's color back to BLACK
     */
    public void unhighlightTheStreet(){
        for (int i = 2; i < street_shape.size(); i+=2) {
            Shape line = street_shape.get(i);
            line.setOpacity(0);
        }
    }

    /**
     * Selects the street
     * Changes it's width to 3 (makes it wider)
     */
    public void selectStreet(){
        for (int i = 1; i < street_shape.size(); i+=2) {
            Shape line = street_shape.get(i);
            line.setStrokeWidth(3);
        }
    }

    /**
     * Deselects the street
     * Changes it's width back to 1
     */
    public void deselectStreet(){
        for (int i = 1; i < street_shape.size(); i+=2) {
            Shape line = street_shape.get(i);
            line.setStrokeWidth(1);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Street street = (Street) o;
        return traffic_overload == street.traffic_overload &&
                closed == street.closed &&
                Objects.equals(id_str, street.id_str) &&
                Objects.equals(list_of_coordinates, street.list_of_coordinates) &&
                Objects.equals(list_of_stops, street.list_of_stops) &&
                Objects.equals(street_shape, street.street_shape);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_str, list_of_coordinates, list_of_stops, traffic_overload, closed, street_shape);
    }
}

