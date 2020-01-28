package Controllers;

import Utilities.ChangeScene;
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

public class LeavingAppClientPopUpController extends Pane {
    @FXML private Label text;
    @FXML private  JFXButton buttonYes;
    @FXML private  JFXButton buttonNo;
    private static Stage leavingPopUp;

    public LeavingAppClientPopUpController(boolean deleteAccount){
        leavingPopUp = new Stage();
        createLookPopUp();
        checkPopUpType(deleteAccount);
        leavingPopUp.showAndWait();
    }

    private void checkPopUpType(boolean deleteAccount){
        if (deleteAccount) {
            text.setText("Deleting account. Are You sure?");
            buttonYes.setOnAction(event -> deleteAccount());
        } else {
            text.setText("Logging out. Are You sure?");
            buttonYes.setOnAction(event -> logOut());
        }
    }

    private Parent getNewPopUpWindow(){
        FXMLLoader loader = new FXMLLoader();
        URL xmlURL = getClass().getResource("/Views/leavingAppClientView.fxml");
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

        leavingPopUp.setMinHeight(132);
        leavingPopUp.setMinWidth(500);
        leavingPopUp.initOwner(Main.getStage());
        leavingPopUp.initModality(Modality.APPLICATION_MODAL);
        leavingPopUp.setScene(popUpLook);
        leavingPopUp.setResizable(false);
        leavingPopUp.initStyle(StageStyle.UNDECORATED);
        buttonNo.setOnAction(event -> close());
    }

    private void deleteAccount(){
        Main.getWebClient().deleteAccount();
        Main.getWebClient().loggingOut();
        close();
        ChangeScene.launchScene("/Views/welcomePageView.fxml");
    }

    private void logOut(){
        Main.getWebClient().loggingOut();
        close();
        ChangeScene.launchScene("/Views/welcomePageView.fxml");
    }

    private void close(){ leavingPopUp.close(); }
}
