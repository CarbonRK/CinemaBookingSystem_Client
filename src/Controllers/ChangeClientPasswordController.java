package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import API.Main;

public class ChangeClientPasswordController {

    @FXML private JFXButton changePassAccept;
    @FXML private JFXPasswordField oldPass;
    @FXML private JFXPasswordField newPass;
    @FXML private JFXPasswordField retypeNewPass;

    @FXML public void initialize(){
        changePassAccept.setOnAction(event -> accept());
        oldPass.setOnMouseClicked(event -> oldPass.setUnFocusColor(Color.web("#ab7cff")));
        newPass.setOnMouseClicked(event -> newPass.setUnFocusColor(Color.web("#ab7cff")));
        retypeNewPass.setOnMouseClicked(event -> retypeNewPass.setUnFocusColor(Color.web("#ab7cff")));
    }

    private void accept() {
        if (checkPassword()){
            Main.getWebClient().changePassword(oldPass.getText(), newPass.getText());
            oldPass.clear();
            newPass.clear();
            retypeNewPass.clear();
        }
    }

    private boolean checkPassword(){
        boolean checkAll;

        checkAll = true;
        if(!newPass.getText().equals(retypeNewPass.getText()) || newPass.getText().length() < 8) {
            newPass.setUnFocusColor(Color.web("#ff0000"));
            retypeNewPass.setUnFocusColor(Color.web("#ff0000"));
            checkAll = false;
        }
        if(oldPass.getText().equals("") || oldPass.getText().equals(newPass.getText())) {
            oldPass.setUnFocusColor(Color.web("#ff0000"));
            checkAll = false;
        }
        return checkAll;
    }
}
