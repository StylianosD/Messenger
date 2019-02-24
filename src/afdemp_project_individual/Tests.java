
package afdemp_project_individual;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author DStelios
 */
public class Tests {
    private static Database DB;
    
    public Tests(Database db){
        if(DB == null) this.DB = db;
    }
    
    static void testFiles(File file){
        
        
        //file.messagesToF();

    }
    
    static void testUser(){
        UserDAO ud = new UserDAO(DB);
        RoleDAO rd = new RoleDAO(DB);
        Role role = rd.getRole("guest");
         
        User user = new User("Styl2", "123",  "Dafermos",  "Stelios",  role);
        System.out.println("before insertion " + user.getId());
        ud.insertUser(user);
        System.out.println(user.getId());
        
    }
    
    static void testGetRole(){
        RoleDAO rd = new RoleDAO(DB);
        Role role = rd.getRole("guest");
        System.out.println(role.toString() );
        
        System.out.print("\n\n");
        
        Role role2 = rd.getRole(3);
        System.out.println(role2.toString() );
    }
    
    
    static void testSent(){
        
        String sql = "INSERT INTO sent_messages(this_id, to_id, date, message) VALUES (?, ?, ?, ?)";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        //System.out.println(timestamp);
        
        System.out.println(timestamp.toString());
        //System.out.println(sdf.format(timestamp));
      
        try(Connection con = DB.getConnection();
            PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, 1);
            st.setInt(2, 2);
            //st.setString(3, sdf.format(timestamp));
            st.setString(3, timestamp.toString() );
            st.setString(4, "lalalalalalaalalala");
            st.executeUpdate();

        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
