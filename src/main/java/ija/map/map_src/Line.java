/*
 * Source code for the final IJA project
 * Line class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 * 
 */

package ija.map.map_src;

import java.util.AbstractMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.scene.paint.Color;

/**
 * Represents abstract MHD route by which buses are riding.
 * The line have defined route and some buses.
 */
public class Line{
    /// Unique identification.
    private final int id;
    /// Route handler.
    private Route route;
    /// List of buses.
    private final List<Bus> buses = new CopyOnWriteArrayList<Bus>();

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
        return true;
    }

    /**
     * Create new bus on this line, that will start at begin of route. Line can have only 4 buses.
     * @return new created bus f buses are less then 4, null otherwise
     */
    public Bus createBus(){
        int counter = 0;
        for(Bus bus : buses){
            if(!bus.getDeleteFlag()){
                counter++;
            }
        }
        if(!(counter<4)){
            return null;
        }
        Bus bus = new Bus(String.valueOf(this.buses.size()+1), this.route.getFirst());
        this.buses.add(bus);
        return bus;
    }

    /**
     * Delete one bus on this line, but on line must be at least one bus.
     * @return True if delete was successful, false otherwise
     */
    public boolean deleteBus(){
        int number_of_buses = this.buses.size();
        if(number_of_buses > 1){
            for(int i=0; i<(number_of_buses-1); i++){
                if(!this.buses.get(i).getDeleteFlag()){
                    this.buses.get(i).setDeleteFlag();
                    this.buses.get(i).getShapes().get(0).setFill(Color.DARKRED);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Move with all buses on line.
     */
    public void move(){
        for(Bus bus : this.buses){
            if(!bus.waitOnStop()){
                if(this.moveWithBus(bus)){
                    bus.getShapes().get(0).setOpacity(0);
                    this.buses.remove(bus);
                }
            }
        }
    }

    /**
     * Move with bus. And return if bus must be deleted.
     * @param bus Which bus is moving.
     * @return true if after move must be bus deleted, false otherwise
     */
    private boolean moveWithBus(Bus bus){
        this.setDirection(bus);
        // Check if bus reach navigation point.
        while(bus.getPosition().equals(bus.getNavigationPoint().getKey())){
            // Set actual reached street.
            bus.setStreet(bus.getNavigationPoint().getValue());
            if(bus.isStart2end()){
                AbstractMap.SimpleImmutableEntry<Coordinate, Street> tmp = this.route.getNext(bus.getNavigationPoint());
                if(tmp.getValue().isClosed()){
                    return false;
                }
                bus.setNavigationPoint(tmp);
            }else{
                AbstractMap.SimpleImmutableEntry<Coordinate, Street> tmp = this.route.getPrevious(bus.getNavigationPoint());
                if(tmp.getValue().isClosed()){
                    return false;
                }
                bus.setNavigationPoint(tmp);
                // Set actual reached street.
                bus.setStreet(bus.getNavigationPoint().getValue());
            }
            //Check if bus isn`t on some stop.
            if(route.shouldStop(bus.getPosition())) {
                if(bus.getDeleteFlag()){
                    return true;
                }
                bus.setWaitingTime();
                return false;
            }
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
        return false;
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

    /**
     * Selects the route and all line`s buses
     * Increases it's opacity
     * Also selects every stop on the route
     */
    public void selectLine(){
        this.route.selectRoute();
        for(Bus bus : this.buses){
            bus.selectBus();
        }
    }

    /**
     * Deselect the route and all line`s buses
     * Decreases it's opacity to 0 so it's invisible
     * Also deselects every stop on the route
     */
    public void deselectLine(){
        this.route.deselectRoute();
        for(Bus bus : this.buses){
            bus.deselectBus();
        }
    }

    /**
     * Getter for the route
     * @return Route of the line
     */
    public Route getRoute() {
        return this.route;
    }
}
