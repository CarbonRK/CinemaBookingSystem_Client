package Controllers;

import Utilities.ChangeScene;
import Utilities.CustomProgressIndicator;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import API.Main;
import java.util.concurrent.ExecutionException;

public class WelcomePageController {

    @FXML private JFXButton logIn;
    @FXML private JFXButton exit;
    @FXML private JFXButton forgetPassword;
    @FXML private JFXButton createAccount;
    @FXML private JFXTextField email;
    @FXML private JFXPasswordField password;
    @FXML private AnchorPane anchorPane;
    private CustomProgressIndicator progressIndicator;

    @FXML
    public void initialize(){
        progressIndicator = new CustomProgressIndicator(354,145,anchorPane);
        setButtonFunctions();
    }

    private void setButtonFunctions(){
        logIn.setOnAction(event -> logInFunc());
        exit.setOnAction(event -> exitFunc());
        createAccount.setOnAction(event -> createAccountFunc());
        forgetPassword.setOnAction(event -> forgottenPasswordFunc());
        email.setOnMouseClicked(event -> email.setUnFocusColor(Color.web("#ab7cff")));
        password.setOnMouseClicked(event -> password.setUnFocusColor(Color.web("#ab7cff")));
        email.setOnKeyPressed(e ->{if(e.getCode() == KeyCode.ENTER) logInFunc();});
        password.setOnKeyPressed(e ->{if(e.getCode() == KeyCode.ENTER) logInFunc();});
    }

    private void createAccountFunc(){ ChangeScene.launchScene("/Views/registerNewClientView.fxml");}

    private void forgottenPasswordFunc(){ ChangeScene.launchScene("/Views/resetPasswordForClientView.fxml");}

    private void disableAllButtons(boolean set){
        JFXButton[] jfxButtons = new JFXButton[]{logIn,createAccount,forgetPassword,exit};
        for (JFXButton jfxButton : jfxButtons) {
            jfxButton.setDisable(set);
        }
    }

    private boolean isNotLoginOrPasswordEntered(){
        boolean empty;

        empty = false;
        if (email.getText().equals("")) {
            email.setUnFocusColor(Color.web("#ff0000"));
            empty = true;
        }
        if (password.getText().equals("")) {
            password.setUnFocusColor(Color.web("#ff0000"));
            empty = true;
        }

        return empty;
    }

    private Task<Boolean> getLogInAttempt(){
        return new Task<>() {
            @Override
            protected Boolean call() {
                if (Main.getWebClient().loginRequest(email.getText(), password.getText())) {
                    return true;
                } else {
                    password.setUnFocusColor(Color.web("#ff0000"));
                    email.setUnFocusColor(Color.web("#ff0000"));
                    return false;
                }
            }
        };
    }

    private void makeEndOptionsForLogInAttempt(Task<Boolean> asyncTask){
        asyncTask.setOnSucceeded((e) -> {
            progressIndicator.hide();
            disableAllButtons(false);
            try {
                if (asyncTask.get()){
                    ChangeScene.launchScene("/Views/loggedInClientView.fxml");
                }
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        });

        asyncTask.setOnFailed((e) -> {
            progressIndicator.hide();
            disableAllButtons(false);
        });
    }

    private void logInFunc(){
        if (isNotLoginOrPasswordEntered()){
            return;
        }

        disableAllButtons(true);
        progressIndicator.show();

        Task<Boolean> asyncTask =  getLogInAttempt();
        makeEndOptionsForLogInAttempt(asyncTask);

        new Thread(asyncTask).start();
    }

    private void exitFunc(){
        Main.getWebClient().disconnectRequest();
        Platform.exit();
    }
}
