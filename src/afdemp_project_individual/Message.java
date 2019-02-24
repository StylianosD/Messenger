
package afdemp_project_individual;

import java.sql.Timestamp;

/**
 *
 * @author Stelios
 */
public class Message {
    private String text;
    private int id;
    private int senderId;
    private int receiverId;
    private Timestamp date;
    
    public Message(int sender, int receiver, String text, Timestamp date){
        this.senderId = sender;
        this.receiverId = receiver;
        this.text = text;
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
    
    @Override
    public String toString(){
        return "Sender ID: " + senderId + "\nReceiver ID: " + receiverId + 
                "\nDate: " + date + "\nMessage: " + text;
    }
     
}
