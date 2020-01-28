package Controllers;

import Utilities.ChangeScene;
import Utilities.CommonUIHandling;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import API.Main;

public class RegisterNewClientController {

    @FXML private JFXButton back;
    @FXML private JFXButton signIn;
    @FXML private JFXTextField name;
    @FXML private JFXTextField surname;
    @FXML private JFXTextField email;
    @FXML private JFXPasswordField pass;
    @FXML private JFXPasswordField re_pass;
    @FXML private JFXComboBox<String> sex;
    @FXML private JFXComboBox<String> day;
    @FXML private JFXComboBox<String> month;
    @FXML private JFXComboBox<String> year;
    private CommonUIHandling commonUIHandling;

    @FXML
    public void initialize(){
        commonUIHandling = new CommonUIHandling(year,month,day);
        commonUIHandling.setCalendar(year,month);
        setUIActionsOnMouseClick();
    }

    private void setUIActionsOnMouseClick(){
        back.setOnAction(event -> back());
        signIn.setOnAction(event -> signIn());
        name.setOnMouseClicked(event -> name.setUnFocusColor(Color.web("#ab7cff")));
        surname.setOnMouseClicked(event -> surname.setUnFocusColor(Color.web("#ab7cff")));
        pass.setOnMouseClicked(event -> pass.setUnFocusColor(Color.web("#ab7cff")));
        re_pass.setOnMouseClicked(event -> re_pass.setUnFocusColor(Color.web("#ab7cff")));
        email.setOnMouseClicked(event -> email.setUnFocusColor(Color.web("#ab7cff")));
        sex.setOnMouseClicked(event -> sex.setUnFocusColor(Color.web("#ab7cff")));
        year.setOnMouseClicked(event -> year.setUnFocusColor(Color.web("#ab7cff")));
        month.setOnMouseClicked(event -> month.setUnFocusColor(Color.web("#ab7cff")));
        day.setOnMouseClicked(event -> day.setUnFocusColor(Color.web("#ab7cff")));
    }

    private boolean areAllConditionsMatch(){
        boolean test;

        test = commonUIHandling.isNameSurnameEmpty(new JFXTextField[]{name, surname});
        test &= commonUIHandling.isEmailCorrect(email);
        test &= commonUIHandling.isPasswordsValid(re_pass,pass);
        test &= commonUIHandling.isSexChosen(sex);
        test &= commonUIHandling.checkDates(new JFXComboBox[]{day,month,year});

        return test;
    }

    private void signIn(){
        String date;
        char sexChar;

        if (areAllConditionsMatch()) {
            date = day.getSelectionModel().getSelectedItem()+'.'+month.getSelectionModel().getSelectedItem()+'.'+year.getSelectionModel().getSelectedItem();
            sexChar = sex.getSelectionModel().getSelectedItem().charAt(0);

            Main.getWebClient().registerRequest(name.getText(),surname.getText(),sexChar,date,pass.getText(),email.getText());
            ChangeScene.launchScene("/Views/welcomePageView.fxml");
        }
    }

    private void back() {
        ChangeScene.launchScene("/Views/welcomePageView.fxml");
    }
}
