package gui;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import map.map_src.Street;

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
        allStreetButtons = buttons;
        this.setText(street.getId());
        this.setMinSize(150, 30);
        this.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for(StreetButton button : allStreetButtons){
                    button.getStreet().deselectStreet();
                }
                street.selectStreet();
                setVboxSetting();
            }
        });
        setSetting();
    }

    private void setVboxSetting(){
        vbox_setting.getChildren().clear();
        vbox_setting.getChildren().add(close_open_button);
        vbox_setting.getChildren().add(new Label("traffic overload"));
        vbox_setting.getChildren().add(slider);
    }

    private void setSetting(){
        close_open_button.setText("close street");
        close_open_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(street.isClosed()){
                    close_open_button.setText("close street");
                    street.open_street();
                }else{
                    close_open_button.setText("open street");
                    street.close_street();
                }
            }
        });
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
                street.selectStreet();
            }
        });
    }

    public Street getStreet(){
        return this.street;
    }
}
