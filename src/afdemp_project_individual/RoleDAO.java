
package afdemp_project_individual;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Stelios
 */
public class RoleDAO {
    private Database db;
     
    public RoleDAO(Database db){
        this.db = db;
    }
    
    
    List<String> getRoleNames(){
        List<String> names = new ArrayList<>();
        String query = "SELECT " + db.getDbname() + ".roles.name FROM " + db.getDbname() + ".roles";
        
        try(Connection con = db.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query)){
            
            while(rs.next()){
                names.add(rs.getString(1));
            }        
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return names;
    }
    
    // Get a role from table by name
    Role getRole(String name){    
        return get(".roles.name = '" + name + "'");
    }
    
    // Get role from table by id
    Role getRole(int id){
        return get(".roles.id = " + id);
    }
    
    private Role get(String sql){
        Role role = null;
        String query = "SELECT * FROM " + db.getDbname() + ".roles "
                     + "WHERE " + db.getDbname() + sql;

        try(Connection con = db.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query)){
            
            rs.first();
            role = new Role(rs.getString("name"), rs.getBoolean("view"),rs.getBoolean("edit"),rs.getBoolean("delete"));
            role.setId(rs.getInt("id"));            
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return role;
    }
    
    // void insertRole();
    // void deleteRole();
    // void updateRole();
}
