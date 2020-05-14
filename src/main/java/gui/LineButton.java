package gui;

import javafx.scene.control.Button;
import map.map_src.Line;

public class LineButton extends Button {
    private final Line line;

    public LineButton(Line line){
        this.line = line;
        this.setText(String.valueOf(line.getId()));
        this.setMinSize(50, 30);
    }

    public Line getLine() {
        return line;
    }
}
