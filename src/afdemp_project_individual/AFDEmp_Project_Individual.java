 
package afdemp_project_individual;

import consoleapp.ConsoleUtils;


public class AFDEmp_Project_Individual {

    private static final String DB_OPTIONS = "?zeroDateTimeBehavior=convertToNull&serverTimezone=Europe/Athens&useSSL=false&"
            + "useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&allowPublicKeyRetrieval=true";    
    
    public static void main(String[] args)  {
            
        Database db = new Database("localhost:3306", DB_OPTIONS, "root", "root", "messenger");
        MyMenu menu = new MyMenu(db);
        
        // Set true to run app from console, false otherwise
        ConsoleUtils.inConsole = true;
        menu.loginMenu();
       
        Files.closeFile(); 
        
    } // end main
   
} // end class
