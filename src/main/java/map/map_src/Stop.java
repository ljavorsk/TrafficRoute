/*
 * Source code for the final IJA project
 * Stop class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 * 
 */

package map.map_src;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Representing the Stop within the Street.
 * Stop have it's own unique ID, representing Coordinates.
 */
public class Stop implements Drawable{
    /// Name of the stop
    private final String name;
    /// Position of the stop
    private final Coordinate coordinate;
    /// Street in which is the stop located
    private Street street;
    /// Stop's GUI shape
    private final List<Shape> stop_shape = new ArrayList<>();

    public Stop(String name, Coordinate coordinate){
        this.name = name;
        this.coordinate = coordinate;
    }

    /**
     * Getter for name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for coordinate.
     * @return the coordinate
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Getter for the stop's street
     * @return Stop's street
     */
    public Street getStreet(){
        return this.street;
    }

    /**
     * Setter for the stop's street
     * @param s Street to be set
     */
    public void setStreet(Street s){
        this.street = s;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Stop))
            return false;
        Stop objStop = (Stop) obj;
        return this.name.equals(objStop.getName());
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 42 + this.name.hashCode();
        hash = hash * 42 + this.coordinate.hashCode();
        hash = hash * 42 + (this.street == null ? 0 : this.street.hashCode());
        return hash;
    }

    @Override
    public List<Shape> getShapes() {
        Text text = new Text(coordinate.getX() -  ((float) name.length()*2), coordinate.getY() - 10, name);
        text.setFill(Color.RED);
        text.setStyle("-fx-font: 9 arial;");
        text.setOpacity(0.7);
        stop_shape.add(text);
        stop_shape.add(new Circle(coordinate.getX(), coordinate.getY(), 5, Color.RED));

        return stop_shape;
    }

    /**
     * Selects the stop
     * Change it's color to PURPLE
     */
    public void selectStop(){
        for (Shape shape : stop_shape) {
                shape.setFill(Color.PURPLE);
        }
    }

    /**
     * Deselects the stop
     * Change it's color back to RED
     */
    public void deselectStop(){
        for (Shape shape : stop_shape) {
            shape.setFill(Color.RED);
        }
    }
}