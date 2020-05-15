package ija.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import ija.map.map_src.Bus;
import ija.map.map_src.Line;
import ija.map.map_src.Street;

import java.util.ArrayList;
import java.util.List;

public class LineButton extends HBox {
    private final Button main_button = new Button();
    private final Label main_label = new Label();

    private final Line line;
    private final VBox vbox_setting;
    private final VBox vbox_middle;
    private final List<LineButton> allLineButtons;
    private final Button new_bus_button = new Button("CREATE BUS");
    private final Button delete_bus_button = new Button("REMOVE BUS");
    private final Button detour_button = new Button("CREATE DETOUR");
    private final Pane main_content;
    private final List<Street> other_streets;
    private final List<Street> allStreets;

    public LineButton(Line line, List<LineButton> buttons, VBox vbox_setting, Pane main_content, VBox vbox_middle, List<Street> streets){
        this.line = line;
        this.allLineButtons = buttons;
        this.vbox_setting = vbox_setting;
        this.main_content = main_content;
        this.vbox_middle = vbox_middle;
        this.other_streets = new ArrayList<>(streets);
        this.allStreets = new ArrayList<>(streets);

        for(Street street : line.getRoute().getStreets()){
            this.other_streets.remove(street);
        }

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
        setSettingButtons();
    }

    private void setSettingButtons(){
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
        delete_bus_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(line.deleteBus()){
                    updateBusCounter();
                }
            }
        });
        detour_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setDeturScreen();
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
        vbox_setting.getChildren().add(detour_button);
    }

    private void setDeturScreen(){
        line.deselectLine();
        List<ComboBox> comboBoxList = new ArrayList<>();
        for(Street street : line.getRoute().getStreets()){
            street.selectStreet();
        }
        Button confirm = new Button("CONFIRM");
        Button cancel = new Button("CANCEL");
        Button add = new Button("ADD");
        ComboBox remove_combo = new ComboBox();
        remove_combo.setVisibleRowCount(5);
        for(Street street : line.getRoute().getStreets()){
            remove_combo.getItems().add(street.getId());
        }
        List<Node> tmp = new ArrayList<>();
        for(int i=0; i<vbox_middle.getChildren().size(); i++){
            tmp.add(vbox_middle.getChildren().get(i));
        }

        vbox_middle.getChildren().clear();
        vbox_middle.getChildren().add(new Label(String.valueOf(line.getId())));
        vbox_middle.getChildren().add(new Label("REMOVE STREET:"));
        vbox_middle.getChildren().add(remove_combo);
        vbox_middle.getChildren().add(new Label("STREETS OF DETOUR:"));
        vbox_middle.getChildren().add(add);
        addNewStreet(comboBoxList, add);
        vbox_setting.getChildren().clear();
        vbox_setting.getChildren().add(confirm);
        vbox_setting.getChildren().add(cancel);
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                vbox_middle.getChildren().clear();
                vbox_middle.getChildren().addAll(tmp);
                setVboxSetting();
                line.selectLine();
                for(Street street : line.getRoute().getStreets()){
                    street.deselectStreet();
                    street.unhighlightTheStreet();
                }
            }
        });
        remove_combo.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                String street_name = (String) remove_combo.valueProperty().getValue();
                for(Street street : line.getRoute().getStreets()){
                    street.unhighlightTheStreet();
                    if(street.getId().equals(street_name)){
                        street.highlightForDetour(Color.RED);
                    }
                }
            }
        });
    }

    private void addNewStreet(List<ComboBox> comboBoxList, Button add){
        ComboBox comboBox = new ComboBox();
        comboBoxList.add(comboBox);
        comboBox.setVisibleRowCount(5);
        for(Street street : other_streets){
            comboBox.getItems().add(street.getId());
        }
        comboBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                String street_name = (String) comboBox.valueProperty().getValue();
                for(Street street : line.getRoute().getStreets()){
                    street.unhighlightTheStreet();
                    if(street.getId().equals(street_name)){
                        street.highlightForDetour(Color.GREEN);
                    }
                }
            }
        });
        vbox_middle.getChildren().remove(add);
        vbox_middle.getChildren().add(comboBox);
        vbox_middle.getChildren().add(add);
    }

    public Line getLine() {
        return line;
    }
}
