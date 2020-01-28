package Utilities;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;

public class CustomProgressIndicator extends ProgressIndicator {

    private AnchorPane anchorPane;

    public CustomProgressIndicator(double x, double y, AnchorPane anchorPane){
        this.anchorPane = anchorPane;
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setStyle("-fx-progress-color: white ");
    }

    public void show(){ anchorPane.getChildren().add(this); }

    public void hide(){ anchorPane.getChildren().remove(this); }
}
