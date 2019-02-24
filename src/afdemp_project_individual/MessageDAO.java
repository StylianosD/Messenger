
package afdemp_project_individual;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stelios
 */
public class MessageDAO {
    private Database db;
    
    public MessageDAO(Database db){
        this.db = db;
    }
    
    boolean sendMessage(Message message){
        return insertToSent(message) && insertToReceived(message);
    }
    
    boolean insertToSent(Message message){
        String sql = "INSERT INTO sent_messages(from_id, to_id, date, message) VALUES (?, ?, ?, ?)";
        boolean msgSent = false;
        
        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            st.setInt(1, message.getSenderId());
            st.setInt(2, message.getReceiverId());
            st.setTimestamp(3, message.getDate());
            st.setString(4, message.getText());
      
            msgSent = st.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(MessageDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return msgSent;
    }
    
    boolean insertToReceived(Message message){
        String sql = "INSERT INTO received_messages(to_id, from_id, date, message) VALUES (?, ?, ?, ?)";
        boolean msgReceived = false;

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            st.setInt(1, message.getReceiverId());
            st.setInt(2, message.getSenderId());
            st.setTimestamp(3, message.getDate());
            st.setString(4, message.getText());
            
            msgReceived = st.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(MessageDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return msgReceived;
    }
    
    
    void viewInbox(int userId){
        String sql = "SELECT id, from_id AS `Sender ID`, date, message FROM received_messages\n"
                + "WHERE to_id = " + userId;
        db.print(sql);
    }
    
    void viewSent(int userId){
        String sql = "SELECT id, to_id AS `Receiver ID`, date, message FROM sent_messages\n"
                + "WHERE from_id = " + userId;
        db.print(sql);
    }
    
    
    boolean isInboxEmpty(int userId){
      return isEmpty("SELECT to_id FROM received_messages WHERE to_id = " + userId);  
    }
    
    boolean isSentEmpty(int userId){
      return isEmpty("SELECT from_id FROM sent_messages WHERE from_id = " + userId); 
    }
    
    // Return true if table is empty
    private boolean isEmpty(String sql){
        boolean check = true;
        try(Connection con = db.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)){
            if(rs.first()) check = false;  
        } catch (SQLException ex) {
            Logger.getLogger(MessageDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return check;
    }
    

    boolean deleteMessage(String table, int id){
        String sql = "DELETE FROM " + table + " WHERE id = " + id;
        return db.executeUpdate(sql); 
    }
     
    
    boolean editMessage(String table, Message message){
        String sql = "UPDATE " + table + " SET message= '" + message.getText()
                + "' WHERE id = " + message.getId();
        return db.executeUpdate(sql);
    }
    
    Message getMessage(String table, int id){
        Message message = null;
        String sql = "SELECT * FROM " + table + " WHERE id = " + id;
        
        try(Connection con = db.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)){
           
            if(rs.first()){
                message = new Message(rs.getInt("from_id"), rs.getInt("to_id"),
                        rs.getString("message"), rs.getTimestamp("date"));
                message.setId(rs.getInt("id"));
            }  
        } catch (SQLException ex) {
            Logger.getLogger(MessageDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return message;
    }
    
    
    boolean checkMessageId(String table, String id, int loggedId){
        boolean check = false;
        
        String sql = "SELECT id FROM " + table + " WHERE id = " + id;
        if(table.equals("received_messages")) sql += " AND to_id = " + loggedId;
        else sql += " AND from_id = " + loggedId;
                
        try(Connection con = db.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)){
            if(rs.first()) check = true;
            
        } catch (SQLException ex) {
            Logger.getLogger(MessageDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return check;
    }
      
}
