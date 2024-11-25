package role;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

import database.database;
import task.Task;
import task.TaskManager;
import user.User;

public class JuniorRole implements Role {
    @Override
    public void operate(User user0, Scanner scanner) {
    	User userdb = database.getInstance().getUserdb();
        while (true) {
        	System.out.println("Please select the following options: (Junior)");
            System.out.println("1. Add a task");
            System.out.println("2. List all my tasks");
            System.out.println("3. List all my tasks by date");
            System.out.println("4. Edit my Task");
            System.out.println("5. Delete my Task");
            System.out.println("0. Exit");
            int option = -1;
            while (option == -1) {
                try {
                    System.out.print("Enter your choice: ");
                    option = scanner.nextInt();
                    scanner.nextLine();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine();
                }
            }
            switch (option) {
	            case 1:
					userdb.addTask(scanner, user0);
	                break;
	            case 2:
	            	userdb.getTaskManager().listAllTask(scanner,user0); 
	                break;
	            case 3:
	                Date date = user0.getTaskDueDate(scanner); //add user0.readDateFromUser(scanner)
	                userdb.getTaskManager().listAllTasksByDate(date, user0); //assignedTask to user0.getTaskManager()
	                break;
	            case 4:
	            	userdb.getTaskManager().editTask(scanner, user0); // Call edit task method //assignedTask to user0.getTaskManager()
	                break;
	            case 5:
	            	userdb.getTaskManager().removeTask(scanner,user0); //assignedTask to user0.getTaskManager()
					break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }	
        
	
}