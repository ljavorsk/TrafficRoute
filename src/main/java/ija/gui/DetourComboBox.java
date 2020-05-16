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

public class DetourComboBox extends HBox {
    private final ComboBox main_comboBox = new ComboBox();
    private final Button main_button = new Button("X");

    private Street street = null;
    private final List<Street> streetList;
    private final List<DetourComboBox> detourComboBoxList;
    private final VBox parent_vbox;

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

    private void detourComboAction(String street_name){
        for(Street street : streetList){
            street.unhighlightTheStreet();
            if(street.getId().equals(street_name)){
                if(this.street != null){
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
