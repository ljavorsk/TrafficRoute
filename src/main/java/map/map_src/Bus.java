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
    private final List<Shape> shapes;
    /// Current position of the bus
    private AbstractMap.SimpleImmutableEntry<Coordinate, Street> current_position;
    /// Nearest navigation point which line can cross.
    private AbstractMap.SimpleImmutableEntry<Coordinate, Street> navigation_point;
    /// Flag if line is going from start to end of in opposite direction.
    private boolean start_2_end = true;
    /// How many time must line wait until can move.
    private int waiting_time = 0;
    /// Flag for delete bus when arrived to the nearest stop.
    private boolean delete_flag = false;

    /**
     * Constructor
     * @param id Unique ID of the Bus
     * @param starting_position Starting position + street
     */
    public Bus(String id, AbstractMap.SimpleImmutableEntry<Coordinate, Street> starting_position) {
        this.id = id;
        this.current_position = starting_position;
        shapes = new ArrayList<>();
        Circle circle = new Circle(starting_position.getKey().getX(), starting_position.getKey().getY(), 9, Color.GREEN);
        circle.setOpacity(0.8);
        shapes.add(circle);
        this.setNavigationPoint(starting_position);
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
     * Getter for flag if bus will be destroyed.
     * @return true if flag is set, false otherwise
     */
    public boolean getDeleteFlag(){
        return this.delete_flag;
    }

    /**
     * Set delete flag to true;
     */
    public void setDeleteFlag(){
        this.delete_flag = true;
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
        Coordinate old_pos = this.current_position.getKey();
        this.current_position = new AbstractMap.SimpleImmutableEntry<Coordinate, Street>(position, this.current_position.getValue());

        for(Shape shape : shapes ){
            shape.setTranslateX(current_position.getKey().getX() - old_pos.getX() + shape.getTranslateX());
            shape.setTranslateY(current_position.getKey().getY() - old_pos.getY() + shape.getTranslateY());
        }
    }

    /**
     * Setter for the street where is current bus position.
     * @param street Street to be set
     */
    public void setStreet(Street street){
        this.current_position = new AbstractMap.SimpleImmutableEntry<>(this.current_position.getKey(), street);
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
        int waiting_constant = 2;
        this.waiting_time = waiting_constant;
    }


    @Override
    public List<Shape> getShapes() {
        return shapes;
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

    /**
     * Select the bus
     * Change his fill to BLUE color and increase opacity
     */
    public void selectBus(){
        shapes.get(0).setFill(Color.BLUE);
        shapes.get(0).setOpacity(0.95);
    }

    /**
     * Deselect the bus
     * Change his fill back to GREEN and decrease opacity
     * If the bus is removed (DARKRED color) it's not affected
     */
    public void deselectBus(){
        Color current_color = (Color) shapes.get(0).getFill();
        // If the bus is removed, don't change his color back to green
        if (current_color.equals(Color.DARKRED)){
            return;
        }
        shapes.get(0).setFill(Color.GREEN);
        shapes.get(0).setOpacity(0.8);
    }

}
