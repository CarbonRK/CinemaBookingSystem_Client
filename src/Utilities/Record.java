package Utilities;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;

public class Record {
    private String title;
    private String description;
    private Image image;

    public Record(String title, String description, byte[] fileInputStream){
        this.title = title;
        this.description = description;
        this.image = new Image(new ByteArrayInputStream(fileInputStream));
    }

    public Record(String title, String description){
        this.title = title;
        this.description = description;
        this.image = new Image("/Views/Images/ticket.png");
    }

    public String getTitle() {
        return title;
    }

    public Image getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }
}
