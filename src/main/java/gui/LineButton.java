package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import map.map_src.Bus;
import map.map_src.Line;

import java.util.List;

public class LineButton extends HBox {
    private final Button main_button = new Button();
    private final Label main_label = new Label();

    private final Line line;
    private final VBox vbox_setting;
    private final VBox vbox_middle;
    private final List<LineButton> allLineButtons;
    private final Button new_bus_button = new Button();
    private final Button delete_bus_button = new Button();
    private final Pane main_content;

    public LineButton(Line line, List<LineButton> buttons, VBox vbox_setting, Pane main_content, VBox vbox_middle){
        this.line = line;
        this.allLineButtons = buttons;
        this.vbox_setting = vbox_setting;
        this.main_content = main_content;
        this.vbox_middle = vbox_middle;

        this.main_button.setText(String.valueOf(line.getId()));
        this.main_button.setMinSize(50, 30);
        this.main_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for(LineButton button : allLineButtons){
                    button.getLine().deselectLine();
                }
                line.selectLine();
                setVboxSetting();
            }
        });

        this.updateBusCounter();

        this.getChildren().add(this.main_button);
        this.getChildren().add(this.main_label);
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
                    updateBusCounter();
                }
            }
        });
        delete_bus_button.setText("REMOVE BUS");
        delete_bus_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(line.deleteBus()){
                    updateBusCounter();
                }
            }
        });
    }

    private void updateBusCounter(){
        int counter = 0;
        for(Bus bus : line.getBuses()){
            if(!bus.getDeleteFlag()){
                counter++;
            }
        }
        main_label.setText("   BUS COUNTER: " + counter);
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
