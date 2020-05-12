/*
 * Source code for the final IJA project
 * Bus class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 *
 */

package map.map_src;

/**
 * Represents the Bus vehicle that is an object within Line
 * Bus has it's own id, position and speed
 */
public class Bus {
    /// Unique ID of the Bus
    private final String id;
    /// Current position of the Bus
    private Coordinate position;
    /// Current speed of the Bus
    private double speed;

    /**
     * Constructor
     * @param id Unique ID of the Bus
     * @param position Starting position
     * @param speed Starting speed
     */
    public Bus(String id, Coordinate position, double speed) {
        this.id = id;
        this.position = position;
        this.speed = speed;
    }

    /**
     * Setter for the speed of the bus
     * @param speed Speed to be set
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Getter for the ID
     * @return The ID of the bus
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for the position of the bus
     * @param position Position to be set
     */
    public void setPosition(Coordinate position) {
        this.position = position;
    }

    /**
     * Getter for the position of the bus
     * @return Position of the bus
     */
    public Coordinate getPosition() {
        return position;
    }

    /**
     * Getter for the speed of the bus
     * @return Speed of the bus
     */
    public double getSpeed() {
        return speed;
    }
}
