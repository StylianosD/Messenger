
package afdemp_project_individual;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author DStelios
 */
public class Database {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private String options;
    private String dbname; 
    private String server; 
    private String username; 
    private String password;
    //private Database dbInstance;
    
    
    public Database(String server, String options, String user, String password, String dbname){
        this.server = server;
        this.options = options;
        this.username = user;
        this.password = password;
        this.dbname = dbname;
    }
    
//    public static Database getDatabase(){
//        if(dbInstance == null){
//            dbInstance = new Database(server,options,user,password,dbname);
//        }
//        return dbInstance;
//    }
    
    String getUsername(){ return this.username; }
    String getServer(){ return this.server; }
    String getDbname(){ return this.dbname; }
    String getPassword(){ return this.password; }
    String getOptions(){ return this.options; }
   
    void setUsername(String username){ this.username = username; }
    void setServer(String server) {this.server = server; }
    void setDbname(String dbname){ this.dbname = dbname; }
    void setPassword(String password){ this.password = password; }
    void setOptions(String options){ this.options = options; }

    
    public Connection getConnection()  {
        Connection connection = null;
            try {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(this.getFullConnectionURL(), this.getUsername(), this.getPassword());
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }  
        return connection;
    }
    
    public Statement getStatement() {
        try {
            return getConnection().createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public String getFullConnectionURL() {
        return "jdbc:mysql://" + this.getServer() + "/" + this.getDbname() + this.getOptions();
    }
    

    // Prints the result of a query in table format
    public void print(String query){
        try(Connection con = getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query)){
              
            if(!rs.first()){
                System.out.println("Nothing to show!");
                return;
            }
            else{
                rs.beforeFirst();
            }
            
            ResultSetMetaData rsmd = rs.getMetaData();                    
            int colCount = rsmd.getColumnCount();
            
            int[] max = new int[colCount];
            String[] labels = new String[colCount];
            // Get labels and their length
            for (int i = 0; i < colCount; i++) {
                labels[i] = rsmd.getColumnLabel(i+1).toUpperCase();
                max[i] = labels[i].length();
            }
            // Get content's max length for each column
            while(rs.next()){
                int size;
                for (int i = 0; i < colCount; i++) {
                    size = rs.getString(i+1).length();
                    if(size > max[i]) max[i] = size;
                }
            }
                     
            // Make a line of dashes
            int sum = 0;
            for (int i = 0; i < max.length; i++) sum += max[i]+3;
            String dashLine = new String(new char[sum-3]).replace('\0', '+');
            
            // Print labels
            System.out.println(dashLine);
            for (int i = 0; i < colCount; i++) {
                System.out.print(String.format("%-" + (max[i]) + "s", labels[i]));
                if(i < colCount-1)System.out.print(" | ");
            }
            System.out.print("\n");
            
            // Print table
            System.out.println(dashLine);
            rs.beforeFirst();
            while(rs.next()){
                for (int i = 0; i < colCount; i++) {
                    System.out.print(String.format("%-" + (max[i]) + "s", rs.getString(i+1)));
                    if(i < colCount-1)System.out.print(" | ");
                }    
                System.out.print("\n");
            }
            System.out.println(dashLine);
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    
    boolean executeUpdate(String sql){
        boolean updated = false;
        try(Connection con = this.getConnection();
                Statement st = con.createStatement()){
       
            updated = st.executeUpdate(sql) > 0;
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return updated;
    }
}
