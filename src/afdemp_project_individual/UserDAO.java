package afdemp_project_individual;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// **************** TO DO: ****************
// Include a message in all exceptions indicationg where it happened ? (stack trace is propably enough for this..)
// Check what can be added to database


public class UserDAO {
    
    private Database db;

    public UserDAO(Database db) {
        this.db = db;
    }


    // Why get the id ? maybe only necessary when geting users from database
    boolean insertUser(User user) {
        String sql = "INSERT INTO users(username, password, lname, fname, role_id) VALUES (?, ?, ?, ?, ?)";
        boolean userInserted = false;

        try (Connection con = db.getConnection();
             PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            st.setString(1, user.getUsername());
            st.setString(2, user.getPassword());
            st.setString(3, user.getLastname());
            st.setString(4, user.getFirstname());
            st.setInt(5, user.getRole().getId());
            userInserted = st.executeUpdate() > 0;

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }   
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return userInserted;
    }
    
    boolean deleteUser(User user) {
        boolean userDeleted = false;
        String sql = "DELETE FROM users WHERE id = ?";
        
        try(Connection con = db.getConnection();
            PreparedStatement st = con.prepareStatement(sql)){
       
            st.setInt(1, user.getId());
            userDeleted = st.executeUpdate() > 0;
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return userDeleted;
    }
    
    // Gets users from database and prints them
    void showUsers() {
        String sql = "SELECT users.id,users.username,users.`password`,users.lname AS `Last Name`,users.fname AS `First Name`, roles.`name` AS `Role`\n"
                   + "FROM users\n" 
                   + "INNER JOIN roles ON `users`.`role_id` = `roles`.`id`\n"
                   + "ORDER BY id;";
       db.print(sql);
    }
    
    // Gets a list of user objects and prints their info
//    void showUsers(List<User> users) throws SQLException{
//        String dashLine = new String(new char[82]).replace('\0', '-');
//        
//        // Print column labels
//        System.out.println(dashLine);
//        System.out.printf("%3s %16s %16s %16s %16s %10s\n",
//                labels[0], labels[1], labels[2], labels[3], labels[4], labels[5]);
//        System.out.println(dashLine);
//        
//        // Print all users
//        for (User user : users) {
//                System.out.printf("%3d %16s %16s %16s %16s %10s\n", 
//                        user.getId(), user.getUsername(), user.getPassword(), 
//                        user.getLastname(), user.getFirstname(), user.getRole()); 
//        }
//        System.out.println(dashLine);
//    }
    

//    public List<User> listAllUsers() {
//        List<User> listUsers = new ArrayList<>();
//        String sql = "SELECT * FROM users";
//            
//        try(Connection con = db.getConnection();
//            Statement st = con.createStatement();    
//            ResultSet resultSet = st.executeQuery(sql)){
//            
//            while (resultSet.next()) {
//                int id = resultSet.getInt("id");
//                String username = resultSet.getString("username");
//                String password = resultSet.getString("password");
//                String lname = resultSet.getString("lname");
//                String fname = resultSet.getString("fname");
//                int roleId = resultSet.getInt("role_id");
//
//                User user = new User(id, username, password, lname, fname, roleId);
//                listUsers.add(user);
//            }
//        }
//        catch(SQLException ex){
//            ex.printStackTrace();
//        }
//        return listUsers;
//    }
    
    
    boolean updateUser(User user) {
        boolean userUpdated = false;
        String sql = "UPDATE users SET username = ?, password = ?, lname = ?, fname = ?, role_id = ? "
                + "WHERE id = ?";

        try(Connection con = db.getConnection();
            PreparedStatement st = con.prepareStatement(sql)){

            st.setString(1, user.getUsername());
            st.setString(2, user.getPassword());
            st.setString(3, user.getLastname());
            st.setString(4, user.getFirstname());
            st.setInt(5, user.getRole().getId());
            st.setInt(6, user.getId());
            
            userUpdated = st.executeUpdate() > 0;
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return userUpdated;
    }
    
    
    User getUser(int id) {
        return get("SELECT * FROM users WHERE id = " + id);
    }
    
    User getUser(String username) {
        return get("SELECT * FROM users WHERE username = '" + username + "'");
    }
    
    User get(String sql)  {
        User user = null;
        try (Connection con = db.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String lname = rs.getString("lname");
                String fname = rs.getString("fname");
                int roleId = rs.getInt("role_id");

                user = new User(username, password, lname, fname, new RoleDAO(db).getRole(roleId));
                user.setId(rs.getInt("id"));
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return user;
    }
    
    // Return true if a username exists
    public boolean usernameCheck(String username){
        boolean check = false;
        String query  = "SELECT username FROM users WHERE username = '" + username + "'";
        
        try (Connection con = db.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            
            if (rs.first()) check = true;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return check;
    }
    


    
    
} // end class
