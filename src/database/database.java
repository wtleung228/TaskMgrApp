package database;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import user.*;

public class database {
	ArrayList<User> userDatabase = new ArrayList<User>();
    private static database instance;
    
    // Private constructor to prevent instantiation from outside
    private database() {
        //For testing
    	String fileName = "src/database/UserList.txt";
    	readFile(fileName);
    	//
    }
     
    private void readFile(String fileName) {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                if (parts.length == 4) {
                    String staffName = parts[0];
                    String staffID = parts[1];
                    String password = parts[2];
                    String title = parts[3];
                    // Process the staffName, userName, and password as needed
                    User user;
                    switch (title) {
                        case "Junior":
                            user = new Junior(staffName, staffID, password, title);
                            break;
                        case "Senior":
                            user = new Senior(staffName, staffID, password, title);
                            break;
                        case "Manager":
                            user = new Manager(staffName, staffID, password, title);
                            break;
                        default:
                            System.err.println("Invalid title: " + title);
                            continue;
                    }
                    userDatabase.add(user);
                } else {
                    System.err.println("Invalid format in line: " + line);
                }
            }
            for (User user: userDatabase) {
            	user.displayInfo();
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }
    
    public static database getInstance() {
        if (instance == null) {
            instance = new database();
        }
        return instance;
    }
    
	public void displayAllUsers() {
		for (User user : userDatabase) {
			user.displayInfo();
		}
	}
    
    // Other database-related methods can be added here
    public User query(String staffID) {
    	
    	for (User user: userDatabase) {
    		if (user.getID().equals(staffID)) {
    			return user;
    		}
    	}
    	
        return null;
    }
}