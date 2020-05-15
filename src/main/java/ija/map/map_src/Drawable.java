/*
 * Source code for the final IJA project
 * Drawable interface
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 *
 */


package ija.map.map_src;

import javafx.scene.shape.Shape;

import java.util.List;

public interface Drawable {
    /**
     * Getter for the GUI Shapes
     * @return Shapes in GUI
     */
    List<Shape> getShapes();
}
