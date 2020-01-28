package Controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import API.Main;

import java.io.IOException;
import java.net.URL;


public class PopupServerUnavailableController extends Pane {

    @FXML private Label text;
    @FXML private JFXButton serverUnavailableButton;
    private static Stage serverUnavailableStage;

    private Parent getNewPopUpWindow(){
        FXMLLoader loader = new FXMLLoader();
        URL xmlURL = getClass().getResource("/Views/popupServerUnavailableView.fxml");
        loader.setLocation(xmlURL);
        loader.setController(this);

        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void createLookPopUp(){
        Scene popUpLook = new Scene(getNewPopUpWindow());

        serverUnavailableStage.setMinHeight(132);
        serverUnavailableStage.setMinWidth(500);
        serverUnavailableStage.initOwner(Main.getStage());
        serverUnavailableStage.initModality(Modality.APPLICATION_MODAL);
        serverUnavailableStage.setScene(popUpLook);
        serverUnavailableStage.setResizable(false);
        serverUnavailableStage.initStyle(StageStyle.UNDECORATED);
        serverUnavailableButton.setOnAction(event -> close());
    }

    public PopupServerUnavailableController(){
        serverUnavailableStage = new Stage();
        createLookPopUp();
        serverUnavailableStage.showAndWait();
    }

    private void close(){ serverUnavailableStage.close();}
}
