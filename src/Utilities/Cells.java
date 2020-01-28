package Utilities;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

class Cells extends ListCell<Record> {
    HBox hBox = new HBox();
    Label title = new Label();
    Label description = new Label();
    ImageView imageView = new ImageView();

    public Cells(){
        super();

        Pane pane;
        VBox vBox;

        pane = new Pane();
        vBox = new VBox();
        vBox.setSpacing(-10);

        vBox.getChildren().addAll(title);
        vBox.getChildren().addAll(description);

        imageView.setFitHeight(50);
        imageView.setFitWidth(50);

        hBox.getChildren().addAll(imageView,vBox ,pane);
        HBox.setHgrow(pane, Priority.ALWAYS);
    }

    public void updateItem(Record name, boolean empty) {
        super.updateItem(name,empty);

        if (name != null && !empty) {
            title.setText(name.getTitle());
            description.setText(name.getDescription());
            title.setPadding(new Insets(5));
            description.setPadding(new Insets(5));
            imageView.setImage(name.getImage());
            setGraphic(hBox);
            setStyle("-fx-border-style: solid; ");
        }else{
            setVisible(false);
            setStyle("-fx-border-style: hidden; -fx-background-color: #3e4760");
        }
    }
}
