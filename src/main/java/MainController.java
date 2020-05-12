import javafx.fxml.FXML;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

public class MainController {
    @FXML
    private Pane main_content;

    @FXML
    private void zoom(ScrollEvent event){
        event.consume();
        double zoom_by = 0.15;
        double zoom_value = event.getDeltaY() > 0 ? (1 + zoom_by) : (1 - zoom_by);
        main_content.setScaleX(zoom_value * main_content.getScaleX());
        main_content.setScaleY(zoom_value * main_content.getScaleY());
    }
}
