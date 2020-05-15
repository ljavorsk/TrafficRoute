package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import map.map_src.Bus;
import map.map_src.Line;

import java.util.List;

public class LineButton extends Button {
    private final Line line;
    private final VBox vbox_setting;
    private final List<LineButton> allLineButtons;
    private final Button new_bus_button = new Button();
    private final Button delete_bus_button = new Button();
    private final Pane main_content;

    public LineButton(Line line, List<LineButton> buttons, VBox vbox_setting, Pane main_content){
        this.line = line;
        this.allLineButtons = buttons;
        this.vbox_setting = vbox_setting;
        this.main_content = main_content;

        this.setText(String.valueOf(line.getId()));
        this.setMinSize(50, 30);
        this.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for(LineButton button : allLineButtons){
                    button.getLine().deselectLine();
                }
                line.selectLine();
                setVboxSetting();
            }
        });
        setSetting();
    }

    private void setSetting(){
        new_bus_button.setText("CREATE BUS");
        new_bus_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Bus bus = line.createBus();
                if(bus != null){
                    main_content.getChildren().addAll(bus.getShapes());
                    line.selectLine();
                }
            }
        });
        delete_bus_button.setText("REMOVE BUS");
        delete_bus_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                line.deleteBus();
            }
        });
    }

    private void setVboxSetting(){
        vbox_setting.getChildren().clear();
        vbox_setting.getChildren().add(new_bus_button);
        vbox_setting.getChildren().add(delete_bus_button);
    }

    public Line getLine() {
        return line;
    }
}
