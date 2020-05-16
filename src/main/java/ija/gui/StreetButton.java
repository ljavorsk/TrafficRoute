package ija.gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import ija.map.map_src.Street;

import java.util.List;

public class StreetButton extends Button {
    private final Street street;
    private final List<StreetButton> allStreetButtons;
    private final VBox vbox_setting;
    private final Button close_open_button = new Button();
    private final Slider slider = new Slider();

    public StreetButton(Street street, List<StreetButton> buttons, VBox vbox_setting){
        this.vbox_setting = vbox_setting;
        this.street = street;
        this.allStreetButtons = buttons;
        this.setText(street.getId());
        this.setMinSize(140, 30);
        this.setOnAction(e -> mainButtonAction());
        setSetting();
    }

    private void mainButtonAction(){
        for(StreetButton button : allStreetButtons){
            button.getStreet().deselectStreet();
        }
        street.selectStreet();
        setVboxSetting();
    }

    private void setVboxSetting(){
        vbox_setting.getChildren().clear();
        vbox_setting.getChildren().add(close_open_button);
        vbox_setting.getChildren().add(new Label("\nTRAFFIC OVERLOAD"));
        vbox_setting.getChildren().add(slider);
    }

    private void setSetting(){
        close_open_button.setText("CLOSE STREET");
        close_open_button.setOnAction(e -> closeOpenButtonAction());
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

    private void closeOpenButtonAction(){
        if(street.isClosed()){
            close_open_button.setText("CLOSE STREET");
            street.open_street();
        }else{
            close_open_button.setText("OPEN STREET");
            street.close_street();
        }
        street.highlightTheStreet();
    }

    public Street getStreet(){
        return this.street;
    }
}
