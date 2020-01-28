package Utilities;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import java.time.YearMonth;

public class CommonUIHandling {
    private JFXComboBox<String> year;
    private JFXComboBox<String> month;
    private JFXComboBox<String> day;

    public CommonUIHandling(JFXComboBox<String> year, JFXComboBox<String> month, JFXComboBox<String> day){
        this.year = year;
        this.month = month;
        this.day = day;
        this.month.setDisable(true);
        this.day.setDisable(true);
        this.year.getSelectionModel().selectedItemProperty().addListener(this::unlockMonths);
        this.month.getSelectionModel().selectedItemProperty().addListener(this::unlockDays);
    }

    public boolean checkDates(JFXComboBox<String>[] comboBoxes){
        boolean checkAll;

        checkAll = true;
        for (JFXComboBox<String> comboBox: comboBoxes) {
            if (comboBox.getSelectionModel().getSelectedItem() == null){
                comboBox.setUnFocusColor(Color.web("#ff0000"));
                checkAll = false;
            }
        }

        return checkAll;
    }

    public void setCalendar(JFXComboBox<String> year, JFXComboBox<String> month){
        for (int i = 1940; i < 2020; i++){
            year.getItems().add(Integer.toString(i));
        }

        for (int i = 1; i < 13; i++){
            month.getItems().add(Integer.toString(i));
        }
    }

    public void setDays(String yearString, String monthString, JFXComboBox<String> day){
        int year, month;

        year = Integer.parseInt(yearString);
        month = Integer.parseInt(monthString);
        YearMonth yearMonthObject = YearMonth.of(year, month);
        day.getItems().clear();

        for (int i = 1; i < yearMonthObject.lengthOfMonth() + 1; i++){
            day.getItems().add(Integer.toString(i));
        }
    }

    private void unlockDays(ObservableValue<? extends String> options, String oldValue, String newValue) {
        String chosenYear, chosenMonth;

        if (newValue != null) {
            chosenMonth = month.getSelectionModel().getSelectedItem();
            chosenYear = year.getSelectionModel().getSelectedItem();

            day.setDisable(false);
            setDays(chosenYear,chosenMonth ,day);
        } else {
            day.setDisable(true);
        }
    }

    private void unlockMonths(ObservableValue<? extends String> options, String oldValue, String newValue) {
        if (newValue != null) {
            month.setDisable(false);
        } else {
            month.setDisable(true);
        }
    }

    public boolean isPasswordsValid(JFXPasswordField re_pass, JFXPasswordField pass){
        if(!re_pass.getText().equals(pass.getText())) {
            pass.setUnFocusColor(Color.web("#ff0000"));
            re_pass.setUnFocusColor(Color.web("#ff0000"));
            return false;
        }
        if (pass.getText().length() < 8){
            pass.setUnFocusColor(Color.web("#ff0000"));
            re_pass.setUnFocusColor(Color.web("#ff0000"));
            return false;
        }

        return true;
    }

    public boolean isSexChosen(JFXComboBox<String> comboBox){
        if(comboBox.getSelectionModel().getSelectedItem() == null){
            comboBox.setUnFocusColor(Color.web("#ff0000"));
            return false;
        }

        return true;
    }

    public boolean isNameSurnameEmpty(JFXTextField[] textFields) {
        boolean checkAll;

        checkAll = true;
        for (JFXTextField textField: textFields) {
            if(!textField.getText().matches("\\p{L}+")) {
                textField.setUnFocusColor(Color.web("#ff0000"));
                checkAll = false;
            }
        }

        return checkAll;
    }

    public boolean isEmailCorrect(JFXTextField textField) {
        if(!textField.getText().matches(".+@.+\\.[a-z]+")) {
            textField.setUnFocusColor(Color.web("#ff0000"));
            return false;
        }
        return true;
    }
}