package Model;

import javax.net.ssl.SSLSocketFactory;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class WebClient {

    private Socket socket;
    private DataInputStream reader;
    private DataOutputStream writer;
    private String email;

    public WebClient() {
        System.setProperty("javax.net.ssl.trustStore", System.getProperty("user.dir") + "/keysSSL/truststore.ts");
        System.setProperty("javax.net.ssl.trustStorePassword", "zdarzenia");
    }

    public boolean connect(){
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();

        try {
            this.socket = sslSocketFactory.createSocket("10.8.224.142", 54321);
            this.reader = new DataInputStream(socket.getInputStream());
            this.writer = new DataOutputStream(socket.getOutputStream());
            return true;
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("reader/write error: " + ex.getMessage());
        }
        return false;
    }

    private void sendDataEncrypted(String data){
        try {
            writer.writeUTF(data);
        } catch (IOException ex) {
            System.out.println("Send data error: " + ex.getMessage());
            disconnect();
        }
    }

    public boolean changePassword(String oldPass, String newPass){
        String str= "change " +oldPass+" "+newPass;
        sendDataEncrypted(str);
        try {
            return reader.readUTF().equals("change");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateInfo(String name, String surname, String birthday, char sex){
        String str;

        str = "update " + name + " " + surname + " " + birthday+ " " + sex + " ";
        sendDataEncrypted(str);

        try {
            return reader.readUTF().equals("update");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerRequest(String name, String surname, char sex, String date, String password, String email) {
        String str = "register " + name + ' ' + surname + ' '  + sex + ' ' + date + ' ' + password + ' ' + email;
        sendDataEncrypted(str);

        try {
            return reader.readUTF().equals("registered");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean loginRequest(String email, String password) {
        String str = "login " + email + ' ' + password;
        sendDataEncrypted(str);
        try {
            if(reader.readUTF().equals("logged")) {
                this.email = email;
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void loggingOut(){
        String message = "unlogin ";
        sendDataEncrypted(message);
        this.email = null;
    }

    public boolean disconnectRequest(){
        sendDataEncrypted("exit_server");

        try {
            if(reader.readUTF().equals("exit_client")){
                disconnect();
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void makeOrder(String order){
        String allOrder = "order " + order;
        sendDataEncrypted(allOrder);
    }

    public void disconnect() {
        try {
          socket.close();
        } catch (IOException ex) {
          System.out.println("Disconnect error: " + ex.getMessage());
        }
    }

    public void forgottenPassword(String inputEmail){
        String str = "forgotten " + inputEmail;
        sendDataEncrypted(str);
    }

    public String commonGet(String[] infos){
        StringBuilder clientQuestion;
        String serverResponse, data;

        clientQuestion = new StringBuilder();
        for (String info: infos){
            clientQuestion.append(info).append(" ");
        }

        sendDataEncrypted(clientQuestion.toString());
        try {
            data = reader.readUTF();
            serverResponse = data.split(" ")[0];
            if(serverResponse.equals(infos[0])) {
                return data;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFilms(){
       return commonGet(new String[]{"films"});
    }

    public String getDatesFilm(String filmName){
        return commonGet(new String[]{"dates", filmName});
    }

    public String getInfos(){
        return commonGet(new String[]{"info", email});
    }

    public String getTickets(){
        return commonGet(new String[]{"tickets"});
    }

    public String getFilmsImage(int id){ return commonGet(new String[]{"image", Integer.toString(id)}); }

    public String getPlaces(String filmName, String date, String time){ return commonGet(new String[]{"places",filmName,date,time}); }

    public boolean deleteAccount(){
        String str = "delete " + email;
        sendDataEncrypted(str);

        try {
            if(reader.readUTF().equals("deleted")){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}

