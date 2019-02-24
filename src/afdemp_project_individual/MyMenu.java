
package afdemp_project_individual;
import static consoleapp.ConsoleUtils.clearConsole;
import static consoleapp.ConsoleUtils.requestConfirmation;

import consoleapp.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DStelios
 */
public class MyMenu {
    
    private static Scanner input;
    private UserDAO ud;
    private RoleDAO rd;
    private MessageDAO md;
    private Login login;
    private Database db;
    private User LoggedUser;
    private Files file;
    public boolean inConsole;
    private int delay = 2000;
    
    public MyMenu(Database db){
        this.db = db;
        this.ud = new UserDAO(db);
        this.rd = new RoleDAO(db);
        this.md = new MessageDAO(db);
        if(input == null) input = new Scanner(System.in);
        this.login = new Login(db);
        this.file = new Files(db);
    }
    
    public void loginMenu(){
        clearConsole(); 
        Menu menu = new Menu();
        menu.setTitle("*** Messenger Application ***");
        menu.addItem(new MenuItem("Login", this, "login"));
        menu.execute();
    }
    
    public void login(){
        clearConsole();
        System.out.println("");
        LoggedUser = login.getLogin();
        if(LoggedUser.isAdmin()) this.adminMenu();
        else this.userMenu();
    }
            
    private void adminMenu() {
        clearConsole(); 
        Menu menu = new Menu();
        menu.setTitle("Welcome, Admin!");
        menu.addItem(new MenuItem("Create User", this, "createUser"));
        menu.addItem(new MenuItem("View Users", this, "viewSubMenu"));
        menu.execute();
    }
    
    private void userMenu() {
        clearConsole();
        Menu menu = new Menu();
        menu.setTitle("Welcome, " + LoggedUser.getFirstname() + "!");
        menu.addItem(new MenuItem("Send Mail", this, "sendMail"));
        if(LoggedUser.getRole().hasView()){
            menu.addItem(new MenuItem("View Inbox", this, "viewInbox"));
            menu.addItem(new MenuItem("View Sent Mail", this, "viewSent"));
        }
        menu.execute();
    }
    
    private void userSubMenu(String type) {
        Menu menu = new Menu();
        menu.setTitle(type);       
        if (LoggedUser.getRole().hasEdit()){
            String edit = type.equals("Inbox") ? "editInbox" : "editSent";
            menu.addItem(new MenuItem("Edit message", this, edit));
        }    
        if (LoggedUser.getRole().hasDelete()){ 
            String delete = type.equals("Inbox") ? "deleteInbox" : "deleteSent";
            menu.addItem(new MenuItem("Delete message", this, delete));
        }
        menu.execute();
    }
    
    
    public void sendMail(){
        String receiver = this.getExistingUsername(); // Receiver can be the same as Sender
        User user = ud.getUser(receiver);
        
        String text = this.getMessage();
        int senderId = LoggedUser.getId();
        int receiverId = user.getId();
        Message message = new Message(senderId, receiverId, text, new Timestamp(System.currentTimeMillis()));
        
        if(md.sendMessage(message)){
            System.out.println("\nMessage delivered!");
            file.sendLog(message, LoggedUser.getUsername());
        }
        else
            System.out.println("\nMessage delivery failed!");
        this.msDelay(delay);  
        clearConsole(); 
    }
    
    public void viewInbox(){
        if(md.isInboxEmpty(LoggedUser.getId())) System.out.println("\nInbox is empty");
        else{
            md.viewInbox(LoggedUser.getId());
            this.userSubMenu("Inbox");
        }
    }
    
    public void viewSent(){
        if(md.isSentEmpty(LoggedUser.getId())) System.out.println("\nSent mail is empty");
        else{
            md.viewSent(LoggedUser.getId());
            this.userSubMenu("Sent Mail");
        }
    } 
    
    
    public void editInbox(){ 
        if(md.isInboxEmpty(LoggedUser.getId()))
            System.out.println("\nInbox is empty");
        else
           this.edit("received_messages"); 
    }
    public void editSent(){ 
        if(md.isSentEmpty(LoggedUser.getId()))
            System.out.println("\nSent mail is empty");
        else
           this.edit("received_messages"); 
    }
    
    private void edit(String table){
        int id = this.getMessageId(table);
        Message message = md.getMessage(table, id);
        message.setText(this.getMessage());
        
        if(md.editMessage(table, message)){
            System.out.println("Message updated!");
            file.updateLog(LoggedUser.getUsername(), message, table, "edited");
        }
        else
            System.out.println("Message update failed!"); 
        
        this.msDelay(delay);
        clearConsole();
        if(table.equals("received_messages")) md.viewInbox(LoggedUser.getId());
        else md.viewSent(LoggedUser.getId());
    }
    
    public void deleteInbox(){ 
        if(md.isInboxEmpty(LoggedUser.getId()))
            System.out.println("\nInbox is empty");
        else
            this.delete("received_messages"); 
    }
    
    public void deleteSent(){
        if(md.isSentEmpty(LoggedUser.getId()))
            System.out.println("\nSent mail is empty");
        else 
            this.delete("sent_messages"); 
    }
    
