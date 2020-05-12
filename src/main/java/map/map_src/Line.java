/*
 * Source code for the final IJA project
 * Line class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 * 
 */

package map.map_src;

import java.util.List;
import java.util.AbstractMap.SimpleImmutableEntry;

public class Line{
    /// Constant for line to move by value.
    private final float move_by_constant = 1;
    /// Constant for waiting time on stop.
    private final int waiting_constant = 5;

    /// Unique identification.
    private final int id;
    /// Actual position of line.
    private SimpleImmutableEntry<Coordinate, Street> actual_position;
    /// Nearest navigation point which line can cross.
    private SimpleImmutableEntry<Coordinate, Street> navigation_point;
    /// Route handler.
    private Route route;
    /// Flag if line is going from start to end of in opposite direction.
    private boolean start_2_end = true;
    /// How many time must line wait until can move.
    private int waiting_time = 0;

    /**
     * Constructor
     * @param id Int id is unique identification for lines
     */
    public Line(int id){
        this.id = id;
    }

    /**
     * Getter for link id.
     * @return the link id
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for actual street where line is.
     * @return the actual positions street
     */
    public Street getStreet(){
        return this.actual_position.getValue();
    }

    /**
     * Getter for actual link coordinate.
     * @return the actual positions coordinate
     */
    public Coordinate getCoordinate(){
        return this.actual_position.getKey();
    }

    /**
     * Getter for all stops that line have.
     * @return list of stops
     */
    public List<Stop> getStops(){
        return this.route.getStops();
    }

    /**
     * If line must wait on stop it decrement parameter waiting_time.
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
     * Prepare link for traffic.
     * @param streets Which streets does the route contain, must be ordered
     * @param stops Which stops does the route contain, must be ordered
     * @param starting_point Where does the route begins
     * @return true if streets and stops are ordered, false otherwise
     */
    public boolean prepare(List<Street> streets, List<Stop> stops, Coordinate starting_point){
        this.route = Route.defaultRoute(streets, stops, starting_point);
        if(this.route == null){
            return false;
        }
        this.actual_position = this.route.getFirst();
        this.navigation_point = this.actual_position;
        return true;
    }

    /**
     * Move with line on its route.
     * @return actual positions coordinate
     */
    public Coordinate move(){
        if(this.actual_position.getValue().isClosed()){
            return this.actual_position.getKey();
        }
        this.setDirection();
        // Check if line reach navigation point.
        if(this.actual_position.getKey().equals(this.navigation_point.getKey())){
            // Set actual reached street.
            this.actual_position.setValue(this.navigation_point.getValue());
            if(start_2_end){
                this.navigation_point = this.route.getNext(this.navigation_point);
            }else{
                this.navigation_point = this.route.getPrevious(this.navigation_point);
                // Set actual reached street.
                this.actual_position.setValue(this.navigation_point.getValue());
            }
            //Check if line isn`t on some stop.
            if(route.shouldStop(this.actual_position.getKey())) {
                this.waiting_time = this.waiting_constant;
            }
        }
        float new_x = this.actual_position.getKey().getX();
        float new_y = this.actual_position.getKey().getY();
        // X value of vector which line used for move.
        float x_vector = this.navigation_point.getKey().getX() - new_x;
        // Y value of vector which line used for move.
        float y_vector = this.navigation_point.getKey().getY() - new_y;
        // Distance between actual position and navigation point.
        float distance_2_navig_point;
        if(x_vector == 0){
            distance_2_navig_point = Math.abs(new_y - this.navigation_point.getKey().getY());
            if(distance_2_navig_point > this.move_by_constant){
                new_y = this.navigation_point.getKey().getY();
            }else{
                new_y += this.move_by_constant;
            }
        }else if(y_vector == 0){
            distance_2_navig_point = Math.abs(new_x - this.navigation_point.getKey().getX());
            if(distance_2_navig_point > this.move_by_constant){
                new_x = this.navigation_point.getKey().getX();
            }else{
                new_x += this.move_by_constant;
            }
        }else{
            distance_2_navig_point = (float) Math.sqrt(x_vector*x_vector + y_vector*y_vector);
            if(distance_2_navig_point > this.move_by_constant){
                new_x = navigation_point.getKey().getX();
                new_y = navigation_point.getKey().getY();
            }else{
                new_x = (this.move_by_constant / distance_2_navig_point) * new_x;
                new_y = (this.move_by_constant / distance_2_navig_point) * new_y;
            }
        }
        this.actual_position = new SimpleImmutableEntry<Coordinate, Street>(Coordinate.create(new_x, new_y),this.actual_position.getValue());
        return getCoordinate();
    }

    /**
     * Check if line is on start of her route or on end. It change value of flag start_2_end.
     */
    private void setDirection(){
        if(this.actual_position.equals(this.route.getStartingPoint())){
            this.start_2_end = true;
        }else if(this.actual_position.equals(this.route.getEndingPoint())){
            this.start_2_end = false;
        }
    }

    /**
     * Recalculate the route with given closed_street and given detour_streets.
     * @param closed_street The street that is closed, and have to be deleted from route.
     * @param list_of_streets The streets that will be connected to the route.
     * They have to be ordered, first one needs to be connected to street that was closed
     * and the last one needs to be connected to the other end of the closed_street.
     * @return true, or false if anything went wrong
     */
    public boolean detour(Street closed_street, List<Street> list_of_streets){
        Route new_route = this.route.detourRoute(closed_street, list_of_streets);
        if(new_route == null){
            return false;
        }
        this.route = new_route;
        return true;
    }

}
