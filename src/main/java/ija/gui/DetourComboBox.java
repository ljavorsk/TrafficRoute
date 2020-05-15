package ija.gui;

import ija.map.map_src.Street;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.List;

public class DetourComboBox extends HBox {
    private final ComboBox main_comboBox = new ComboBox();

    private Street street;
    private final List<Street> streetList;
    private final List<DetourComboBox> detourComboBoxList;

    public DetourComboBox(List<Street> streetList, List<DetourComboBox> detourComboBoxList){
        this.streetList = streetList;
        this.detourComboBoxList = detourComboBoxList;

        main_comboBox.setVisibleRowCount(5);
        for(Street street : streetList){
            main_comboBox.getItems().add(street.getId());
        }
        main_comboBox.setOnAction(event -> detourComboAction((String) main_comboBox.valueProperty().getValue()));
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
                            comboBox.getItems().add(this.street.getId());
                        }
                    }
                }
                street.highlightForDetour(Color.GREEN);
                for(DetourComboBox comboBox : this.detourComboBoxList){
                    if(comboBox != this.main_comboBox){
                        comboBox.getItems().remove(street.getId());
                    }
                }
                this.street = street;
                this.streetList.remove(street);
            }
        }
    }
}
