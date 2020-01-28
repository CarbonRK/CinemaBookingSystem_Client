package Controllers;

import Utilities.CommonUIHandling;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import API.Main;

public class UpdateClientInformationsController {

    @FXML private JFXButton changeInfoAccept;
    @FXML private JFXTextField name;
    @FXML private JFXTextField surname;
    @FXML private JFXComboBox<String> sex;
    @FXML private JFXComboBox<String> day;
    @FXML private JFXComboBox<String> month;
    @FXML private JFXComboBox<String> year;
    @FXML private CommonUIHandling commonUIHandling;

    @FXML
    public void initialize(){
        commonUIHandling = new CommonUIHandling(year,month,day);
        makeUI();
        commonUIHandling.setCalendar(year,month);
    }

    private void makeUI(){
        changeInfoAccept.setOnAction(event -> accept());
        name.setOnMouseClicked(event -> name.setUnFocusColor(Color.web("#ab7cff")));
        surname.setOnMouseClicked(event -> surname.setUnFocusColor(Color.web("#ab7cff")));
        sex.setOnMouseClicked(event -> sex.setUnFocusColor(Color.web("#ab7cff")));
        year.setOnMouseClicked(event -> year.setUnFocusColor(Color.web("#ab7cff")));
        month.setOnMouseClicked(event -> month.setUnFocusColor(Color.web("#ab7cff")));
        day.setOnMouseClicked(event -> day.setUnFocusColor(Color.web("#ab7cff")));
    }

    private boolean isAllCellsFiled(){
        boolean test;

        test = commonUIHandling.isNameSurnameEmpty(new JFXTextField[]{name,surname});
        test &= commonUIHandling.isSexChosen(sex);
        test &= commonUIHandling.checkDates(new JFXComboBox[]{year,month,day});

        return test;
    }

    private void accept() {
        String date;
        char sexChar;

        if (isAllCellsFiled()){
            date = day.getSelectionModel().getSelectedItem()+'.'+month.getSelectionModel().getSelectedItem()+'.'+year.getSelectionModel().getSelectedItem();
            sexChar = sex.getSelectionModel().getSelectedItem().charAt(0);
            Main.getWebClient().updateInfo(name.getText(),surname.getText(),date,sexChar);
        }
    }
}
