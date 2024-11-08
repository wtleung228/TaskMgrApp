package role;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

import database.database;
import task.Task;
import task.TaskManager;
import user.User;

public class SeniorRole implements Role {
	//private User globleuser;
    @Override
    public void operate(User user0, Scanner scanner) {
    	//globleuser = user0;
        while (true) {
            System.out.println("Please select the following options (Senior):");
            System.out.println("1. Add a task");
            System.out.println("2. List all tasks");
            System.out.println("3. List all tasks by date");
            System.out.println("4. Edit Task"); // Edit task functionality
            System.out.println("5. Assign Task"); // Assign task functionality
            System.out.println("6. Check user tasks progress"); // Check user tasks progress functionality
            System.out.println("0. Exit");
            int option = -1; // Initialize choice
            while (option == -1) { // Loop until valid input is received
                try {
                    System.out.print("Enter your choice: ");
                    option = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // Clear the invalid input
                }
            }
            switch (option) {
                case 1:
                	user0.addTask(scanner);
                    break;
                case 2:
                	user0.getTaskManager().selectTask(scanner);
                    break;
                case 3:
                    Date date = user0.getTaskDueDate(scanner);
                    user0.getTaskManager().listAllTasksByDate(date);
                    break;
                case 4:
                	user0.getTaskManager().editTask(scanner); // Call edit task method
                    break;
                case 5:
					User user = findUser(scanner);
					Task task = user0.getTaskManager().selectTask(scanner);
					assignTaskToUser(user, task);
					break;
				case 6:
					User user1 = findUser(scanner);
					ArrayList<Task> tasksList = checkUserTasksProgress(user1);
					for (Task task1 : tasksList) {
						task1.showInfo();
					}
					break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }	
        
	    public void assignStaff(User newStaff) {
			//assignedStaff.add(newStaff);
		}
    	
    	public User findUser(Scanner scanner) {
    		System.out.println("Please choose a Staff (choose by ID): ");
    		database db = database.getInstance();
    		db.displayAllUsers();
    		String userID = scanner.nextLine();
    		return db.query(userID);
    	}
    	
    	public void assignTaskToUser(User user, Task task) {
    	    user.getTaskManager().addTask(task);
    	    System.out.println("Task " + task.getTitle() + " assigned to " + user.getName() + ".");
    	}
    	
    	public ArrayList<Task> checkUserTasksProgress(User user) {
    		ArrayList<Task> tasksList = new ArrayList<Task>();
    	    System.out.println("Checking tasks for user: " + user.getName());
    	    for (Task task : user.getTaskManager().getTasks()) {
    			tasksList.add(task);
    	    }
    	    return tasksList;
    	}
}