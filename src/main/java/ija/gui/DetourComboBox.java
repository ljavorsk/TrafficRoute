/*
 * Source code for the final IJA project
 * DetourComboBox class
 * (C) Lukas Javorsky (xjavor20)
 * (C) Patrik Ondriga (xondri08)
 *
 */

package ija.gui;

import ija.map.map_src.Street;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Represents custom comboBox by which can user choose one street.
 * DetourComboBox contain comboBox and self destruction button.
 */
public class DetourComboBox extends HBox {
    /// With this comboBox user can choose one street
    private final ComboBox main_comboBox = new ComboBox();
    /// Self destruction button for delete itself.
    private final Button main_button = new Button("X");

    /// Street which was choose
    private Street street = null;
    /// This list contain streets for user choose
    private final List<Street> streetList;
    /// List of all detourComboBox
    private final List<DetourComboBox> detourComboBoxList;
    /// Vbox which is on the right side of the screen
    private final VBox parent_vbox;

    /**
     * Constructor
     * @param streetList List of all streets, that can be choose
     * @param detourComboBoxList List of all objects type DetourComboBox
     * @param parent_vbox Vbox on right side of the screen
     */
    public DetourComboBox(List<Street> streetList, List<DetourComboBox> detourComboBoxList, VBox parent_vbox){
        this.streetList = streetList;
        this.detourComboBoxList = detourComboBoxList;
        this.parent_vbox = parent_vbox;

        main_comboBox.setVisibleRowCount(5);
        for(Street street : streetList){
            main_comboBox.getItems().add(street.getId());
        }
        main_comboBox.setOnAction(event -> detourComboAction((String) main_comboBox.valueProperty().getValue()));
        main_button.setOnAction(e -> deleteButtonAction());

        this.getChildren().add(main_comboBox);
        this.getChildren().add(main_button);
    }

    /**
     * Destructor for this object
     */
    private void deleteButtonAction(){
        this.parent_vbox.getChildren().remove(this);
        this.detourComboBoxList.remove(this);
        if(this.street != null){
            this.streetList.add(this.street);
            for(DetourComboBox comboBox : this.detourComboBoxList){
                comboBox.getComboBox().getItems().add(this.street.getId());
            }
        }
    }

    /**
     * Try detour street. If something went wrong user is notice by alert message.
     * @param street_name Street name, that will be detour
     */
    private void detourComboAction(String street_name){
        for(Street street : streetList){
            street.unhighlightTheStreet();
            if(street.getId().equals(street_name)){
                if(this.street != null){
                    // Return earlier selected street as an option for other detourComboBoxes.
                    this.street.unhighlightTheStreet();
                    this.streetList.add(this.street);
                    for(DetourComboBox comboBox : this.detourComboBoxList){
                        if(comboBox != this){
                            comboBox.getComboBox().getItems().add(this.street.getId());
                        }
                    }
                }
                street.highlightForDetour(Color.GREEN);
                for(DetourComboBox comboBox : this.detourComboBoxList){
                    if(comboBox != this){
                        comboBox.getComboBox().getItems().remove(street.getId());
                    }
                }
                this.street = street;
                this.streetList.remove(street);
            }
        }
    }

    /**
     * Getter for street.
     * @return street which is selected
     */
    public Street getStreet() {
        return street;
    }

    /**
     * Getter for comboBox.
     * @return the main comboBox
     */
    public ComboBox getComboBox() {
        return main_comboBox;
    }
}
