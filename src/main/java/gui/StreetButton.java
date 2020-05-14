package gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import map.map_src.Street;

import java.util.ArrayList;
import java.util.List;

public class StreetButton extends Button {
    private final Street street;
    private final List<StreetButton> allStreetButtons;

    public StreetButton(Street street, List<StreetButton> buttons){
        this.street = street;
        allStreetButtons = buttons;
        this.setText(street.getId());
        this.setMinSize(150, 20);
        this.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for(StreetButton button : allStreetButtons){
                    button.getStreet().deselectStreet();
                }
                street.selectStreet();
            }
        });
    }

    public Street getStreet(){
        return this.street;
    }
}
