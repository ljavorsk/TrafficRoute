/*
 * Source code for the final IJA project
 * Coordinate class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 * 
 */

package ija.map.map_src;

public class Coordinate{

    private int x_coordinate;
    private int y_coordinate;

    /**
     * Constructor
     */
    private Coordinate(int x, int y){
        this.x_coordinate = x;
        this.y_coordinate = y;
    }

    /**
     * Public contructor function
     * @param x X coordinate
     * @param y Y coordinate
     * @return Assigned coordinate
     *         if x or y is lower than 0, null is returned
     */    
    public static Coordinate create(int x, int y){
        if (x >= 0 && y >= 0){
            return new Coordinate(x, y);
        }
        return null;
    }

    /**
     * Getter for X coordinate
     * @return X coordinate
     */
    public int getX(){
        return this.x_coordinate;
    }

    /**
     * Getter for Y coordinate
     * @return Y coordinate
     */
    public int getY(){
        return this.y_coordinate;
    }

    /**
     * Override for equals function of the Coordinate object
     * Needed for comparing two coordinates
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj){ // Test for identity
            return true;
        }
        if (!(obj instanceof Coordinate)){ // Test before casting
            return false;
        }
        Coordinate coord_obj = (Coordinate) obj; // Casting
        return (coord_obj.getX() == (this.getX()) && coord_obj.getY() == (this.getY()));
    }

    /**
     * Override for hashCode function of the Coordinate object
     */
    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 42 + this.getX();
        hash = hash * 42 + this.getY();
        return hash;
    }
}