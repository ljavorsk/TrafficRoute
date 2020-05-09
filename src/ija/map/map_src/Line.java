/*
 * Source code for the final IJA project
 * Line class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 * 
 */

package ija.map.map_src;

import java.util.List;
import java.util.AbstractMap.SimpleImmutableEntry;

import ija.map.map_src.Coordinate;
import ija.map.map_src.Street;
import ija.map.map_src.Stop;
import ija.map.map_src.Route;

public class Line{
    private final float move_by = 1;
    private final int id;
    private SimpleImmutableEntry<Coordinate, Street> actual_position;
    private SimpleImmutableEntry<Coordinate, Street> navigation_point;
    private Route route;
    private float x_vector;
    private float y_vector;
    private float move_coefficient;

    /**
     * Constructor
     */
    public Line(int id){
        this.id = id;
    }

    /**
     * Getter for link id.
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Prepare link for traffic.
     * @param streets Which streets does the route contain, must be ordered
     * @param stops Which stops does the route contain, must be ordered
     * @param starting_point Where does the route begins
     * @return true if streets and stops are ordered, else false
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
     * @return actual coordinate
     */
    public Coordinate move(){
        // Check if line reach navigation point.
        if(this.actual_position.getKey().equals(this.navigation_point.getKey())){
            this.navigation_point = this.route.getNext(this.navigation_point);
            x_vector = this.navigation_point.getKey().getX() - this.actual_position.getKey().getX();
            y_vector = this.navigation_point.getKey().getY() - this.actual_position.getKey().getY();
            if(x_vector != 0 && y_vector != 0){
                this.move_coefficient = (float) Math.sqrt(x_vector*x_vector + y_vector*y_vector);
            }
        }
        float new_x = this.actual_position.getKey().getX();
        float new_y = this.actual_position.getKey().getY();
        if(x_vector == 0){
            new_y += this.move_by;
        }else if(y_vector == 0){
            new_x += this.move_by;
        }else{
            new_x = (this.move_by / this.move_coefficient) * new_x;
            new_y = (this.move_by / this.move_coefficient) * new_y;
        }
        this.actual_position = new SimpleImmutableEntry<Coordinate, Street>(Coordinate.create(new_x, new_y),this.actual_position.getValue());
        return getCoordinate();
    }

    /**
     * Getter for actual street where line is.
     * @return street
     */
    public Street getStreet(){
        return this.actual_position.getValue();
    }

    /**
     * Getter for actual link coordinate.
     * @return coordinate
     */
    public Coordinate getCoordinate(){
        return this.actual_position.getKey();
    }
}