/*
 * Source code for the final IJA project
 * StreetButton class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 *
 */

package ija.gui;

import ija.map.map_src.Bus;
import ija.map.map_src.Line;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import ija.map.map_src.Street;

import java.util.List;

/**
 * Represent one street on map.
 * It`s button by which can by street choose.
 */
public class StreetButton extends Button {
    /// Street which streetButton represent
    private final Street street;
    /// List of all objects type of StreetButton
    private final List<StreetButton> allStreetButtons;
    /// Vbox which is on right bottom corner of the screen
    private final VBox vbox_setting;
    /// Button for close or open street
    private final Button close_open_button = new Button("CLOSE STREET");
    /// Slider for set up traffic overload of the street
    private final Slider slider = new Slider();
    /// All line in the map
    private final List<Line> lines;

    /**
     * Constructor
     * @param street Street, that streetButton represent
     * @param buttons List of all streetButtons
     * @param vbox_setting Vbox on right bottom corner of the screen
     * @param lines Lines that needs to be assigned for bus checking in street closure
     */
    public StreetButton(Street street, List<StreetButton> buttons, VBox vbox_setting, List<Line> lines){
        this.vbox_setting = vbox_setting;
        this.street = street;
        this.allStreetButtons = buttons;
        this.lines = lines;

        this.setText(street.getId());
        this.setMinSize(140, 30);
        this.setOnAction(e -> mainButtonAction());
        setSetting();
    }

    /**
     * Define behavior for press this streetButton.
     * It deselect all streets and then select street, that this object represent.
     */
    private void mainButtonAction(){
        for(StreetButton button : allStreetButtons){
            button.getStreet().deselectStreet();
        }
        street.selectStreet();
        setVboxSetting();
    }

    /**
     * Setter for right bottom corner of the screen.
     * It show button for close and open street and slider for setting traffic overload.
     */
    private void setVboxSetting(){
        vbox_setting.getChildren().clear();
        vbox_setting.getChildren().add(close_open_button);
        vbox_setting.getChildren().add(new Label("\nTRAFFIC OVERLOAD"));
        vbox_setting.getChildren().add(slider);
    }

    /**
     * Set up button for open and close button and slider.
     */
    private void setSetting(){
        close_open_button.setOnAction(e -> closeOpenButtonAction());
        close_open_button.setPrefSize(120,25);
        slider.setMin(1);
        slider.setMax(4);
        slider.setValue(1);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(1);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                slider.setValue(Math.round(newValue.doubleValue()));
                street.setTrafficOverload((short) slider.getValue());
                street.highlightTheStreet();
            }
        });
    }

    /**
     * Define behavior for open and close button press.
     * If is street open, then this action street close and other way round.
     */
    private void closeOpenButtonAction(){
        if(street.isClosed()){
            close_open_button.setText("CLOSE STREET");
            street.open_street();
        }else{
            if(!checkBusesOnStreet()){
                return;
            }
            close_open_button.setText("OPEN STREET");
            street.close_street();
        }
        street.highlightTheStreet();
    }

    /**
     * Check if some bus isn`t on street which streetButton represent.
     * If there is a bus, than it show alert message on screen.
     * @return True if there wasn`t any bus on street, false otherwise
     */
    private boolean checkBusesOnStreet(){
        for(Line line : this.lines){
            for(Bus bus : line.getBuses()){
                if(bus.getStreet().equals(this.street)){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "STREET CAN BE CLOSE ONLY IF IS EMPTY");
                    alert.showAndWait();
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Getter for street
     * @return The street
     */
    public Street getStreet(){
        return this.street;
    }
}
