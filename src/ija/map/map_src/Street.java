/*
 * Source code for the final IJA project
 * Street class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 * 
 */

package ija.map.map_src;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Represents the Street in the Map.
 * The Street have it's unique ID, representing Coordinates, and may have Stops
 */
public interface Street {

    /**
     * Getter for the Street's ID
     * @return ID
     */
    public String getId();

    /**
     * Getter for the list of Coordinates of the street. First one is the beggining and the last one is the end of the street
     * @return List if Coordinates
     */
    public List<Coordinate> getCoordinates();

    /**
     * Getter for the list of Stops within the Street
     * @return List of Stops
     *         if the Street doesn't have a stop, List is empty
     */
    public List<Stop> getStops();

    /**
     * Adds the Stop in the Street's list of Stops
     * @param stop Stop that's going to be added
     * @return If the process was successful
     */
    public boolean addStop(Stop stop);


    /**
     * Return the end of the street
     * @return ending Coordinate
     */
    public Coordinate end_of_the_street();

    /**
     * Return the begin of the street
     * @return beggining Coordinate
     */
    public Coordinate begin_of_the_street();

    /**
     * Return if the given Street follows the current Street
     * @param s Street to compare
     * @return true if the given street follows the current one, false if not
     */
    public boolean follows(Street s);

    /**
     * Creates the default street
     * @param id ID of the street
     * @param coordinates Coordinates that represents the street
     * @return The created street
     */
    public static Street defaultStreet(String id, Coordinate... coordinates){
        Coordinate previous_c = coordinates[0];
        for (Coordinate coordinate : coordinates) {
            if (coordinate.getX() != previous_c.getX() && coordinate.getY() != previous_c.getY()){
                return null;
            }
            previous_c = coordinate;
        }
        return new Street(){
            private String id_str = id;
            private List<Coordinate> list_of_coordinates = Arrays.asList(coordinates);
            private List<Stop> list_of_stops = new ArrayList<Stop>();
        
            @Override
            public List<Stop> getStops() {
                return this.list_of_stops;
            }
        
            @Override
            public String getId() {
                return this.id_str;
            }
        
            @Override
            public List<Coordinate> getCoordinates() {
                return this.list_of_coordinates;
            }
        
            @Override
            public boolean follows(Street s) {
                if (s == null){
                    return false;
                }
                Coordinate beginning_first = this.begin();
                Coordinate end_first = this.end();

                Coordinate beginning_second = s.getCoordinates().get(0);
                Coordinate end_second = s.getCoordinates().get(s.getCoordinates().size() -1);

                if (beginning_first.equals(end_second) || beginning_second.equals(end_first)){
                    return true;
                }
                else {
                    return false;
                }
            }
        
            @Override
            public Coordinate end_of_the_street() {
                List<Coordinate> street_coords = this.getCoordinates();
                return street_coords.get(street_coords.size() -1);
            }
        
            @Override
            public Coordinate begin_of_the_street() {
                List<Coordinate> street_coords = this.getCoordinates();
                return street_coords.get(0);
            }
        
            @Override
            public boolean addStop(Stop stop) {
                Coordinate stop_c = stop.getCoordinate();
                for (int i = 0; i < this.list_of_coordinates.size() - 1; i++){
                    Coordinate c_A = this.list_of_coordinates.get(i);
                    Coordinate c_B = this.list_of_coordinates.get(i+1);

                    if (c_A.getX() == c_B.getX()){      // Look like __
                        if (c_A.getX() != stop_c.getX()){ continue; }
                        if((stop_c.getY() >= c_A.getY() && stop_c.getY() <= c_B.getY()) ||
                            (stop_c.getY() <= c_A.getY() && stop_c.getY() >= c_B.getY())){   // Inside of street
                                this.list_of_stops.add(stop);
                                stop.setStreet(this);
                                return true;
                        }
                    }
                    else if(c_A.getY() == c_B.getY()) { // Look like |
                        if (c_A.getY() != stop_c.getY()){ continue; }
                        if((stop_c.getX() >= c_A.getX() && stop_c.getX() <= c_B.getX()) || 
                            (stop_c.getX() <= c_A.getX() && stop_c.getX() >= c_B.getX())){   // Inside of street
                                this.list_of_stops.add(stop);
                                stop.setStreet(this);
                                return true;
                        }
                    }
                }
                return false;
            }
        };

    }

}
