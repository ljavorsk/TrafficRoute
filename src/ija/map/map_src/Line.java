/**
 * Source code for the final IJA project
 * Line class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 * 
 */

package ija.map.map_src;

import java.util.ArrayList;
import java.util.List;
import java.util.AbstractMap.SimpleImmutableEntry;

import ija.map.map_src.Coordinate;
import ija.map.map_src.Street;
import ija.map.map_src.Stop;
import ija.map.map_src.Route;

public class Line{
    private final int id;
    private SimpleImmutableEntry<Coordinate, Street> actual_position;
    private SimpleImmutableEntry<Coordinate, Street> navigation_point;
    private Route route;
    private float x_vector;
    private float y_vector;
    private float move_coeficient;

    /**
     * Constructor
     */
    public Line(int id, List<Street> streets, List<Stop> stops){
        this.id = id;
        this.route = new Route(streets, stops);
        this.actual_position = this.route.getFirst();
        this.navigation_point = this.actual_position;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    public void move(float move_about){
        if(this.actual_position.getKey().equals(this.navigation_point.getKey())){
            this.navigation_point = this.route.getNext(this.navigation_point);
            x_vector = this.navigation_point.getKey().getX() - this.actual_position.getKey().getX();
            y_vector = this.navigation_point.getKey().getY() - this.actual_position.getKey().getY();
            if(x_vector != 0 && y_vector != 0){
                this.move_coeficient = Math.sqrt(x_vector*x_vector + y_vector*y_vector);
            }
        }
        float new_x = this.actual_position.getKey().getX();
        float new_y = this.actual_position.getKey().getY();
        if(x_vector == 0){
            new_y += move_about;
        }else if(y_vector == 0){
            new_x += move_about;
        }else{
            new_x = (move_about / this.move_coeficient) * new_x;
            new_y = (move_about / this.move_coeficient) * new_y;
        }
        this.actual_position = new SimpleImmutableEntry<Coordinate, Street>(Coordinate.create(new_x, new_y),this.actual_position.getValue());
    }

    public Street getStreet(){
        return this.actual_position.getValue();
    }

    public Coordinate getCoordinate(){
        return this.actual_position.getKey();
    }
}