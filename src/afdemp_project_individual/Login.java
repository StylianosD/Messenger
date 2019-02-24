
package afdemp_project_individual;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


/**
 *
 * @author Stelios
 */
public class Login {
    private Database db;
    private static Scanner in;
    
    public Login(Database db){
        this.db = db;
        if(this.in == null){ 
            this.in = new Scanner(System.in);
        }
    }
    
    User getLogin(){ 
        while(true){
            System.out.print("Enter username: ");
            String username = in.nextLine().trim();
            System.out.print("Enter password: ");
            String password = in.nextLine().trim();
            
            User user = checkLogin(username,password);
            if(user == null){
                System.out.println("Wrong username or password. Try again"); 
            }
            else{
               return user;
            }
        }
    }
    
    // Returns a user if username and password are correct, else null
    User checkLogin(String username, String password){
        if(username.equals("admin") && password.equals("admin")){
            return new User("admin");
        } 

        User user = null;
        String sql = "SELECT * FROM users WHERE users.username = '" + username + "'"
                + " AND users.password = '" + password + "'";
        
        try(Connection con = db.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)){
            
            if(rs.first()){
                user = new UserDAO(db).getUser(username);
            } 
        }
        catch(SQLException e){
            e.printStackTrace();
        } 
        return user;
    } 
}
