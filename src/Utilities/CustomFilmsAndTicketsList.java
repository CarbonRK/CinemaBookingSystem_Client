package Utilities;

import API.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;

public class CustomFilmsAndTicketsList extends ListView<Record> {

    public CustomFilmsAndTicketsList(boolean showFilms)  {
        ObservableList<Record> recordObservableList;
        recordObservableList = setListItems(showFilms);
        this.setItems(recordObservableList);
        this.setCellFactory(recordListView -> new Cells());
    }

    private byte[] stringToImage(String photo){
        byte[] bytePhoto;
        String[] bytesInString;

        photo = photo.substring(6);
        bytesInString = photo.split(",");
        bytePhoto = new byte[bytesInString.length];

        for (int i =0;i < bytesInString.length ; i++) {
            bytePhoto[i] = (byte) Integer.parseInt(bytesInString[i]);
        }

        return  bytePhoto;
    }

    private Record[] getFilms(){
        String films, images;
        String[] choppedFilms;
        byte[] realPhoto;
        Record[] records;

        films = Main.getWebClient().getFilms();
        choppedFilms = films.split(" ");
        records = new Record[(choppedFilms.length - 1) / 2];

        for (int i = 1, j = 1 ;i < choppedFilms.length; i += 2, j++){
            images = Main.getWebClient().getFilmsImage(j);
            realPhoto = stringToImage(images);
            records[j - 1] = new Record(choppedFilms[i], choppedFilms[i + 1], realPhoto);
        }

        return records;
    }

    private Record[] getTickets(){
        Record[] records;
        String tickets;
        String[] choppedFilms;

        tickets = Main.getWebClient().getTickets();
        choppedFilms = tickets.split(" ");
        records = new Record[(choppedFilms.length - 1) / 6];

        for (int i = 1, j = 0;i < choppedFilms.length; i += 6, j++){
            String title= "Ticket no."+ choppedFilms[i] +" about " +choppedFilms[i+1];
            String desc = "Place no."+ choppedFilms[i+2]+" for "+choppedFilms[i+3]+ " start "+ choppedFilms[i+4]+" "+choppedFilms[i+5];
            records[j] = new Record(title,desc);
        }

        return records;
    }


    private ObservableList<Record> setListItems(boolean showFilms) {
        ObservableList<Record> recordObservableList;
        recordObservableList = FXCollections.observableArrayList();
        Record[] records;

        if (showFilms) {
            records = getFilms();
        } else {
           records = getTickets();
        }

        recordObservableList.addAll(records);
        return  recordObservableList;
    }
}