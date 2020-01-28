package Controllers;

import Utilities.ChangeScene;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import API.Main;

public class ResetPasswordForClientController {

    @FXML private JFXButton forgottenBack;
    @FXML private JFXButton forgottenNext;
    @FXML private JFXTextField forgottenText;

    @FXML
    public void initialize(){
        forgottenBack.setOnAction(event -> forgottenBackFunc());
        forgottenNext.setOnAction(event -> forgottenNextFunc());
        forgottenText.setOnMouseClicked(event -> forgottenText.setUnFocusColor(Color.web("#ab7cff")));
    }

    private boolean isEmailCorrect() {
        if(!forgottenText.getText().matches(".+@.+\\.[a-z]*")){
            forgottenText.setUnFocusColor(Color.web("#ab7cff"));
            return false;
        }
        return true;
    }

    private void forgottenNextFunc() {
        if (isEmailCorrect()){
             Main.getWebClient().forgottenPassword(forgottenText.getText());
             forgottenBackFunc();
        }
    }

    private void forgottenBackFunc() {
        ChangeScene.launchScene("/Views/welcomePageView.fxml");
    }
}
