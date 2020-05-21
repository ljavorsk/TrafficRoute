/*
 * Source code for the final IJA project
 * LineButton class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 *
 */

package ija.gui;

import ija.map.map_src.Stop;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import ija.map.map_src.Bus;
import ija.map.map_src.Line;
import ija.map.map_src.Street;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represent on Line on map.
 * It`s button and label. By button can user show options for modify line with this object represent.
 * Label showing information about number of buses, that line have.
 */
public class LineButton extends Button {
    /// Label for showing number of buses from line
    private final Label main_label = new Label();
    /// Line which represent this object
    private final Line line;
    /// Vbox on right bottom corner of the screen
    private final VBox vbox_setting;
    /// Vbox on right side of the screen
    private final VBox vbox_middle;
    /// List of all objects type of LineButton
    private final List<LineButton> list_lineButton;
    /// Main content by which is showing map with streets and lines
    private final Pane main_content;
    /// List of all streets
    private final List<Street> list_street;
    /// Button for remove all detours.
    private final Button button_deleteDetours = new Button("REMOVE DETOURS");
    /// Visibility for button delete detours.
    private boolean deleteDetoursVisibility = false;

    /**
     * Constructor
     * @param line Line which this represent
     * @param list_lineButton List of all lineButtons
     * @param vbox_setting Vbox on right bottom corner of the screen
     * @param main_content Main content showing whole map
     * @param vbox_middle Vbox on right side of the screen
     * @param streets List of all streets on map
     */
    public LineButton(Line line, List<LineButton> list_lineButton, VBox vbox_setting, Pane main_content, VBox vbox_middle, List<Street> streets){
        this.line = line;
        this.list_lineButton = list_lineButton;
        this.vbox_setting = vbox_setting;
        this.main_content = main_content;
        this.vbox_middle = vbox_middle;
        this.list_street = new CopyOnWriteArrayList<>(streets);

        this.setUpMainButton();
        this.updateBusCounter();
    }

    /**
     * Set up for main button.
     */
    private void setUpMainButton(){
        this.setText(String.valueOf(line.getId()));
        this.setMinSize(80, 30);
        this.setOnAction(e -> mainButtonAction());
    }

    /**
     * Deselect all lines and than select line which lineButton represent.
     */
    private void mainButtonAction(){
        for(LineButton button : LineButton.this.list_lineButton){
            button.getLine().deselectLine();
        }
        line.selectLine();
        setUpSettingField();
        vbox_middle.getChildren().clear();
        vbox_middle.getChildren().add(new Label("   "+this.line.getId()));
        vbox_middle.getChildren().add(this.main_label);
        vbox_middle.getChildren().add(new Label(""));
        for(Stop stop : this.line.getStops()){
            String stop_info = stop.getName()+" ("+stop.getStreet().getId()+")";
            vbox_middle.getChildren().add(new Label(stop_info));
            for(int i=0; i<2; i++){
                vbox_middle.getChildren().add(new Label("|"));
            }
        }
        for(int i=0; i<2; i++){
            vbox_middle.getChildren().remove(vbox_middle.getChildren().size()-1);
        }
    }

    /**
     * Update label with actual number of buses.
     */
    private void updateBusCounter(){
        int counter = 0;
        for(Bus bus : line.getBuses()){
            if(!bus.getDeleteFlag()){
                counter++;
            }
        }
        main_label.setText("   BUS COUNTER: " + counter);
    }

    /**
     * Create 3 buttons and show them on right side of the screen.
     * One create bus, one delete bus and last create detour.
     */
    private void setUpSettingField(){
        this.vbox_setting.getChildren().clear();

        Button button_newBus = new Button("CREATE BUS");
        button_newBus.setPrefSize(150,25);
        button_newBus.setOnAction(e -> newBusButtonAction());
        Button button_deleteBus = new Button("REMOVE BUS");
        button_deleteBus.setPrefSize(150,25);
        button_deleteBus.setOnAction(e -> deleteBusButtonAction());
        Button button_detour = new Button("CREATE DETOUR");
        button_detour.setPrefSize(150,25);
        button_detour.setOnAction(e -> detourButtonAction());

        this.button_deleteDetours.setPrefSize(150,25);
        this.button_deleteDetours.setOnAction(e -> deleteDetoursButtonAction());
        this.button_deleteDetours.setVisible(this.deleteDetoursVisibility);

        this.vbox_setting.getChildren().add(button_newBus);
        this.vbox_setting.getChildren().add(button_deleteBus);
        this.vbox_setting.getChildren().add(button_detour);
        this.vbox_setting.getChildren().add(this.button_deleteDetours);
    }

    /**
     * Create new bus.
     */
    private void newBusButtonAction(){
        Bus bus = line.createBus();
        if(bus != null){
            main_content.getChildren().addAll(bus.getShapes());
            line.selectLine();
            updateBusCounter();
        }
    }

    /**
     * Delete one bus.
     */
    private void deleteBusButtonAction(){
        if(line.deleteBus()){
            updateBusCounter();
        }
    }

    /**
     * Remove all detours. If this operation is unsuccessful, than user is warn by alert message on screen.
     */
    private void deleteDetoursButtonAction(){
        this.line.deselectLine();
        if(!this.line.deleteDetours()){
            showErrorAlert("BUS ON DETOUR");
        }else{
            this.button_deleteDetours.setVisible(false);
            this.deleteDetoursVisibility = false;
        }
        mainButtonAction();
    }

    /**
     * Set up and open detour settings on right side of the screen.
     */
    private void detourButtonAction(){
        // Deselect line and select streets, that contain line.
        line.deselectLine();
        List<DetourComboBox> list_detourComboBox = new CopyOnWriteArrayList<>();
        for(Street street : line.getRoute().getStreets()){
            street.selectStreet();
        }
        vbox_middle.getChildren().clear();

        List<Street> other_streets = new CopyOnWriteArrayList<>(this.list_street);
        for(Street street : line.getRoute().getStreets()){
            other_streets.remove(street);
        }

        // Create and set up comboBox for selection of streets to detour.
        ComboBox<String> comboBox_remove = new ComboBox<>();
        comboBox_remove.setVisibleRowCount(5);
        for(Street street : line.getRoute().getStreets()){
            comboBox_remove.getItems().add(street.getId());
        }
        comboBox_remove.setOnAction(e -> removeComboAction((String) comboBox_remove.valueProperty().getValue()));

        // Create and set up 3 buttons.
        Button button_confirm = new Button("CONFIRM");
        button_confirm.setPrefSize(90,25);
        button_confirm.setOnAction(e -> confirmButtonAction(comboBox_remove, list_detourComboBox));
        Button button_cancel = new Button("CANCEL");
        button_cancel.setPrefSize(90,25);
        button_cancel.setOnAction(e -> cancelButtonAction());
        Button button_add = new Button("ADD");
        button_add.setOnAction(e -> addButtonAction(list_detourComboBox, button_add, other_streets));

        // Add all on screen.
        vbox_middle.getChildren().add(new Label(String.valueOf(line.getId())));
        vbox_middle.getChildren().add(new Label(""));
        vbox_middle.getChildren().add(new Label("REMOVE STREET:"));
        vbox_middle.getChildren().add(comboBox_remove);
        vbox_middle.getChildren().add(new Label("STREETS OF DETOUR:"));
        vbox_middle.getChildren().add(button_add);
        addButtonAction(list_detourComboBox, button_add, other_streets);
        vbox_setting.getChildren().clear();
        vbox_setting.getChildren().add(button_confirm);
        vbox_setting.getChildren().add(button_cancel);
    }

    /**
     * Try detour street, that user select. If something go wrong it show alert message on the screen.
     * @param comboBox_remove ComboBox where user choose street to detour
     * @param list_detourComboBox List of detourComboBox, that user choose streets for detour
     */
    private void confirmButtonAction(ComboBox<String> comboBox_remove, List<DetourComboBox> list_detourComboBox){
        Street street_remove = null;
        List<Street> list_streetsForDetour = new CopyOnWriteArrayList<>();
        // Find street by its name.
        for(Street street : this.line.getRoute().getStreets()){
            if(street.getId().equals(comboBox_remove.getValue())){
                street_remove = street;
            }
        }
        // Load list of street for detour.
        for(DetourComboBox comboBox : list_detourComboBox){
            if(comboBox.getStreet() != null){
                list_streetsForDetour.add(comboBox.getStreet());
            }
        }
        if(!this.checkSelectedDetour(street_remove, list_streetsForDetour)){
            return;
        }
        // Check if none of the line`s buses is actual on the removing street.
        if(!street_remove.isClosed()){
            this.showErrorAlert("ONLY CLOSED STREET COULD DETOUR");
            return;
        }
        // Try make detour operation.
        if(!line.detour(street_remove, list_streetsForDetour)){
            Collections.reverse(list_streetsForDetour);
            if(!line.detour(street_remove, list_streetsForDetour)) {
                this.showErrorAlert("BAD DEFINED DETOUR");
            }
        }
        for(Street street : this.list_street){
            street.unhighlightTheStreet();
        }
        main_content.getChildren().addAll(line.getRoute().getShapes());
        cancelButtonAction();
        this.button_deleteDetours.setVisible(true);
        this.deleteDetoursVisibility = true;
    }

    /**
     * Cancel detour operation. Return right side of the screen as it was before the create detour button was press.
     */
    private void cancelButtonAction(){
        for(Street street : list_street) {
            street.deselectStreet();
            street.unhighlightTheStreet();
        }
        mainButtonAction();
    }

    /**
     * Action for comboBox if use change selected street.
     * All streets unhighlight and then highlight street which is selected by its name.
     * @param street_name Name of street for highlight
     */
    private void removeComboAction(String street_name){
        for(Street street : line.getRoute().getStreets()){
            street.unhighlightTheStreet();
            if(street.getId().equals(street_name)){
                street.highlightForDetour(Color.RED);
            }
        }
    }

    /**
     * Create new detourComboBox and show it on right side of the screen.
     * @param comboBoxList List of all detourComboBox objects
     * @param add Button for create new detourComboBox
     */
    private void addButtonAction(List<DetourComboBox> comboBoxList, Button add, List<Street> other_streets){
        if(!comboBoxList.isEmpty()){
            if(comboBoxList.get(comboBoxList.size()-1).getStreet() == null){
                return;
            }
        }
        DetourComboBox comboBox = new DetourComboBox(other_streets, comboBoxList, vbox_middle);
        comboBoxList.add(comboBox);
        vbox_middle.getChildren().remove(add);
        vbox_middle.getChildren().add(comboBox);
        vbox_middle.getChildren().add(add);
    }

    /**
     * Check if user selected street to detour and if selected streets for detour.
     * If something wasn`t selected, then on screen will be show alert message.
     * @param street_remove Street, that will be detour
     * @param list_streetsForDetour List of streets by them will by detour calculate
     * @return True if everything was choose, false otherwise
     */
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

    /**
     * Show alert message on the screen.
     * @param message Message, that will be show on the screen
     */
    private void showErrorAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

    /**
     * Getter for the line
     * @return The line
     */
    public Line getLine() {
        return line;
    }
}
