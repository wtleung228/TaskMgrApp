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
                	user0.addTask(scanner);
                    break;
                case 2:
                	user0.getTaskManager().listAllTask(scanner);
                    break;
                case 3:
                	Date date = user0.getTaskDueDate(scanner); 
                    user0.getTaskManager().listAllTasksByDate(date);
                    break;
                case 4:
                	user0.getTaskManager().editTask(scanner); 
                    break;
                case 5:
                	user0.getTaskManager().removeTask(scanner,user0); 
					break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
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