/*
 * Source code for the final IJA project
 * Bus class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 *
 */

package map.map_src;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Bus vehicle that is an object within Line
 * Bus has it's own id, position and speed
 */
public class Bus implements Drawable{
    /// Unique ID of the Bus
    private final String id;
    /// Shapes in GUI
    private final List<Shape> shape;
    /// Current position of the bus
    private AbstractMap.SimpleImmutableEntry<Coordinate, Street> current_position;
    /// Nearest navigation point which line can cross.
    private AbstractMap.SimpleImmutableEntry<Coordinate, Street> navigation_point;
    /// Flag if line is going from start to end of in opposite direction.
    private boolean start_2_end = true;
    /// How many time must line wait until can move.
    private int waiting_time = 0;

    /**
     * Constructor
     * @param id Unique ID of the Bus
     * @param starting_position Starting position + street
     */
    public Bus(String id, AbstractMap.SimpleImmutableEntry<Coordinate, Street> starting_position) {
        this.id = id;
        this.current_position = starting_position;
        shape = new ArrayList<>();
        shape.add(new Circle(starting_position.getKey().getX(), starting_position.getKey().getY(), 9, Color.GREEN));
        shape.get(0).setOpacity(0.7);
    }

    /**
     * Getter for actual street where line is.
     * @return the actual positions street
     */
    public Street getStreet(){
        return this.current_position.getValue();
    }

    /**
     * Getter for current bus position.
     * @return Position of the bus
     */
    public Coordinate getPosition(){
        return this.current_position.getKey();
    }

    /**
     * Getter for the ID
     * @return The ID of the bus
     */
    public String getId() {
        return id;
    }

    /**
     * Getter for navigation point
     * @return positon and street of navigation point
     */
    public AbstractMap.SimpleImmutableEntry<Coordinate, Street> getNavigationPoint() {
        return navigation_point;
    }

    /**
     * Getter for flag if bus direction is from start to end.
     * @return Value of flag start_2_end
     */
    public boolean isStart2end(){
        return this.start_2_end;
    }

    /**
     * Setter for flag start to end
     * @param start_2_end Flag to be set
     */
    public void setStart2end(boolean start_2_end){
        this.start_2_end = start_2_end;
    }

    /**
     * Setter for the position of the bus.
     * @param position Position to be set
     */
    public void setPosition(Coordinate position) {
        this.current_position = new AbstractMap.SimpleImmutableEntry<Coordinate, Street>(position, this.current_position.getValue());
    }

    /**
     * Setter for the street where is current bus position.
     * @param street Street to be set
     */
    public void setStreet(Street street){
        this.current_position.setValue(street);
    }

    /**
     * Setter for the navigation point.
     * @param navigation_point Navigation point to be set
     */
    public void setNavigationPoint(AbstractMap.SimpleImmutableEntry<Coordinate, Street> navigation_point) {
        this.navigation_point = navigation_point;
    }

    /**
     * Set waiting time for bus. Set value of waiting_time to waiting_constant.
     */
    public void setWaitingTime(){
        // Constant for waiting time on stop.
        int waiting_constant = 5;
        this.waiting_time = waiting_constant;
    }

    @Override
    public List<Shape> getShapes() {
        return shape;
    }

    /**
     * If bus must wait on stop it decrement parameter waiting_time.
     * @return true if line must wait on stop, false otherwise
     */
    public boolean waitOnStop() {
        if(this.waiting_time != 0){
            this.waiting_time--;
            return true;
        }
        return false;
    }
}
