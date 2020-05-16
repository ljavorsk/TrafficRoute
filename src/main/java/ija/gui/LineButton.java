/*
 * Source code for the final IJA project
 * DetourComboBox class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 *
 */

package ija.gui;

import javafx.scene.Node;
import javafx.scene.control.Alert;
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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LineButton extends HBox {
    private final Button main_button = new Button();
    private final Label main_label = new Label();

    private final Line line;
    private final VBox vbox_setting;
    private final VBox vbox_middle;
    private final List<LineButton> list_lineButton;
    private final Pane main_content;
    private final List<Street> other_streets;
    private final List<Street> list_street;

    public LineButton(Line line, List<LineButton> list_lineButton, VBox vbox_setting, Pane main_content, VBox vbox_middle, List<Street> streets){
        this.line = line;
        this.list_lineButton = list_lineButton;
        this.vbox_setting = vbox_setting;
        this.main_content = main_content;
        this.vbox_middle = vbox_middle;
        this.other_streets = new CopyOnWriteArrayList<>(streets);
        this.list_street = new CopyOnWriteArrayList<>(streets);

        for(Street street : line.getRoute().getStreets()){
            this.other_streets.remove(street);
        }

        this.setUpMainButton();
        this.updateBusCounter();

        this.getChildren().add(this.main_button);
        this.getChildren().add(this.main_label);

    }

    private void setUpMainButton(){
        this.main_button.setText(String.valueOf(line.getId()));
        this.main_button.setMinSize(60, 30);
        this.main_button.setOnAction(e -> mainButtonAction());
    }

    private void mainButtonAction(){
        for(LineButton button : LineButton.this.list_lineButton){
            button.getLine().deselectLine();
        }
        line.selectLine();
        setUpSettingField();
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

    private void setUpSettingField(){
        vbox_setting.getChildren().clear();

        Button button_newBus = new Button("CREATE BUS");
        button_newBus.setOnAction(e -> newBusButtonAction());
        Button button_deleteBus = new Button("REMOVE BUS");
        button_deleteBus.setOnAction(e -> deleteBusButtonAction());
        Button button_detour = new Button("CREATE DETOUR");
        button_detour.setOnAction(e -> detourButtonAction());

        vbox_setting.getChildren().add(button_newBus);
        vbox_setting.getChildren().add(button_deleteBus);
        vbox_setting.getChildren().add(button_detour);
    }

    private void newBusButtonAction(){
        Bus bus = line.createBus();
        if(bus != null){
            main_content.getChildren().addAll(bus.getShapes());
            line.selectLine();
            updateBusCounter();
        }
    }

    private void deleteBusButtonAction(){
        if(line.deleteBus()){
            updateBusCounter();
        }
    }

    private void detourButtonAction(){
        line.deselectLine();
        List<DetourComboBox> list_detourComboBox = new CopyOnWriteArrayList<>();
        for(Street street : line.getRoute().getStreets()){
            street.selectStreet();
        }
        List<Node> tmp = new CopyOnWriteArrayList<>();
        for(int i=0; i<vbox_middle.getChildren().size(); i++){
            tmp.add(vbox_middle.getChildren().get(i));
        }
        vbox_middle.getChildren().clear();

        ComboBox comboBox_remove = new ComboBox();
        comboBox_remove.setVisibleRowCount(5);
        for(Street street : line.getRoute().getStreets()){
            comboBox_remove.getItems().add(street.getId());
        }
        comboBox_remove.setOnAction(e -> removeComboAction((String) comboBox_remove.valueProperty().getValue()));

        Button button_confirm = new Button("CONFIRM");
        button_confirm.setOnAction(e -> confirmButtonAction(comboBox_remove, list_detourComboBox, tmp));
        Button button_cancel = new Button("CANCEL");
        button_cancel.setOnAction(e -> cancelButtonAction(tmp));
        Button button_add = new Button("ADD");
        button_add.setOnAction(e -> addButtonAction(list_detourComboBox, button_add));

        vbox_middle.getChildren().add(new Label(String.valueOf(line.getId())));
        vbox_middle.getChildren().add(new Label("REMOVE STREET:"));
        vbox_middle.getChildren().add(comboBox_remove);
        vbox_middle.getChildren().add(new Label("STREETS OF DETOUR:"));
        vbox_middle.getChildren().add(button_add);
        addButtonAction(list_detourComboBox, button_add);
        vbox_setting.getChildren().clear();
        vbox_setting.getChildren().add(button_confirm);
        vbox_setting.getChildren().add(button_cancel);
    }

    private void confirmButtonAction(ComboBox comboBox_remove, List<DetourComboBox> list_detourComboBox, List<Node> tmp){
        Street street_remove = null;
        List<Street> list_streetsForDetour = new CopyOnWriteArrayList<>();
        for(Street street : this.line.getRoute().getStreets()){
            if(street.getId().equals(comboBox_remove.getValue())){
                street_remove = street;
            }
        }
        for(DetourComboBox comboBox : list_detourComboBox){
            if(comboBox.getStreet() != null){
                list_streetsForDetour.add(comboBox.getStreet());
            }
        }
        if(!this.checkSelectedDetour(street_remove, list_streetsForDetour)){
            return;
        }
        for(Bus bus : line.getBuses()){
            if(bus.getStreet().equals(street_remove)){
                this.showErrorAlert("AUTOBUS CAN NOT BE ON REMOVE STREET");
                return;
            }
        }
        if(!line.detour(street_remove, list_streetsForDetour)){
            this.showErrorAlert("BED DEFINED DETOUR");
        }
        for(Street street : this.list_street){
            street.unhighlightTheStreet();
        }
        main_content.getChildren().addAll(line.getRoute().getShapes());
        cancelButtonAction(tmp);
    }

    private void cancelButtonAction(List<Node> tmp){
        vbox_middle.getChildren().clear();
        vbox_middle.getChildren().addAll(tmp);
        setUpSettingField();
        line.selectLine();
        for(Street street : list_street){
            street.deselectStreet();
            street.unhighlightTheStreet();
        }
    }

    private void removeComboAction(String street_name){
        for(Street street : line.getRoute().getStreets()){
            street.unhighlightTheStreet();
            if(street.getId().equals(street_name)){
                street.highlightForDetour(Color.RED);
            }
        }
    }

    private void addButtonAction(List<DetourComboBox> comboBoxList, Button add){
        if(!comboBoxList.isEmpty()){
            if(comboBoxList.get(comboBoxList.size()-1).getStreet() == null){
                return;
            }
        }
        DetourComboBox comboBox = new DetourComboBox(this.other_streets, comboBoxList, vbox_middle);
        comboBoxList.add(comboBox);
        vbox_middle.getChildren().remove(add);
        vbox_middle.getChildren().add(comboBox);
        vbox_middle.getChildren().add(add);
    }

    private boolean checkSelectedDetour(Street street_remove, List<Street> list_streetsForDetour){
        String errorMessage = "";
        if(street_remove == null){
            errorMessage += "NOTHING SELECTED FOR REMOVE STREET\n";
        }
        if(list_streetsForDetour.isEmpty()){
            errorMessage += "NOTHING SELECTED FOR DETOUR\n";
        }
        if(errorMessage.equals("")){
            return true;
        }else{
            showErrorAlert(errorMessage);
            return false;
        }
    }

    private void showErrorAlert(String message){
        Alert wrong_detour = new Alert(Alert.AlertType.ERROR, message);
        wrong_detour.showAndWait();
    }

    public Line getLine() {
        return line;
    }
}
