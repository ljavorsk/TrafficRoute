/*
 * Source code for the final IJA project
 * Stop class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 * 
 */

package ija.map.map_src;

import ija.map.map_src.Coordinate;
import ija.map.map_src.Street;

/**
 * Representing the Stop within the Street.
 * Stop have it's own unique ID, representing Coordinates.
 */
public class Stop {
    /// Name of the stop
    private final String name;
    /// Position of the stop
    private Coordinate coordinate;
    /// Street where is stop 
    private Street street;

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
     * Setter for street.
     * @param street the street to set
     */
    public void setStreet(Street street) {
        this.street = street;
    }

    /**
     * Getter for street.
     * @return the street
     */
    public Street getStreet() {
        return street;
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
}