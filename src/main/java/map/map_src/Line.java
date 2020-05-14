/*
 * Source code for the final IJA project
 * Line class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 * 
 */

package map.map_src;

import java.util.ArrayList;
import java.util.List;

public class Line{
    /// Unique identification.
    private final int id;
    /// Route handler.
    private Route route;
    /// List of buses.
    private final List<Bus> buses = new ArrayList<>();

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
     * Getter for all stops that line have.
     * @return list of stops
     */
    public List<Stop> getStops(){
        return this.route.getStops();
    }

    /**
     * Getter for all buses on line.
     * @return list of buses
     */
    public List<Bus> getBuses() {
        return this.buses;
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
        this.buses.add(new Bus(String.valueOf(this.buses.size()+1), this.route.getFirst()));
        this.buses.get(0).setNavigationPoint(this.route.getFirst());
        return true;
    }

    /**
     * Create new bus on this line, that will start at begin of route.
     */
    public void createBus(){
        this.buses.add(new Bus(String.valueOf(this.buses.size()+1), this.route.getFirst()));
    }

    /**
     * Move with all buses on line.
     */
    public void move(){
        for(Bus bus : this.buses){
            if(!bus.waitOnStop()){
                this.moveWithBus(bus);
            }
        }
    }

    /**
     * Move with bus.
     * @param bus Which bus is moving.
     */
    private void moveWithBus(Bus bus){
        this.setDirection(bus);
        // Check if bus reach navigation point.
        while(bus.getPosition().equals(bus.getNavigationPoint().getKey())){
            // Set actual reached street.
            bus.setStreet(bus.getNavigationPoint().getValue());
            if(bus.isStart2end()){
                bus.setNavigationPoint(this.route.getNext(bus.getNavigationPoint()));
            }else{
                bus.setNavigationPoint(this.route.getPrevious(bus.getNavigationPoint()));
                // Set actual reached street.
                bus.setStreet(bus.getNavigationPoint().getValue());
            }
            //Check if bus isn`t on some stop.
            if(route.shouldStop(bus.getPosition())) {
                bus.setWaitingTime();
                return;
            }
        }
        if(bus.getStreet().isClosed()){
            return;
        }
        float move_by = this.setSpeed(bus.getStreet().getTrafficOverload());
        float new_x = bus.getPosition().getX();
        float new_y = bus.getPosition().getY();
        // X value of vector which line used for move.
        float x_vector = bus.getNavigationPoint().getKey().getX() - new_x;
        // Y value of vector which line used for move.
        float y_vector = bus.getNavigationPoint().getKey().getY() - new_y;
        // Distance between actual position and navigation point.
        float distance_2_navig_point;
        if(x_vector == 0){
            distance_2_navig_point = Math.abs(new_y - bus.getNavigationPoint().getKey().getY());
            if (y_vector > 0){
                if(distance_2_navig_point < move_by){
                    new_y = bus.getNavigationPoint().getKey().getY();
                }else{
                    new_y += move_by;
                }
            } else {
                if(distance_2_navig_point < move_by){
                    new_y = bus.getNavigationPoint().getKey().getY();
                }else{
                    new_y -= move_by;
                }
            }
        }else if(y_vector == 0){
            distance_2_navig_point = Math.abs(new_x - bus.getNavigationPoint().getKey().getX());
            if (x_vector > 0){
                if(distance_2_navig_point < move_by){
                    new_x = bus.getNavigationPoint().getKey().getX();
                }else{
                    new_x += move_by;
                }
            } else {
                if(distance_2_navig_point < move_by){
                    new_x = bus.getNavigationPoint().getKey().getX();
                }else{
                    new_x -= move_by;
                }
            }
        }else{
            distance_2_navig_point = (float) Math.sqrt(x_vector*x_vector + y_vector*y_vector);
            if(distance_2_navig_point < move_by){
                new_x = bus.getNavigationPoint().getKey().getX();
                new_y = bus.getNavigationPoint().getKey().getY();
            }else{
                new_x = (move_by / distance_2_navig_point) * x_vector + new_x;
                new_y = (move_by / distance_2_navig_point) * y_vector + new_y;
            }
        }
        bus.setPosition(Coordinate.create(new_x, new_y));
    }

    /**
     * Check if bus is on start of her route or on end. It change value of flag start_2_end.
     * @param bus Bus for which is set direction.
     */
    private void setDirection(Bus bus){
        if(bus.getPosition().equals(this.route.getStartingPoint())){
            bus.setStart2end(true);
        }else if(bus.getPosition().equals(this.route.getEndingPoint())){
            bus.setStart2end(false);
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

    /**
     * Set bus speed by traffic overload.
     * @param traffic_overload Traffic overload by which is choose bus speed.
     * @return 1, 0.8, 0.6 or 0.4. It depend on traffic overload.
     */
    private float setSpeed(int traffic_overload){
        switch (traffic_overload){
            case 1:
                return 4f;
            case 2:
                return 2.5f;
            case 3:
                return 1.5f;
            default:
                return 1f;
        }
    }

}
