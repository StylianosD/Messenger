
package afdemp_project_individual;


public class User {
    private int id;
    private String username;
    private String password;
    private String lname;
    private String fname;
    private Role role;
    private boolean isAdmin = false;
    
    public User(String username){
        if(username.equals("admin")) this.isAdmin = true;
    }
    
    public User(String username, String password, String lname, String fname, Role role){
        this.username = username;
        this.password = password;
        this.lname = lname;
        this.fname = fname;
        this.role = role;
    }
    
    // Getters
    int getId(){ return this.id; }
    String getUsername(){ return this.username; }
    String getPassword(){ return this.password; }
    String getLastname(){ return this.lname; }
    String getFirstname(){ return this.fname; }
    Role getRole(){ return this.role; }
    
    // Setters
    void setId(int id){  this.id = id; }
    void setUsername(String username){  this.username = username; }
    void setPassword(String password){  this.password = password; }
    void setLastname(String lname){  this.lname = lname; }
    void setFirstname(String fname){  this.fname = fname; }
    void setRole(Role role){  this.role = role; }
    
        
    void sendMessage(){
        
    }
    
    void viewLog(){
        
    }
    
    void editMessage(){
        
    }
    
    void deleteMessage(){
        
    }
    
    @Override
    public String toString(){
        return "ID: " + id + "\nUsername: " + username + "\nPassword: " + password + "\nLast Name: " + lname + 
               "\nFirst Name: " + fname + "\nRole: " + role.getName();
    }
    
    boolean isAdmin(){
        return this.isAdmin;
    }

}