    private void delete(String table){
        int id = this.getMessageId(table);
        Message message = md.getMessage(table, id);
        
        if(md.deleteMessage(table, message.getId())){
            System.out.println("Message deleted!");
            file.updateLog(LoggedUser.getUsername(), message, table, "deleted");
        }
        else
            System.out.println("Message deletion failed!");
        
        this.msDelay(delay);
        clearConsole();
        if(table.equals("received_messages")) md.viewInbox(LoggedUser.getId());
        else md.viewSent(LoggedUser.getId());
    }
    
    
    public void createUser() {
        String username = this.getNewUsername();
        String password = this.getPassword();
        String lname = this.getLastName();
        String fname = this.getFirstName();
        Role role = this.getRole();
        User user = new User(username, password, lname, fname, role);
        
        if(ud.insertUser(user))
            System.out.println("User successfuly created!");
        else
            System.out.println("User creation failed!");
        this.msDelay(delay);
        clearConsole(); 
    }
    
    
    public void viewSubMenu() {
        ud.showUsers();
	Menu menu = new Menu();
	menu.setTitle("Action to be performed on users ?");
        menu.addItem(new MenuItem("Update User", this, "updateUser"));
        menu.addItem(new MenuItem("Delete User", this, "deleteUser"));
	menu.execute();
    }
    
    public void updateUser(){ 
        String username = this.getExistingUsername();
        User user = ud.getUser(username);
  
        System.out.print("\nDo you want to assign a new username ? ");
        if(requestConfirmation()) user.setUsername(this.getNewUsername()); 
        
        System.out.print("Do you want to assign a new password ? ");
        if(requestConfirmation()) user.setPassword(this.getPassword()); 
        
        System.out.print("Do you want to assign a new last name ? ");
        if(requestConfirmation()) user.setLastname(this.getLastName()); 
        
        System.out.print("Do you want to assign a new first name ? ");
        if(requestConfirmation()) user.setFirstname(this.getFirstName()); 
        
        System.out.print("Do you want to assign a new role ? ");
        if(requestConfirmation()) user.setRole(this.getRole()); 
          
        if(ud.updateUser(user)) 
            System.out.println("\nUser successfuly updated!");
        else
            System.out.println("\nUser update failed!");
        this.msDelay(delay);
        clearConsole(); 
        ud.showUsers();
    }
    
    
    public void deleteUser(){
        String username = this.getExistingUsername();
        User user = ud.getUser(username);
        if(ud.deleteUser(user)) 
            System.out.println("\nUser successfuly deleted!");
        else
            System.out.println("\nUser deletion failed!");
        this.msDelay(delay);
        clearConsole(); 
        ud.showUsers();
    }
    

    private String getNewUsername(){
        while(true){
            System.out.println("Please enter a new username: ");
            String username = input.nextLine().trim();
            if(username.equals("")) 
                System.out.println("Username can't be empty! Try again.");
            else if(ud.usernameCheck(username))
                System.out.println("Username already exists! Try again.");
            else return username;
        }
    }

    private String getExistingUsername(){
        while(true){
            System.out.println("Please enter the user's username: ");
            String  username = input.nextLine().trim();
            if(!ud.usernameCheck(username))
                System.out.println("Username doesn't exist! Try again.");
            else return username; 
        }
    }
    
    private String getPassword(){
        int minimumLength = 8;
        while(true){
            System.out.println("Please enter user's password: ");
            String password = input.nextLine().trim();
            if(password.equals("")) {
                System.out.println("Password can't be empty!");
            }
            else if(password.length() < minimumLength){
                System.out.println("Password must be at least 8 characters long");
            }
            else return password;
        }
    }
    
    private String getLastName(){
        while(true){
            System.out.println("Please enter user's last name: ");
            String lname = input.nextLine().trim();
            if(lname.equals("")) {
                System.out.println("Last name can't be empty!");
            }
            else if(isNumber(lname)){
                System.out.println("Last name can't be numeric!");
            }
            else return lname;
        }
    }
    
    private String getFirstName(){
        while(true){
            System.out.println("Please enter user's first name: ");
            String fname = input.nextLine().trim();
            if(fname.equals("")) {
                System.out.println("First name can't be empty!");
            }
            else if(isNumber(fname)){
                System.out.println("First name can't be numeric!");
            }
            else return fname;
        }
    }
    
    // TO DO: print roles from rd , not hardcoded
    private Role getRole(){
        while(true){
            System.out.println("Please enter user's role (guest, regular or premium):");
            String role = input.nextLine().trim();
            if(!rd.getRoleNames().contains(role)) 
                System.out.println("\nWrong choice. Try again");             
            else return rd.getRole(role);
        }
    }
    
    private String getMessage(){
        while(true){
            System.out.println("Please enter a message:");
            String message = input.nextLine().trim();
            if(message.equals("")) 
                System.out.println("Message can't be empty! Try again.");
            else return message;
        }
    }
    
    private int getMessageId(String table){
        while(true){
            System.out.println("Please enter the ID of the message:");
            String id = input.nextLine().trim();
            if(!isNumber(id)) 
                System.out.println("The ID must be a number! Try again.");
            else if(!md.checkMessageId(table, id, LoggedUser.getId())) 
                System.out.println("There is no such ID in the table! Try again.");
            else return Integer.parseInt(id);
        }
    }
    
 
    private boolean isNumber(String input) {
        for (int i = 0; i < input.length(); i++) {
            if (!Character.isDigit(input.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    private void msDelay(int delay){
        try { 
            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }    
}

