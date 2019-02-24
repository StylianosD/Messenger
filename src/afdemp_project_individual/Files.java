
package afdemp_project_individual;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import java.util.logging.Level;
import java.util.logging.Logger;


public class Files {
    private static final String fileName = "Log.txt";
    private static PrintWriter LogFile = null;
    private Database db;
    private UserDAO ud; 
    
    public Files(Database db) {
        this.db = db;
        this.ud = new UserDAO(db);
        openFile();
    }
    

    private boolean openFile() {
        boolean result = false;
        
        try {
            File f = new File(fileName);
            if(f.exists()) {
                LogFile = new PrintWriter(new FileWriter(fileName, true));
                result = true;
            } else {
                LogFile = new PrintWriter(fileName, "UTF-8");
            }
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(Files.class.getName()).log(Level.SEVERE, null, ex);
            return result;
        } 
        catch(IOException ex){
            ex.printStackTrace();
        }
        return result;
    }
 
    
    private void logToFile(String s) {
        LogFile.append(s);
        LogFile.println("");
        LogFile.flush();
    }
    
    // type values: "edited" or "deleted" in order to display the right log 
    // from values: "sent_messages " or "received_messages"
    public void updateLog(String loggedUser, Message message, String from, String type){     
        String s = "User " + loggedUser + " " + type + " a message";
        if(from.equals("sent_messages")){
            User receiver = ud.getUser(message.getReceiverId());
            s += " sent to " + receiver.getUsername() + " at "
                    + message.getDate() + ": " + message.getText();
        }
        else if(from.equals("received_messages")){
            User sender = ud.getUser(message.getSenderId());
            s += " received by " + sender.getUsername() + " at "
                    + message.getDate() + ": " + message.getText();
        }
        logToFile(s);
    }
    
    public void sendLog(Message message, String loggedUser){
        User receiver = ud.getUser(message.getReceiverId()); 
        String s = "User " + loggedUser + " sent a message to " + receiver.getUsername() + 
                " at " + message.getDate() + ": " + message.getText();
        logToFile(s);
    }
 
    
    public static void closeFile() {
        if(LogFile != null)LogFile.close();
    }
}
